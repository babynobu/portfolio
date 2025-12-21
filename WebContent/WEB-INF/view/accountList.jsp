<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--概要：管理者ダッシュボード画面  --%>

<html>
<head>
<title>アカウント一覧</title>
</head>
<body>
	<h2>アカウント一覧</h2>
	<table border="1">
		<tr>
			<th>ユーザーID</th>
			<th>ユーザー名</th>
			<th>ユーザー名 カナ</th>
			<th>メールアドレス</th>
			<th>ステータス</th>
		</tr>

		<c:forEach var="r" items="${ACCOUNT_LIST}">
			<tr>
				<td>${r.userId}</td>
				<td>${r.userName}</td>
				<td>${r.userNameKana}</td>
				<td>${r.email}</td>
				<td>${r.status}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<form action="<%= request.getContextPath() %>/ManagerDashboard">
		<button type="submit">戻る</button>
	</form>
</body>
</html>

