<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%--概要：お問い合わせ一覧画面  --%>

<html>
<head>
<title>お問い合わせ一覧</title>
</head>
<body>
	<h2>お問い合わせ一覧</h2>
	<table border="1">
		<tr>
			<th>お問い合わせID</th>
			<th>カテゴリー</th>
			<th>本文</th>
			<th>メールアドレス</th>
			<th class="status">ステータス</th>
			<th>作成日時</th>
			<th>最終更新日時</th>
			<th>詳細</th>
		</tr>

		<c:forEach var="r" items="${CONTACT_US_LIST}">
			<tr>
				<td>${r.contactUsId}</td>
				<td>${r.categoryName}</td>
				<td>
					${fn:substring(r.body,0,10)}
					<c:if test="${fn:length(r.body) > 10}">...</c:if>
				</td>
				<td>${r.email}</td>
				<td class="status">${r.statusLabel}</td>
				<td>${r.createdAt}</td>
				<td>${r.updatedAt}</td>
				<td>
					<form action="<%= request.getContextPath() %>/ContactUsDetail" >
						<input type="hidden" name="contactUsId" value="${r.contactUsId}">
						<input type="submit" value="詳細">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<form action="<%= request.getContextPath() %>/ManagerDashboard">
		<button type="submit">戻る</button>
	</form>


	<%--
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
	--%>

</body>
</html>

