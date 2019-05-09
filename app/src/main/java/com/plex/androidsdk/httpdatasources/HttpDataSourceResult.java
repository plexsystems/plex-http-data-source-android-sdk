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

package com.plex.androidsdk.httpdatasources;

/**
 * Wrapper class that returns the result of the http data source call. This is passed to the UI thread for the JSON to be parsed and other logic
 * applied.
 */
public class HttpDataSourceResult {

  private String _jsonResponse;
  private Exception _exception;
  private int _responseCode = 0;

  public HttpDataSourceResult(String jsonResponse, int responseCode) {
    _jsonResponse = jsonResponse;
    _responseCode = responseCode;
  }

  public HttpDataSourceResult(Exception exception) {
    _exception = exception;
  }

  /**
   * The payload JSON response.
   *
   * @return JSON response.
   */
  public String getJsonResponse() {
    return _jsonResponse;
  }

  /**
   * The Http response code for the http data source call.
   *
   * @return Http response code.
   */
  public int getHTTPResponseCode() {
    return _responseCode;
  }

  /**
   * Any Exception that occurred during the execution of the http data source call. Null if no Exception.
   *
   * @return The Exception.
   */
  public Exception getException() {
    return _exception;
  }
}
