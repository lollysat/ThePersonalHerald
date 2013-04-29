/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.textextractor;

import java.net.URL;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwtm.mysql.MysqlConnector;

/**
 *
 * @author kmadanagopal
 */
public class Extractor {
    private String dbServer;
    private String dbServerPort;
    private String dbName;
    private String dbUser;
    private String dbPwd;
    
    public Extractor(String dbServer, String dbServerPort,String dbName, String dbUser, String dbPwd)
    {
        this.dbServer = dbServer;
        this.dbServerPort = dbServerPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPwd = dbPwd;
    }
    
    public void extract()
    {
        extract("");
    }
    
    public void extract(String userid)
    {        
        TextExtractor txtExtractor = new TextExtractor();      
        //ImageExtractor imgExtractor = new ImageExtractor();
        String selectQuery;
        if(!userid.isEmpty())
            selectQuery = "select distinct url,uid,filePath from PersonalHerald.Crawler where parenturl in (select url from userpref where userid = \'"+ userid +"\')";
        else
            selectQuery = "select distinct url,uid,filePath from PersonalHerald.Crawler";
        MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        HashSet<String> extractedURLs = new HashSet<String>();
        try {
            ResultSet rs = conn.executeSelectQuery(selectQuery);           
            while(rs.next()) {                
               String uid = rs.getString("uid");
               String url = rs.getString("url");
               if(extractedURLs.contains(url)) continue;
               String title =  TitleExtractor.getPageTitle(url);    
               String img = "";//imgExtractor.extractImage(url);
               String text = txtExtractor.extractText(url);                                             
               persistData(uid,title,text,img,conn);
               extractedURLs.add(url);
            }
            conn.closeConnection();
        }
        catch (Exception ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }
            
    private void persistData(String uid, String title, String text, String img, MysqlConnector conn)
    {
        try {     
             title = title.replace("'", "\\'");             
             text = text.replace("'", "\\'");             
             if(title.isEmpty() || text.isEmpty()) return;
             String insertString = "INSERT INTO TextExtractor (uid,title,extractedText, image) VALUES (\'"+uid+"\',\'"+title+"\', \'"+text+"\', \'"+img+"\') ";                 
             //System.out.println(insertString);
             conn.executeInsertQuery(insertString);             
         } catch (Exception ex) {
             Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}
