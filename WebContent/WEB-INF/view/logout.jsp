<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：ログアウト画面 --%>

<html>
	<head>
		<title>ログアウト画面</title>
	</head>
	<body>
		<h2>ログアウトしました</h2>
		<a href = "<%= request.getContextPath() %>/LogIn">ログイン画面へ</a>
	</body>
</html>