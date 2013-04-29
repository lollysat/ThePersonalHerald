package com.phweb.utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public class Constants 
{
	
	public static String DBHOST = "";
	public static String DBPORT = "";
	public static String DBUSERNAME = "";
	public static String DBPASSWORD = "";
	public static String DBSCHEMA = "";
	
	static
	{		
		DBHOST = LocalizedTextUtil.findDefaultText("DBHOST", ActionContext.getContext().getLocale());
		DBPORT = LocalizedTextUtil.findDefaultText("DBPORT", ActionContext.getContext().getLocale());
		DBUSERNAME = LocalizedTextUtil.findDefaultText("DBUSERNAME", ActionContext.getContext().getLocale());
		DBPASSWORD = LocalizedTextUtil.findDefaultText("DBPASSWORD", ActionContext.getContext().getLocale());
		DBSCHEMA = LocalizedTextUtil.findDefaultText("DBSCHEMA", ActionContext.getContext().getLocale());
		
	}
	
	private Constants()
	{
		
	}

}
