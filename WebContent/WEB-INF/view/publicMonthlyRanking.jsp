<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>月間いいねランキング</title>

<style>
/* Aパターン：学習用・最低限のレスポンシブ対応（カード一覧） */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
}

.area {
  max-width: 960px;
}

/* グリッドはそのまま活用 */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}

.card {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 12px;
}

.rank {
  font-weight: bold;
  margin-bottom: 6px;
}

.name {
  margin-top: 6px;
  font-weight: bold;
}

/* 画像 */
img.avatar {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 10px;
  border: 1px solid #ddd;
  display: block;
}

/* ボタンエリア：スマホは縦、PCは横 */
.actions {
  margin-top: 10px;
}

.actions button,
.actions a {
  display: inline-block;
  padding: 8px;
  border: 1px solid #aaa;
  background: #fff;
  text-align: center;
  text-decoration: none;
  color: #000;
  cursor: pointer;
  box-sizing: border-box;
  width: 100%;
  margin-top: 8px;
}

@media (min-width: 481px) {
  .actions {
    display: flex;
    gap: 8px;
  }
  .actions button,
  .actions a {
    width: auto;
    flex: 1;
    margin-top: 0;
  }
}

/* ページネーション */
.pager {
  margin-top: 16px;
}

.pager a, .pager strong {
  display: inline-block;
  margin-right: 8px;
}
</style>
</head>

<body>
  <div class="area">
    <h2>月間いいねランキング</h2>

    <div class="grid">
      <c:forEach var="r" items="${RANKING_LIST}">
        <div class="card">
          <div class="rank">#${r.rank}</div>

          <c:choose>
            <c:when test="${not empty r.profileImagePath}">
              <img class="avatar"
                   src="${pageContext.request.contextPath}${r.profileImagePath}"
                   alt="profile">
            </c:when>
            <c:otherwise>
              <img class="avatar"
                   src="${pageContext.request.contextPath}/img/profile/default.png"
                   alt="default">
            </c:otherwise>
          </c:choose>

          <div class="name">${r.userName}</div>

          <div class="actions">
            <button type="button" onclick="like(${r.userId}, this)">
              今月いいね <span class="likeCount">${r.likeCount}</span>
            </button>

            <a href="<%=request.getContextPath()%>/PublicAccountDetail?userId=${r.userId}&from=ranking&page=${currentPage}">
              詳細
            </a>
          </div>
        </div>
      </c:forEach>
    </div>

    <!-- ページネーション -->
    <c:if test="${totalPage >= 2}">
      <div class="pager">
        <c:if test="${currentPage > 1}">
          <a href="?page=${currentPage - 1}">前へ</a>
        </c:if>

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

        <c:if test="${currentPage < totalPage}">
          <a href="?page=${currentPage + 1}">次へ</a>
        </c:if>
      </div>
    </c:if>

    <br>
    <form action="<%=request.getContextPath()%>/PublicHome" method="get">
      <button type="submit">戻る</button>
    </form>
  </div>

<script>
function like(userId, btn) {
  fetch('<%=request.getContextPath()%>/PublicLike', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'userId=' + encodeURIComponent(userId)
  })
  .then(res => res.json())
  .then(data => {
    if (data.result === 'ok') {
      btn.querySelector('.likeCount').textContent = data.likeCount;
      // ※ランキングの並び替え（順位更新）までやるなら、ここで location.reload() が一番簡単
      location.reload();
    } else {
      alert('失敗しました');
    }
  })
  .catch(() => alert('通信エラー'));
}
</script>

</body>
</html>
