<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 概要：管理者アカウント一覧画面（削除済み） --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>アカウント一覧</title>

<style>
/* Aパターン：最低限のレスポンシブ対応（一覧表） */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
}

h2 {
  margin-top: 0;
}

/* ★画面全体の最大幅（横に無限に広がるのを防ぐ） */
.page {
  max-width: 1200px;   /* 列が多いので manager より広め */
  margin: 0 auto;      /* 中央寄せ */
}

/* 表は崩さず、はみ出した分だけ横スクロール */
.table-wrap {
  width: 100%;
  overflow-x: auto;
}

/* 内容で横に暴れないよう固定レイアウト */
table {
  border-collapse: collapse;
  width: 100%;
  min-width: 900px;     /* スマホは横スクロール前提 */
  table-layout: fixed;  /* 内容に引きずられない */
}

/* 基本は折り返しOK */
th, td {
  border: 1px solid #999;
  padding: 8px;
  text-align: left;

  white-space: normal;
  overflow-wrap: anywhere;
}

/* 操作列（復元・削除）は折り返さない */
td:last-child,
td:nth-last-child(2) {
  white-space: nowrap;
}

button {
  padding: 8px 10px;
  font-size: 14px;
}

.actions {
  margin-top: 12px;
}


</style>

</head>

<body>
  <h2>アカウント一覧</h2>

  <div class="page">

  <div class="table-wrap">
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
          <td>${r.name}</td>
          <td>${r.kana}</td>
          <td class="role">${r.roleLabel}</td>
          <td>${r.email}</td>

          <!-- 復元 -->
          <td>
            <button type="button" onclick="restoreAccount(${r.userId}, this)">復元</button>
          </td>

          <!-- 削除（物理削除） -->
          <td>
            <button type="button" onclick="physicsDeleteAccount(${r.userId}, this)">削除</button>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>

  <div class="actions">
    <form action="<%= request.getContextPath() %>/ManagerDashboard" method="get">
      <button type="submit">戻る</button>
    </form>
  </div>

  </div>

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
