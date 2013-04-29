<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="break"></div>
	<s:if test="#session.user != null">
		<p style="float:right;margin-right:10px">Welcome <s:property value="#session.user" /></p>
	</s:if>
	
    <div class="logo">
      <h1>The Personal Herald</h1>
      <p style="align:left;">Your personal newspaper</p>
    </div>
<div class="break"></div>