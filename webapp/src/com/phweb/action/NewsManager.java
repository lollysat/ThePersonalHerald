package com.phweb.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import jwtm.articleranker.ClassifyCategory;
import jwtm.articleranker.MalletTest;
import jwtm.clusteringengine.ClusteringEngine;
import jwtm.feedreader.RSSFeedReader;
import jwtm.textextractor.Extractor;
import jwtm.textextractor.TextExtractor;
import jwtm.textsummarizer.SimpleTextSummarizer;
import jwtm.textsummarizer.Summarizer;
import jwtm.textsummarizer.TextSummarizer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.phweb.utils.Constants;
import com.phweb.utils.DBManger;
import com.phweb.utils.NewsArticle;

public class NewsManager extends ActionManager
{
	private Logger logger = Logger.getLogger(NewsManager.class);
	private ArrayList<NewsArticle> news = new ArrayList<NewsArticle>();
	private String url;
	private NewsArticle demoResult;
	
	private String getUserName()
	{
		Map<String, Object> session = ActionContext.getContext().getSession();
		return (String)session.get("user");
	}
	
	public String getNewsArticles()
	{
		//logger.info("Entered UserManager getUserPrefs");
		
		news = DBManger.getUserNews(getUserName());
		
		return SUCCESS;
		
	}
	
	public String runDemo()
	{
			String result = "{"; 
		 	TextExtractor extractor = new TextExtractor();
	        String extractedText = extractor.extractText(url);
	        String extractedTitle = extractor.extractTitle(url);
	        
	        demoResult = new NewsArticle(extractedTitle,extractedText,url);
	        
	        TextSummarizer summarizer = new TextSummarizer();
	        String summary = summarizer.summarize(extractedText, 5);
	        
	        
	        
	        SimpleTextSummarizer classifier4j = new SimpleTextSummarizer();
	        String classifier4jSummary = classifier4j.summarize(extractedText);
	        
	        
	        demoResult.setSummary(summary + "$$classifier4jSummary$$" + classifier4jSummary);
	        
	        MalletTest classifier = new MalletTest();
	        String cls = "";
			try {
				cls = classifier.classify(extractedTitle+summary);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cls = Character.toUpperCase(cls.charAt(0)) + cls.substring(1);
			demoResult.setCategory(cls);
	        	        
	        return SUCCESS;
	}
	
	public String regenerate()
	{
		String username = getUserName();
		
		try
		{
			DBManger.clearUserData(username);
	       
	        RSSFeedReader reader = new RSSFeedReader(Constants.DBHOST,Constants.DBPORT,Constants.DBSCHEMA,Constants.DBUSERNAME,Constants.DBPASSWORD);
	        reader.readFeeds(username);  
	        
	        
	        Extractor extractor = new Extractor(Constants.DBHOST,Constants.DBPORT,Constants.DBSCHEMA,Constants.DBUSERNAME,Constants.DBPASSWORD);
	        extractor.extract(username);   
	        
	        
	        Summarizer summarizer = new Summarizer(Constants.DBHOST,Constants.DBPORT,Constants.DBSCHEMA,Constants.DBUSERNAME,Constants.DBPASSWORD);
	        summarizer.summarize(username);
	        
	        
	        ClassifyCategory classifier = new ClassifyCategory(Constants.DBHOST,Constants.DBPORT,Constants.DBSCHEMA,Constants.DBUSERNAME,Constants.DBPASSWORD);
	        classifier.classify(username);
	        
	        
	        ClusteringEngine clsEngine = new ClusteringEngine(Constants.DBHOST,Constants.DBPORT,Constants.DBSCHEMA,Constants.DBUSERNAME,Constants.DBPASSWORD);
	        clsEngine.cluster(username);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		
        return SUCCESS;
	}

	public ArrayList<NewsArticle> getNews() {
		return news;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = StringEscapeUtils.unescapeHtml(url);
	}

	public NewsArticle getDemoResult() {
		return demoResult;
	}

	public void setDemoResult(NewsArticle demoResult) {
		this.demoResult = demoResult;
	}

		
	
}
