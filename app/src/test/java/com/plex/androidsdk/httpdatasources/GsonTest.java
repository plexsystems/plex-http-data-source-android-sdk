package com.plex.androidsdk.httpdatasources;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GsonTest {

    @Test
    public void parseResultTest() {
        Gson gson = new Gson();

        // TODO: Implement Gson tests
    }

    @Test
    public void parseInputTest() {
        String expectedValue = "{\"inputs\":{\"inputString1\":\"String1\",\"inputString2_serializedName\":\"String2\",\"inputInt1\":9999,\"inputInt2_serializedName\":9999}}";

        InputTestData sourceData = new InputTestData();
        BaseInputs jsonInputs = new BaseInputs(sourceData);

        Gson gson = new Gson();
        String jsonRequest = gson.toJson(jsonInputs);

        assertEquals(expectedValue, jsonRequest);
    }

    private class InputTestData implements IBaseInput {
        public String inputString1 = "String1";
        @SerializedName("inputString2_serializedName")
        public String inputString2 = "String2";
        public int inputInt1 = 9999;
        @SerializedName("inputInt2_serializedName")
        public int inputInt2 = 9999;
    }

}
