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
			<th>ステータス</th>
			<th>ステータス切り替え</th>
			<th>編集</th>
			<th>削除</th>
		</tr>

		<c:forEach var="r" items="${ACCOUNT_LIST}">
			<tr>
				<td>${r.userId}</td>
				<td>${r.userName}</td>
				<td>${r.userNameKana}</td>
				<td class="role">${r.roleLabel}</td>
				<td>${r.email}</td>
				<td class="status">${r.statusLabel}</td>
				<!-- ステータス切り替え -->
				<td>
					<button onclick="toggleStatus(${r.userId}, this)">切替</button>
				</td>
				<!-- 編集 -->
				<td>
					<form action="<%= request.getContextPath() %>/AccountEdit" >
						<input type="hidden" name="userId" value="${r.userId}">
						<input type="hidden" name="role" value="${r.role}">
						<input type="submit" value="編集">
					</form>
				</td>
				<!-- 削除（論理削除） -->
				<td>
					<button onclick="deleteAccount(${r.userId}, this)">削除</button>
				</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<form action="<%= request.getContextPath() %>/ManagerDashboard">
		<button type="submit">戻る</button>
	</form>
	<script>

	function toggleStatus(userId, btn) {
		fetch('<%= request.getContextPath() %>/AccountAction', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: 'action=status&userId=' + userId
		})
		.then(res => res.json())
		.then(data => {
			if (data.result === 'ok') {
				// ステータス表示を書き換え
				btn.closest('tr').querySelector('.status').textContent = data.newStatus;
			} else {
				alert('失敗しました');
			}
		});
	}

	function deleteAccount(userId, btn) {
		if (!confirm('本当に削除しますか？')) return;

		fetch('<%= request.getContextPath() %>/AccountAction', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: 'action=delete&userId=' + userId
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

