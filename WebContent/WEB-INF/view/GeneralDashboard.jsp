<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：一般ダッシュボード画面 --%>

<html>
	<head>
		<title>ダッシュボード</title>
	</head>
	<body>
		<h2>ようこそ</h2>
		<form action = "<%= request.getContextPath() %>/LogOut" method = "post">
			<button type = "submit">ログアウト</button>
		</form>
	</body>
</html>