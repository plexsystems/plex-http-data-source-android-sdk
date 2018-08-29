/*
 * Copyright 2018 Plex Systems, Inc
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
 */

package com.plex.androidsdk.webservices;

import android.os.AsyncTask;
import android.util.Xml;
import com.plex.androidsdk.webservices.IWebServiceCallback.Progress;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Abstract implementation of AsyncTask to perform web service calls to Plex.
 *
 * https://developer.android.com/training/basics/network-ops/connecting
 */
public abstract class WebServiceTask extends
    AsyncTask<WebServiceRequest, Integer, WebServiceResult> {

  private static final String PRODUCTION_WEB_SERVICE_URL = "https://mercury.plexonline.com/DataSource/Service.asmx";
  private static final String TEST_WEB_SERVICE_URL = "https://hermes.plexonline.com/DataSource/Service.asmx";
  // TODO: Should we use the api URL's instead of mercury & hermes?
//    private static final String PRODUCTION_WEB_SERVICE_URL = "https://devapi.plexonline.com/DataSource/Service.asmx";
//    private static final String TEST_WEB_SERVICE_URL = "https://testapi.plexonline.com/DataSource/Service.asmx";

  private static final String USER_AGENT = "Mozilla/5.0";
  private static final String HTTP_REQUEST_METHOD = "POST";
  private static final String CONTENT_TYPE = "text/xml; charset=utf-8";
  private static final String SOAP_ACTION = "\"http://www.plexus-online.com/DataSource/ExecuteDataSource\"";

  private IWebServiceCallback _callback = null;
  private WebServiceCredentials _credentials = null;
  private boolean _test = false;
  private Map<String, String> _inputParameters = new Hashtable<>();

  public WebServiceTask(IWebServiceCallback callback, WebServiceCredentials credentials) {
    this(callback, credentials, false);
  }

  public WebServiceTask(IWebServiceCallback callback, WebServiceCredentials credentials,
      boolean test) {
    _callback = callback;
    _credentials = credentials;
    _test = test;
  }

  /**
   * Execute the task
   */
  public void execute() {
    this.execute(getWebServiceRequest());
  }

  /**
   * Add input parameters to be sent as part of the SOAP request to the web service.
   */
  public void addInputParameter(String key, String value) {
    _inputParameters.put(key, value);
  }

  /**
   * Perform the web service call in a background thread.
   *
   * @param webServiceRequests Data to performe the web service call.
   * @return The result of the web service call.
   */
  @Override
  protected WebServiceResult doInBackground(WebServiceRequest... webServiceRequests) {
    WebServiceRequest request = webServiceRequests[0];

    WebServiceResult webServiceResult;

    try {
      webServiceResult = callWebService(request);
    } catch (IOException | XmlPullParserException e) {
      publishProgress(Progress.ERROR);
      return new WebServiceResult(e);
    }

    return webServiceResult;
  }

  @Override
  protected void onProgressUpdate(Integer... values) {
    super.onProgressUpdate(values);

    _callback.onProgressUpdate(values[0]);
  }

  @Override
  protected void onPostExecute(WebServiceResult result) {
    super.onPostExecute(result);
    _callback.onWebServiceComplete(result);
    _callback.onFinish();
  }

  /**
   * Get a WebServiceRequest instance for the current request.
   */
  protected WebServiceRequest getWebServiceRequest() {
    return new WebServiceRequest(_credentials, _inputParameters, _test);
  }

  /**
   * Get the Plex data source key for the web service call.
   */
  protected abstract int getDataSourceKey();

  /**
   * Get the calling process.
   */
  protected IWebServiceCallback getCallback() {
    return _callback;
  }

  /**
   * Perform a web service call to the remote server.
   *
   * @param request The WebServiceRequest containing the data to complete the call.
   * @return A WebServiceResult containing the results of the call.
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @throws XmlPullParserException This exception is thrown to signal XML Pull Parser related
   * faults.
   */
  private WebServiceResult callWebService(WebServiceRequest request)
      throws IOException, XmlPullParserException {

    WebServiceResult webServiceResult;
    URL url = new URL(request.getTest() ? TEST_WEB_SERVICE_URL : PRODUCTION_WEB_SERVICE_URL);
    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

    try {
      publishProgress(IWebServiceCallback.Progress.CONNECTION_SUCCESS);

      // Send the SOAP request
      connection.setRequestMethod(HTTP_REQUEST_METHOD);
      connection.setRequestProperty("User-Agent", USER_AGENT);
      connection.setRequestProperty("Content-Type", CONTENT_TYPE);
      connection.addRequestProperty("SOAPAction", SOAP_ACTION);
      connection
          .addRequestProperty("Authorization",
              request.getWebServiceCredentials().getAuthorization());

      connection.setDoOutput(true);
      connection.setChunkedStreamingMode(0);
      connection.setDoInput(true);

      DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
      outStream.writeBytes(getSoapRequest());
      outStream.flush();
      outStream.close();
      publishProgress(IWebServiceCallback.Progress.REQUEST_SENT);

      int responseCode = connection.getResponseCode();
      publishProgress(Progress.RESPONSE_RECEIVED);

      if (responseCode == 200) {
        publishProgress(Progress.PROCESSING_RESULT);
        DataSourceResult resultData = parseXmlResult(connection.getInputStream());
        webServiceResult = new WebServiceResult(resultData, responseCode);
      } else {
        webServiceResult = new WebServiceResult(null, responseCode);
      }
    } finally {
      connection.disconnect();
    }

    publishProgress(Progress.PROCESSING_RESULT_COMPLETE);

    return webServiceResult;
  }

  /**
   * Get the Soap envelope containing the request to send to the server.
   *
   * @return The Xml Soap envelope
   */
  private String getSoapRequest() {
    StringBuilder soapRequest = new StringBuilder(
        "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:dat=\"http://www.plexus-online.com/DataSource\">");
    soapRequest.append("<soap:Header/><soap:Body><dat:ExecuteDataSource>");
    soapRequest
        .append("<ExecuteDataSourceRequest xmlns=\"http://www.plexus-online.com/DataSource\">");
    soapRequest.append("<DataSourceKey>").append(getDataSourceKey()).append("</DataSourceKey>");
    soapRequest.append("<InputParameters>");

    for (String key : _inputParameters.keySet()) {
      String name = key.startsWith("@") ? key : "@" + key;
      soapRequest.append("<InputParameter><Value>");
      soapRequest.append(_inputParameters.get(key));
      soapRequest.append("</Value><Name>");
      soapRequest.append(name);
      soapRequest.append("</Name></InputParameter>");
    }

    soapRequest.append("</InputParameters>");
    soapRequest
        .append("</ExecuteDataSourceRequest></dat:ExecuteDataSource></soap:Body></soap:Envelope>");

    return soapRequest.toString();
  }

  /**
   * Parse the result Xml into key-value pairs.
   *
   * https://developer.android.com/training/basics/network-ops/xml
   *
   * @param inStream The HttpsURLConnection input stream .
   * @return The parsed result of the data source execution.
   */
  private DataSourceResult parseXmlResult(InputStream inStream)
      throws XmlPullParserException, IOException {

    DataSourceResult result = null;

    XmlPullParser parser = Xml.newPullParser();
    parser.setInput(inStream, "utf-8");

    // Looking for START_TAG for "ExecuteDataSourceResult"
    int eventType = parser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      if (eventType == XmlPullParser.START_TAG) {
        if (parser.getName().equalsIgnoreCase("ExecuteDataSourceResult")) {
          result = parseDataSourceResult(parser);
        }
      }

      eventType = parser.next();
    }

    return result;
  }

  /**
   * Parse the Xml within the ExecuteDataSourceResult tag.
   *
   * @param xpp Instance of XmlPullParser indexed to the ExecuteDataSourceResult tag.
   * @return Parsed result.
   * @throws IOException
   * @throws XmlPullParserException
   */
  private DataSourceResult parseDataSourceResult(XmlPullParser xpp)
      throws IOException, XmlPullParserException {

    int statusNo = 0;
    boolean error = false;
    int errorNo = 0;
    String message = "";
    int instanceNo = 0;
    List resultsets = null;

    int eventType = xpp.next();
    boolean done = false;
    while (!done) {
      if (eventType == XmlPullParser.END_TAG && xpp.getName()
          .equalsIgnoreCase("ExecuteDataSourceResult")) {
        done = true;
      } else {
        if (eventType == XmlPullParser.START_TAG) {
          switch (xpp.getName().toLowerCase()) {
            case "statusno":
              statusNo = Integer.valueOf(xpp.nextText());
              break;
            case "error":
              error = Boolean.valueOf(xpp.nextText());
              break;
            case "errorno":
              errorNo = Integer.valueOf(xpp.nextText());
              break;
            case "message":
              message = xpp.nextText();
              break;
            case "instanceno":
              instanceNo = Integer.valueOf(xpp.nextText());
              break;
            case "resultsets":
              resultsets = parseResultSets(xpp);
              break;
          }
        }

        eventType = xpp.next();
      }
    }

    DataSourceResult dsResult = new DataSourceResult(statusNo, error, errorNo, message, instanceNo,
        resultsets);
    return dsResult;
  }

  /**
   * Parse the data within the ResultSets tag.
   *
   * @param xpp XmlPullParser indexed to the ResultSets tag.
   * @return Parsed ResultSets
   * @throws IOException
   * @throws XmlPullParserException
   */
  private List parseResultSets(XmlPullParser xpp) throws IOException, XmlPullParserException {

    List resultsets = new ArrayList();
    List currentRows = null;
    Map<String, String> currentColumns = null;
    String currentValue = null;
    String currentName = null;

    int eventType = xpp.next();
    boolean done = false;
    while (!done) {
      if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("resultsets")) {
        done = true;
      } else {
        if (eventType == XmlPullParser.START_TAG) {
          switch (xpp.getName().toLowerCase()) {
            case "resultset":
              break;
            case "rows":
              currentRows = new ArrayList();
              break;
            case "row":
              break;
            case "columns":
              currentColumns = new HashMap<String, String>();
              break;
            case "column":
              currentValue = null;
              currentName = null;
              break;
            case "value":
              currentValue = xpp.nextText();
              break;
            case "name":
              currentName = xpp.nextText();
              break;
          }
        } else if (eventType == XmlPullParser.END_TAG) {
          switch (xpp.getName().toLowerCase()) {
            case "resultset":
              break;
            case "rows":
              resultsets.add(currentRows);
              currentRows = null;
              break;
            case "row":
              break;
            case "columns":
              currentRows.add(currentColumns);
              currentColumns = null;
              break;
            case "column":
              currentColumns.put(currentName, currentValue);
              break;
          }
        }

        eventType = xpp.next();
      }
    }

    return resultsets;
  }
}