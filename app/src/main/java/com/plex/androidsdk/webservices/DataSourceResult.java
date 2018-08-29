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

import java.util.List;
import java.util.Map;

public class DataSourceResult {

  private int _statusNo;
  private boolean _error;
  private int _errorNo;
  private String _message;
  private int _instanceNo;
  private List _resultSets;

  /**
   * DataSourceResult contains the parsed result from a data source execution on a Plex server.
   * Below is an example of the result from an execution, which is returned within the
   * ExecuteDataSourceResult tag.
   *
   **/
  // formatter:off
  /**
   *  Example data source result
   *
   *  <ExecuteDataSourceResult>
   *    <StatusNo>0</StatusNo>
   *    <Error>false</Error>
   *    <ErrorNo>0</ErrorNo>
   *    <Message>Success</Message>
   *    <InstanceNo>3832778</InstanceNo>
   *    <DataSourceKey>9680</DataSourceKey>
   *    <DataSourceName>Container_Get1</DataSourceName>
   *    <ResultSets>
   *      <ResultSet>
   *        <RowCount>1</RowCount>
   *        <Rows>
   *          <Row>
   *            <Columns>
   *              <Column><Value>16744-0</Value><Name>Part_No_Revision</Name></Column>
   *              <Column><Value>Forged Flange</Value><Name>Name</Name></Column>
   *              <Column><Value>486</Value><Name>Part_Key</Name></Column>
   *              <Column><Value>Ship</Value><Name>Operation_Code</Name></Column>
   *              <Column><Value>10</Value><Name>Quantity</Name></Column>
   *              <Column><Value>Defective</Value><Name>Container_Status</Name></Column>
   *              <Column><Value>QC Hold</Value><Name>Location</Name></Column>
   *              <Column><Value /><Name>Note</Name></Column>
   *              <Column><Value>194</Value><Name>Operation_Key</Name></Column>
   *            </Columns>
   *          </Row>
   *        </Rows>
   *      </ResultSet>
   *    </ResultSets>
   *  </ExecuteDataSourceResult>
   */
  // formatter:on

  /**
   * The status information is parsed and stored as properties. The ResultSets data is stored as
   * hierarchical lists of the form ResultSets[].Rows[].Columns[]. The Column data is stored as a
   * Map<String, String> key-value pair.
   *
   * @param statusNo The status returned by the data source execution.
   * @param error Was there an error executing the data source?
   * @param errorNo If an error occurred, a non-zero number indicating the error.
   * @param message Any success or error message associated with the data source execution.
   * @param instanceNo The data source execution identifier.
   * @param resultSets The parsed result of the data source execution.
   */

  public DataSourceResult(int statusNo, boolean error, int errorNo, String message, int instanceNo,
      List resultSets) {
    _statusNo = statusNo;
    _error = error;
    _errorNo = errorNo;
    _message = message;
    _instanceNo = instanceNo;
    _resultSets = resultSets;
  }

  public int getStatusNo() {
    return _statusNo;
  }

  public boolean isError() {
    return _error;
  }

  public int getErrorNo() {
    return _errorNo;
  }

  public String getMesssage() {
    return _message;
  }

  public int getInstanceNo() {
    return _instanceNo;
  }

  public List getResultSets() {
    return _resultSets;
  }

  /**
   * Returns the Columns from the first row of the first result set.
   *
   * @return A HashMap representing the key-value pairs of column values. Null if no result set.
   */
  public Map<String, String> getFirstColumn() {
    Map<String, String> columns = null;

    if (_resultSets.isEmpty() == false) {
      List rows = (List) _resultSets.get(0);
      if (rows.isEmpty() == false) {
        columns = (Map<String, String>) rows.get(0);
      }
    }

    return columns;
  }
}
