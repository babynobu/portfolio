<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--概要：管理者アカウント一覧画面  --%>

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
			<th>権限</th>
			<th>メールアドレス</th>
			<th>復元</th>
			<th>完全削除</th>
		</tr>

		<c:forEach var="r" items="${ACCOUNT_LIST}">
			<tr>
				<td>${r.userId}</td>
				<td>${r.userName}</td>
				<td>${r.userNameKana}</td>
				<td class="role">${r.roleLabel}</td>
				<td>${r.email}</td>
			<!-- 	<td class="status">${r.statusLabel}</td>
				<td>
					<button onclick="toggleStatus(${r.userId}, this)">切替</button>
				</td> -->
				<!-- 復元 -->
				<td>
					<button onclick="restoreAccount(${r.userId}, this)">復元</button>
				</td>
				<!-- 削除（物理削除） -->
				<td>
					<button onclick="physicsDeleteAccount(${r.userId}, this)">削除</button>
				</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<form action="<%= request.getContextPath() %>/ManagerDashboard">
		<button type="submit">戻る</button>
	</form>


	<script>

	function restoreAccount(userId, btn) {
		fetch('<%= request.getContextPath() %>/AccountAction', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: 'action=restore&userId=' + userId
		})
		.then(res => res.json())
		.then(data => {
			if (data.result === 'ok') {
				// 行を削除
				btn.closest('tr').remove();
			} else {
				alert('復元に失敗しました');
			}
		});
	}

	function physicsDeleteAccount(userId, btn) {
		if (!confirm('削除は取り消せません。本当に削除しますか？')) return;

		fetch('<%= request.getContextPath() %>/AccountAction', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: 'action=physicsDelete&userId=' + userId
		})
		.then(res => res.json())
		.then(data => {
			if (data.result === 'ok') {
				// 行を削除
				btn.closest('tr').remove();
			} else {
				alert('削除に失敗しました');
			}
		});
	}
	</script>

</body>
</html>

