<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：管理者ダッシュボード画面 --%>

<html>
	<head>
		<title>管理者用ダッシュボード</title>
	</head>
	<body>
		<h2>管理者用ダッシュボード</h2>
		<form action = "<%= request.getContextPath() %>/LogOut" method = "post">
			<button type = "submit">ログアウト</button>
		</form>
	</body>
</html>