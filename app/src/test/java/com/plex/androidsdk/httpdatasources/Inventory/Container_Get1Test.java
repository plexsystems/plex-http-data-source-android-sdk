package com.plex.androidsdk.httpdatasources.Inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
     * Test parsing a row.
     */
    @Test
    public void parseRow_Good() {
        ExpectedGoodRow expectedData = new ExpectedGoodRow();
        JsonArray inputData = this.getGoodRowJsonArray();

        Container_Get1.Row row = (Container_Get1.Row) new Container_Get1(null, null, null).parseRow(inputData);

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
     * Test parsing a row containing nulls.
     */
    @Test
    public void parseRow_WithNulls() {
        int expectedValue = 0;
        JsonArray inputData = this.getNullRowJsonArray();

        Container_Get1.Row row = (Container_Get1.Row) new Container_Get1(null, null, null).parseRow(inputData);

        assertNotNull(row);
        assertEquals(expectedValue, row.getOperationKey());
        assertEquals(expectedValue, row.getReworkOperation());

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
     * Returns a valid, non-null, JsonArray of a row.
     *
     * @return JsonArray A row of data.
     */
    private JsonArray getGoodRowJsonArray() {
        String testRow = " [\"Part No Revision Value\",\n" +
                "\"Part Name Value\",\n" +
                "99999,\n" +
                "\"Operation Code Value\",\n" +
                "99999,\n" +
                "\"Container Status Value\",\n" +
                "\"Location Value\",\n" +
                "\"Note Value\",\n" +
                "99999,\n" +
                "99999,\n" +
                "\"Special Instructions Value\",\n" +
                "\"Defect Type Value\"]";

        return new JsonParser().parse(testRow).getAsJsonArray();
    }

    /**
     * A class containing the expected data values for getGoodRowJsonArray.
     */
    private class ExpectedGoodRow {
        String PartNoRevision = "Part No Revision Value";
        String Name = "Part Name Value";
        int PartKey = 99999;
        String OperationCode = "Operation Code Value";
        BigDecimal Quantity = BigDecimal.valueOf(99999);
        String ContainerStatus = "Container Status Value";
        String Location = "Location Value";
        String Note = "Note Value";
        int OperationKey = 99999;
        int ReworkOperation = 99999;
        String SpecialInstructions = "Special Instructions Value";
        String DefectType = "Defect Type Value";
    }

    /**
     * Returns a valid JsonArray of a row containing nulls.
     *
     * @return JsonArray A row of data.
     */
    private JsonArray getNullRowJsonArray() {
        String testRow = " [\"Part No Revision Value\",\n" +
                "\"Part Name Value\",\n" +
                "99999,\n" +
                "\"Operation Code Value\",\n" +
                "99999,\n" +
                "\"Container Status Value\",\n" +
                "\"Location Value\",\n" +
                "\"Note Value\",\n" +
                "null,\n" +
                "null,\n" +
                "\"Special Instructions Value\",\n" +
                "\"Defect Type Value\"]";

        return new JsonParser().parse(testRow).getAsJsonArray();
    }
}