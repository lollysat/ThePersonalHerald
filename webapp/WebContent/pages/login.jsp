<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><s:text name="home.label.title"/></title>
    <link href="<s:url value="/styles/main.css"/>" rel="stylesheet" type="text/css"/>
    
      <script type="text/javascript">
		var imgList = new Array("/images/banner1.jpg","/images/banner2.png","/images/banner3.png");
		var linkList = new Array("../pages/Login.jsp","../pages/Login.jsp","../pages/Login.jsp");
		var altLinkNameList = new Array("banner1","banner2","banner3");
		var currentBannerNo = 0;
		var imageCount = 3;
		//window.setInterval("rotate()",10000);

		function rotate()
		{
		  if (currentBannerNo == imageCount)
			currentBannerNo = 0;
 		  
		  var banner = document.getElementById('bannerImg');
		  var link = document.getElementById('bannerLink');
		  banner.src=imgList[currentBannerNo]
		  banner.alt=altLinkNameList[currentBannerNo]
		  document.getElementById('bannerLink').href=linkList[currentBannerNo]
		  currentBannerNo++;
		}
		
		</script>
   </head>

  <body>
  
   <center> 
	
	<!-- BANNER DIV -->
	<s:include value="banner.jsp" />
    
    <!-- To assign a border and center align everything -->
    <div id="contentDiv"> 
	  <div id="content">
		<div id="colOne">
		   <div id="logo2" >
			 <div id="logo">
				
				<!-- LOGIN FORM -->
				<form action="login" method="post">
				  <table width="100%" border="0">
					     <tr>
						    <td width="50%" align="left">User Name: </td>
							<td width="50%" align="left"><input class="inputTextBoxFont" type="text" name="username" size="25" /></td>
						 </tr>
						 <tr>
						    <td width="50%" align="left">Password: </td>
						    <td width="50%" align="left"><input class="inputTextBoxFont" type="password" name="password" size="25"/></td>
						 </tr>
						 <tr>
						    <td height="5px" colspan="2">						       
						    </td>
						 </tr>
		                 
					</table>
					 
					<table width="100%" border="0">
						  <tr>
						     <td align="center">
						        <input type="submit" value="Login" id="loginSubmit"/>
						    </td>
						  </tr>
						  
		                  <tr>
						    <td align="center">
						        
						    </td>
						  </tr>
	
		  				  <tr>
		  				     <td align="center">
		  				     	<s:if test='#session.user.roleFunctions.contains("loadForgotPassword")'>
		  				        <a href="<%= request.getContextPath() %>/example/showForgotPassword.action">forgot password</a>
		  				        </s:if>
		  				       
		  				     </td>
		  				 </tr>
		  				 
		  				 <!--  Display Login error messages if any -->
		  				 <tr>
		  				   <td align="center">
		  				      <div class="errorMsg">
								 <s:iterator value="errorMsgList">
								     <s:property />
								     <br/>
								 </s:iterator>
							   </div> 
						   </td>
		  				 </tr>
				     </table>
				 </form>
				 
			 </div>
			</div>
			
		   <br/>
		   <br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<div class="box">
				<h3>News and Events</h3>
				<ul class="bottom">
					<li class="first"><a href="#">Heniomap contract awarded to KBSI</a></li>
					<li><a href="#">Comprehensive modeling of COA selection decision problems</a></li>
					<li><a href="#">Defined Decision theory rules and ISMAUT metrics</a></li>
					<li><a href="#">Developed PREFERENCE models to support EC 10 / Green Devil II demonstration</a></li>
					<li><a href="#">Developed the first release of HENIOMAP COA ontology</a></li>
					<li><a href="#">Developed an Eclipse plug-in to transform the RDF ISMAUT representation into a linear program</a></li>
					<li><a href="#">Developed the framework for the HENIOMAP application for end users</a></li>
					<li><a href="#">Published and submitted papers</a></li>
				</ul>
			</div>
			
		</div>
		
		<div id="colTwo">
			<h2>Welcome to HEN-IO-MAP</h2>
			<img src="<s:url value="/images/heniomap_small.png"/>" alt="" width="300" height="160" class="image" />
			<div class="box">
				<p class="bottom">The focus of <strong>Hidden Enemy Network Influence Operations(HEN-IO-MAP)</strong> system is to provide an innovative utility-theoretic preference model for influence operations (IO) modeling.   The main goal of the HEN-IO-MAP project is to influence positive and discouraging negative behavior within an enemy network.<br/></p>
				<br/><br/><br/>	
										
				<h3>&nbsp;Key innovations in the system include</h3>
				<ul class="bottom">
					<li>HEN-IO-MAP provides a visualization of the network identity space in the form of an influence operations contour map</li>
					<li>HEN-IO-MAP simplifies the knowledge acquisition process in building decision models for hidden enemy network influence</li>
					<li>HEN-IO-MAP minimizes the amount of technical knowledge required by non-technical subject-matter experts (SME) to build social and cultural models for IO selection and assessment.</li>
					<li>HEN-IO-MAP allows a decision-maker to select the operations that encourage positive behavior and discourage negative behavior froma countour map</li>
				</ul>
			</div>

			<div class="box">
				<h3>&nbsp;Current Problem Being Addressed</h3>
				<p class="bottom">Influence Operations (IO) are intended to encourage positive behavior and discourage negative behavior within and among hidden enemy networks.By winning the hearts and minds of the general population, the critical infrastructural support to an adversary (that is, finances, material, recruitment, etc.) is eroded.
				  Many prior approaches for Influence Operations (IO) planning require the planners (or their proxies) to build complex models that require some technical expertise, or to engage in lengthy manual simulations or games.  This approach is complex, error prone, brittle, time and effort intensive, and costly.</p>
			</div>

			<div class="box">
				<h3>Proposed Solution Approach</h3>
				<p class="bottom">HEN-IO-MAP would simplify the knowledge acquisition effort that is required to build a social and cultural model of enemy networks. In HEN-IO-MAP, SMEs would only have to provide a ranking of possible IO outcomes and associated objective attributes. 
				 Planners would then easily identify the most preferred IO outcomes by observing the outcomes located at “higher elevations” in an appropriately drawn contour map.</p>
			</div>
		</div>
	  </div>
	 
	</div>
	
	<!--  Include the Footer  -->
	<s:include value="footer.jsp" />
  </center>
  
</body>
</html>

