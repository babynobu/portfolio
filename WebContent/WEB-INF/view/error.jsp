<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：エラー画面 --%>

<html>
	<head>
		<title>エラー</title>
	</head>
	<body>
		<h2>エラーが発生しました</h2>
		<a href = "<%= request.getContextPath() %>/LogIn">ログイン画面へ</a>
	</body>
</html>