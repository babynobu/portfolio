<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--概要：管理者ダッシュボード画面  --%>

<html>
<head>
<title>管理者用ダッシュボード</title>
</head>
<body>
	<h2>管理者用ダッシュボード</h2>
	<a>年間いいねランキング</a>
	<br>
	<table border="1">
		<tr>
			<th>順位</th>
			<th>ユーザー名</th>
			<th>いいね数</th>
		</tr>

		<c:forEach var="r" items="${YEARLY_RANKING}">
			<tr>
				<td>${r.ranking}</td>
				<td>${r.userName}</td>
				<td>${r.stampCount}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<a>月間いいねランキング</a>
	<br>
	<table border="1">
		<tr>
			<th>順位</th>
			<th>ユーザー名</th>
			<th>いいね数</th>
		</tr>

		<c:forEach var="r" items="${MONTHLY_RANKING}">
			<tr>
				<td>${r.ranking}</td>
				<td>${r.userName}</td>
				<td>${r.stampCount}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<a href="<%= request.getContextPath() %>/AccountList">アカウント一覧へ</a>
	<br>
	<br>
	<a href="<%= request.getContextPath() %>/DeleteAccountList">削除アカウント一覧へ</a>
	<br>
	<br>
	<a href="<%= request.getContextPath() %>/AccountAdd">アカウント追加</a>
	<br>
	<br>
	<a href="<%= request.getContextPath() %>/ContactUs">お問い合わせ一覧</a>
	<br>
	<br>
	<form action="<%= request.getContextPath() %>/LogOut" method="post">
		<button type="submit">ログアウト</button>
	</form>
</body>
</html>

