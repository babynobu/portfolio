<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- 概要：管理者用お問い合わせ一覧画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ一覧</title>

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

/* ★画面全体の最大幅（無限横拡大を防ぐ） */
.page {
  max-width: 1200px;   /* contactUs は列が多いので少し広め */
  margin: 0 auto;
}

/* 表がはみ出したら、ここで横スクロール */
.table-wrap {
  width: 100%;
  overflow-x: auto;
}

/* 内容で横に暴れないよう固定 */
table {
  border-collapse: collapse;
  width: 100%;
  min-width: 960px;     /* スマホは横スクロール前提 */
  table-layout: fixed;  /* 内容に引きずられない */
}

th, td {
  border: 1px solid #999;
  padding: 8px;
  text-align: left;
  vertical-align: top;

  white-space: normal;
  overflow-wrap: anywhere;
}

/* 本文列は読みやすさ優先 */
td.body {
  min-width: 220px;
}

/* 操作列（詳細など）は折り返さない */
td:last-child {
  white-space: nowrap;
}

button,
input[type="submit"] {
  padding: 8px 10px;
  font-size: 14px;
}

.actions {
  margin-top: 12px;
}

</style>

</head>

<body>
  <h2>お問い合わせ一覧</h2>

	<div class="page">
  <div class="table-wrap">
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
          <td class="body">
            ${fn:substring(r.body,0,10)}
            <c:if test="${fn:length(r.body) > 10}">...</c:if>
          </td>
          <td>${r.email}</td>
          <td class="status">${r.statusLabel}</td>
          <td>${r.createdAt}</td>
          <td>${r.updatedAt}</td>
          <td>
            <form action="<%= request.getContextPath() %>/ContactUsDetail" method="get">
              <input type="hidden" name="contactUsId" value="${r.contactUsId}">
              <input type="submit" value="詳細">
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>

  <div class="actions">
    <a href="<%= request.getContextPath() %>/CategoryList">カテゴリー一覧へ</a>

    <form action="<%= request.getContextPath() %>/ManagerDashboard" method="get">
      <button type="submit">戻る</button>
    </form>
  </div>
  </div>

</body>
</html>
