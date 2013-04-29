package com.phweb.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.phweb.utils.DBManger;


public class LoginAction extends ActionManager
{
	private String username;
	private String password;
	private ArrayList<String> errorMsgList = new ArrayList<String>(4);
	private Logger logger = Logger.getLogger(LoginAction.class);


	public String execute()
	{
		String operationResult = ERROR;
		boolean isValid = DBManger.validateLogin(username, password);
		
		logger.info("Login attempt by " + username + " Status: " + isValid);
		
		if (isValid)
		{
			
			errorMsgList.clear();
			Map<String, Object> session = ActionContext.getContext().getSession();
			session.put("user", username);
			/*
			if(DBManger.getUserPref(username,null).size() > 0)
				operationResult = "VIEWPAPER";
			else
			*/
				operationResult = SUCCESS;
		}
		else
		{
			errorMsgList.add(getText("loginErr"));
		}
		
		return (operationResult);
	}


	//Atul: This method logs the user out from the web application and invalidates the current session
	public String logout() throws Exception
	{
		Map session = ActionContext.getContext().getSession();
		session.remove("user");
		
		return SUCCESS;
	}


	public String getPassword()
	{
		return password;
	}


	public void setPassword(String password)
	{
		this.password = password;
	}


	public String getUsername()
	{
		return username;
	}


	public void setUsername(String username)
	{
		this.username = username;
	}


	public ArrayList getErrorMsgList()
	{
		return this.errorMsgList;
	}

}
