/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.textsummarizer;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwtm.mysql.MysqlConnector;

/**
 *
 * @author kmadanagopal
 */
public class Summarizer{
    private String dbServer;
    private String dbServerPort;
    private String dbName;
    private String dbUser;
    private String dbPwd;
    
    public Summarizer(String dbServer, String dbServerPort,String dbName, String dbUser, String dbPwd)
    {
        this.dbServer = dbServer;
        this.dbServerPort = dbServerPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPwd = dbPwd;     
    }
    
    public void summarize()
    {
        summarize("");
    }
    
    public void summarize(String userid)
    {
        TextSummarizer summariser = new TextSummarizer();
        String selectQuery;
        if(!userid.isEmpty())            
            selectQuery = "select uid,title,extractedText from PersonalHerald.TextExtractor where uid in (select crawler.uid from UserPref join crawler on UserPref.url = crawler.parentUrl where userid = \'"+userid+"\')";
        else
            selectQuery = "select uid,title,extractedText from PersonalHerald.TextExtractor";
        MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        try {
            ResultSet rs = conn.executeSelectQuery(selectQuery);
            
            while(rs.next()) {
               String uid = rs.getString("uid");
               String title = rs.getString("title");
               String text = rs.getString("extractedText");               
               String summary = summariser.summarize(text,5);
               persistData(uid,title,summary);
            }
            conn.closeConnection();
        } catch (Exception ex) {
            Logger.getLogger(Summarizer.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    private void persistData(String uid, String title, String text)
    {
        try {         
             title = title.replace("'", "\\'");
             text = text.replace("'", "\\'");
             if(title.isEmpty() || text.isEmpty()) return;
             String insertString = "INSERT INTO Summarizer (uid,title,summary) VALUES (\'"+uid+"\',\'"+title+"\', \'"+text+"\') ";                          
             MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);
             conn.executeInsertQuery(insertString);
             
         } catch (Exception ex) {
             Logger.getLogger(Summarizer.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}
