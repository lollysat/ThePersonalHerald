package jwtm.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwtm.mysql.MysqlConnector;

/**
 * Hello world!
 *
 */
public class Crawler implements jwtm.Crawler
{
    private String crawlTempFolder;
    private String dbServer;
    private String dbServerPort;
    private String dbName;
    private String dbUser;
    private String dbPwd;
    public Crawler(String crawlTempFolder, String dbServer, String dbServerPort,String dbName, String dbUser, String dbPwd)
    {        
        this.crawlTempFolder = crawlTempFolder;
        this.dbServer = dbServer;
        this.dbServerPort = dbServerPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPwd = dbPwd;        
        PersistenceController.getInstance().setDbName(dbName);
        PersistenceController.getInstance().setDbServer(dbServer);
        PersistenceController.getInstance().setDbServerPort(dbServerPort);
        PersistenceController.getInstance().setDbUser(dbUser);
        PersistenceController.getInstance().setDbPwd(dbPwd);        
    }
    
    public void crawl()            
    {
        try
        {
                /*
                 * crawlStorageFolder is a folder where intermediate crawl data is
                 * stored.
                 */
                String crawlStorageFolder = this.crawlTempFolder;

                /*
                 * numberOfCrawlers shows the number of concurrent threads that should
                 * be initiated for crawling.
                 */
                int numberOfCrawlers = 1;

                CrawlConfig config = new CrawlConfig();

                config.setCrawlStorageFolder(crawlStorageFolder);

                /*
                 * Be polite: Make sure that we don't send more than 1 request per
                 * second (1000 milliseconds between requests).
                 */
                config.setPolitenessDelay(1000);

                /*
                 * You can set the maximum crawl depth here. The default value is -1 for
                 * unlimited depth
                 */
                config.setMaxDepthOfCrawling(0);

                /*
                 * You can set the maximum number of pages to crawl. The default value
                 * is -1 for unlimited number of pages
                 */
                config.setMaxPagesToFetch(1000);
           
                /*
                 * This config parameter can be used to set your crawl to be resumable
                 * (meaning that you can resume the crawl from a previously
                 * interrupted/crashed crawl). Note: if you enable resuming feature and
                 * want to start a fresh crawl, you need to delete the contents of
                 * rootFolder manually.
                 */
                config.setResumableCrawling(false);

                /*
                 * Instantiate the controller for this crawl.
                 */
                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

                /*
                 * For each crawl, you need to add some seed urls. These are the first
                 * URLs that are fetched and then the crawler starts following links
                 * which are found in these pages
                 */

                List<String> urls =  getURLList();
                for(String url : urls)
                {                    
                    controller.addSeed(url);
                }                                

                /*
                 * Start the crawl. This is a blocking operation, meaning that your code
                 * will reach the line after this only when crawling is finished.
                 */
                controller.startNonBlocking(BasicCrawler.class, numberOfCrawlers);
                
                controller.waitUntilFinish();
                controller.Shutdown();  
        }catch(Exception ex)
        {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public  List<String> getURLList()
    {
        ArrayList<String> urls = new ArrayList<String>();
        String selectQuery = "select distinct url from UserPref where type = \'Blog\'";
        MysqlConnector conn = new MysqlConnector(dbServer, dbServerPort, dbName, dbUser, dbPwd);
        try {
            ResultSet rs = conn.executeSelectQuery(selectQuery);

            while(rs.next()) {
                if(urls.contains(rs.getString(1))) continue;
                urls.add(rs.getString(1));
            }
            conn.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return urls;
    }
    
    
}
