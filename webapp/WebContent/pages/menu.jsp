<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<ol id="menuList">
      <li><a href="<s:url action='loadPaper' />">News-Paper</a></li>
      <li><a href="<s:url action='loadPrefs' />">Preferences</a></li>
      <li id="refreshPaper" style="display:none;"><a href="javascript:generatePaper()"><img style="height:13px;vertical-align:middle" src="<s:url value='/images/refresh.png' />" /> Refresh</a></li>
      <li style="float:right"><a href="<s:url action='logout' />">Logout</a></li>
</ol>