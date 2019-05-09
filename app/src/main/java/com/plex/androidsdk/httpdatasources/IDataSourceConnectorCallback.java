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
 * Interface to implement when processing calls to classes that extend HttpConnectorTask.
 */
public interface IDataSourceConnectorCallback {

  /**
   * Progress status codes
   */
  interface Progress {

    int ERROR = -1;
    int CONNECTION_SUCCESS = 0;
    int REQUEST_SENT = 1;
    int RESPONSE_RECEIVED = 2;
    int PROCESSING_RESULT = 3;
    int PROCESSING_RESULT_COMPLETE = 4;
  }

  /**
   * Returns the result of the data source connector to the callback handler.
   *
   * @param result The result of the data source connector.
   */
  void onDataSourceConnectorComplete(HttpDataSourceResult result);

  /**
   * Indicate to callback handler any progress update.
   *
   * @param progressCode One of the constants defined in IDataSourceConnectorCallback.Progress.
   */
  void onProgressUpdate(int progressCode);
}
