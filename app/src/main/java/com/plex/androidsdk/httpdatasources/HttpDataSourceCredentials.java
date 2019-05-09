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

import android.util.Base64;

/**
 * Contains the credentials used to make an http data source call.
 */
public class HttpDataSourceCredentials {

  private String _userName;
  private String _password;

  public HttpDataSourceCredentials(String userName, String password) {
    _userName = userName;
    _password = password;
  }

  /**
   * Helper method to get the Http Authorization header text for the credentials.
   *
   * @return The Http Authorization header text.
   */
  public String getAuthorization() {
    String baseTest = _userName + ":" + _password;
    String base64string = new String(Base64.encode(baseTest.getBytes(), Base64.NO_WRAP));
    String basicAuthorization = "Basic " + base64string;
    return basicAuthorization;
  }
}
