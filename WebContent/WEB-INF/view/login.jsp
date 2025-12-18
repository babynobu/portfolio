<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：ログイン画面 --%>

<html>
	<head>
		<title>ログイン画面</title>
	</head>
	<body>
		<h2>ログイン</h2>

		<form action = "<%= request.getContextPath() %>/ExecuteLogin" method = "post" onsubmit="return checkform();">
			<p>ユーザーID<br>
				<input type = "text" name = "USER_ID" maxlength = "20" id = "ID_USER_ID" >
			</p>
			<p>パスワード<br>
				<input type = "password" name = "PASSWORD" id = "ID_PASSWORD">
			</p>
			<div id="errorMsg" style="color:red;"></div>
				<input type = "submit" value = "送信">
		</form>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/brank-check.js"></script>
	</body>
</html>