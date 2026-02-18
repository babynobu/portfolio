<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>プロフィール詳細</title>

<style>
/* Aパターン：学習用・最低限のレスポンシブ対応 */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
}

.area {
  max-width: 760px;
}

img.avatar {
  width: 100%;
  height: auto;
  max-height: 520px;
  object-fit: contain;      /* 見切れ防止 */
  border: 1px solid #ddd;
  border-radius: 12px;
  display: block;
}

/* 行 */
.row {
  margin-top: 10px;
}

.label {
  font-weight: bold;
}

/* ボタン・リンク（Aパターンなので軽め） */
.actions {
  margin-top: 14px;
}

.actions button,
.actions a {
  display: inline-block;
  padding: 10px;
  border: 1px solid #aaa;
  text-decoration: none;
  color: #000;
  background: #fff;
  cursor: pointer;
  text-align: center;
  margin-right: 8px;
  margin-top: 8px;
  box-sizing: border-box;
}

/* スマホは押しやすく横いっぱい、PCは自然幅 */
.actions button {
  width: 100%;
  max-width: 520px;
  font-size: 16px;
}

.actions a {
  width: 100%;
  max-width: 520px;
}

@media (min-width: 481px) {
  .actions button,
  .actions a {
    width: auto;
    min-width: 180px;
  }
}
</style>
</head>

<body>
  <div class="area">
    <h2>プロフィール詳細</h2>

    <c:choose>
      <c:when test="${not empty ACCOUNT.profileImagePath}">
        <img class="avatar"
             src="${pageContext.request.contextPath}${ACCOUNT.profileImagePath}"
             alt="profile">
      </c:when>
      <c:otherwise>
        <img class="avatar"
             src="${pageContext.request.contextPath}/img/profile/default.png"
             alt="default">
      </c:otherwise>
    </c:choose>

    <div class="row">
      <span class="label">名前：</span>${ACCOUNT.userName}
    </div>

    <div class="row">
      <span class="label">フリガナ：</span>${ACCOUNT.userNameKana}
    </div>

    <div class="row">
      <span class="label">性別：</span>
      <c:choose>
        <c:when test="${ACCOUNT.gender == 1}">男性</c:when>
        <c:when test="${ACCOUNT.gender == 2}">女性</c:when>
        <c:otherwise>未設定</c:otherwise>
      </c:choose>
    </div>

    <div class="row">
      <span class="label">年齢：</span>
      <c:choose>
        <c:when test="${not empty ACCOUNT.age}">${ACCOUNT.age}歳</c:when>
        <c:otherwise>未設定</c:otherwise>
      </c:choose>
    </div>

    <div class="row">
      <span class="label">自己紹介：</span><br>
      <c:out value="${ACCOUNT.introduction}" />
    </div>

    <div class="actions">
      <!-- 既存 PublicLike を使う（非同期） -->
      <button type="button" onclick="like(${ACCOUNT.userId}, this)">
        今月いいね <span class="likeCount">${ACCOUNT.monthlyLikeCount}</span>
      </button>

      <!-- 戻る -->
      <c:choose>
        <c:when test="${from == 'ranking'}">
          <a href="<%=request.getContextPath()%>/PublicMonthlyRanking?page=${backPage}">ランキングに戻る</a>
        </c:when>
        <c:otherwise>
          <a href="<%=request.getContextPath()%>/PublicAccountList?page=${empty backPage ? 1 : backPage}">
            一覧に戻る
          </a>
        </c:otherwise>
      </c:choose>
    </div>
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
    } else {
      alert('失敗しました');
    }
  })
  .catch(() => alert('通信エラー'));
}
</script>

</body>
</html>
