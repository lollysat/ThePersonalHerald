package com.phweb.action;

import java.util.ArrayList;
import java.util.Map;

import jwtm.feedreader.RSSFeedReader;
import jwtm.textextractor.Extractor;
import jwtm.textsummarizer.Summarizer;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.phweb.utils.Constants;
import com.phweb.utils.DBManger;
import com.phweb.utils.NewsArticle;


public class RunEngine extends ActionManager
{
	private Logger logger = Logger.getLogger(RunEngine.class);
	private ArrayList<NewsArticle> news = new ArrayList<NewsArticle>();
	
	
	public void runFeedReader(String username)
	{
		
		 //Read the feeds
        RSSFeedReader reader = new RSSFeedReader(Constants.DBHOST, Constants.DBPORT, Constants.DBSCHEMA, 
        										Constants.DBUSERNAME, Constants.DBPASSWORD);
        reader.readFeeds(); 
	}
	
	public String runExtractor()
	{
		
		String username = getUserFromSession();
		
		runFeedReader(username);
		
        //Extract the text
        Extractor extractor = new Extractor(Constants.DBHOST, Constants.DBPORT, Constants.DBSCHEMA, 
				Constants.DBUSERNAME, Constants.DBPASSWORD);
        
        extractor.extract(); 
        news = DBManger.getTextExtractorResults(username);
        
        return SUCCESS;
	}
	
	public String runSummarizer()
	{
		System.out.println("entered 3");
		String username = getUserFromSession();
		//Extract the Summary
        Summarizer summarizer = new Summarizer(Constants.DBHOST, Constants.DBPORT, Constants.DBSCHEMA, 
								Constants.DBUSERNAME, Constants.DBPASSWORD);
        summarizer.summarize();
        
        news = DBManger.getTextSummarizerResults(username);
        
        return SUCCESS;
	}

	public ArrayList<NewsArticle> getNews() {
		return news;
	}

	public void setNews(ArrayList<NewsArticle> news) {
		this.news = news;
	}
	
	private String getUserFromSession()
	{
		Map<String, Object> session = ActionContext.getContext().getSession();
		String username = (String)session.get("user");
		
		return username;
	}
	/*
	public void runRanker()
	{
		
	}
	*/
	
}
