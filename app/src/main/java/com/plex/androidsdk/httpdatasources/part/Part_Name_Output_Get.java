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

import com.google.gson.annotations.SerializedName;
import com.plex.androidsdk.httpdatasources.BaseOutputs;
import com.plex.androidsdk.httpdatasources.DataSource;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;
import com.plex.androidsdk.httpdatasources.IBaseInput;
import com.plex.androidsdk.httpdatasources.IDataSourceCallback;
import com.plex.androidsdk.httpdatasources.IDataSourceConnector;
import java.lang.reflect.Type;

/**
 * Data source: Part_Name_Output_get
 */
public class Part_Name_Output_Get extends DataSource {

  private Part_Name_Output_Get.InputParameters _inputParameters = new Part_Name_Output_Get.InputParameters();

  /**
   * {@inheritDoc} Shortcut constructor for production.
   */
  public Part_Name_Output_Get(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName) {
    this(iDataSourceCallback, credentials, serverName, false);
  }

  /**
   * {@inheritDoc}
   */
  public Part_Name_Output_Get(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName,
      boolean useTestServer) {
    super(iDataSourceCallback, credentials, serverName, useTestServer);
  }

  /**
   * {@inheritDoc}
   */
  protected Part_Name_Output_Get(IDataSourceCallback iDataSourceCallback, HttpDataSourceCredentials credentials, String serverName,
      boolean useTestServer,
      IDataSourceConnector connector) {
    super(iDataSourceCallback, credentials, serverName, useTestServer, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int getDataSourceKey() {
    return 721;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IBaseInput getBaseInput() {
    return _inputParameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Type getOutputType() {
    return OutputParameters.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Type getRowType() {
    return null;
  }

  /**
   * Get the Part Key input parameter.
   *
   * @return The current Part Key.
   */
  public int getPartKey() {
    return _inputParameters.getPartKey();
  }

  /**
   * Set the Part Key input parameter.
   *
   * @param partKey The Part Key to get the name of.
   */
  public void setPartKey(int partKey) {
    _inputParameters.setPartKey(partKey);
  }

  //region INTERNAL CLASSES

  /**
   * Input parameters for data source call. Used by GSON to serialize into JSON.
   */
  private class InputParameters implements IBaseInput {

    @SerializedName("Part_Key")
    private int partKey;

    int getPartKey() {
      return partKey;
    }

    void setPartKey(int partKey) {
      this.partKey = partKey;
    }
  }

  class OutputParameters extends BaseOutputs {

    public String Name;
  }
  //endregion
}
