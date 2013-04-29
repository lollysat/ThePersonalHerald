package jwtm.clusteringengine;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.store.*;
import jwtm.clusteringengine.model.indexer.*;
import jwtm.clusteringengine.model.duplicate.*;
import jwtm.clusteringengine.model.vectorspace.*;
import jwtm.mysql.MysqlConnector;

/**
 * @author Julian Riediger
 *
 */

public class ClusteringEngine {			
	public XMLParser parser;
	public Indexer indexer;
	public SimilarityMatrix simi;
	public ClusterDuplicates duplicate;
	private String dbServer;
        private String dbServerPort;
        private String dbName;
        private String dbUser;
        private String dbPwd;
        
        public ClusteringEngine(String dbServer, String dbServerPort,String dbName, String dbUser, String dbPwd)
        {
            this.dbServer = dbServer;
            this.dbServerPort = dbServerPort;
            this.dbName = dbName;
            this.dbUser = dbUser;
            this.dbPwd = dbPwd; 
        }
        
	private void startParserModule(Indexer indexer) throws IOException{
		parser = new XMLParser(indexer);
	}
	
	private void startIndexerModule(Directory directory) throws IOException{
		indexer = new Indexer(directory);
	}
        
        private void loadInstances(String userid) throws SQLException, IOException
        {
            MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);
            String selectQuery;
            if(!userid.isEmpty())
                selectQuery = "select uid,title,summary from PersonalHerald.summarizer";
            else
                selectQuery = "select uid,title,summary from PersonalHerald.summarizer where uid in (select crawler.uid from UserPref join crawler on UserPref.url = crawler.parentUrl where userid = \'"+userid+"\')";            
            try {
                    ResultSet rs = conn.executeSelectQuery(selectQuery);

                    while(rs.next()) {
                       String uid = rs.getString("uid");
                       String title = rs.getString("title");
                       String summary = rs.getString("summary");               
                       indexer.addDocument(title, summary,uid);                       
                    }
                    conn.closeConnection();
                } catch (Exception ex) {
                    Logger.getLogger(ClusteringEngine.class.getName()).log(Level.SEVERE, null, ex);
                }                            
            conn.closeConnection();
            
            indexer.closeIndex();
        }
	
        public void cluster()
        {
            cluster("");
        }
        
	public void cluster(String userid){		
            try {
                    RAMDirectory directory = new RAMDirectory();
            
                    startIndexerModule(directory);

                    startParserModule(indexer);

                    loadInstances(userid);


                    simi = new SimilarityMatrix(directory, 2); // 2nd parameter is similarity mode, see class SimilarityMatrix for details
                    simi.openIndex();
                    simi.createMatrix();
                    duplicate = new ClusterDuplicates(simi);
                    duplicate.clusterCentroid(0.5);

                    updateDuplucates(duplicate);				
                    simi.closeIndex();
                } catch (Exception ex) {
                Logger.getLogger(ClusteringEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
        
        private void updateDuplucates(ClusterDuplicates clusterDuplicates) throws IOException{
                MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);		
                String origDocId = "";
		for (int i=0;i<clusterDuplicates.getNumDocs();i++){
			if(clusterDuplicates.getDuplicateList()[i]==1){
                                origDocId = clusterDuplicates.getSimilarityMatrix().getIndexReader().document(i).get("id");
				
				for (int j=0;j<clusterDuplicates.getNumDocs();j++){
					if (clusterDuplicates.getDuplicateMatrix()[i][j]>0){
                                            String duplicateId = clusterDuplicates.getSimilarityMatrix().getIndexReader().document(j).get("id");
                                            
                                            String updateString = "UPDATE personalherald.summarizer\n" +
                                                                  "SET duplicate=\""+origDocId+"\"\n" +
                                                                  "WHERE uid=\""+duplicateId+"\"";
                                            try {
                                                PreparedStatement pstmt = conn.getConn().prepareStatement(updateString);
                                                pstmt.executeUpdate();                                               
                                            } catch (SQLException ex) {
                                                Logger.getLogger(ClusteringEngine.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            												
					}
				}								
			}
		}
            try {
                conn.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(ClusteringEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
	}                

}
