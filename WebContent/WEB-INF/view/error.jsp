<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：エラー画面 アカウント一覧が表示できない  --%>

<html>
	<head>
		<title>エラー</title>
	</head>
	<body>
		<h2>エラーが発生しました</h2>
		<a href = "<%= request.getContextPath() %>/LogIn">ログイン画面へ</a>
		<!-- AccountList.javaにてエラー時にセッション削除をしている -->
	</body>
</html>