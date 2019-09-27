/*
 *  Copyright 2018 Plex Systems, Inc
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *   associated documentation files (the "Software"), to deal in the Software without restriction,
 *   including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *   sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all copies or
 *   substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 *   BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *   DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.plex.androidsdk.httpdatasources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.Type;

public abstract class DataSource implements IDataSourceConnectorCallback {

  private IDataSourceCallback _dataSourceCallback;
  private IDataSourceConnector _connector;
  private HttpDataSourceCredentials _credentials;
  private String _serverName;
  private boolean _useTestServer;
  private Gson _rowGson;

  /**
   * Default constructor
   *
   * @param iDataSourceCallback The caller who will receive the data source results.
   * @param credentials The Plex credentials to use when connecting with the http data source server.
   * @param serverName Based on data center data is hosted in. AH = cloud, US1/US2 = {customer code}.
   * @param useTestServer If true, use the test api environment.
   */
  public DataSource(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer) {
    this(iDataSourceCallback, credentials, serverName, useTestServer, new HttpDataSourceConnector());
  }

  protected DataSource(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer,
      IDataSourceConnector connector) {
    _dataSourceCallback = iDataSourceCallback;
    _credentials = credentials;
    _serverName = serverName;
    _useTestServer = useTestServer;
    _connector = connector;
  }

  /**
   * Execute the data source
   */
  public void execute() {
    this.execute(0);
  }

  public void execute(int index) {
    _connector.execute(this.getDataSourceKey(), _credentials, _serverName, _useTestServer, this.getJsonRequest(), this, index);
  }

  /**
   * Helper method to convert the input parameters into JSON.
   *
   * @return The JSON request
   */
  public String getJsonRequest() {
    String jsonRequest = null;
    IBaseInput baseInput = this.getBaseInput();

    if (baseInput != null) {
      Gson gson = new Gson();
      jsonRequest = gson.toJson(baseInput);
    }

    return jsonRequest;
  }

  /**
   * {@inheritDoc}
   */
  public void onDataSourceConnectorComplete(HttpDataSourceResult result, int index) {
    if (_dataSourceCallback != null) {
      DataSourceResult dsResult;

      if (result.getHTTPResponseCode() == 200) {
        dsResult = this.parseJsonResponse(result.getJsonResponse());
      } else {
        dsResult = this.parseJsonError(result.getJsonResponse());
      }

      if (result.getException() != null) {
        dsResult.setException(result.getException());
      }

      _dataSourceCallback.onDataSourceComplete(dsResult, index);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void onProgressUpdate(int progressCode, int index) {

  }

  /**
   * Parses the JSON returned from the http data source call.
   *
   * @param jsonResponse The JSON string.
   * @return An instance of DataSourceResult containing the results of the parsed json.
   */
  private DataSourceResult parseJsonResponse(String jsonResponse) {

    DataSourceResult dsResult = new DataSourceResult();

    JsonObject jsonObject = new JsonParser().parse(jsonResponse).getAsJsonObject();
    if (jsonObject.isJsonObject()) {

      // Convert any outputs to an instance object
      if (jsonObject.has("outputs") ) {

        JsonElement outputsElement = jsonObject.get("outputs");

        BaseOutputs outputs = this.getBaseOutputs();
        // Ignore if not output is defined.
        if(outputs != null) {
          outputs = (BaseOutputs) new Gson().fromJson(outputsElement, outputs.getClass());
          dsResult.setOutputs(outputs);
        }
      }

      // Convert any rows to instance objects
      if (jsonObject.has("rows")) {
        JsonArray rowsArray = jsonObject.get("rows").getAsJsonArray();

        // Loop through the rows array
        for (int i = 0; i < rowsArray.size(); ++i) {
          JsonObject rowObject = rowsArray.get(i).getAsJsonObject();
          BaseRow baseRow = this.parseRow(rowObject);

          if (baseRow != null) {
            dsResult.addRow(baseRow);
          }
        }
      }

      if(jsonObject.has("rowLimitExceeded")) {
        dsResult.setRowLimitExceeded(jsonObject.get("rowLimitExceeded").getAsBoolean());
      }

      // Every result will have "transaction no", so we don't even test. Just read it.
      dsResult.setTransactionNo(jsonObject.get("transactionNo").getAsString());
    }

    return dsResult;
  }

  private BaseRow parseRow(JsonObject rowObject) {
    if (_rowGson == null) {
      _rowGson = new Gson();
    }

    BaseRow newRow = _rowGson.fromJson(rowObject, this.getRowType());
    return newRow;
  }

  /**
   * Parses the error JSON returned from the http data source call.
   *
   * @param jsonResponse The error JSON string.
   * @return An instance of DataSourceResult containing the results of the parsed json.
   */
  private DataSourceResult parseJsonError(String jsonResponse) {
    DataSourceResult dsResult = new DataSourceResult();
    dsResult.setHttpDataSourceErrors(new Gson().fromJson(jsonResponse, HttpDataSourceErrors.class));

    return dsResult;
  }

  //region ABSTRACT METHODS

  /**
   * Get the Plex data source key for the http data source.
   */
  protected abstract int getDataSourceKey();

  /**
   * Input parameters for the data source. The returned instance should contain fields with field names that match the data source parameter tags.
   * Used by GSON to serialize into JSON.
   *
   * @return An instance that contains the input parameters for the data source.
   */
  protected abstract IBaseInput getBaseInput();

  /**
   * Returns the Type of the class that will hold the output, implemented by the class extending DataSource. Use by GSON to Deserialize the "outputs"
   * section of the result JSON.
   *
   * @return The Type of the class that will hold the outputs.
   */
  protected abstract Type getOutputType();
  protected BaseOutputs getBaseOutputs(){return null;}

  /**
   * Returns the Type of the class that will hold the row, implemented by the class extending DataSource. Use by Gson to Deserialize the "row" section
   * of the result JSON.
   *
   * @return The Type of the class that will hold the row.
   */
  protected abstract Type getRowType();

  //endregion
}
