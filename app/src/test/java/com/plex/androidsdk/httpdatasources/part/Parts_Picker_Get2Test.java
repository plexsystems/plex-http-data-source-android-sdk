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

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.plex.androidsdk.httpdatasources.DataSourceResult;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;
import com.plex.androidsdk.httpdatasources.HttpDataSourceResult;
import com.plex.androidsdk.httpdatasources.IDataSourceCallback;
import com.plex.androidsdk.httpdatasources.IDataSourceConnector;
import com.plex.androidsdk.httpdatasources.IDataSourceConnectorCallback;
import java.util.List;
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
   * Test parsing a row
   */
  @Test
  public void parseRow_Good() {
    ExpectedGoodRow expectedData = new ExpectedGoodRow();
    JsonArray inputData = this.getGoodRowJsonArray();

    Parts_Picker_Get2.Row row = (Parts_Picker_Get2.Row) new Parts_Picker_Get2(null, null, null).parseRow(inputData);

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
   * @return A JsonArray of know values representing a row.
   */
  private JsonArray getGoodRowJsonArray() {
    String testRow = " [12345, \n\"Part No Revision Value\",\n\"Name Value\",\n\"Part Status Value\",\n\"Old Part No Value\"]";
    return new JsonParser().parse(testRow).getAsJsonArray();
  }

  /**
   * A class containing the expected data values for getGoodRowJsonArray.
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
      int expectedColumnCount = 5;
      String expectedColumn1 = "Part_Key";
      String expectedColumn2 = "Part_No_Revision";
      String expectedColumn3 = "Name";
      String expectedColumn4 = "Part_Status";
      String expectedColumn5 = "Old_Part_No";

      assertEquals(expectedRowCount, dataSourceResult.getTable().getRows().size());

      Parts_Picker_Get2.Row row0 = (Parts_Picker_Get2.Row) dataSourceResult.getTable().getRows().get(0);

      assertNotNull(row0);
      assertEquals(expectedPartKey, row0.getPartKey());
      assertEquals(expectedPartNoRevision, row0.getPartNoRevision());
      assertEquals(expectedName, row0.getName());
      assertEquals(expectedPartStatus, row0.getPartStatus());

      List<String> columns = dataSourceResult.getTable().getColumns();
      assertEquals(expectedColumnCount, columns.size());
      assertEquals(expectedColumn1, columns.get(0));
      assertEquals(expectedColumn2, columns.get(1));
      assertEquals(expectedColumn3, columns.get(2));
      assertEquals(expectedColumn4, columns.get(3));
      assertEquals(expectedColumn5, columns.get(4));

    }
  }

  /**
   * Test connector that represents a connection to a server. Returns a known package to test parsing logic.
   * Inject into the data source constructor.
   */
  private class TestConnector implements IDataSourceConnector {

    @Override
    public void execute(int dataSourceKey, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer, String jsonRequest,
        IDataSourceConnectorCallback callback) {

      String jsonResponse = "{\n"
          + "    \"outputs\": {},\n"
          + "    \"tables\": [\n"
          + "        {\n"
          + "            \"columns\": [\n"
          + "                \"Part_Key\",\n"
          + "                \"Part_No_Revision\",\n"
          + "                \"Name\",\n"
          + "                \"Part_Status\",\n"
          + "                \"Old_Part_No\"\n"
          + "            ],\n"
          + "            \"rows\": [\n"
          + "                [\n"
          + "                    1859416,\n"
          + "                    \"ABC\",\n"
          + "                    \"ABC Name\",\n"
          + "                    \"Production\",\n"
          + "                    \"\"\n"
          + "                ],\n"
          + "                [\n"
          + "                    246334,\n"
          + "                    \"ABC-1-A\",\n"
          + "                    \"Large Stamping\",\n"
          + "                    \"Production\",\n"
          + "                    \"OldPart No\"\n"
          + "                ]\n"
          + "            ],\n"
          + "            \"rowLimitExceeded\": false\n"
          + "        }\n"
          + "    ],\n"
          + "    \"transactionNo\": \"3835453\"\n"
          + "}";

      HttpDataSourceResult result = new HttpDataSourceResult(jsonResponse, 200);

      callback.onDataSourceConnectorComplete(result);
    }
  }
}