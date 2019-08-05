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

import com.google.gson.annotations.SerializedName;
import com.plex.androidsdk.httpdatasources.BaseRow;
import com.plex.androidsdk.httpdatasources.DataSource;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;
import com.plex.androidsdk.httpdatasources.IBaseInput;
import com.plex.androidsdk.httpdatasources.IDataSourceCallback;
import com.plex.androidsdk.httpdatasources.IDataSourceConnector;
import java.lang.reflect.Type;
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
   * {@inheritDoc}
   */
  protected Container_Get1(IDataSourceCallback callback, HttpDataSourceCredentials credentials, String serverName, boolean test, IDataSourceConnector connector) {
    super(callback, credentials, serverName, test, connector);
  }

  /**
   * Get the Serial No
   *
   * @return String The serial no.
   */
  public String getSerialNo() {
    return inputParameters.getSerialNo();
  }

  /**
   * Method to enable setting the Serial No input parameter.
   *
   * @param serialNo The Serial No to search for.
   */
  public void setSerialNo(String serialNo) {
    inputParameters.setSerialNo(serialNo);
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

  @Override
  protected Type getOutputType() {
    return null;
  }

  @Override
  protected Type getRowType() {
    return Row.class;
  }

  //endregion

  //region INTERNAL CLASSES

  /**
   * Input parameters for data source call. Used by Gson to serialize into JSON.
   */
  private class InputParameters implements IBaseInput {

    @SerializedName("Serial_No")
    private String serialNo;

    String getSerialNo() {
      return serialNo;
    }

    void setSerialNo(String serialNo) {
      this.serialNo = serialNo;
    }
  }

  /**
   * Extends BaseRow to represent the structure of a row returned by Container_Get1.
   */
  public class Row extends BaseRow {

    @SerializedName("Container_Status")
    private String containerStatus;
    @SerializedName("Defect_Type")
    private String defectType;
    @SerializedName("Location")
    private String location;
    @SerializedName("Name")
    private String name;
    @SerializedName("Note")
    private String note;
    @SerializedName("Operation_Code")
    private String operationCode;
    @SerializedName("Operation_Key")
    private int operationKey;
    @SerializedName("Part_Key")
    private int partKey;
    @SerializedName("Part_No_Revision")
    private String partNoRevision;
    @SerializedName("Quantity")
    private BigDecimal quantity;
    @SerializedName("Rework_Operation")
    private int reworkOperation;
    @SerializedName("Special_Instructions")
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
