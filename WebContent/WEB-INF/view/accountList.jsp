<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--概要：アカウント一覧画面  --%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>アカウント一覧</title>

  <style>
    .container{
      max-width: 960px;
      margin: 0 auto;
      padding: 16px;
    }

    /* スマホで表が崩れるのを防ぐ（横スクロール） */
    .table-wrap{
      overflow-x: auto;
      -webkit-overflow-scrolling: touch;
      border: 1px solid #ddd;
      border-radius: 10px;
    }

    table{
      width: 100%;
      border-collapse: collapse;
      min-width: 900px; /* 列が多いので保険（好みで調整OK） */
    }

    th, td{
      padding: 8px;
      border: 1px solid #ddd;
      vertical-align: middle;
      white-space: nowrap; /* 行が縦に伸びすぎない */
    }

    th{
      background: #f7f7f7;
      text-align: left;
    }

    button, input[type="submit"]{
      padding: 6px 10px;
      border: 1px solid #aaa;
      border-radius: 8px;
      background: #fff;
      cursor: pointer;
    }

    .pagination{
      margin-top: 10px;
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      align-items: center;
    }

    .pagination a{
      text-decoration: none;
      border: 1px solid #aaa;
      border-radius: 8px;
      padding: 6px 10px;
      color: #000;
      display: inline-block;
    }

    .pagination strong{
      border: 1px solid #000;
      border-radius: 8px;
      padding: 6px 10px;
      display: inline-block;
    }

    .footer-actions{
      margin-top: 16px;
    }
  </style>
</head>

<body>
<div class="container">

  <h2>アカウント一覧</h2>

  <div class="table-wrap">
    <table>
      <tr>
        <th>ユーザーID</th>
        <th>ユーザー名</th>
        <th>ユーザー名 かな</th>
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
          <td>${r.name}</td>
          <td>${r.kana}</td>
          <td class="role">${r.roleLabel}</td>
          <td>${r.email}</td>
          <td class="status">${r.statusLabel}</td>

          <!-- ステータス切り替え -->
          <td>
            <button type="button" onclick="toggleStatus(${r.userId}, this)">切替</button>
          </td>

          <!-- 編集 -->
          <td>
            <form action="<%=request.getContextPath()%>/AccountEdit" method="get">
              <input type="hidden" name="userId" value="${r.userId}">
              <input type="hidden" name="role" value="${r.role}">
              <input type="hidden" name="page" value="${currentPage}">
              <input type="submit" value="編集">
            </form>
          </td>

          <!-- 削除（論理削除） -->
          <td>
            <button type="button" onclick="deleteAccount(${r.userId}, this)">削除</button>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>

  <!-- ページネーション -->
  <div class="pagination">

    <!-- 前へ -->
    <c:if test="${currentPage > 1}">
      <a href="?page=${currentPage - 1}">前へ</a>
    </c:if>

    <!-- ページ番号 -->
    <c:forEach var="p" begin="1" end="${totalPage}">
      <c:choose>
        <c:when test="${p == currentPage}">
          <strong>${p}</strong>
        </c:when>
        <c:otherwise>
          <a href="?page=${p}">${p}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>

    <!-- 次へ -->
    <c:if test="${currentPage < totalPage}">
      <a href="?page=${currentPage + 1}">次へ</a>
    </c:if>

  </div>

  <div class="footer-actions">
    <form action="<%=request.getContextPath()%>/ManagerDashboard">
      <button type="submit">戻る</button>
    </form>
  </div>

  <script>
    function toggleStatus(userId, btn) {
      fetch('<%=request.getContextPath()%>/AccountAction', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
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

    function deleteAccount(userId, btn) {
      if (!confirm('本当に削除しますか？')) return;

      fetch('<%=request.getContextPath()%>/AccountAction', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=delete&userId=' + userId
      })
      .then(res => res.json())
      .then(data => {
        if (data.result === 'ok') {
          location.reload();
        } else {
          alert('削除に失敗しました');
        }
      });
    }
  </script>

</div>
</body>
</html>
