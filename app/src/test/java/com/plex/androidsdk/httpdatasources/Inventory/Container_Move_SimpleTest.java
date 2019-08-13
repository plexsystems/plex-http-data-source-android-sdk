/*
 * Copyright 2019 Plex Systems, Inc
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or
 *  substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 *  BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.plex.androidsdk.httpdatasources.Inventory;

import static org.junit.Assert.*;

import org.junit.Test;

public class Container_Move_SimpleTest {

  /**
   * Test the getDataSourceKey method
   */
  @Test
  public void getDataSourceKey() {
    int expected = 17218;
    int output = new Container_Move_Simple(null, null, null).getDataSourceKey();
    assertEquals(expected, output);
  }

  /**
   * Test default JSON input parameters values
   */
  @Test
  public void testDefaultInputParametersJSON() {

    String expectedValue = "{\"Location\":\"\",\"Serial_No\":\"\",\"Update_By\":0,\"ValidateLocation\":false}";
    Container_Move_Simple cgl = new Container_Move_Simple(null, null, null);
    assertEquals(expectedValue, cgl.getJsonRequest());
  }

  /**
   * Test JSON input parameters values
   */
  @Test
  public void testInputParametersJSON() {
    String serialNo = "S123456";
    String location = "here";
    int updateBy = 8;
    boolean validateLocation = true;
    String expectedValue = "{\"Location\":\"here\",\"Serial_No\":\"S123456\",\"Update_By\":8,\"ValidateLocation\":true}";

    Container_Move_Simple cgl = new Container_Move_Simple(null, null, null);
    cgl.setSerialNo(serialNo);
    cgl.setLocation(location);
    cgl.setUpdateBy(updateBy);
    cgl.setValidateLocation(validateLocation);
    assertEquals(expectedValue, cgl.getJsonRequest());
  }
}