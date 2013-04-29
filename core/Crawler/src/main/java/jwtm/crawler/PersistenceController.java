/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.crawler;

import jwtm.mysql.MysqlConnector;

/**
 *
 * @author kmadanagopal
 */
public class PersistenceController {
    private static PersistenceController theInstance = new PersistenceController( );
    private static String persistDirectory = "C:\\Users\\kmadanagopal\\Desktop\\Ph";
    private static String dbServer;
    private static String dbServerPort;
    private static String dbName;
    private static String dbUser;
    private static String dbPwd;
    private static MysqlConnector sqlConn= null;
    private PersistenceController(){                 
    }
             
    public static PersistenceController getInstance( ) {
      if(theInstance == null) {
         theInstance = new PersistenceController();
      }
      return theInstance;
   }
               
    public void setPersistDirectory(String perDirectory ) {
      persistDirectory = perDirectory;
   }
    
     public String getPersistDirectory( ) {
      return persistDirectory;
   }

    public String getDbServer() {
        return dbServer;
    }

    public void setDbServer(String dbServer) {
        PersistenceController.dbServer = dbServer;
    }

    public String getDbServerPort() {
        return dbServerPort;
    }

    public void setDbServerPort(String dbServerPort) {
        PersistenceController.dbServerPort = dbServerPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        PersistenceController.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        PersistenceController.dbUser = dbUser;
    }

    public String getDbPwd() {
        return dbPwd;
    }

    public void setDbPwd(String dbPwd) {
        PersistenceController.dbPwd = dbPwd;
    }
    
   
}
