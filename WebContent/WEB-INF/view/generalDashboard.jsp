<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：一般ダッシュボード画面 --%>

<html>
	<head>
		<title>ダッシュボード</title>
	</head>
	<body>
		<h2>ようこそ</h2>
		<a>年間いいね数：${YEARLY_COUNT}</a><br>
		<a>月間いいね数：${MONTHLY_COUNT}</a>
		<form action = "<%= request.getContextPath() %>/LogOut" method = "post">
			<button type = "submit">ログアウト</button>
		</form>
	</body>
</html>
