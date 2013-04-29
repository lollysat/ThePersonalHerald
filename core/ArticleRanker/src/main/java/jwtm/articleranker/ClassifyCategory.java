/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.articleranker;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwtm.mysql.MysqlConnector;

/**
 *
 * @author kmadanagopal
 */
public class ClassifyCategory {
    private String dbServer;
    private String dbServerPort;
    private String dbName;
    private String dbUser;
    private String dbPwd;
    
    public ClassifyCategory(String dbServer, String dbServerPort,String dbName, String dbUser, String dbPwd)
    {
        this.dbServer = dbServer;
        this.dbServerPort = dbServerPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPwd = dbPwd;
        
    }
    
    public void classify()
    {
        classify("");
    }
    
    public void classify(String userid)
    {
        try
        {
            MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);            
            Classifier classifier = loadClassifier();

            String selectQuery;
            if(!userid.isEmpty())
                selectQuery = "select uid, title, summary from summarizer where uid in (select crawler.uid from UserPref join crawler on UserPref.url = crawler.parentUrl where userid = \'"+userid+"\')";
            else                
                selectQuery = "select uid, title, summary from summarizer";
                                    
            try {
                ResultSet rs = conn.executeSelectQuery(selectQuery);
                while(rs.next()) {
                    String uid = rs.getString(1);
                    String text = rs.getString(2) + " "+rs.getString(3);

                    Classification cf =  classifier.classify(text);
                    if(cf != null)
                        persistData(uid,cf.getLabeling().getBestLabel().toString(),conn);
                }            
            } catch (SQLException ex) {
                Logger.getLogger(ClassifyCategory.class.getName()).log(Level.SEVERE, null, ex);
            }            
            conn.closeConnection();           
        }
        catch(Exception ex)
        {
            Logger.getLogger(ClassifyCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Classifier loadClassifier() throws FileNotFoundException, IOException, ClassNotFoundException {                                         
        
        Classifier classifier;
        InputStream modelIn = ClassifyCategory.class.getClassLoader().getResourceAsStream("jwtm/classifier/news-group.classifier");
        ObjectInputStream ois = new ObjectInputStream (modelIn);
        classifier = (Classifier) ois.readObject();
        ois.close();

        return classifier;
    }
    
    public void persistData(String uid, String category, MysqlConnector conn)
    {
        String updateString = "UPDATE personalherald.summarizer\n" +
                                "SET category=\""+category+"\"\n" +
                                "WHERE uid=\""+uid+"\"";
        try {
            PreparedStatement pstmt = conn.getConn().prepareStatement(updateString);
            pstmt.executeUpdate();                                               
        } catch (SQLException ex) {
            Logger.getLogger(ClassifyCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

