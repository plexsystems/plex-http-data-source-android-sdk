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
import com.plex.androidsdk.httpdatasources.DataSourceResult;
import com.plex.androidsdk.httpdatasources.HttpDataSourceCredentials;
import com.plex.androidsdk.httpdatasources.HttpDataSourceError;
import com.plex.androidsdk.httpdatasources.HttpDataSourceErrors;
import com.plex.androidsdk.httpdatasources.IHttpDataSourceCallback;
import com.plex.androidsdk.httpdatasources.Inventory.Container_Get1;

/**
 * This Activity demonstrates the use of an http data source task to retrieve the data for a container.
 */
public class ExampleActivity extends AppCompatActivity implements IHttpDataSourceCallback, OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
    }

    /**
     * Perform the search using the Container_Get1 http data source task.
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
                // Perform the http data source call.
                HttpDataSourceCredentials credentials = new HttpDataSourceCredentials(userName, password);
                boolean useTestServer = true;
                String serverName = "cloud"; // This is dependent on where your data is stored.

                Container_Get1 containerGet1 = new Container_Get1(this, credentials, serverName, useTestServer);
                containerGet1.setSerialNo(serialNo);
                containerGet1.execute();
            }
        }
    }

    /**
     * Handle the result from the http data source call.
     *
     * @param dataSourceResult The HttpDataSourceResult containing the data source result.
     */
    @Override
    public void onHttpDataSourceComplete(DataSourceResult dataSourceResult) {
        if (dataSourceResult.getException() == null) {  // Was there an exception during the execution?
            if (dataSourceResult.isError() == false) {  // Was the http request successful?
                // For this data source there should only ever by 1 row because it's a GET method..
                if (dataSourceResult.getTable().getRows().size() > 0) {
                    // Display the data
                    Container_Get1.Row row = (Container_Get1.Row) dataSourceResult.getTable().getRows().get(0);
                    ((TextView) findViewById(R.id.partNoRevision)).setText(row.getPartNoRevision());
                    ((TextView) findViewById(R.id.partName)).setText(row.getName());
                    ((TextView) findViewById(R.id.operationCode)).setText(row.getOperationCode());
                    ((TextView) findViewById(R.id.quantity)).setText(row.getQuantity().toString());
                    ((TextView) findViewById(R.id.status)).setText(row.getContainerStatus());
                    ((TextView) findViewById(R.id.location)).setText(row.getLocation());
                    ((TextView) findViewById(R.id.note)).setText(row.getNote());
                } else {
                    // No records found. Display appropriate message.
                    ((TextView) findViewById(R.id.error)).setText("No record(s) found");
                }
            } else {
                HttpDataSourceErrors httpDataSourceErrors = dataSourceResult.getHttpDataSourceErrors();
                if (httpDataSourceErrors != null) {
                    HttpDataSourceError[] errors = httpDataSourceErrors.getErrors();
                    if (errors != null && errors.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Error: ").append(errors[0].getCode()).append(" - ").append(errors[0].getMessage());
                        ((TextView) findViewById(R.id.error)).setText(sb.toString());
                    }
                } else {
                    ((TextView) findViewById(R.id.error)).setText("An unspecified error occured");
                }

            }
        } else {
            // Exception was thrown during execution.
            ((TextView) findViewById(R.id.error)).setText(dataSourceResult.getException().getMessage());
        }
    }

    /**
     * Display the progress of the http data source call to the user.
     *
     * @param progressCode One of the constants defined in IHttpDataSourceCallback.Progress.
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
