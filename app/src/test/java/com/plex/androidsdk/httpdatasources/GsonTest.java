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

//    @Test
//    public void parseInputTest() {
//        String expectedValue = "{\"inputs\":{\"inputString1\":\"String1\",\"inputString2_serializedName\":\"String2\",\"inputInt1\":9999,\"inputInt2_serializedName\":9999}}";
//
//        InputTestData sourceData = new InputTestData();
//        BaseInputs jsonInputs = new BaseInputs(sourceData);
//
//        Gson gson = new Gson();
//        String jsonRequest = gson.toJson(jsonInputs);
//
//        assertEquals(expectedValue, jsonRequest);
//    }
//
//    private class InputTestData implements IBaseInput {
//        public String inputString1 = "String1";
//        @SerializedName("inputString2_serializedName")
//        public String inputString2 = "String2";
//        public int inputInt1 = 9999;
//        @SerializedName("inputInt2_serializedName")
//        public int inputInt2 = 9999;
//    }

}
