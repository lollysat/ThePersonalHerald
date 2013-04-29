/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.integrationtest;

import jwtm.feedreader.RSSFeedReader;

/**
 *
 * @author kmadanagopal
 */
public class FeedReaderTest {
    
    public static void main(String[] args) {   
        RSSFeedReader reader = new RSSFeedReader("localhost", "3307", "PersonalHerald", "root", "helloworld2");
        reader.readFeeds();       
    }        
}
