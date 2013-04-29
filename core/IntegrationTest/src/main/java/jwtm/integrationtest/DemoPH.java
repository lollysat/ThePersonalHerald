/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.integrationtest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import jwtm.articleranker.ClassifyCategory;
import jwtm.articleranker.MalletTest;
import jwtm.textextractor.TextExtractor;
import jwtm.textsummarizer.TextSummarizer;

/**
 *
 * @author kmadanagopal
 */
public class DemoPH {
    
    public String demoPH(String urlString) throws MalformedURLException, IOException, ClassNotFoundException
    {       
        
        StringBuilder result = new StringBuilder();
        
        result.append("<b>URL: </b>").append(urlString).append("<BR><BR>");
        
        TextExtractor extractor = new TextExtractor();
        String extractedText = extractor.extractText(urlString);
        String extractedTitle = extractor.extractTitle(urlString);
        
        result.append("<div><b>Extractor Output: </b>").append(urlString).append("<BR>");
        result.append("<b>Title: </b>").append(extractedTitle).append("<BR>");
        result.append("<b>Text: </b>").append(extractedText).append("</div><BR><BR>");
        
        TextSummarizer summarizer = new TextSummarizer();
        String summary = summarizer.summarize(extractedText, 5);
        result.append("<div><b>Summarizer Output: </b>").append(urlString).append("<BR>");
        result.append("<b>Title: </b>").append(extractedTitle).append("<BR>");
        result.append("<b>Summary: </b>").append(summary).append("</div><BR><BR>");
        
        MalletTest classifier = new MalletTest();
        String cls = classifier.classify(extractedTitle+summary);
        result.append("<div><b>Classifier Output: </b>").append(cls.toUpperCase()).append("</div><BR>");
        
        return result.toString();
    }
    
    
    public static void main(String args[]) throws MalformedURLException, IOException, ClassNotFoundException
    {
        DemoPH demo = new DemoPH();
        System.out.println(demo.demoPH("http://www.bbc.co.uk/news/world-europe-22328710"));
    }
    
}
