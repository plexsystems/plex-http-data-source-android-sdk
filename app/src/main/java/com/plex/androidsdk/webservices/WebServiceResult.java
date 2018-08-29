/*
 * Copyright 2018 Plex Systems, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.plex.androidsdk.webservices;

/**
 * Wrapper class that serves as a union of a Http response code, result value, and an exception.
 * When the download task has completed, the following combinations are possible: Exception is
 * non-null (Response Code will be 0) Respone Code = 200 and result value is non-null Http Response
 * Code != 200 result value is null.
 *
 * This allows you to pass exceptions to the UI thread that were thrown during doInBackground() and
 * to report any Http response status codes.
 */

public class WebServiceResult {

  private DataSourceResult _result;
  private Exception _exception;
  private int _responseCode = 0;

  public WebServiceResult(DataSourceResult result, int responseCode) {
    _result = result;
    _responseCode = responseCode;
  }

  public WebServiceResult(Exception exception) {
    _exception = exception;
  }

  /**
   * The parsed result of the data source execution if successful, otherwise null.
   * @return The result of the data source execution.
   */
  public DataSourceResult getDataSourceResult() {
    return _result;
  }

  /**
   * The Http response code for the web service call.
   * @return Http response code.
   */
  public int getHTTPResponseCode() {
    return _responseCode;
  }

  /**
   * Any Exception that occurred during the execution of the web service call. Null if no Exception.
   * @return The Exception.
   */
  public Exception getException() {
    return _exception;
  }
}
