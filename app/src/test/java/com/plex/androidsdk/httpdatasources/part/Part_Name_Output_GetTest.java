/*
 * Copyright 2019 Plex Systems, Inc
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
 *
 */

package com.plex.androidsdk.httpdatasources.part;

import static org.junit.Assert.assertEquals;

import com.plex.androidsdk.httpdatasources.DataSourceResult;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;
import com.plex.androidsdk.httpdatasources.HttpDataSourceResult;
import com.plex.androidsdk.httpdatasources.IDataSourceCallback;
import com.plex.androidsdk.httpdatasources.IDataSourceConnector;
import com.plex.androidsdk.httpdatasources.IDataSourceConnectorCallback;
import org.junit.Test;

public class Part_Name_Output_GetTest {

  @Test
  public void TestDatSourceKey() {
    int expectedValue = 721;
    Part_Name_Output_Get pnog = new Part_Name_Output_Get(null, null, null);
    assertEquals(expectedValue, pnog.getDataSourceKey());
  }

  @Test
  public void TestInputParameters() {
    Part_Name_Output_Get pnog = new Part_Name_Output_Get(null, null, null);

    int expectedInitialValue = 0;
    assertEquals(expectedInitialValue, pnog.getPartKey());

    int expectedValue = 1234;
    pnog.setPartKey(expectedValue);
    assertEquals(expectedValue, pnog.getPartKey());
  }

  /**
   * Test JSON input parameters format
   */
  @Test
  public void testInputParameterJSON() {
    int partKey = 1234;
    String expectedValue = "{\"Part_Key\":1234}";

    Part_Name_Output_Get pnog = new Part_Name_Output_Get(null, null, null);
    pnog.setPartKey(partKey);
    assertEquals(expectedValue, pnog.getJsonRequest());
  }

  @Test
  public void TestParsing() {
    TestConnector_Good ta = new TestConnector_Good();
    ta.Test();
  }

  /**
   * Test end-to-end flow
   */
  private class TestConnector_Good implements IDataSourceCallback {

    public void Test() {
      Part_Name_Output_Get pnog = new Part_Name_Output_Get(this, null, null, false, new TestConnector());
      pnog.execute();
    }

    @Override
    public void onDataSourceComplete(DataSourceResult dataSourceResult) {
      String expectedPartName = "Rear Suspension Arm";

      Part_Name_Output_Get.OutputParameters outputParameters = (Part_Name_Output_Get.OutputParameters) dataSourceResult.getOutputs();

      assertEquals(expectedPartName, outputParameters.Name);
    }
  }


  /**
   * Test connector that represents a connection to a server. Returns a known package to test parsing logic. Inject into the data source constructor.
   */
  private class TestConnector implements IDataSourceConnector {

    @Override
    public void execute(int dataSourceKey, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer, String jsonRequest,
        IDataSourceConnectorCallback callback) {

      String jsonResponse = "{\"outputs\":{\"Name\":\"Rear Suspension Arm\"},\"rows\":[],\"rowLimitExceeded\":false,\"transactionNo\":\"3836083\"}";

      HttpDataSourceResult result = new HttpDataSourceResult(jsonResponse, 200);

      callback.onDataSourceConnectorComplete(result);
    }
  }


}