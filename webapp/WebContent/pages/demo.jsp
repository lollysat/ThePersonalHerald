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
		var imagesFolder = "<s:url value="/images" />";
		function onPageLoad()
		{
			$("#resultsTbl").hide();
			$("#msgDiv").hide();
		}
		
		function getIconStr(category)
		{
			var categoryIcon = "<img src='" + imagesFolder + "/";
			
			categoryIcon += getCategoryIcon(category);
			
			categoryIcon += "' alt='" + category +"' />";
			
			return categoryIcon;
		}
		
		function runDemo()
		{
			$("#resultsTbl").hide();
			$("#msgDiv").show();
			
			var urlValue = $("#url").val();
			$.ajax({
		        url: "runDemo.action",
		        type: "POST",    
		        data: {url:urlValue},
		        dataType: "json",  
		        error: function(jqXHR, textStatus, errorThrown){
		        	handleWrongContentType(jqXHR, textStatus, errorThrown);
		        },
		        success: function(data)
		        {
		        	var category = data.demoResult.category;
		        	$("#urlValue").html(getIconStr(category) + "<a href='" + urlValue + "' >" + urlValue + "</a>");
		        	$("#titleValue").html("<b>" + data.demoResult.title + "</b>");
		        	$("#extractedText").html(data.demoResult.text);
		        	
		        	var summary = data.demoResult.summary;
		        	var delimiter = "$$classifier4jSummary$$";
		        	
		        	var index = summary.indexOf(delimiter);
		        	$("#summaryText").html(summary.substring(0,index));
		        	$("#c4jsummaryText").html(summary.substring(index+delimiter.length));
		        	
		        	
		        	
		        	$("#msgDiv").hide();
		        	$("#resultsTbl").show();
		        }
			});
		}
	</script>
</head>
<body onload="onPageLoad()">
<center>

  <div id="header">
   
    <s:include value="banner.jsp" />
    
    <ol>
      <li><a href="index">Back</a></li>
      
	</ol>
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
	    			<td style="align:midle;">
	    				<input type="text" id="url"  name="url" size="50"/> 
	    				</td>
	    				<td>
		    			<input type="button" name="btnExtractor" id="btnExtractor" class="btnLogin" value="Run" onclick="runDemo();"/>
		    			</td>
	    		</tr>	
	    	</table>
	    	<br/>
	    	<div id="results" style="max-height:400px;">
	    		<div id="msgDiv"> Generating ... </div>
		    	<table class="dataTable" width="90%" id="resultsTbl">
		    		<tr>
		    			<td id="titletd">URL</td>
		    			<td id="urlValue" align="left" style="vertical-align:middle"></td>
		    		</tr>
		    		<tr>
		    			<td id="titletd">Title</td>
		    			<td id="titleValue" align="left"></td>
		    		</tr>
		    		<tr>
		    			<td id="titletd">Text</td>
		    			<td id="extractedText" align="left"></td>
		    		</tr>
		    		<tr>
		    			<td id="titletd">Summary</td>
		    			<td id="summaryText" align="left"></td>
		    		</tr>
		    		<tr>
		    			<td id="titletd">Classifier4J Summary</td>
		    			<td id="c4jsummaryText" align="left"></td>
		    		</tr>
		    	</table>
		    	<br/>
	    		<br/>
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
	