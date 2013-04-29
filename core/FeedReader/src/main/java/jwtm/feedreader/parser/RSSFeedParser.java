package jwtm.feedreader.parser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kmadanagopal
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import jwtm.feedreader.RSSFeedReader;
import jwtm.feedreader.model.Feed;
import jwtm.feedreader.model.FeedMessage;


public class RSSFeedParser {
  static final String TITLE = "title";
  static final String DESCRIPTION = "description";
  static final String CHANNEL = "channel";
  static final String LANGUAGE = "language";
  static final String COPYRIGHT = "copyright";
  static final String LINK = "link";
  static final String AUTHOR = "author";
  static final String ITEM = "item";
  static final String PUB_DATE = "pubDate";
  static final String GUID = "guid";

  final URL url;

  public RSSFeedParser(String feedUrl) {
    try {
      this.url = new URL(feedUrl);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  public Feed readFeed() {
    Feed feed = null;
    try {
      boolean isFeedHeader = true;
      // Set header values intial to the empty string
      String description = "";
      String title = "";
      String link = "";
      String language = "";
      String copyright = "";
      String author = "";
      String pubdate = "";
      String guid = "";

      // First create a new XMLInputFactory
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      // Setup a new eventReader
      InputStream in = read();
      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
      // Read the XML document
      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();
        if (event.isStartElement()) {
          String localPart = event.asStartElement().getName()
              .getLocalPart();
          
          if(localPart.equals(ITEM))
          {
              if (isFeedHeader) {
              isFeedHeader = false;
              feed = new Feed(title, link, description, language,
                  copyright, pubdate);
            }
            event = eventReader.nextEvent();
          }
          else if(localPart.equals(TITLE))
          {
              title = getCharacterData(event, eventReader);
          }
          else if(localPart.equals(DESCRIPTION))
          {
              description = getCharacterData(event, eventReader);
          }
          else if(localPart.equals(LINK))
          {
              link = getCharacterDataLink(event, eventReader);
          }
          else if(localPart.equals(GUID))
          {
              guid = getCharacterData(event, eventReader);
          }          
          else if(localPart.equals(LANGUAGE))
          {
              language = getCharacterData(event, eventReader);
          }
          else if(localPart.equals(AUTHOR))
          {
              author = getCharacterData(event, eventReader);
          }
          else if(localPart.equals(PUB_DATE))
          {
              pubdate = getCharacterData(event, eventReader);
          }
          else if(localPart.equals(COPYRIGHT))
          {
              copyright = getCharacterData(event, eventReader);
          }          
        } else if (event.isEndElement()) {
          if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
            FeedMessage message = new FeedMessage();
            message.setAuthor(author);
            message.setDescription(description);
            message.setGuid(guid);
            message.setLink(link);
            message.setTitle(title);
            message.setPubDate(pubdate);
            feed.getMessages().add(message);
            event = eventReader.nextEvent();
            continue;
          }
        }
      }
    } catch (XMLStreamException e) {
      Logger.getLogger(RSSFeedParser.class.getName()).log(Level.SEVERE, null, e);
    }
    return feed;
  }

  private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
      throws XMLStreamException {
    String result = "";
    event = eventReader.nextEvent();
    if (event instanceof Characters) {
      result = event.asCharacters().getData();
    }        
    return result;
  }

  private String getCharacterDataLink(XMLEvent event, XMLEventReader eventReader)
      throws XMLStreamException {
    String result = "";
    result = eventReader.getElementText();            
    if(result.contains("url="))
    {
        result = result.substring(result.indexOf("url=")+4);
        result = removeSpecialChar(result);
    }
    return result;
  }
  
  private InputStream read() {
    try {
      return url.openStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  private String removeSpecialChar(String myString)
    {
        String removeStr = myString;                                   
        removeStr = removeStr.replace("%3f", "?");
        removeStr = removeStr.replace("%25", "%");            
        removeStr = removeStr.replace("%40", "@");
        removeStr = removeStr.replace("%23", "#");
        removeStr = removeStr.replace("%24", "$");
        removeStr = removeStr.replace("%26", "&");
        removeStr = removeStr.replace("%2f", "/");
        removeStr = removeStr.replace("%5c", "\\");
        removeStr = removeStr.replace("%3c", "<");
        removeStr = removeStr.replace("%3e", ">");
        removeStr = removeStr.replace("%7c", "|");
        removeStr = removeStr.replace("%3b", ";");
        removeStr = removeStr.replace("%60", "`");
        removeStr = removeStr.replace("%2b", "+");
        removeStr = removeStr.replace("%3d", "=");
        removeStr = removeStr.replace("%3D", "=");
        removeStr = removeStr.replace("%5e", "^");
        removeStr = removeStr.replace("%3a", ":");
        // etc
        removeStr = removeStr.trim();        
        if(removeStr.contains("&c="))
            removeStr = removeStr.substring(0,removeStr.indexOf("&c="));
        return removeStr;
        
    } 
  
  private static String extractURL(String text)
    {
        Pattern pattern = Pattern.compile("http://[a-z][.a-zA-Z/0-9]+");
        Matcher m = pattern.matcher(text);
        
        if (m.find()) {
              return m.group(0);
            } 
        return null;
    }
  public static void main(String args[])
  {
      String url = "http://www.bing.com/news?q=top+stories&format=RSS";
      RSSFeedParser parser = new RSSFeedParser(url);
      Feed feed = parser.readFeed();                     
        for (FeedMessage message : feed.getMessages()) {
          try {
              if(url.startsWith("http://www.twitter-rss.com"))
              {
                  String eurl = extractURL(message.getDescription());
                  if(eurl == null)
                      continue;
                  
                  
              }   
              Date date = new SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH).parse(message.getPubDate().substring(message.getPubDate().indexOf(",")+1));
              System.out.println(date); // Sat Jan 02 00:00:00 BOT 2010
              //System.out.println(message.getPubDate());
          } catch (ParseException ex) {
              Logger.getLogger(RSSFeedParser.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
  }
} 
