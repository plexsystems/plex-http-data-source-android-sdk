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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpDataSourceConnector implements IDataSourceConnector {

  private static final String PRODUCTION_URL_FORMAT = "https://%1s.plex.com/api/datasources/%2s/execute?format=2";
  private static final String TEST_URL_FORMAT = "https://test.%1s.plex.com/api/datasources/%2s/execute?format=2";
  private IDataSourceConnectorCallback _callback;
  private int _index = 0;

  @Override
  public void execute(int dataSourceKey, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer, String jsonRequest,
      IDataSourceConnectorCallback callback, int index) {

    _callback = callback;
    _index = index;
    String url = this.getUrl(serverName, dataSourceKey, useTestServer);
    HttpDataSourceRequest dsRequest = new HttpDataSourceRequest(jsonRequest, credentials, url);

    new HttpConnectorTask().execute(dsRequest);
  }

  /**
   * Perform the http data source call in a background thread.
   */
  private class HttpConnectorTask extends AsyncTask<HttpDataSourceRequest, Integer, HttpDataSourceResult> {

    static final String HTTP_REQUEST_METHOD = "POST";
    static final String CONTENT_TYPE = "application/json; charset=utf-8";
    static final String ACCEPT = "application/json";
    static final String ACCEPT_ENCODING = "gzip, deflate";

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
        publishProgress(IDataSourceConnectorCallback.Progress.ERROR);
        return new HttpDataSourceResult(e);
      }

      return httpDataSourceResult;
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
        publishProgress(IDataSourceConnectorCallback.Progress.CONNECTION_SUCCESS);

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
        publishProgress(IDataSourceConnectorCallback.Progress.REQUEST_SENT);

        int responseCode = connection.getResponseCode();
        publishProgress(IDataSourceConnectorCallback.Progress.RESPONSE_RECEIVED);

        String responseBody;
        if (responseCode == 200) {
          responseBody = this.getStringFromInputStream(connection.getInputStream());
        } else {
          responseBody = this.getStringFromInputStream(connection.getErrorStream());
        }

        publishProgress(IDataSourceConnectorCallback.Progress.PROCESSING_RESULT);
        httpDataSourceResult = new HttpDataSourceResult(responseBody, responseCode);
      } catch (IOException ioe) {
        httpDataSourceResult = new HttpDataSourceResult(ioe);
      } finally {
        connection.disconnect();
      }

      publishProgress(IDataSourceConnectorCallback.Progress.PROCESSING_RESULT_COMPLETE);

      return httpDataSourceResult;
    }


    @Override
    protected void onPostExecute(HttpDataSourceResult httpDataSourceResult) {
      super.onPostExecute(httpDataSourceResult);
      onAsyncTaskDone(httpDataSourceResult);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      onAsyncTaskUpdate(values[0]);
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
  }

  /**
   * Called by AsyncTask on completion of task.
   *
   * @param httpDataSourceResult The outputs from the task.
   */
  private void onAsyncTaskDone(HttpDataSourceResult httpDataSourceResult) {
    _callback.onDataSourceConnectorComplete(httpDataSourceResult, _index);
  }

  /**
   * Called by AsyncTask to report progress change.
   *
   * @param progressCode The progress status code value defined in IDataSourceConnectorCallback.Progress.
   */
  private void onAsyncTaskUpdate(int progressCode) {
    _callback.onProgressUpdate(progressCode, _index);
  }

  /**
   * Gets the Url for the http data source call.
   *
   * @return The Url
   */
  private String getUrl(String serverName, int dataSourceKey, boolean useTestServer) {
    return String.format(useTestServer ? TEST_URL_FORMAT : PRODUCTION_URL_FORMAT, serverName, dataSourceKey);
  }
}
