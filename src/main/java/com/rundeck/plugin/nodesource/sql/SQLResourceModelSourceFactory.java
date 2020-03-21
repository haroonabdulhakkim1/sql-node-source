package com.rundeck.plugin.nodesource.sql;

import com.dtolabs.rundeck.core.common.Framework;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.*;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceFactory;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;

import java.util.*;

import static com.rundeck.plugin.nodesource.sql.SQLResourceModelSourceFactory.PROVIDER_NAME;

@Plugin(name = PROVIDER_NAME, service = ServiceNameConstants.ResourceModelSource)
public class SQLResourceModelSourceFactory implements ResourceModelSourceFactory, Describable {
    public static final String PROVIDER_NAME = "sql-resource-model-source";
    private Framework framework;

    public static final String SQL_QUERY = "sqlQuery";
    public static final String CONNECTION_URL = "connectionUrl";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String JDBC_DRIVER = "jdbcDriver";
    public static final String MS_SQL_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String MY_SQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String POST_GRE_SQL_JDBC_DRIVER = "org.postgresql.Driver";
    public static final String ORACLE_JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String MS_SQL_JDBC_DRIVER_LABEL = "MS-SQL";
    public static final String MY_SQL_JDBC_DRIVER_LABEL = "MySQL";
    public static final String POST_GRE_SQL_JDBC_DRIVER_LABEL = "PostgreSQL";
    public static final String ORACLE_JDBC_DRIVER_LABEL = "Oracle";
    public static final List<String> jdbcDriverList = new ArrayList<>(Arrays.asList(MS_SQL_JDBC_DRIVER,
            MY_SQL_JDBC_DRIVER,POST_GRE_SQL_JDBC_DRIVER,ORACLE_JDBC_DRIVER));
    public static Map<String,String> jdbcDriverLabelMap = new HashMap<>();

    static {
        jdbcDriverLabelMap.put(MS_SQL_JDBC_DRIVER,MS_SQL_JDBC_DRIVER_LABEL);
        jdbcDriverLabelMap.put(MY_SQL_JDBC_DRIVER,MY_SQL_JDBC_DRIVER_LABEL);
        jdbcDriverLabelMap.put(POST_GRE_SQL_JDBC_DRIVER,POST_GRE_SQL_JDBC_DRIVER_LABEL);
        jdbcDriverLabelMap.put(ORACLE_JDBC_DRIVER,ORACLE_JDBC_DRIVER_LABEL);
    }
    public SQLResourceModelSourceFactory(final Framework framework) {
        this.framework = framework;
    }

    @Override
    public ResourceModelSource createResourceModelSource(Properties configuration) throws ConfigurationException {
        final SQLResourceModelSource sqlResourceModelSource = new SQLResourceModelSource(configuration);
        return sqlResourceModelSource;
    }

    static Description DESC = DescriptionBuilder.builder()
            .name(PROVIDER_NAME)
            .title("SQL Resource Model Source")
            .description("Obtains node information from SQL Databases")
            .property(PropertyUtil.string(SQL_QUERY,"SQL query","e.g. select * from nodes where projectname = 'project_one'",true,null))
            .property(PropertyUtil.string(CONNECTION_URL,"Connection URL","e.g. jdbc:sqlserver://my-server;DatabaseName=mynodesDB",true,"jdbc:"))
            .property(PropertyUtil.string(USERNAME,"Username",null,true,null))
            .property(PropertyUtil.string(PASSWORD,"Password",null,true,null,null,null, Collections.singletonMap(StringRenderingConstants.DISPLAY_TYPE_KEY, (Object) StringRenderingConstants.DisplayType.PASSWORD)))
            .property(PropertyUtil.freeSelect(JDBC_DRIVER,"JDBC driver name",null,true,null,jdbcDriverList,jdbcDriverLabelMap,null,null,null))
            .build();

    @Override
    public Description getDescription() {
        return DESC;
    }
}
