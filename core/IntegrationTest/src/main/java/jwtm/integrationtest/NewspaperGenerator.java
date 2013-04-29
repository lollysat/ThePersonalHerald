/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.integrationtest;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwtm.mysql.MysqlConnector;

/**
 *
 * @author kmadanagopal
 */
public class NewspaperGenerator{
     
   public void generateNewspaper()
   {
       StringBuilder newspaper = new StringBuilder();
       try
       {
           
           newspaper.append("<HTML>");
           newspaper.append("<HEAD>");
           newspaper.append(style);
           newspaper.append("</HEAD>");           
           newspaper.append("<BODY>");
           
           newspaper.append("<section>");
           newspaper.append("<h1>The Personal Herald</h1>");
           newspaper.append("<p>4th April 2013 • College Station, TX</p>");

           MysqlConnector conn = new MysqlConnector("localhost", "3307", "PersonalHerald", "root", "helloworld2");
           
           String selectQuery = "select uid,title,summary from PersonalHerald.Summarizer";
            try {
                ResultSet rs = conn.executeSelectQuery(selectQuery);

                while(rs.next()) {
                   newspaper.append("<article>");
                   String title = rs.getString("title");
                   newspaper.append("<h2>").append(title).append("</h2>");
                   String text = rs.getString("summary");               
                   newspaper.append("<p>").append(text).append("</p>");
                   newspaper.append("</article>");
                   
                }
                conn.closeConnection();
            } catch (Exception ex) {
                Logger.getLogger(ExtractorTest.class.getName()).log(Level.SEVERE, null, ex);
            }                               
           
           newspaper.append("</section>");
           newspaper.append("</BODY>");
           newspaper.append("</HTML>");
       }
       catch(Exception ex)
       {
           
       }
       System.out.println(newspaper.toString());
   }
   private static String style = "<style>\n" +
"		/* Fonts */\n" +
"		@import url(http://fonts.googleapis.com/css?family=PT+Sans:regular,bold&subset=cyrillic,latin);\n" +
"		@import url(http://fonts.googleapis.com/css?family=PT+Sans+Narrow:regular,bold&subset=cyrillic,latin);\n" +
"		@import url(http://fonts.googleapis.com/css?family=PT+Serif:regular,bold&subset=cyrillic,latin);\n" +
"\n" +
"		/* Reset */\n" +
"		HTML,BODY,DIV,SPAN,APPLET,OBJECT,IFRAME,H1,H2,H3,H4,H5,H6,P,\n" +
"		BLOCKQUOTE,PRE,A,ABBR,ACRONYM,ADDRESS,BIG,CITE,CODE,DEL,DFN,\n" +
"		EM,FONT,IMG,INS,KBD,Q,S,SAMP,SMALL,STRIKE,STRONG,SUB,SUP,\n" +
"		TT,VAR,HR,B,U,I,CENTER,DL,DT,DD,OL,UL,LI,FIELDSET,FORM,\n" +
"		LABEL,LEGEND,TABLE,CAPTION,TBODY,TFOOT,THEAD,TR,TH,TD {\n" +
"			padding:0;\n" +
"			margin:0;\n" +
"			border:none;\n" +
"			outline:none;\n" +
"			vertical-align:baseline;\n" +
"			font-family:inherit;\n" +
"			font-size:100%;\n" +
"			}\n" +
"		DFN,I,CITE,VAR,ADDRESS,EM {\n" +
"			font-style:normal;\n" +
"			}\n" +
"		TH,B,STRONG,H1,H2,H3,H4,H5,H6{\n" +
"			font-weight:normal;\n" +
"			}\n" +
"		TEXTAREA,INPUT,SELECT{\n" +
"			font-family:inherit;\n" +
"			font-size:1em;\n" +
"			}\n" +
"		BLOCKQUOTE,Q {\n" +
"			quotes:none;\n" +
"			}\n" +
"		Q:before,Q:after,\n" +
"		BLOCKQUOTE:before,\n" +
"		BLOCKQUOTE:after {\n" +
"			content:'';\n" +
"			content:none;\n" +
"			}\n" +
"		OL,UL {\n" +
"			list-style:none;\n" +
"			}\n" +
"		INS {\n" +
"			text-decoration:none;\n" +
"			}\n" +
"		DEL {\n" +
"			text-decoration:line-through;\n" +
"			}\n" +
"		TABLE {\n" +
"			border-collapse:collapse;\n" +
"			border-spacing:0;\n" +
"			}\n" +
"		CAPTION,TH,TD {\n" +
"			text-align:left;\n" +
"			}\n" +
"\n" +
"		/* Typography */\n" +
"		BODY {\n" +
"			padding:50px 0;\n" +
"			background:#75777E url(images/linen.jpg);\n" +
"			color:#000;\n" +
"			font:15px 'PT Serif', serif;\n" +
"			}\n" +
"		SECTION {\n" +
"			padding:70px 70px;\n" +
"			width:70%;\n" +
"			min-width:600px;\n" +
"			max-width:1000px;\n" +
"			margin:auto;\n" +
"			background-color:#FFF;\n" +
"			background-image:\n" +
"				-o-linear-gradient(\n" +
"					90deg,\n" +
"					transparent 0,\n" +
"					rgba(204, 204, 204, 0.5) 25%,\n" +
"					transparent 25.3%,\n" +
"					rgba(204, 204, 204, 0.5) 50%,\n" +
"					transparent 50.3%,\n" +
"					rgba(204, 204, 204, 0.5) 75%,\n" +
"					transparent 75.3%,\n" +
"					rgba(204, 204, 204, 0.5) 100%\n" +
"					),\n" +
"				-o-linear-gradient(\n" +
"					180deg,\n" +
"					transparent 0,\n" +
"					rgba(204, 204, 204, 0.5) 50%,\n" +
"					transparent 50.7%,\n" +
"					rgba(204, 204, 204, 0.5) 100%\n" +
"					),\n" +
"				-o-linear-gradient(\n" +
"					225deg,\n" +
"					rgba(255, 255, 255, 0.5) 0,\n" +
"					rgba(204, 204, 204, 0.5) 5%,\n" +
"					transparent 5.3%\n" +
"					);\n" +
"			-webkit-box-shadow:0 0 50px rgba(0, 0, 0, 0.5);\n" +
"			-moz-box-shadow:0 0 50px rgba(0, 0, 0, 0.5);\n" +
"			box-shadow:0 0 50px rgba(0, 0, 0, 0.5);\n" +
"			}\n" +
"			SECTION > H1 {\n" +
"				padding:23px 0 25px 0;\n" +
"				border:1px solid #000;\n" +
"				border-width:5px 0 3px;\n" +
"				color:#000;\n" +
"				text-align:center;\n" +
"				font:bold 100px/1 'PT Sans Narrow', sans-serif;\n" +
"				}\n" +
"			SECTION > P {\n" +
"				margin:0 0 40px;\n" +
"				padding:10px 0;\n" +
"				border-bottom:3px double #000;\n" +
"				text-align:center;\n" +
"				font:bold 15px/1 'PT Sans', sans-serif;\n" +
"				}\n" +
"			SECTION > ARTICLE {\n" +
"				margin:0 0 30px;\n" +
"				padding:0 0 10px;\n" +
"				border-bottom:3px solid #000;\n" +
"				-webkit-column-count:3;\n" +
"				-moz-column-count:3;\n" +
"				column-count:3;\n" +
"				-webkit-column-gap:30px;\n" +
"				-moz-column-gap:30px;\n" +
"				column-gap:30px;\n" +
"				-webkit-column-rule:1px solid #BBB;\n" +
"				-moz-column-rule:1px solid #BBB;\n" +
"				column-rule:1px solid #BBB;\n" +
"				}\n" +
"			SECTION > ARTICLE:last-of-type {\n" +
"				margin:0;\n" +
"				}\n" +
"				SECTION > ARTICLE H2 {\n" +
"					margin:0 0 15px;\n" +
"					padding:0 0 15px;\n" +
"					border-bottom:3px double #BBB;\n" +
"					font:bold 30px/1 'PT Sans Narrow', sans-serif;\n" +
"					}\n" +
"				SECTION > ARTICLE P {\n" +
"					margin:0 0 15px;\n" +
"					}\n" +
"				SECTION > ARTICLE P:first-letter {\n" +
"					float:left;\n" +
"					margin-right:10px;\n" +
"					font:bold 30px/1 'PT Sans Narrow', sans-serif;\n" +
"					}\n" +
"				SECTION > ARTICLE IMG {\n" +
"					display:block;\n" +
"					margin:0 0 15px;\n" +
"					width:100%;\n" +
"					}\n" +
"				SECTION > ARTICLE ARTICLE {\n" +
"					margin:30px 0 0;\n" +
"					padding:15px 0 0;\n" +
"					border-top:3px double #BBB;\n" +
"					column-count:auto;\n" +
"					column-span:all;\n" +
"					}\n" +
"					SECTION > ARTICLE ARTICLE H3 {\n" +
"						margin:0 0 15px;\n" +
"						font:bold 25px/1 'PT Sans Narrow', sans-serif;\n" +
"						}\n" +
"	</style>";

public static void main(String args[])
{
    NewspaperGenerator gen = new NewspaperGenerator();
    gen.generateNewspaper();
}
}
