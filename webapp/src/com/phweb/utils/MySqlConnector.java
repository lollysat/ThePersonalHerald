package com.phweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnector {
    
    private Connection conn = null;
    private String srvName = "";
    private String srvPort = "";
    private String dbName = "";
    private String uName = "";
    private String pwd = "";
    
    public MySqlConnector(String srvName, String srvPort, String dbName, String uName, String pwd )
    {
        this.srvName = srvName;
        this.srvPort = srvPort;
        this.dbName = dbName;
        this.uName = uName;
        this.pwd = pwd;        
        createConnection();
    }
    public Connection getConn() {
        return conn;
    }
    public void closeConnection() throws SQLException
    {
        conn.close();
    }
    
    private final void createConnection()
    {
        String url = "jdbc:mysql://"+srvName+":"+srvPort+"/";        
        String driver = "com.mysql.jdbc.Driver";                
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url+dbName,uName,pwd);
        }
        catch(Exception ex)
        {
        	System.err.println(ex.getMessage());
        	ex.printStackTrace();
            
        }
    }
    
    public ResultSet executeSelectQuery(String selectQuery) throws SQLException
    {
        if(conn == null)
            return null;
        Statement stmt = conn.createStatement() ;        
        java.sql.ResultSet rs = stmt.executeQuery(selectQuery) ;
        return rs;
    }
    
    public int executeInsertQuery(String selectQuery) throws SQLException
    {
        if(conn == null)
            return -1;
        
        Statement stmt = conn.createStatement() ;        
        return stmt.executeUpdate(selectQuery) ;        
    }
    
}

