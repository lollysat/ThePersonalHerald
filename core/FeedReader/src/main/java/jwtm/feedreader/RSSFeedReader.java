/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.feedreader;

import jwtm.feedreader.parser.RSSFeedParser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jwtm.feedreader.model.Feed;
import jwtm.feedreader.model.FeedMessage;
import jwtm.mysql.MysqlConnector;
import jwtm.mysql.util.UidGenerator;

/**
 *
 * @author kmadanagopal
 */
public class RSSFeedReader {
    private String dbServer;
    private String dbServerPort;
    private String dbName;
    private String dbUser;
    private String dbPwd;
    
    public RSSFeedReader(String dbServer, String dbServerPort,String dbName, String dbUser, String dbPwd)
    {
        this.dbServer = dbServer;
        this.dbServerPort = dbServerPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPwd = dbPwd;     
    }
    
    public void readFeeds()
    {
        readFeeds("");
    }
    
    public void readFeeds(String userId)
    {
        MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        List<String> urls =  getURLList(conn,userId);
        for(String url : urls)
        {                    
            parse(url,conn);
        }      
        
        try { 
                conn.closeConnection();
            } catch (Exception ex) {
                Logger.getLogger(RSSFeedReader.class.getName()).log(Level.SEVERE, null, ex);
            }     
        
    }
    
    private void parse(String url,MysqlConnector conn)
    {        
        try
        {            
            RSSFeedParser parser = new RSSFeedParser(url);
            Feed feed = parser.readFeed();        
            for (FeedMessage message : feed.getMessages()) {                
                if(url.startsWith("http://www.twitter-rss.com"))
                {
                    String eurl = extractURL(message.getDescription());
                    if(eurl == null)
                        continue;
                    persistData(eurl, url, message.getPubDate(), conn);
                }
                else
                {
                    persistData(message.getLink(), url,message.getPubDate(), conn);
                }
            }
            
        }catch (Exception ex) {
                Logger.getLogger(RSSFeedReader.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
    }
    
   
    
    private String extractURL(String text)
    {
        Pattern pattern = Pattern.compile("http://[a-z][.a-zA-Z/0-9]+");
        Matcher m = pattern.matcher(text);
        
        if (m.find()) {
              return m.group(0);
            } 
        return null;
    }
    
    private void persistData(String url, String parentUrl,String dt, MysqlConnector conn) 
    {
        String uid = UidGenerator.generateUID();        
        Date date = new Date();
        if(dt != null && dt.contains(","))
            try {
                date = new SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH).parse(dt.substring(dt.indexOf(",")+1));
            } catch (ParseException ex) {
                Logger.getLogger(RSSFeedReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        String strdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);                  
         try {                                                 
             String insertString = "INSERT INTO crawler (uid,url, filePath,crawlDate,parentUrl) VALUES (\'"+uid+"\',\'"+url+"\',\'\' ,\'"+strdate+"\',\'"+parentUrl+"\') ";                                      
             conn.executeInsertQuery(insertString);             
         } catch (Exception ex) {
             Logger.getLogger(RSSFeedReader.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    private List<String> getURLList(MysqlConnector conn, String userId)
    {
        ArrayList<String> urls = new ArrayList<String>();
        String selectQuery;
        if(!userId.isEmpty())
            selectQuery = "select distinct url from UserPref where type = \'RSS\' and userid = \'"+userId+"\'";   
        else
            selectQuery = "select distinct url from UserPref where type = \'RSS\'";   
        
        try {
            ResultSet rs = conn.executeSelectQuery(selectQuery);

            while(rs.next()) {
                urls.add(rs.getString(1));
            }            
        } catch (SQLException ex) {
            Logger.getLogger(RSSFeedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return urls;
    }
}
