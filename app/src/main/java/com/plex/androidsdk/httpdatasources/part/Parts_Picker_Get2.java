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


import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import com.plex.androidsdk.httpdatasources.BaseRow;
import com.plex.androidsdk.httpdatasources.DataSource;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;
import com.plex.androidsdk.httpdatasources.IBaseInput;
import com.plex.androidsdk.httpdatasources.IDataSourceCallback;
import com.plex.androidsdk.httpdatasources.IDataSourceConnector;

/**
 * Data source: Parts_Picker_Get2
 */
public class Parts_Picker_Get2 extends DataSource {

  private Parts_Picker_Get2.InputParameters inputParameters = new Parts_Picker_Get2.InputParameters();

  /**
   * {@inheritDoc} Shortcut constructor for production.
   */
  Parts_Picker_Get2(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName) {
    this(iDataSourceCallback, credentials, serverName, false);
  }

  /**
   * {@inheritDoc}
   */
  Parts_Picker_Get2(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName, boolean useTestServer) {
    super(iDataSourceCallback, credentials, serverName, useTestServer);
  }

  /**
   * {@inheritDoc}
   */
  protected Parts_Picker_Get2(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName,
      boolean useTestServer,
      IDataSourceConnector connector) {
    super(iDataSourceCallback, credentials, serverName, useTestServer, connector);
  }

  /**
   * Get the current part no filter.
   *
   * @return The current part no filter.
   */
  public String getPartNo() {
    return inputParameters.getPartNo();
  }

  /**
   * Set the part no filter.
   *
   * @param partNo The part no to filter on.
   */
  public void setPartNo(String partNo) {
    inputParameters.setPartNo(partNo);
  }

  @Override
  protected int getDataSourceKey() {
    return 1791;
  }

  @Override
  protected IBaseInput getBaseInput() {
    return inputParameters;
  }

  @Override
  protected BaseRow parseRow(JsonArray rowArray) {
    Row row = null;

    // Make sure there is data
    if (rowArray.size() > 0) {
      row = new Row();

      // Loop through all the fields and assign the data to Row.
      for (int columnIndex = 0; columnIndex < rowArray.size(); ++columnIndex) {
        switch (columnIndex) {
          case 0: // Part Key
            row.setPartKey(rowArray.get(0).getAsInt());
            break;
          case 1: // Part No Revision
            row.setPartNoRevision(rowArray.get(1).getAsString());
            break;
          case 2: // Name
            row.setName(rowArray.get(2).getAsString());
            break;
          case 3: // Part Status
            row.setPartStatus(rowArray.get(3).getAsString());
            break;
          case 4: // Old Part No
            row.setOldPartNo(rowArray.get(4).getAsString());
            break;
        }
      }
    }

    return row;
  }

  //region INTERNAL CLASSES

  /**
   * Input parameters for data source call. Used by GSON to serialize into JSON.
   */
  private class InputParameters implements IBaseInput {

    @SerializedName("Part_No")
    private String partNo;

    String getPartNo() {
      return partNo;
    }

    void setPartNo(String partNo) {
      this.partNo = partNo;
    }
  }

  /**
   * Extends BaseRow to represent the structure of a row returned by Part_Picker_Get2.
   */
  public class Row extends BaseRow {

    private int partKey;
    private String partNoRevision;
    private String name;
    private String partStatus;
    private String oldPartNo;

    public int getPartKey() {
      return partKey;
    }

    void setPartKey(int partKey) {
      this.partKey = partKey;
    }

    public String getPartNoRevision() {
      return partNoRevision;
    }

    void setPartNoRevision(String partNoRevision) {
      this.partNoRevision = partNoRevision;
    }

    public String getName() {
      return name;
    }

    void setName(String name) {
      this.name = name;
    }

    public String getPartStatus() {
      return partStatus;
    }

    public void setPartStatus(String partStatus) {
      this.partStatus = partStatus;
    }

    public String getOldPartNo() {
      return oldPartNo;
    }

    public void setOldPartNo(String oldPartNo) {
      this.oldPartNo = oldPartNo;
    }
  }

  //endregion
}
