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
	<script src="<s:url value="/scripts/jquery-ui-1.10.2.custom.js"/>" type="text/javascript"></script>

	<script type="text/javascript">
		var tblName = "userPrefTbl";
		var wrongURL = '<s:text name="Msg.wrongURL" />';
		
		
		
		function onPageLoad()
		{
		
			$('#urlType').change(function() {
			    var val = $("#urlType option:selected").text();
			    
			    if(val=='Twitter')
			    {
			    	$("#urlTextLabel").html('Twitter User ids: ');
			    	
			    }
			    else
			    {
			    	$("#urlTextLabel").html('Site URL: ');
			    	
			    }
			    
			});
				
			$.ajax({
		        url: "getUserPrefs.action",
		        type: "POST",    
		        data: {},
		        dataType: "json",  
		        error: function(jqXHR, textStatus, errorThrown){
		        	handleWrongContentType(jqXHR, textStatus, errorThrown);
		        },
		        success: function(data)
		        {
		        	processPrefData(data.prefs);
		        }
			});
			resetFooter();
			
		}
		
		function processPrefData(prefs)
		{
			var totalPrefs = prefs.length;
			for(var i=0;i<totalPrefs;i++)
			{
				var newRowStr = "<tr><td>" + (i+1) + "</td><td width='20px'>" 
								+ prefs[i] + "</td>"  
								+ "<td><input type='button' class='upButton' alt='Move Up' onClick='moveUp(this)'/>&nbsp;<input type='button' class='downButton' onClick='moveDown(this)'/></td>"
								+ "<td><input type='button' class='DeleteButton' alt='Move Down' onClick='deletePref(this)' /></td></tr>"
				$('#'+tblName).append(newRowStr);
			}
			
			if(totalPrefs>0)
				$("#" + tblName).show();
			else
				$("#" + tblName).hide();
		
			resetFooter();
		}
		
		function AddItem()
		{
			clearAllErrors();
			var rowLength = $('#' + tblName + ' tr').length; 
			var urlValue = $("#url").val();
			var urlTypeValue = $("select[name='urlType']").val();
			
			if(checkURL(urlValue))
			{
			
				$.ajax({
			        url: "addUserPrefs.action",
			        type: "POST",    
			        data: {url: urlValue,
			        	   urlType: urlTypeValue
			        	},
			        dataType: "json",  
			        error: function(jqXHR, textStatus, errorThrown){
			        	handleWrongContentType(jqXHR, textStatus, errorThrown);
			        },
			        success: function(data){
			        	if(data.actionErrors != null && data.actionErrors.length > 0)
			        	{
			        		showJsonMessages(data);
			        	}
			        	else
			        	{
			        		$("#url").val('');
			        		$("#" + tblName).find("tr:gt(0)").remove();
			        		
			        		processPrefData(data.prefs);
			        	}
			        }
				});
			}
			else
			{
				showClientErrors("<li><span>" + wrongURL + "</span></li>");
			}
			
		}
		  
		 function deletePref(deleteRow)
		 {
			var rowLength = $('#' + tblName + ' tr').length; 
			var urlValue = deleteRow.parentNode.parentNode.childNodes[1].innerHTML;
			//alert(urlValue);
			$.ajax({
		        url: "deleteUserPrefs.action",
		        type: "POST",    
		        data: {url: urlValue},
		        dataType: "json",  
		        error: function(jqXHR, textStatus, errorThrown){
		        	handleWrongContentType(jqXHR, textStatus, errorThrown);
		        },
		        success: function(data){
		        	if(data.actionErrors != null && data.actionErrors.length > 0)
		        	{
		        		showJsonMessages(data);
		        	}
		        	else
		        	{
		        		$("#" + tblName).find("tr:gt(0)").remove();
		        		processPrefData(data.prefs);
		        		resetFooter();
		        	}
		        }
				});
		 }
		 
		 function moveUp(row)
		 {
			    //var rowIndex = row.parentNode.parentNode.rowIndex;
			    
				var urlValue = row.parentNode.parentNode.childNodes[1].innerHTML;
				//alert(urlValue);
				$.ajax({
			        url: "increasePrefOrder.action",
			        type: "POST",    
			        data: {url: urlValue},
			        dataType: "json",  
			        error: function(jqXHR, textStatus, errorThrown){
			        	handleWrongContentType(jqXHR, textStatus, errorThrown);
			        },
			        success: function(data){
			        	if(data.actionErrors != null && data.actionErrors.length > 0)
			        	{
			        		showJsonMessages(data);
			        	}
			        	else
			        	{
			        		$("#" + tblName).find("tr:gt(0)").remove();
			        		processPrefData(data.prefs);
			        		resetFooter();
			        	}
			        }
					});
		 }
		 
		 function moveDown(row)
		 {
			    //var rowIndex = row.parentNode.parentNode.rowIndex;
			    
				var urlValue = row.parentNode.parentNode.childNodes[1].innerHTML;
				//alert(urlValue);
				$.ajax({
			        url: "decreasePrefOrder.action",
			        type: "POST",    
			        data: {url: urlValue},
			        dataType: "json",  
			        error: function(jqXHR, textStatus, errorThrown){
			        	handleWrongContentType(jqXHR, textStatus, errorThrown);
			        },
			        success: function(data){
			        	if(data.actionErrors != null && data.actionErrors.length > 0)
			        	{
			        		showJsonMessages(data);
			        	}
			        	else
			        	{
			        		$("#" + tblName).find("tr:gt(0)").remove();
			        		processPrefData(data.prefs);
			        		resetFooter();
			        	}
			        }
					});
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
	    	<br />
	    	<table id="specifyURLTbl" >
	    		<tr>
	    			<td align="left" id="urlTextLabel">Site URL:</td>
	    			<td align="left">
	    				<input type="text" id="url" name="url" size="50"/>
	    				<select name="urlType" id="urlType">
	    					<option value="Blog" selected="selected">Blog</option>
	    					<option value="RSS" >RSS</option>
	    					<option value="Twitter">Twitter</option>
	    				</select>&nbsp;&nbsp;
	    				<input class="buttonClass" type="button" name="add" value="Add" onclick="AddItem()" />
	    			</td>
	    		</tr>
	    	</table>
	    	
	    	<br /><br />
	    	<div style="max-height: 550px;overflow:auto;width:60%;border-top:solid 1px black;border-bottom:solid 1px black">
	    	<table id="userPrefTbl" class="dataTable" style="display:none; width:auto;">
	    		<tr>
	    			<th width="25px">#</th>
	    			<th width="90%">URL</th>
	    			<th width="10%">Order</th>
	    			<th width="5%">Delete</th>
	    		</tr>
	    		<tbody>
	    		</tbody>
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
