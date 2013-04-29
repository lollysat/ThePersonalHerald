/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.integrationtest;

import jwtm.crawler.Crawler;

/**
 *
 * @author kmadanagopal
 */
public class CrawlerTest {
    
   public static void main(String args[])
    {
        Crawler crl = new Crawler("c:\\users\\kmadanagopal\\desktop\\", "localhost", "3307", "PersonalHerald", "root", "helloworld2");
        crl.crawl();
    } 
   
}
