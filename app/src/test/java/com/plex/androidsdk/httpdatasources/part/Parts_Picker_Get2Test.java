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
import org.junit.Test;

public class Parts_Picker_Get2Test {

  /**
   * Test the getDataSourceKey method
   */
  @Test
  public void getDataSourceKey() {
    int expected = 1791;
    int output = new Parts_Picker_Get2(null, null, null).getDataSourceKey();
    assertEquals(expected, output);
  }

  /**
   * Test setting and getting serial no.
   */
  @Test
  public void testSerialNo() {
    String expectedValue = "abc";

    Parts_Picker_Get2 ppg = new Parts_Picker_Get2(null, null, null);
    ppg.setPartNo(expectedValue);

    assertEquals(expectedValue, ppg.getPartNo());
  }

  /**
   * Test JSON input parameters format
   */
  @Test
  public void testInputParameterJSON() {
    String partNo = "4709-A";
    String expectedValue = "{\"Part_No\":\"4709-A\"}";

    Parts_Picker_Get2 ppg = new Parts_Picker_Get2(null, null, null);
    ppg.setPartNo(partNo);
    assertEquals(expectedValue, ppg.getJsonRequest());
  }

  /**
   * Test parsing a row
   */
  @Test
  public void parseRow_Good() {
    ExpectedGoodRow expectedData = new ExpectedGoodRow();
    JsonObject inputObject = this.getGoodRowJsonObject();

    Gson gson = new Gson();
    Parts_Picker_Get2.Row row = gson.fromJson(inputObject, Parts_Picker_Get2.Row.class);

    assertNotNull(row);
    assertEquals(expectedData.PartKey, row.getPartKey());
    assertEquals(expectedData.PartNoRevision, row.getPartNoRevision());
    assertEquals(expectedData.Name, row.getName());
    assertEquals(expectedData.PartStatus, row.getPartStatus());
    assertEquals(expectedData.OldPartNo, row.getOldPartNo());
  }

  /**
   * Test parsing of full JSON package.
   */
  @Test
  public void ConnectionTest_Good() {
    TestConnector_Good ta = new TestConnector_Good();
    ta.Test();
  }

  /**
   * Data to test row parsing logic.
   *
   * @return A JsonArray of know values representing a row.
   */
  private JsonObject getGoodRowJsonObject() {
    String testRow = "{\"Part_Key\":12345, \"Part_No_Revision\":\"Part No Revision Value\",\"Name\":\"Name Value\",\"Part_Status\":\"Part Status Value\",\"Old_Part_No\":\"Old Part No Value\"}";
    return new JsonParser().parse(testRow).getAsJsonObject();
  }

  /**
   * A class containing the expected data values for getGoodRowJsonObject.
   */
  private class ExpectedGoodRow {

    int PartKey = 12345;
    String PartNoRevision = "Part No Revision Value";
    String Name = "Name Value";
    String PartStatus = "Part Status Value";
    String OldPartNo = "Old Part No Value";
  }


  /**
   * Test end-to-end flow
   */
  private class TestConnector_Good implements IDataSourceCallback {

    public void Test() {
      Parts_Picker_Get2 ppg2 = new Parts_Picker_Get2(this, null, null, false, new TestConnector());
      ppg2.execute();
    }

    @Override
    public void onDataSourceComplete(DataSourceResult dataSourceResult) {

      int expectedRowCount = 2;
      int expectedPartKey = 1859416;
      String expectedPartNoRevision = "ABC";
      String expectedName = "ABC Name";
      String expectedPartStatus = "Production";

      assertEquals(expectedRowCount, dataSourceResult.getRows().size());

      Parts_Picker_Get2.Row row0 = (Parts_Picker_Get2.Row) dataSourceResult.getRows().get(0);

      assertNotNull(row0);
      assertEquals(expectedPartKey, row0.getPartKey());
      assertEquals(expectedPartNoRevision, row0.getPartNoRevision());
      assertEquals(expectedName, row0.getName());
      assertEquals(expectedPartStatus, row0.getPartStatus());
    }
  }

  /**
   * Test connector that represents a connection to a server. Returns a known package to test parsing logic. Inject into the data source constructor.
   */
  private class TestConnector implements IDataSourceConnector {

    @Override
    public void execute(int dataSourceKey, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer, String jsonRequest,
        IDataSourceConnectorCallback callback) {

      String jsonResponse = "{\n"
          + "    \"outputs\": {},    \"rows\": [\n"
          + "        {\n"
          + "            \"Part_Key\": 1859416,\n"
          + "            \"Part_No_Revision\": \"ABC\",\n"
          + "            \"Name\": \"ABC Name\",\n"
          + "            \"Part_Status\": \"Production\",\n"
          + "            \"Old_Part_No\": \"\"\n"
          + "        },\n"
          + "        {\n"
          + "            \"Part_Key\": 246334,\n"
          + "            \"Part_No_Revision\": \"ABC-1-A\",\n"
          + "            \"Name\": \"Large Stamping\",\n"
          + "            \"Part_Status\": \"Production\",\n"
          + "            \"Old_Part_No\": \"OldPart No\"\n"
          + "        }],"
          + "    \"rowLimitExceeded\": false,\n"
          + "    \"transactionNo\": \"3836077\"\n"
          + "}";

      HttpDataSourceResult result = new HttpDataSourceResult(jsonResponse, 200);

      callback.onDataSourceConnectorComplete(result);
    }
  }
}