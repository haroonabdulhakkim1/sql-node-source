# nodes-in-SQL
Rundeck NODES in a SQL database. A 'Resource Model' plugin

## Installation
  * Clone this repository or download the zip file and expand. Build using `gradle` wrapper:
    ```
      ./gradlew clean build
    ```
  * Drop `rundeck-sql-nodes-plugin-<version>.jar` to `libext/` under Rundeck installation directory.
  * Add the settings to $RDECK_BASE/server/config/rundeck-config.properties
  * Restart Rundeck.

## Configuration

The provider name is: `sql-resource-model-source`

The properties can be configured via Rundeck GUI. Below are the configuration properties:

* `sqlQuery`: SQL Query to fetch the node details
* `connectionUrl`: JDBC Connection URL
* `username` : Username
* `password`: Password
* `jdbcDriver`: Select from the dropdown options or type the driver class

### Database Structure

```
CREATE TABLE dbo.nodes (
    id INT PRIMARY KEY IDENTITY (1, 1),
    projectname VARCHAR (50) NOT NULL,
    nodename VARCHAR (50) NOT NULL,
    hostname VARCHAR (50) NOT NULL,
    username VARCHAR (50),
    [node-executor] VARCHAR (50),
    [file-copier] VARCHAR (50),
    [cred-type] VARCHAR (50),
    [cred-path] VARCHAR (50),
    description VARCHAR (50),
    tags VARCHAR (50),
    osFamily VARCHAR (50),
    osName VARCHAR (50),
    osArch VARCHAR (50),
    osVersion VARCHAR (50),
    location VARCHAR (50),
    customer VARCHAR (50)
); 
```
