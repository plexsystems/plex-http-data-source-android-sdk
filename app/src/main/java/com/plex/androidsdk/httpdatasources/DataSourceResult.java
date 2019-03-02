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

public class DataSourceResult {
    private HttpDataSourceTask.BaseOutputs outputs;
    private Table table;
    private String transactionNo;
    private HttpDataSourceErrors httpDataSourceErrors;
    private Exception exception;
    private boolean error;

    public void setException(Exception exception) {
        this.exception = exception;
        this.error = true;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public HttpDataSourceTask.BaseOutputs getOutputs() {
        return outputs;
    }

    public void setOutputs(HttpDataSourceTask.BaseOutputs outputs) {
        this.outputs = outputs;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public HttpDataSourceErrors getHttpDataSourceErrors() {
        return httpDataSourceErrors;
    }

    public void setHttpDataSourceErrors(HttpDataSourceErrors httpDataSourceErrors) {
        this.httpDataSourceErrors = httpDataSourceErrors;
        this.error = true;
    }
}
