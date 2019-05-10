## How to create a data source

TODO: Still discussing what namespace to put the data source in.

Class name should match the data source name from the Plex Data Source list.


Extend the class from httpdatasource/DataSource

Implement inherited methods:

getDataSourceKey() – Returns the Plex data source key for the http data source.

getBaseInput() – Returns an instance of an internal class that contains the input parameters for the http data source. The class field names need to match the input parameter names of the data source. This can be done by either naming the field the same as the parameter OR by using com.google.gson.annotations.SerializedName. The instance is used by GSON to serialize into JSON.

eg:
```javascript
class InputParameters implements IBaseInput (
  @SerializedName(“Serial_No”)
  String serialNo;
)
```
OR
```javascript
class InputParameters implements IBaseInput (
  String Serial_No;
)
```
will serialize into
```
{
  "inputs": {
    "Serial_No" : "ST9997141"
  }
}
```

Also, create methods to get/set the InputParameters properties.

getBaseOutput() – Returns any output parameters of the http data source.

parseRow() – Used by the data source to parse the result Json row data into a BaseRow instance. Usually create an internal Row class that extends BaseRow, and contains properties that match the columns of the data source result set.


Remember to create unit tests.
