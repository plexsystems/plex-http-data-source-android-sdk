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

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.plex.androidsdk.httpdatasources.IHttpDataSourceCallback.Progress;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Abstract implementation of AsyncTask to perform http data source calls to PMC.
 * <p>
 * https://developer.android.com/training/basics/network-ops/connecting
 */
public abstract class HttpDataSourceTask extends AsyncTask<HttpDataSourceRequest, Integer, HttpDataSourceResult> {

    private static final String PRODUCTION_URL_FORMAT = "https://%1s.plex.com/api/datasources/%2s/execute";
    private static final String TEST_URL_FORMAT = "https://test.%1s.plex.com/api/datasources/%2s/execute";

    private static final String HTTP_REQUEST_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String ACCEPT = "application/json";
    private static final String ACCEPT_ENCODING = "gzip, deflate";

    private IHttpDataSourceCallback _callback;
    private HttpDataSourceCredentials _credentials;
    private String _serverName;
    private boolean _useTestServer;

    /**
     * Create an instance of HttpDataSourceTask
     * @param callback The process to notify when complete.
     * @param credentials The credentials to use to authenticate.
     * @param serverName For data stored in AH use "cloud". For all others, use customer code.
     */
    public HttpDataSourceTask(IHttpDataSourceCallback callback, HttpDataSourceCredentials credentials, String serverName) {
        this(callback, credentials, serverName, false);
    }

    /**
     * Create an instance of HttpDataSourceTask
     * @param callback The process to notify when complete.
     * @param credentials The credentials to use to authenticate.
     * @param serverName For data stored in AH use "cloud". For all others, use customer code.
     * @param useTestServer Make calls to the test environment.
     */
    public HttpDataSourceTask(IHttpDataSourceCallback callback, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer) {
        _callback = callback;
        _credentials = credentials;
        _serverName = serverName;
        _useTestServer = useTestServer;
    }

    /**
     * Execute the task
     */
    public void execute() {
        this.execute(getHttpDataSourceRequest());
    }

    /**
     * Perform the http data source call in a background thread.
     *
     * @param httpDataSourceRequests Data to perform the http data source call.
     * @return HttpDataSourceResult The result of the http data source call.
     */
    @Override
    protected HttpDataSourceResult doInBackground(HttpDataSourceRequest... httpDataSourceRequests) {
        HttpDataSourceRequest request = httpDataSourceRequests[0];

        HttpDataSourceResult httpDataSourceResult;

        try {
            httpDataSourceResult = callHttpDataSource(request);
        } catch (IOException e) {
            publishProgress(Progress.ERROR);
            return new HttpDataSourceResult(e);
        }

        return httpDataSourceResult;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        _callback.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(HttpDataSourceResult result) {
        super.onPostExecute(result);

        DataSourceResult dsResult;

        if (result.getHTTPResponseCode() == 200) {
            dsResult = this.parseJsonResponse(result.getJsonResponse());
        } else {
            dsResult = this.parseJsonError(result.getJsonResponse());
        }

        if (result.getException() != null) {
            dsResult.setException(result.getException());
        }

        _callback.onHttpDataSourceComplete(dsResult);
        _callback.onFinish();
    }

    /**
     * Get a HttpDataSourceRequest instance for the current request.
     */
    private HttpDataSourceRequest getHttpDataSourceRequest() {
        return new HttpDataSourceRequest(this.getJsonRequest(this.getBaseInput()), _credentials, this.getUrl());
    }

    /**
     * Gets the Url for the http data source call.
     *
     * @return The Url
     */
    private String getUrl() {
        return String.format(_useTestServer ? TEST_URL_FORMAT : PRODUCTION_URL_FORMAT, _serverName, this.getDataSourceKey());
    }


    /**
     * Perform an http call to the remote server to execute the data source and receive the results.
     *
     * @param request The HttpDataSourceRequest containing the data to complete the call.
     * @return A HttpDataSourceResult containing the results of the call.
     * @throws IOException if an I/O error occurs.
     */
    private HttpDataSourceResult callHttpDataSource(HttpDataSourceRequest request) throws IOException {

        HttpDataSourceResult httpDataSourceResult;
        URL url = new URL(request.getUrl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            publishProgress(IHttpDataSourceCallback.Progress.CONNECTION_SUCCESS);

            // Send the Json request
            connection.setRequestMethod(HTTP_REQUEST_METHOD);
            connection.addRequestProperty("Authorization", request.getCredentials().getAuthorization());
            connection.setRequestProperty("Accept", ACCEPT);
            connection.setRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);

            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setDoInput(true);

            DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
            outStream.writeBytes(request.getJsonRequest());
            outStream.flush();
            outStream.close();
            publishProgress(IHttpDataSourceCallback.Progress.REQUEST_SENT);

            int responseCode = connection.getResponseCode();
            publishProgress(Progress.RESPONSE_RECEIVED);

            String responseBody;
            if (responseCode == 200) {
                responseBody = this.getStringFromInputStream(connection.getInputStream());
            } else {
                responseBody = this.getStringFromInputStream(connection.getErrorStream());
            }

            publishProgress(Progress.PROCESSING_RESULT);
            httpDataSourceResult = new HttpDataSourceResult(responseBody, responseCode);
        } catch (IOException ioe) {
            httpDataSourceResult = new HttpDataSourceResult(ioe);
        } finally {
            connection.disconnect();
        }

        publishProgress(Progress.PROCESSING_RESULT_COMPLETE);

        return httpDataSourceResult;
    }

    /**
     * Helper method to convert an input stream to a string.
     *
     * @param inputStream The stream to read.
     * @return A string of the contents of the stream.
     * @throws IOException if an I/O error occurs.
     */
    private String getStringFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBody = new StringBuilder(inputStream.available());
        for (String line; (line = reader.readLine()) != null; ) {
            responseBody.append(line).append('\n');
        }

        return responseBody.toString();
    }

    /**
     * Helper method to convert the input parameters into JSON.
     *
     * @return The JSON request
     */
    private String getJsonRequest(IBaseInput baseInput) {
        String jsonRequest = null;

        if (baseInput != null) {
            Gson gson = new Gson();
            BaseInputs baseInputs = new BaseInputs(baseInput);
            jsonRequest = gson.toJson(baseInputs);
        }

        return jsonRequest;
    }

    /**
     * Parses the JSON returned from the http data source call.
     *
     * @param jsonResponse The JSON string.
     */
    private DataSourceResult parseJsonResponse(String jsonResponse) {

        DataSourceResult dsResult = new DataSourceResult();

        JsonObject jsonTree = new JsonParser().parse(jsonResponse).getAsJsonObject();
        if (jsonTree.isJsonObject()) {

            JsonObject jsonObject = jsonTree.getAsJsonObject();

            // Convert any outputs to an instance object
            if (jsonObject.has("outputs")) {

                JsonElement outputsElement = jsonObject.get("outputs");
                dsResult.setOutputs(new Gson().fromJson(outputsElement, BaseOutputs.class));
            }

            // Convert any tables to instance objects
            if (jsonObject.has("tables")) {
                JsonArray tablesArray = jsonObject.getAsJsonArray("tables");
                // tablesArray.size should always equal 1, as we will never actually return multiple result sets. The code works under that premise.
                if (tablesArray.size() > 0) {
                    Table resultTable = new Table();

                    // Array item 0 will be the result set, so get is as a JsonObject
                    JsonObject tableObject = tablesArray.get(0).getAsJsonObject();

                    // Put the column names into a List
                    if (tableObject.has("columns")) {
                        Type listType = new TypeToken<List<String>>(){}.getType();

                        List<String> columns = new Gson().fromJson(tableObject.get("columns"), listType);
                        resultTable.setColumns(columns);
                    }

                    // Parse the table rows
                    if (tableObject.has("rows")) {
                        JsonArray rowsArray = tableObject.get("rows").getAsJsonArray();

                        for (int i = 0; i < rowsArray.size(); ++i) {
                            JsonArray rowArray = rowsArray.get(i).getAsJsonArray();
                            BaseRow baseRow = parseRow(rowArray);
                            if (baseRow != null) {
                                resultTable.addRow(baseRow);
                            }
                        }
                    }

                    if (tableObject.has("rowLimitExceeded")) {
                        resultTable.setRowLimitExceeded(tableObject.get("rowLimitExceeded").getAsBoolean());
                    }

                    dsResult.setTable(resultTable);
                }
            }

            // Every result will have "transaction no", so we don't even test. Just read it.
            dsResult.setTransactionNo(jsonObject.get("transactionNo").getAsString());
        }

        return dsResult;
    }

    private DataSourceResult parseJsonError(String jsonResponse) {
        DataSourceResult dsResult = new DataSourceResult();
        dsResult.setHttpDataSourceErrors(new Gson().fromJson(jsonResponse, HttpDataSourceErrors.class));

        return dsResult;
    }


    /* ****** ABSTRACT METHODS ****** **/

    /**
     * Get the Plex data source key for the http data source call.
     */
    protected abstract int getDataSourceKey();

    /**
     * Input parameters for the data source.
     * The returned instance should extend BaseInput and add parameters, with field names that match the parameter tags.
     *
     * @return An extension of BaseInput that contains the input parameters.
     */
    protected abstract IBaseInput getBaseInput();

    /**
     * Output parameters of the http data source.
     * Will return null if no output is expected.
     *
     * @return An extension of BaseOutputs that contains the fields of the output parameters.
     */
    protected abstract BaseOutputs getBaseOutput();

    /**
     * Parses a row entry for the returned JSON.
     *
     * @param rowArray A row entry in the returned JSON.
     */
    protected abstract BaseRow parseRow(JsonArray rowArray);

}