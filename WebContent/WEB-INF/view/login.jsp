<%@ page contentType="text/html; charset=UTF-8"%>

<%--login.jsp 概要：--%>

<html>
	<head>
		<title>ログイン画面</title>
	</head>
	<body>
		<h2>ログイン</h2>
		<form action = "<%= request.getContextPath() %>/ExecuteLogin" method = "post" onsubmit="return checkLogin();">
			<p>ログインID<br>
				<input type = "text" name = "LOGIN_ID" maxlength = "20" id = "ID_LOGIN_ID" >
			</p>
			<p>パスワード<br>
				<input type = "password" name = "PASSWORD" id = "ID_PASSWORD">
			</p>
			<div id="errorMsg" style="color:red;"></div>
				<input type = "submit" value = "送信">
		</form>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/userName-check.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/password-check.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/login-check.js"></script>
	</body>
</html>
