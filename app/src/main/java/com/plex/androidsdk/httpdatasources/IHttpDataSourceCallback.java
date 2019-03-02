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
 * Interface to implement when processing calls to classes that extend HttpDataSourceTask.
 */
public interface IHttpDataSourceCallback {

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
     * Returns the result of the http data source call to the callback handler.
     * @param result The result of the http data source call and the data source result.
     */
    void onHttpDataSourceComplete(DataSourceResult result);

    /**
     * Indicate to callback handler any progress update.
     * @param progressCode One of the constants defined in IHttpDataSourceCallback.Progress.
     */
    void onProgressUpdate(int progressCode);

    /**
     * Indicates that the http data source call has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    void onFinish();
}
