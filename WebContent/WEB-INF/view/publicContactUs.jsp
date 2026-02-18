<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ</title>

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

.form-area {
  max-width: 720px;
}

.error {
  color: red;
  font-size: 0.9em;
  margin-top: 6px;
}

input[type="text"], select, textarea {
  width: 100%;
  max-width: 720px;
  padding: 8px;
  font-size: 16px;
  box-sizing: border-box;
}

textarea {
  resize: vertical;
}

button {
  width: 100%;
  max-width: 720px;
  padding: 10px;
  font-size: 16px;
  margin-top: 8px;
}

@media (min-width: 481px) {
  button {
    width: auto;
    min-width: 140px;
  }
}
</style>
</head>

<body>
<h2>お問い合わせ</h2>

<div class="form-area">
  <c:if test="${not empty errorMessage}">
    <div class="error">${errorMessage}</div>
  </c:if>

  <form action="<%=request.getContextPath()%>/ExecutePublicContactUs" method="post">

    <p>
      メールアドレス<br>
      <input type="text" name="email" value="${inputEmail}">
      <c:if test="${not empty errors.email}">
        <div class="error">${errors.email}</div>
      </c:if>
    </p>

    <p>
      問い合わせカテゴリー<br>
      <select name="categoryId">
        <option value="">選択してください</option>
        <c:forEach var="c" items="${CATEGORIES}">
          <option value="${c.categoryId}"
            <c:if test="${inputCategoryId != null && inputCategoryId == c.categoryId}">selected</c:if>>
            ${c.categoryName}
          </option>
        </c:forEach>
      </select>
      <c:if test="${not empty errors.categoryId}">
        <div class="error">${errors.categoryId}</div>
      </c:if>
    </p>

    <p>
      本文<br>
      <textarea name="body" rows="8">${inputBody}</textarea>
      <c:if test="${not empty errors.body}">
        <div class="error">${errors.body}</div>
      </c:if>
    </p>

    <p>
      <button type="submit">送信</button>
    </p>
  </form>

  <form action="<%=request.getContextPath()%>/PublicHome" method="get">
    <p>
      <button type="submit">戻る</button>
    </p>
  </form>
</div>

</body>
</html>
