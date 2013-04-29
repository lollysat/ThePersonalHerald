package jwtm.integrationtest;

import java.net.MalformedURLException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import jwtm.articleranker.ClassifyCategory;
import jwtm.clusteringengine.ClusteringEngine;
import jwtm.feedreader.RSSFeedReader;
import jwtm.mysql.MysqlConnector;
import jwtm.textextractor.Extractor;
import jwtm.textsummarizer.Summarizer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws MalformedURLException, SQLException
    {
        String dbServer = "localhost";
        String dbServerPort = "3307";
        String dbName = "PersonalHerald";
        String dbUser = "root";
        String dbPwd = "helloworld2"; 
        
        //Clear all the tables except userPref and users
        MysqlConnector conn= new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        
        //Use the statement below to clear all the tables irrespective of user
        //CallableStatement stmt = conn.getConn().prepareCall("call personalHerald.clear_all_procedure");
        
        //Use the statement below to clear all the datas for a specific user
        String userid = "UserA";
        CallableStatement stmt = conn.getConn().prepareCall("call personalHerald.clear_all_procedure_user(\""+ userid +"\")");
        stmt.execute();        
        conn.closeConnection();
       
        
        //Read the feeds
        System.out.println("mining feeds...");
        RSSFeedReader reader = new RSSFeedReader(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        reader.readFeeds(userid);  
        
        //Extract the text
        System.out.println("extracting text...");
        Extractor extractor = new Extractor(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        extractor.extract(userid);   
        
        //Extract the Summary
        System.out.println("extracting summary...");
        Summarizer summarizer = new Summarizer(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        summarizer.summarize(userid);
        
        //find the article category
        System.out.println("finding category...");
        ClassifyCategory classifier = new ClassifyCategory(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        classifier.classify(userid);
        
        //Cluster Results by duplicates
        System.out.println("finding duplicates...");
        ClusteringEngine clsEngine = new ClusteringEngine(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        clsEngine.cluster(userid);
    }
    
    
}
