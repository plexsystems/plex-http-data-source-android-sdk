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

package com.plex.androidsdk.httpdatasources.Inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.plex.androidsdk.httpdatasources.DataSourceResult;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;
import com.plex.androidsdk.httpdatasources.HttpDataSourceResult;
import com.plex.androidsdk.httpdatasources.IDataSourceCallback;
import com.plex.androidsdk.httpdatasources.IDataSourceConnector;
import com.plex.androidsdk.httpdatasources.IDataSourceConnectorCallback;
import java.math.BigDecimal;
import org.junit.Test;

public class Container_Get1Test {

  /**
   * Test the getDataSourceKey method
   */
  @Test
  public void getDataSourceKey() {
    int expected = 6455;
    int output = new Container_Get1(null, null, null).getDataSourceKey();
    assertEquals(expected, output);
  }

  /**
   * Test setting and getting serial no.
   */
  @Test
  public void testSerialNo() {
    String expectedValue = "S123456";

    Container_Get1 cg1 = new Container_Get1(null, null, null);
    cg1.setSerialNo(expectedValue);

    assertEquals(expectedValue, cg1.getSerialNo());
  }

  /**
   * Test JSON input parameters format
   */
  @Test
  public void testInputParameterJSON() {
    String serialNo = "S123456";
    String expectedValue = "{\"Serial_No\":\"S123456\"}";

    Container_Get1 cgl = new Container_Get1(null, null, null);
    cgl.setSerialNo(serialNo);
    assertEquals(expectedValue, cgl.getJsonRequest());
  }


  /**
   * Test parsing a row.
   */
  @Test
  public void parseRow_Good() {
    ExpectedGoodRow expectedData = new ExpectedGoodRow();
    JsonObject inputData = this.getGoodRowJsonArray();

    Gson gson = new Gson();
    Container_Get1.Row row = gson.fromJson(inputData, Container_Get1.Row.class);

    assertNotNull(row);
    assertEquals(expectedData.PartNoRevision, row.getPartNoRevision());
    assertEquals(expectedData.Name, row.getName());
    assertEquals(expectedData.PartKey, row.getPartKey());
    assertEquals(expectedData.OperationCode, row.getOperationCode());
    assertEquals(expectedData.Quantity, row.getQuantity());
    assertEquals(expectedData.ContainerStatus, row.getContainerStatus());
    assertEquals(expectedData.Location, row.getLocation());
    assertEquals(expectedData.Note, row.getNote());
    assertEquals(expectedData.OperationKey, row.getOperationKey());
    assertEquals(expectedData.ReworkOperation, row.getReworkOperation());
    assertEquals(expectedData.SpecialInstructions, row.getSpecialInstructions());
    assertEquals(expectedData.DefectType, row.getDefectType());

  }

  /**
   * Test parsing a row containing a null for OperationKey.
   */
  @Test
  public void parseRow_WithNull() {
    ExpectedGoodRowWithNulls expectedData = new ExpectedGoodRowWithNulls();
    JsonObject inputData = this.getGoodRowWithNullJsonArray();

    Gson gson = new Gson();
    Container_Get1.Row row = gson.fromJson(inputData, Container_Get1.Row.class);

    assertNotNull(row);
    assertEquals(expectedData.PartNoRevision, row.getPartNoRevision());
    assertEquals(expectedData.Name, row.getName());
    assertEquals(expectedData.PartKey, row.getPartKey());
    assertEquals(expectedData.OperationCode, row.getOperationCode());
    assertEquals(expectedData.Quantity, row.getQuantity());
    assertEquals(expectedData.ContainerStatus, row.getContainerStatus());
    assertEquals(expectedData.Location, row.getLocation());
    assertEquals(expectedData.Note, row.getNote());
    assertEquals(expectedData.OperationKey, row.getOperationKey());
    assertEquals(expectedData.ReworkOperation, row.getReworkOperation());
    assertEquals(expectedData.SpecialInstructions, row.getSpecialInstructions());
    assertEquals(expectedData.DefectType, row.getDefectType());

  }

  /**
   * Returns a valid, non-null, JsonObject of a row.
   *
   * @return JsonObject A row of data.
   */
  private JsonObject getGoodRowJsonArray() {
    String someObject = "{\n"
        + "            \"Part_No_Revision\": \"4799-AAA\",\n"
        + "            \"Name\": \"Rear Suspension Arm\",\n"
        + "            \"Part_Key\": 837,\n"
        + "            \"Operation_Code\": \"Cut and Blank\",\n"
        + "            \"Quantity\": 723.0000000000000000000,\n"
        + "            \"Container_Status\": \"OK\",\n"
        + "            \"Location\": \"Press 16\",\n"
        + "            \"Note\": \"Some Note\",\n"
        + "            \"Operation_Key\": 230,\n"
        + "            \"Rework_Operation\": 99999,\n"
        + "            \"Special_Instructions\": \"Special Instructions Value\",\n"
        + "            \"Defect_Type\": \"Defect Type Value\"\n"
        + "        }";

    return new JsonParser().parse(someObject).getAsJsonObject();
  }

  /**
   * Returns a valid, non-null, JsonObject of a row.
   *
   * @return JsonObject A row of data.
   */
  private JsonObject getGoodRowWithNullJsonArray() {

    String someObject = "{\n"
        + "            \"Part_No_Revision\": \"4799-AAA\",\n"
        + "            \"Name\": \"Rear Suspension Arm\",\n"
        + "            \"Part_Key\": 837,\n"
        + "            \"Operation_Code\": \"Cut and Blank\",\n"
        + "            \"Quantity\": 723.0000000000000000000,\n"
        + "            \"Container_Status\": \"OK\",\n"
        + "            \"Location\": \"Press 16\",\n"
        + "            \"Note\": \"Some Note\",\n"
        + "            \"Operation_Key\": 230,\n"
        + "            \"Rework_Operation\": null,\n"
        + "            \"Special_Instructions\": \"Special Instructions Value\",\n"
        + "            \"Defect_Type\": \"Defect Type Value\"\n"
        + "        }";

    return new JsonParser().parse(someObject).getAsJsonObject();
  }

  /**
   * A class containing the expected data values for getGoodRowJsonArray.
   */
  private class ExpectedGoodRow {

    String PartNoRevision = "4799-AAA";
    String Name = "Rear Suspension Arm";
    int PartKey = 837;
    String OperationCode = "Cut and Blank";
    BigDecimal Quantity = new BigDecimal("723.0000000000000000000");
    String ContainerStatus = "OK";
    String Location = "Press 16";
    String Note = "Some Note";
    int OperationKey = 230;
    int ReworkOperation = 99999;
    String SpecialInstructions = "Special Instructions Value";
    String DefectType = "Defect Type Value";
  }

  /**
   * A class containing the expected data values for getGoodRowJsonArray.
   */
  private class ExpectedGoodRowWithNulls {

    String PartNoRevision = "4799-AAA";
    String Name = "Rear Suspension Arm";
    int PartKey = 837;
    String OperationCode = "Cut and Blank";
    BigDecimal Quantity = new BigDecimal("723.0000000000000000000");
    String ContainerStatus = "OK";
    String Location = "Press 16";
    String Note = "Some Note";
    int OperationKey = 230;
    int ReworkOperation = 0;
    String SpecialInstructions = "Special Instructions Value";
    String DefectType = "Defect Type Value";
  }

  @Test
  public void TestParsing() {
    TestConnection_Good tcg = new TestConnection_Good();
    tcg.Test();;
  }

  private class TestConnection_Good implements IDataSourceCallback {

    public void Test() {
      Container_Get1 cg = new Container_Get1(this, null, null, false, new TestConnector());
      cg.execute();
    }

    @Override
    public void onDataSourceComplete(DataSourceResult dataSourceResult) {
      int expectedRowSize = 1;
      ExpectedGoodRowWithNulls expectedData = new ExpectedGoodRowWithNulls();

      assertEquals(expectedRowSize, dataSourceResult.getRows().size());

      Container_Get1.Row row = (Container_Get1.Row) dataSourceResult.getRows().get(0);

      assertEquals(expectedData.PartNoRevision, row.getPartNoRevision());
      assertEquals(expectedData.Name, row.getName());
      assertEquals(expectedData.PartKey, row.getPartKey());
      assertEquals(expectedData.OperationCode, row.getOperationCode());
      assertEquals(expectedData.Quantity, row.getQuantity());
      assertEquals(expectedData.ContainerStatus, row.getContainerStatus());
      assertEquals(expectedData.Location, row.getLocation());
      assertEquals(expectedData.Note, row.getNote());
      assertEquals(expectedData.OperationKey, row.getOperationKey());
      assertEquals(expectedData.ReworkOperation, row.getReworkOperation());
      assertEquals(expectedData.SpecialInstructions, row.getSpecialInstructions());
      assertEquals(expectedData.DefectType, row.getDefectType());

    }
  }

  // Test connector
  private class TestConnector implements IDataSourceConnector {

    @Override
    public void execute(int dataSourceKey, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer, String jsonRequest,
        IDataSourceConnectorCallback callback) {
      String jsonResponse = "{\"outputs\":{},\"rows\":[{\"Part_No_Revision\":\"4799-AAA\",\"Name\":\"Rear Suspension Arm\",\"Part_Key\":837,\"Operation_Code\":\"Cut and Blank\",\"Quantity\":723.0000000000000000000,\"Container_Status\":\"OK\",\"Location\":\"Press 16\",\"Note\":\"Some Note\",\"Operation_Key\":230,\"Rework_Operation\":null,\"Special_Instructions\":\"Special Instructions Value\",\"Defect_Type\":\"Defect Type Value\"}],\"rowLimitExceeded\":false,\"transactionNo\":\"3836084\"}";
      HttpDataSourceResult result = new HttpDataSourceResult(jsonResponse, 200);
      callback.onDataSourceConnectorComplete(result);
    }
  }
}