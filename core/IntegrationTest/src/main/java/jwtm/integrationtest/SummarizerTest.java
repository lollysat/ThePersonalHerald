/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.integrationtest;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwtm.mysql.MysqlConnector;
import jwtm.textsummarizer.SimpleTextSummarizer;
import jwtm.textsummarizer.Summarizer;

/**
 *
 * @author kmadanagopal
 */
public class SummarizerTest {
    public static void main(String args[])
    {        
        //Extract the Summary
        Summarizer summarizer = new Summarizer("localhost", "3307", "PersonalHerald", "root", "helloworld2");
        summarizer.summarize();                      
    }
        
}
