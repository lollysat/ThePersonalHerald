<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>The Web News</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" type="text/css" href="<s:url value="/css/style.css"/>" />
	
	<script src="<s:url value="/scripts/jquery-1.9.1.js"/>" type="text/javascript"></script>
	<script src="<s:url value="/scripts/Common.js"/>" type="text/javascript"></script>


	<script type="text/javascript">
	var tblName = "resultsTbl";
	var btnRunExtractor = "#btnExtractor";
	var btnRunSummarizer = "#btnSummarizer";
	var btnRanker = "";
	
	function onPageLoad()
	{
		$("#resultsTbl").hide();
	}
	
	function convertToDone(btnId)
	{
		var doneClass = $(btnId).attr('class');
		if (doneClass.indexOf("Done")==-1)
		{
			$(btnId).removeClass();
			$(btnId).addClass(doneClass+"Done");
		}
	}
	
	function resetButtons(btnId)
	{
		var doneClass = $(btnId).attr('class');
		if (doneClass.indexOf("Done")!=-1)
		{
			var newClass = doneClass.substring(0,doneClass.indexOf("Done"));
			$(btnId).removeClass();
			$(btnId).addClass(newClass);
		}
	}
	function runExtractor()
	{		
		resetButtons(btnRunExtractor);
		resetButtons(btnRunSummarizer);
		resetButtons(btnRanker);
		
		$.ajax({
	        url: "getTextExtractorResults.action",
	        type: "POST",    
	        data: {},
	        dataType: "json",  
	        error: function(jqXHR, textStatus, errorThrown){
	        	
	        	handleWrongContentType(jqXHR, textStatus, errorThrown);
	        },
	        success: function(data)
	        {
	        	totalRows = data.news.length;
	        	articles = data.news;
	        	for(var i=0;i<totalRows;i++)
				{
					var newRowStr = "<tr><td>" + (i+1) + "</td><td>"+ articles[i].source + "</td><td width='20px'>" + articles[i].title + "</td><td>" + articles[i].text+ "</td><td></td></tr>";
					$('#'+tblName).append(newRowStr);
					
					convertToDone(btnRunExtractor);
				}
	        }
		});
		
		
		
	}
	
	function runSummarizer()
	{
		runExtractor();
		
		
		
		$.ajax({
	        url: "getSummarizerResults.action",
	        type: "POST",    
	        data: {},
	        dataType: "json",  
	        error: function(jqXHR, textStatus, errorThrown){
	        	handleWrongContentType(jqXHR, textStatus, errorThrown);
	        },
	        success: function(data)
	        {
	        	totalRows = data.news.length;
	        	articles = data.news;
	        	for(var i=0;i<totalRows;i++)
				{
					var newRowStr = "<tr><td>" + (i+1) + "</td><td>"+ articles[i].source + "</td><td width='20px'>" + articles[i].title + "</td><td>" + articles[i].text+ "</td><td>" + articles[i].summary + "</td></tr>";
					$('#'+tblName).append(newRowStr);
					
					convertToDone(btnRunSummarizer);
				}
	        }
		});
		
		
	}
	
	function runDuplicates()
	{
		runSummarizer();
		convertToDone("#btnDupElim");
	}
	</script>
</head>
<body onload="onPageLoad()">
<center>

  <div id="header">
   
    <s:include value="banner.jsp" />
    
    <s:include value="menu.jsp" />
    <div class="break"></div>
  </div>
  
  <div id="contentWrapper" style="height:700px">
  <!-- END header -->
  <!-- BEGIN content -->
  <form name="prefForm" action="javascript:AddItem()">
  		<br/><br/>
	    <s:include value="messages.jsp" />
	     <div class="errorMsg">
			 <s:iterator value="errorMsgList">
			     <s:property />
			     <br/>
			 </s:iterator>
		   </div> 
	    	
	    	<b>Lets try this -- SHALL WE !!</b>
	    	<br /><br />
	    	<table >	    		
	    		<tr>
	    			<td>
		    			<input type="button" name="btnExtractor" id="btnExtractor" class="textExtractor" onclick="runExtractor();"/>
		    			<img src="<s:url value="/images/arrowRight.png"/>" />
		    			<input type="button" name="btnSummarizer" id="btnSummarizer" class="textSummarizer" onclick="runSummarizer();"/>
		    			<img src="<s:url value="/images/arrowRight.png"/>" />
		    			<input type="button" name="btnDupElim" id="btnDupElim" class="eliminateDups" onclick="runDuplicates();"/>
		    		</td>
	    		</tr>	
	    	</table>
	    	<br/>
	    	<div id="results" style="max-height:400px;">
	    	<table class="dataTable" id="resultsTbl" >
	    		<tr>
	    			<th>Doc ID</th>
	    			<th>Title</th>
	    			<th>Text</th>
	    			<th>Summary</th>
	    			<th>Cluster Tag</th>
	    		</tr>
	    	</table>
	    	</div>
	    	
  </form>

</div>
<!-- BEGIN footer -->
  <s:include value="footer.jsp" />
</center>  
</body>
  <!-- END footer -->
<!-- END wrapper -->
</html>
	