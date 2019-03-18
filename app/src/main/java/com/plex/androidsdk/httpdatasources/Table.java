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

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to hold the JSON response table object.
 */
public class Table {
    private List<String> columns;
    private List<BaseRow> rows = new ArrayList<>();
    private boolean rowLimitExceeded = false;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<BaseRow> getRows() {
        return rows;
    }

    public void addRow(BaseRow row) {
        rows.add(row);
    }

    public boolean isRowLimitExceeded() {
        return rowLimitExceeded;
    }

    public void setRowLimitExceeded(boolean rowLimitExceeded) {
        this.rowLimitExceeded = rowLimitExceeded;
    }
}
