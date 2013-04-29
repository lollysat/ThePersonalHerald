<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div>
	<ul id="clientSideErrors" class="errorMsg">
		<s:iterator value="actionErrors"><li><span><s:property escape="false" /></span></li></s:iterator>
	</ul>
	<ul id="clientSideMessages" class="errorMsg">
		<s:iterator value="actionMessages"><li><span><s:property escape="false" /></span></li></s:iterator>
	</ul>
</div>		