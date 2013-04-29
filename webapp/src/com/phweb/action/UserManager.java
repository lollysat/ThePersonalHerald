package com.phweb.action;

import org.apache.commons.lang.StringEscapeUtils;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.phweb.utils.DBManger;


public class UserManager extends ActionManager
{
	private String url;
	private String urlType;
	private ArrayList<String> prefs = new ArrayList<String>();
	private Logger logger = Logger.getLogger(UserManager.class);
	
	public String getUserPrefs()
	{
		//logger.info("Entered UserManager getUserPrefs");
		Map<String, Object> session = ActionContext.getContext().getSession();
		prefs = DBManger.getUserPref((String)session.get("user"),null);
		session.put("prefs", prefs.size());
		return SUCCESS;
		
	}
	
	public String addUserPrefs()
	{
		logger.info("Entered UserManager addUserPrefs");
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		int added = DBManger.addUserPref((String)session.get("user"),url,urlType);
		
		if(added==-1)
			this.addActionError(getText("addErr"));
		
		getUserPrefs();
		
		return SUCCESS;
		
	}
	
	public String deleteUserPrefs()
	{
		logger.info("Entered UserManager deleteUserPrefs");
		Map<String, Object> session = ActionContext.getContext().getSession();
		int deleted = DBManger.deleteUserPref((String)session.get("user"),url);
		
		if(deleted==-1)
			this.addActionError(getText("deleteErr"));
		
		getUserPrefs();
		
		return SUCCESS;
		
	}
	
	public String increasePrefOrder()
	{
		logger.info("Entered UserManager updatePrefOrder");
		Map<String, Object> session = ActionContext.getContext().getSession();
		DBManger.increasePrefOrder((String)session.get("user"),url);
				
		getUserPrefs();
		
		return SUCCESS;
		
	}
	
	public String decreasePrefOrder()
	{
		logger.info("Entered UserManager decreasePrefOrder " );
		Map<String, Object> session = ActionContext.getContext().getSession();
		DBManger.decreasePrefOrder((String)session.get("user"),url);
		
		getUserPrefs();
		
		return SUCCESS;
		
	}

	public ArrayList<String> getPrefs() {
		return prefs;
	}

	public void setPrefs(ArrayList<String> prefs) {
		this.prefs = prefs;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = StringEscapeUtils.unescapeHtml(url);
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	
	
}
