package com.rundeck.plugin.nodesource.sql;

import com.dtolabs.rundeck.core.common.INodeSet;
import com.dtolabs.rundeck.core.common.NodeEntryImpl;
import com.dtolabs.rundeck.core.common.NodeSetImpl;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceException;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

public class SQLResourceModelSource implements ResourceModelSource {
    private final Properties configuration;

    public SQLResourceModelSource(Properties configuration) {
        this.configuration = configuration;
    }

    @Override
    public INodeSet getNodes() throws ResourceModelSourceException {
        String sqlQuery = configuration.getProperty(SQLResourceModelSourceFactory.SQL_QUERY);
        String connectionUrl = configuration.getProperty(SQLResourceModelSourceFactory.CONNECTION_URL);
        String username = configuration.getProperty(SQLResourceModelSourceFactory.USERNAME);
        String password = configuration.getProperty(SQLResourceModelSourceFactory.PASSWORD);
        String jdbcDriver = configuration.getProperty(SQLResourceModelSourceFactory.JDBC_DRIVER);

        return executeSQLQuery(sqlQuery,connectionUrl,username,password,jdbcDriver);
    }

    private INodeSet mapResultSetToNodeSet(ResultSet rs) {
        if(rs==null)
            return null;
        NodeSetImpl nodeSet = new NodeSetImpl();
        try {
            while (rs.next()) {
                String projectname = rs.getString("projectname");
                String nodename = rs.getString("nodename");
                String hostname = rs.getString("hostname");
                String username = rs.getString("username");
                String nodeexecutor = rs.getString("node-executor");
                String filecopier = rs.getString("file-copier");
                String credtype = rs.getString("cred-type");
                String credpath = rs.getString("cred-path");
                String description = rs.getString("description");
                String tags = rs.getString("tags");
                String osFamily = rs.getString("osFamily");
                String osVersion = rs.getString("osVersion");
                String location = rs.getString("location");
                String customer = rs.getString("customer");

                NodeEntryImpl node = new NodeEntryImpl();

                node.setNodename(nodename);
                node.setHostname(hostname);
                node.setUsername(username);
                node.setFrameworkProject(projectname);


                node.setDescription(description);
                if(tags!=null&&tags.length()>0)
                    node.setTags(new HashSet(Arrays.asList(tags.split(","))));
                node.setOsFamily(osFamily);
                node.setOsName(osFamily); //Need to confirm
                node.setOsVersion(osVersion);
                node.setAttribute("node-executor",nodeexecutor);
                node.setAttribute("file-copier",filecopier);
                node.setAttribute("cred-type",credtype);
                node.setAttribute("cred-path",credpath);
                node.setAttribute("location",location);
                node.setAttribute("customer",customer);

                nodeSet.putNode(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeSet;
    }

    private INodeSet executeSQLQuery(String sqlQuery, String connectionUrl, String username, String password, String jdbcDriver) throws ResourceModelSourceException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs;
        INodeSet nodeSet;
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(connectionUrl, username, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQuery);
            nodeSet = mapResultSetToNodeSet(rs);
        } catch (Exception e){
            e.printStackTrace();
            throw new ResourceModelSourceException(e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nodeSet;
    }
}
