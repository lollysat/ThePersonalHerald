/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import jwtm.mysql.MysqlConnector;
import jwtm.mysql.util.UidGenerator;

/**
 *
 * @author kmadanagopal
 */
public class BasicCrawler extends WebCrawler{
     private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
                        + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
     
    
    // If the crawler depth is set morethan 0, then for every url encontered in the seed url. This method 
    // decides should we crawl or not. Since for our case it is screenscrapper set it to false.
    @Override
    public boolean shouldVisit(WebURL url) {
              return false;
    }
     
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        String title = page.getWebURL().getAnchor();
        String htmlContent = new String(page.getContentData());
        
        persistData(url);
        
// // Some additional properties which can be useful        
//        int docid = page.getWebURL().getDocid();
//        String url = page.getWebURL().getURL();
//        String domain = page.getWebURL().getDomain();
//        String path = page.getWebURL().getPath();
//        String subDomain = page.getWebURL().getSubDomain();
//        String parentUrl = page.getWebURL().getParentUrl();
//        String anchor = page.getWebURL().getAnchor();
    }
    
    public void persistData(String url) 
    {        
        String uid = UidGenerator.generateUID();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());                       
         try {                        
    
             
             String filePath = "";
             String insertString = "INSERT INTO crawler (uid,url, filePath,crawlDate,parentUrl) VALUES (\'"+uid+"\',\'"+url+"\', \'"+filePath+"\' ,\'"+date+"\',\'"+url+"\') ";                          
             MysqlConnector conn = new MysqlConnector(PersistenceController.getInstance().getDbServer(), PersistenceController.getInstance().getDbServerPort(), PersistenceController.getInstance().getDbName(), PersistenceController.getInstance().getDbUser(), PersistenceController.getInstance().getDbPwd());
             conn.executeInsertQuery(insertString);
             conn.closeConnection();
         } catch (Exception ex) {
             Logger.getLogger(BasicCrawler.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    
     
}
