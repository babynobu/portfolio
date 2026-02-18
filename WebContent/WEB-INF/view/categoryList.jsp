<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 概要：カテゴリー一覧画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>カテゴリー一覧</title>

<style>
/* Aパターン：最低限のレスポンシブ対応（一覧表） */
body {
	margin: 0;
	padding: 16px;
	font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo",
		sans-serif;
}

h2 {
	margin-top: 0;
}

/* ★画面全体の最大幅（無限横拡大を防ぐ） */
.page {
	max-width: 960px; /* categoryList は列が少なめなので 960 で十分 */
	margin: 0 auto;
}

/* 横スクロールでスマホ対応（表は無理に崩さない） */
.table-wrap {
	width: 100%;
	overflow-x: auto;
}

/* 内容で横に暴れない固定レイアウト */
table {
	border-collapse: collapse;
	width: 100%;
	min-width: 720px; /* スマホは横スクロール前提 */
	table-layout: fixed; /* 内容に引きずられない */
}

th, td {
	border: 1px solid #999;
	padding: 8px;
	text-align: left;
	white-space: normal;
	overflow-wrap: anywhere;
}

/* 操作列（編集・削除）は折り返さない */
td:last-child, td:nth-last-child(2) {
	white-space: nowrap;
}

button, input[type="submit"] {
	padding: 8px 10px;
	font-size: 14px;
}

.actions {
	margin-top: 12px;
}

.actions form {
	display: inline-block;
	margin-right: 8px;
}

a.add-link {
	display: inline-block;
	margin-top: 12px;
}
</style>

</head>

<body>
	<div class="page">
		<h2>カテゴリー一覧</h2>

		<div class="table-wrap">
			<table border="1">
				<tr>
					<th>カテゴリーID</th>
					<th>カテゴリー名</th>
					<th>作成日</th>
					<th>更新日</th>
					<th>編集</th>
					<th>削除</th>
				</tr>

				<c:forEach var="r" items="${CATEGORY_LIST}">
					<tr>
						<td>${r.categoryId}</td>
						<td>${r.categoryName}</td>
						<td>${r.createdAt}</td>
						<td>${r.updatedAt}</td>

						<!-- 編集 -->
						<td>
							<form action="<%= request.getContextPath() %>/CategoryEdit"
								method="get">
								<input type="hidden" name="categoryId" value="${r.categoryId}">
								<input type="submit" value="編集">
							</form>
						</td>

						<!-- 削除（物理削除） -->
						<td>
							<button type="button"
								onclick="deleteCategory(${r.categoryId}, this)">削除</button>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>

		<div class="actions">
			<form action="<%= request.getContextPath() %>/ContactUs" method="get">
				<button type="submit">戻る</button>
			</form>
		</div>

		<a class="add-link" href="<%= request.getContextPath() %>/CategoryAdd">カテゴリー追加</a>

		<script>
    // ※この画面では使っていないので、残すなら後で整理してもOK
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
          btn.closest('tr').querySelector('.status').textContent = data.newStatus;
        } else {
          alert('失敗しました');
        }
      });
    }

    function deleteCategory(categoryId, btn) {
      if (!confirm('本当に削除しますか？')) return;

      fetch('<%= request.getContextPath() %>/CategoryAction', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'action=physicsDelete&categoryId=' + categoryId
      })
      .then(res => res.json())
      .then(data => {
        if (data.result === 'ok') {
          btn.closest('tr').remove();
        } else {
          alert('削除に失敗しました');
        }
      });
    }
  </script>
	</div>
</body>
</html>
