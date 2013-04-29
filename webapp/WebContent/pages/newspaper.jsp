<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />    
    <title>Test</title>
	<script type="text/javascript" src="<s:url value="/scripts/jquery-1.9.1.js"/>"></script>	
	<script type="text/javascript" src="<s:url value="/scripts/jquery.jcarousel.min.js"/>"></script>
	<script type="text/javascript" src="<s:url value="/scripts/jquery.mousewheel.js"/>"></script>
	<script type="text/javascript" src="<s:url value="/scripts/Common.js"/>"></script>	
	
	<link rel="stylesheet" type="text/css" href="<s:url value="/css/style.css"/>" />
	<link rel="stylesheet" type="text/css" href="<s:url value="/styles/skin.css"/>" />
	<link rel="stylesheet" type="text/css" href="<s:url value="/styles/ph-main.css"/>" />
	
	<script type="text/javascript">
	
	var imagesFolder = "<s:url value="/images" />";
	
	function getIconStr(category)
	{
		var categoryIcon = "<table class='sourceTbl'><tr><td><img src='" + imagesFolder + "/";
		
		categoryIcon += getCategoryIcon(category);
		
		categoryIcon += "' alt='" + category +"' />";
		
		return categoryIcon;
	}
	
	function populateNews(data)
	{
		var news = data.news;
    	var openLi = "<li>";
    	var closeLi = "</li>";
    	var wrapperDiv = "<div class='wrapper'>";
    	var featuresTopDiv = "<div class='featuresColumnTop'>";
    	var featuresBottomDiv = "<div class='featuresColumnTop'>";
    	var closeDiv = "</div>";
    	var h1Open = "<H1>";
    	var h1Close = "</H1>";
    	var spanOpen = "<td><a class='stitle' href='";
    	var spanClose = "</a></td></tr></table>";
    	var pOpen="<p>";
    	var pClose="</p>";
    	
    	var str="";
    	var topCount = 0;
    	var bottomCount = 0;
    	var totalCount = 0;
    	
    	for(var i=0;i<news.length;i++)
    	{	        		
    		if(i%6==0)
    		{
    			totalCount += 1;
    			str += openLi;
    			str += wrapperDiv;
    		}
    		
    		if(topCount<3)
    		{
    			topCount += 1;
    			str += featuresTopDiv; 
    			str += h1Open + news[i].title + h1Close;
    			
    			if(news[i].source.length > 0)
    				str += getIconStr(news[i].category) + spanOpen + news[i].source + "'>" + news[i].source + spanClose;
    			
    			str+= pOpen + news[i].text + pClose;
    			
    			str+= closeDiv;
    		}	        		
    		else if(bottomCount<3)
    		{
    			if(topCount==3 && bottomCount==0)
    			{
    				str += 	closeDiv;
    				str += wrapperDiv;
    			}
    			
    			bottomCount += 1;
    			str += featuresBottomDiv; 
    			str += h1Open + news[i].title + h1Close;
    			
    			if(news[i].source.length > 0)
    				str += getIconStr(news[i].category) + spanOpen + news[i].source + "'>" + news[i].source + spanClose;
    			
    			str+= pOpen + news[i].text + pClose;
    			
    			str+= closeDiv;
    		}
    		
    		if(bottomCount==3)
    		{
    			topCount = 0;
    			bottomCount = 0;
    			
    			str+= closeDiv;
    			str+= closeLi;
    		}
    		
    		
    		
    	}
    	
    	if(bottomCount!=0 || topCount!=0)
    	{	        		
    		str+= closeDiv;
			str+= closeLi;
    	}
    	
    	
    	if(str.length >0)
    	{
    		$("#msgDiv").hide();
    		
        	$("#mycarousel").append(str);
        	$('#mycarousel').jcarousel({
                visible: 1,
                scroll:1,
                size: totalCount
            });
    	}
    	else
    	{
    		$("#msgDiv").show();
    		$("#msg").html("No content Generated");
    	}
    	
    	elements = $(".wrapper .featuresColumnTop");
    	for(var i=1;i<elements.length;i=i+3)
    	{
    		elements[i].style.height = "100%";
    		elements[i].style.borderRight = "1px solid #e7e7e7";
    		elements[i].style.borderLeft = "1px solid #e7e7e7";
    	}
    //$("#mycarousel").removeClass("jcarousel-skin-tango");
    	//$("#mycarousel").addClass("jcarousel-skin-tango");
    	
    	/*
    	resetFooter();
    	$("#footerDiv").css("position","fixed");
    	$("#footerDiv").css("width","100%");
    	$("#footerDiv").css("float","bottom");
    	$("#footerDiv").css("margin-top","0px");
    	*/
	}
	function onPageLoad()
	{
		$("#refreshPaper").show();
		
		$("#msg").html("Generating Newspaper .... This may take few seconds.");
		$("#msgDiv").show();
		
		$.ajax({
	        url: "getNewsArticles.action",
	        type: "POST",    
	        data: {},
	        dataType: "json",  
	        error: function(jqXHR, textStatus, errorThrown){
	        	handleWrongContentType(jqXHR, textStatus, errorThrown);
	        },
	        success: function(data)
	        {
	        	populateNews(data);
	        }
		});
		
	}
	
	function generatePaper()
	{
		$('#contentWrapper').empty();
		
		$("#msg").html("Generating Newspaper .... This may take few seconds.");
		$("#msgDiv").show();
		
		document.getElementById("refreshForm").action = "regenerate.action";
		document.getElementById("refreshForm").submit();
	}
	</script>
	
	<style type="text/css">
		/**
		 * Overwrite for having a carousel with dynamic width.
		 */
		.jcarousel-skin-tango .jcarousel-container-horizontal {
			width: 94.9%;
			height: 90%;
		}

		.jcarousel-skin-tango .jcarousel-clip-horizontal {
			width: 100%;
			height: 95%;
		}
	</style>
    
</head>

<body onload="onPageLoad()">
	<div id="header">
   
	    <s:include value="banner.jsp" />
	    
	    <s:include value="menu.jsp" />
	    
  	</div>
  	<form id="refreshForm"></form>
  	<div id="msgDiv" style="background:white;"><br/></br><p id="msg" style='font-weight:bold'></p></div>
  	
  	<div id="contentWrapper" style="height:700px;">
		<ul id="mycarousel" class="jcarousel-skin-tango">
	        
 	 	</ul>
	</div>
	
  <div id="footerDiv" style="position:fixed; width:100%;float:bottom;margin-top:0px">	
  	<s:include value="footer.jsp" />
  </div>
</body>
</html>