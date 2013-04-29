package com.phweb.utils;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.mysql.jdbc.CallableStatement;

public class DBManger 
{
	private static Logger logger = Logger.getLogger(DBManger.class);
	
	public static MySqlConnector getDBConnection()
	{
		MySqlConnector conn = new MySqlConnector(Constants.DBHOST, Constants.DBPORT, Constants.DBSCHEMA, Constants.DBUSERNAME, Constants.DBPASSWORD);
		
		return conn;
	}

	public static boolean validateLogin(String username,String password)
	{
		
		boolean isValid = false;
		String query = "Select 1 from Users where username='" + username + "' and password='" + password + "'";
		MySqlConnector conn = null;
		ResultSet rs = null;
		
		try
		{
			conn = getDBConnection();
			rs = conn.executeSelectQuery(query);
			
			if(rs!=null && rs.next())
			{
				isValid = true;
				rs.close();
			}
			
			
				
		}
		catch(Exception e)
		{			
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return isValid;
	}
	
	public static ArrayList<String> getUserPref(String username,String type)
	{
		MySqlConnector conn = null;
		ResultSet rs = null;
		
		ArrayList<String> prefs = new ArrayList<String>();
		String filterByType = " type= '" + type + "'";
		String filterByUser = " userId = '" + username + "'";
		String query = "Select distinct url from userpref where ";
		String orderBy = " order by preforder";
		
		if(username!=null)
			query += filterByUser;
		
		if(type!=null)
		{
			if(username!=null)
				filterByType = " and " + filterByType;
			
			query += filterByType;
		}
		
		query += orderBy;
		logger.info(query);
		try
		{
			conn = getDBConnection();
			rs = conn.executeSelectQuery(query);
			
			if(rs!=null)
			{
				while(rs.next())
				{
					prefs.add(rs.getString("url"));
				}
			}
			
			rs.close();
				
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
		return prefs;
	}
	
	public static void clearUserData(String username)
	{
		MySqlConnector conn = null;
		int result = -1;
		
		try
		{
        
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.clear_all_procedure_user(\""+ username +"\")");
	        stmt.executeQuery(); 
	        stmt.close();
	        result = 1;
		}
        catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
	}
	
	public static int addUserPref(String username, String url,String urlType)
	{
		MySqlConnector conn = null;
		int result = -1;
		
		try
		{
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.addURL(\""+ username +"\",\"" + url + "\",\"" + urlType + "\")");
	        ResultSet rs  = stmt.executeQuery();
	        if(rs!=null && rs.next())
	        	result = rs.getInt(1);
	        
	        rs.close();
	        stmt.close();
		}
        catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
			
		return result;
	}
	
	public static int deleteUserPref(String username, String url)
	{
		MySqlConnector conn = null;
		int result = -1;
		
		try
		{
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.deleteURL(\""+ username +"\",\"" + url + "\")");
			ResultSet rs  = stmt.executeQuery();
	        if(rs!=null && rs.next())
	        	result = rs.getInt(1);
	        
	        rs.close(); 
	        stmt.close();
		}
        catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
			
		return result;
	}
	
	/*
	public static int updatePrefOrder(String username, String url)
	{
		String query = "update userpref set where userId='" + username + "' and url='" + url + "'";
		int result = updateUserPref(username,url,query);
		return result;
	}
	*/
	public static void decreasePrefOrder(String username, String url)
	{
		MySqlConnector conn = null;
		int result = -1;
		
		try
		{	
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.decreaseOrder(\""+ username +"\",\"" + url + "\")");
	        stmt.executeQuery(); 
	        stmt.close();
		}
        catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
	}
	
	public static void increasePrefOrder(String username, String url)
	{
		MySqlConnector conn = null;
		int result = -1;
		logger.info(url);
		try
		{
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.increaseOrder(\""+ username +"\",\"" + url + "\")");
			ResultSet rs = stmt.executeQuery();
			System.out.println(rs.getString(1));
	        stmt.close();
		}
        catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
	}
	
	public static ArrayList<NewsArticle> getUserNews(String username)
	{
		MySqlConnector conn = null;
		ResultSet rs = null;
		
		ArrayList<NewsArticle> news = new ArrayList<NewsArticle>();
		//String query = "Select * from news where userId='" + username + "' order by display_order";
		try
		{
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.FetchNewsForUser2(\""+ username +"\")");
	        rs = stmt.executeQuery(); 
			
			if(rs!=null)
			{
				while(rs.next())
				{
					NewsArticle article = new NewsArticle(rs.getString("title"),rs.getString("text"),rs.getString("source"));
					article.setCategory(rs.getString("category"));
					news.add(article);
					
				}
			}
			
			rs.close();
			stmt.close();
				
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
		return news;
	}
	
	public static ArrayList<String> getAllURLSToExtract()
	{
		String query = "select distinct url from PersonalHerald.Crawler";
		MySqlConnector conn = null;
		ResultSet rs = null;
		
		ArrayList<String> urls = new ArrayList<String>();
		try
		{
			conn = getDBConnection();
			rs = conn.executeSelectQuery(query);
			
			if(rs!=null)
			{
				while(rs.next())
				{
					urls.add(rs.getString("url"));
				}
			}
			
			rs.close();
				
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
		return urls;
	}
	
	public static ArrayList<NewsArticle> getTextExtractorResults(String username)
	{
		MySqlConnector conn = null;
		ResultSet rs = null;
		ArrayList<NewsArticle> news = new ArrayList<NewsArticle>();
		
		
		try
		{
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.getExtractedTextForUser(\""+ username +"\")");
	        rs = stmt.executeQuery(); 
			
			if(rs!=null)
			{
				while(rs.next())
				{
					news.add(new NewsArticle(rs.getString("title"),rs.getString("text"),rs.getString("source")));
				}
			}
			
			rs.close();
				
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
		return news;
	}
	
	public static ArrayList<NewsArticle> getTextSummarizerResults(String username)
	{
		MySqlConnector conn = null;
		ResultSet rs = null;
		ArrayList<NewsArticle> news = new ArrayList<NewsArticle>();
		
		try
		{
			conn = getDBConnection();
			CallableStatement stmt = (CallableStatement) conn.getConn().prepareCall("call personalHerald.getSummarizedTextForUser(\""+ username +"\")");
	        rs = stmt.executeQuery(); 
			
			if(rs!=null)
			{
				while(rs.next())
				{
					NewsArticle nart = new NewsArticle(rs.getString("title"),rs.getString("text"),rs.getString("source"));
					nart.setSummary(rs.getString("summary"));
					news.add(nart);
				}
			}			
			rs.close();
				
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if(conn!=null)
					conn.closeConnection();
			}catch(Exception e)
			{
				logger.error(e.getMessage());
			}
		}
		return news;
	}
	
	
}
