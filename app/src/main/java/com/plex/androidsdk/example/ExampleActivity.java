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

package com.plex.androidsdk.example;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.plex.androidsdk.R;
import com.plex.androidsdk.webservices.DataSourceResult;
import com.plex.androidsdk.webservices.IWebServiceCallback;
import com.plex.androidsdk.webservices.Inventory.Container_Get1;
import com.plex.androidsdk.webservices.WebServiceCredentials;
import com.plex.androidsdk.webservices.WebServiceResult;
import java.util.Map;

/**
 * This Activity demonstrates the use of a web service task to retrieve the data for a container.
 */
public class ExampleActivity extends AppCompatActivity implements IWebServiceCallback,
    OnClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    // Link the search button click to this Activity.
    Button searchButton = (Button) findViewById(R.id.searchButton);
    searchButton.setOnClickListener(this);
  }

  /**
   * Perform the search using the Container_Get1 web service task.
   */
  public void onSearch() {
    String userName = ((EditText) findViewById(R.id.userName)).getText().toString();
    String password = ((EditText) findViewById(R.id.password)).getText().toString();

    if (userName.isEmpty() || password.isEmpty()) {
      ((TextView) findViewById(R.id.statusUpdate))
          .setText("User name and password can not be blank");
    } else {
      String serialNo = ((EditText) findViewById(R.id.serialNo)).getText().toString();
      if (serialNo.isEmpty()) {
        ((TextView) findViewById(R.id.statusUpdate)).setText("You must provide a serial number");
      } else {
        // Perform the web service call.
        WebServiceCredentials credentials = new WebServiceCredentials(userName, password);
        boolean useTestServer = true;

        Container_Get1 containerGet1 = new Container_Get1(this, credentials, useTestServer);
        containerGet1.addInputParameter("Serial_No", serialNo);
        containerGet1.execute();
      }
    }
  }

  /**
   * Handle the result from the web service call.
   *
   * @param result The WebServiceResult containing the data source result.
   */
  @Override
  public void onWebServiceComplete(WebServiceResult result) {
    if (result.getException() == null) {  // Was there an exception during the execution?
      if (result.getHTTPResponseCode() == 200) {  // Was the http request successful?
        DataSourceResult dsResult = result.getDataSourceResult();

        if (dsResult.isError() == false) { // Was there a data source error?

          if (dsResult.getResultSets() != null) { // Was there any data returned?

            // Step through the returned columns to pull out the information to display
            Map<String, String> column = result.getDataSourceResult().getFirstColumn();
            for (Map.Entry<String, String> pair : column.entrySet()) {
              switch (pair.getKey().toLowerCase()) {
                case "part_no_revision":
                  ((TextView) findViewById(R.id.partNoRevision)).setText(pair.getValue());
                  break;
                case "name":
                  ((TextView) findViewById(R.id.partName)).setText(pair.getValue());
                  break;
                case "operation_code":
                  ((TextView) findViewById(R.id.operationCode)).setText(pair.getValue());
                  break;
                case "quantity":
                  ((TextView) findViewById(R.id.quantity)).setText(pair.getValue());
                  break;
                case "container_status":
                  ((TextView) findViewById(R.id.status)).setText(pair.getValue());
                  break;
                case "location":
                  ((TextView) findViewById(R.id.location)).setText(pair.getValue());
                  break;
                case "note":
                  ((TextView) findViewById(R.id.note)).setText(pair.getValue());
                  break;
              }
            }
          } else {
            // If no data returned then no records found
            ((TextView) findViewById(R.id.error)).setText("No record(s) found");
          }
        } else {
          // Data source error
          StringBuilder sb = new StringBuilder();
          sb.append("Error: ").append(dsResult.getErrorNo()).append(" - ")
              .append(dsResult.getMesssage());

          ((TextView) findViewById(R.id.error)).setText(sb.toString());
        }
      } else {
        // http request was not successful.
        StringBuilder sb = new StringBuilder();
        sb.append("Http response code: ").append(result.getHTTPResponseCode())
            .append(" - Something went wrong!");
        ((TextView) findViewById(R.id.error)).setText(sb.toString());
      }
    } else {
      // Exception was thrown during execution.
      ((TextView) findViewById(R.id.error)).setText(result.getException().getMessage());
    }
  }

  /**
   * Display the progress of the web service call to the user.
   *
   * @param progressCode One of the constants defined in IWebServiceCallback.Progress.
   */
  @Override
  public void onProgressUpdate(int progressCode) {
    String statusMessage = "";

    switch (progressCode) {
      case Progress.CONNECTION_SUCCESS:
        statusMessage = "Connection Success";
        break;
      case Progress.ERROR:
        statusMessage = "Error";
        break;
      case Progress.REQUEST_SENT:
        statusMessage = "Request sent";
        break;
      case Progress.RESPONSE_RECEIVED:
        statusMessage = "Response received";
        break;
      case Progress.PROCESSING_RESULT:
        statusMessage = "Processing result";
        break;
      case Progress.PROCESSING_RESULT_COMPLETE:
        statusMessage = "Complete";
        break;
    }

    ((TextView) findViewById(R.id.statusUpdate)).setText(statusMessage);
  }

  @Override
  public void onFinish() {
    Log.d("ExampleActivity", "Finished");
  }

  /**
   * When the search button is clicked.
   *
   * @param view A reference to the search button.
   */
  @Override
  public void onClick(View view) {
    clearScreen();
    onSearch();
  }

  /**
   * Clears the data values on the screen.
   */
  private void clearScreen() {
    ((TextView) findViewById(R.id.partNoRevision)).setText("");
    ((TextView) findViewById(R.id.partName)).setText("");
    ((TextView) findViewById(R.id.operationCode)).setText("");
    ((TextView) findViewById(R.id.quantity)).setText("");
    ((TextView) findViewById(R.id.status)).setText("");
    ((TextView) findViewById(R.id.location)).setText("");
    ((TextView) findViewById(R.id.note)).setText("");
    ((TextView) findViewById(R.id.error)).setText("");
    ((TextView) findViewById(R.id.statusUpdate)).setText("");
  }
}
