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

/**
 * Wrapper class for all the data required to make an http data source request using HttpConnectorTask. Passed to the background thread.
 */
class HttpDataSourceRequest {

  private HttpDataSourceCredentials _credentials;
  private String _jsonRequest;
  private String _url;

  public HttpDataSourceRequest(String jsonRequest, HttpDataSourceCredentials credentials, String url) {
    _jsonRequest = jsonRequest;
    _credentials = credentials;
    _url = url;
  }

  public HttpDataSourceCredentials getCredentials() {
    return _credentials;
  }

  public String getJsonRequest() {
    return _jsonRequest;
  }

  public String getUrl() {
    return _url;
  }
}
