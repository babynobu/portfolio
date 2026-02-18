<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 概要：管理者ダッシュボード画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>管理者用ダッシュボード</title>

<style>
/* Aパターン：学習用・最低限のレスポンシブ対応 */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
}

h2 {
  margin-top: 0;
}

/* ★画面全体の最大幅（ここで横に無限に広がるのを止める） */
.page {
  max-width: 960px;   /* 好みで 760 / 960 / 1200 */
  margin: 0 auto;     /* 中央寄せ */
}

/* 表は崩さず、横にはみ出す分はここでスクロール */
.table-wrap {
  width: 100%;
  overflow-x: auto;
}

/* managerDashboard は列が少ないので min-width は小さめでOK */
table {
  border-collapse: collapse;
  width: 100%;
  min-width: 480px;
}

th, td {
  border: 1px solid #999;
  padding: 8px;
  text-align: left;
  white-space: nowrap;
}

.section-title {
  display: inline-block;
  margin-top: 8px;
  margin-bottom: 4px;
}

.menu {
  margin-top: 12px;
}

.menu a {
  display: inline-block;
  margin-bottom: 6px;
}

button {
  padding: 10px;
  font-size: 16px;
  width: 100%;
  max-width: 520px;
  margin-top: 12px;
}

@media (min-width: 481px) {
  button {
    width: auto;
    min-width: 160px;
  }
}
</style>
</head>

<body>
  <div class="page">
    <h2>管理者用ダッシュボード</h2>

    <a class="section-title">年間いいねランキング</a>
    <div class="table-wrap">
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
    </div>

    <br>

    <a class="section-title">月間いいねランキング</a>
    <div class="table-wrap">
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
    </div>

    <br>

    <div class="menu">
      <a href="<%= request.getContextPath() %>/AccountList">アカウント一覧へ</a><br>
      <a href="<%= request.getContextPath() %>/DeleteAccountList">削除アカウント一覧へ</a><br>
      <a href="<%= request.getContextPath() %>/AccountAdd">アカウント追加</a><br>
      <a href="<%= request.getContextPath() %>/ContactUs">お問い合わせ一覧</a><br>
      <a href="<%= request.getContextPath() %>/ManagerSetting">設定</a>
    </div>

    <form action="<%= request.getContextPath() %>/LogOut" method="post">
      <button type="submit">ログアウト</button>
    </form>
  </div>
</body>
</html>
