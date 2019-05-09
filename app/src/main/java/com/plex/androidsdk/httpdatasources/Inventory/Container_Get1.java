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

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import com.plex.androidsdk.httpdatasources.BaseOutputs;
import com.plex.androidsdk.httpdatasources.BaseRow;
import com.plex.androidsdk.httpdatasources.DataSource;
import com.plex.androidsdk.httpdatasources.IBaseInput;
import com.plex.androidsdk.httpdatasources.IDataSourceCallback;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;

import java.math.BigDecimal;

/**
 * Implements HttpDataSourceTask to perform an http data source call to retrieve the data for a container.
 * <p>
 * Data source: Container_Get1
 */
public class Container_Get1 extends DataSource {

  private InputParameters inputParameters = new InputParameters();

  /**
   * {@inheritDoc}
   */
  public Container_Get1(IDataSourceCallback callback, HttpDataSourceCredentials credentials, String serverName) {

    this(callback, credentials, serverName, false);
  }

  /**
   * {@inheritDoc}
   */
  public Container_Get1(IDataSourceCallback callback, HttpDataSourceCredentials credentials, String serverName, boolean test) {
    super(callback, credentials, serverName, test);
  }

  /**
   * Method to enable setting the Serial No input parameter.
   *
   * @param serialNo The Serial No to search for.
   */
  public void setSerialNo(String serialNo) {
    inputParameters.setSerialNo(serialNo);
  }

  /**
   * Get the Serial No
   *
   * @return String The serial no.
   */
  public String getSerialNo() {
    return inputParameters.getSerialNo();
  }

  //region ABSTRACT METHOD IMPLEMENTATION

  /**
   * Get the data source key for Container_Get1.
   *
   * @return The data source key.
   */
  @Override
  protected int getDataSourceKey() {
    return 6455;
  }


  /**
   * The input parameters to be used for the data source call. {@inheritDoc}
   *
   * @return BaseInput Contains the input parameter values.
   */
  @Override
  protected IBaseInput getBaseInput() {
    return inputParameters;
  }

  /**
   * Since this data source doesn't return any outputs, just return null.
   *
   * @return Null
   */
  @Override
  protected BaseOutputs getBaseOutput() {
    return null;
  }

  /**
   * Parse the JSON for a returned row and assign to the Row class.
   *
   * @param rowArray A row entry in the returned JSON.
   * @return Row Contains the values from the parsed JSON. Returns null if no data.
   */
  @Override
  protected BaseRow parseRow(JsonArray rowArray) {
    Row row = null;

    // Make sure there is data
    if (rowArray.size() > 0) {
      row = new Row();

      // Loop through all the fields and assign the data to Row.
      for (int i = 0; i < rowArray.size(); ++i) {
        switch (i) {
          case 0: // Part No Revision
            row.setPartNoRevision(rowArray.get(0).getAsString());
            break;
          case 1: // Name
            row.setName(rowArray.get(1).getAsString());
            break;
          case 2: // Part Key
            row.setPartKey(rowArray.get(2).getAsInt());
            break;
          case 3: // Operation Code
            row.setOperationCode(rowArray.get(3).getAsString());
            break;
          case 4: // Quantity
            row.setQuantity(rowArray.get(4).getAsBigDecimal());
            break;
          case 5: // Container Status
            row.setContainerStatus(rowArray.get(5).getAsString());
            break;
          case 6: // Location
            row.setLocation(rowArray.get(6).getAsString());
            break;
          case 7: // Note
            row.setNote(rowArray.get(7).getAsString());
            break;
          case 8: // Operation Key
            if (rowArray.get(8).isJsonNull() == false) {
              row.setOperationKey(rowArray.get(8).getAsInt());
            }
            break;
          case 9: // Rework Operation
            if (rowArray.get(9).isJsonNull() == false) {
              row.setReworkOperation(rowArray.get(9).getAsInt());
            }
            break;
          case 10: // Special Instructions
            row.setSpecialInstructions(rowArray.get(10).getAsString());
            break;
          case 11: // Defect Type
            row.setDefectType(rowArray.get(11).getAsString());
            break;
        }
      }
    }

    return row;
  }
  //endregion

  //region INTERNAL CLASSES

  /**
   * Input parameters for data source call. Used by GSON to serialize into JSON.
   */
  private class InputParameters implements IBaseInput {

    @SerializedName("Serial_No")
    private String serialNo;

    public void setSerialNo(String serialNo) {
      this.serialNo = serialNo;
    }

    public String getSerialNo() {
      return serialNo;
    }
  }

  /**
   * Extends BaseRow to represent the structure of a row returned by Container_Get1.
   */
  public class Row extends BaseRow {

    private String containerStatus;
    private String defectType;
    private String location;
    private String name;
    private String note;
    private String operationCode;
    private int operationKey;
    private int partKey;
    private String partNoRevision;
    private BigDecimal quantity;
    private int reworkOperation;
    private String specialInstructions;

    public String getSpecialInstructions() {
      return specialInstructions;
    }

    void setSpecialInstructions(String specialInstructions) {
      this.specialInstructions = specialInstructions;
    }

    public int getReworkOperation() {
      return reworkOperation;
    }

    void setReworkOperation(int reworkOperation) {
      this.reworkOperation = reworkOperation;
    }

    public BigDecimal getQuantity() {
      return quantity;
    }

    void setQuantity(BigDecimal quantity) {
      this.quantity = quantity;
    }

    public String getPartNoRevision() {
      return partNoRevision;
    }

    void setPartNoRevision(String partNoRevision) {
      this.partNoRevision = partNoRevision;
    }

    public int getPartKey() {
      return partKey;
    }

    void setPartKey(int partKey) {
      this.partKey = partKey;
    }

    public int getOperationKey() {
      return operationKey;
    }

    void setOperationKey(int operationKey) {
      this.operationKey = operationKey;
    }

    public String getOperationCode() {
      return operationCode;
    }

    void setOperationCode(String operationCode) {
      this.operationCode = operationCode;
    }

    public String getNote() {
      return note;
    }

    void setNote(String note) {
      this.note = note;
    }

    public String getName() {
      return name;
    }

    void setName(String name) {
      this.name = name;
    }

    public String getLocation() {
      return location;
    }

    void setLocation(String location) {
      this.location = location;
    }

    public String getDefectType() {
      return defectType;
    }

    void setDefectType(String defectType) {
      this.defectType = defectType;
    }

    public String getContainerStatus() {
      return containerStatus;
    }

    void setContainerStatus(String containerStatus) {
      this.containerStatus = containerStatus;
    }
  }
  //endregion
}
