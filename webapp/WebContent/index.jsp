<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>The Web News</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	
	<link rel="stylesheet" type="text/css" href="<s:url value="/css/style.css" />" />
<!--[if lte IE 6]><link rel="stylesheet" type="text/css" href="ie.css" /><![endif]-->
</head>
<script>
	var demoPage = "<s:url value="/pages/demo.jsp" />";
	function fwdToDemoPage(){ 
	document.location.href(demoPage); 
	} 
</script>
<body>
<center>

  <div id="header">
   
    <s:include value="pages/banner.jsp" />
    
    <ol>
      <li><a href="#"></a></li>
      
	</ol>
    <div class="break"></div>
  </div>
  <div id="contentWrapper" style="height:700px">
  <!-- END header -->
  <!-- BEGIN content -->
  <form action="login.action">
  

	    
	    <br /><br />
	     <div class="errorMsg">
			 <s:iterator value="errorMsgList">
			     <s:property />
			     <br/>
			 </s:iterator>
		   </div> 
	    	<br /><br />
	    	<table width="200px">
	    		<tr>
	    			<td width="50%" align="right">Username</td>
	    			<td width="50%" align="left"><input type="text" name="username" /></td>
	    		</tr>
	    		<tr>
	    			<td width="50%" align="right">Password</td>
	    			<td width="50%" align="left"><input type="password" name="password" /></td>
	    		</tr>
	    		<tr>
	    			<td colspan="2" align="center">
	    				<input type="submit" id="btnSubmit" value="Login" class="btnLogin"/>
	    				
	    				<input type="button" value="Demo" class="btnDemo" onClick="fwdToDemoPage()" />
	    			</td>
	    			
	    		</tr>
	    	</table>
	    	
	    	
	    	
	 	
	    
    
  </form>
 

</div>

<!-- BEGIN footer -->
  <s:include value="pages/footer.jsp" />
</center> 
</body>
<!-- END wrapper -->
</html>
