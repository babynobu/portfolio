<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>カテゴリー編集</title>

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
  max-width: 520px;
}

.error {
  color: red;
  font-size: 0.9em;
  margin-top: 6px;
}

input[type="text"] {
  width: 100%;
  max-width: 520px;
  padding: 8px;
  font-size: 16px;
}

input[type="submit"], button {
  width: 100%;
  max-width: 520px;
  padding: 10px;
  font-size: 16px;
  margin-top: 8px;
}

@media (min-width: 481px) {
  input[type="submit"], button {
    width: auto;
    min-width: 120px;
  }
}
</style>
</head>

<body>
<h2>カテゴリー編集</h2>

<div class="form-area">
  <form action="<%=request.getContextPath()%>/ExecuteCategoryEdit" method="post">
    <input type="hidden" name="categoryId" value="${category.categoryId}">

    <p>
      カテゴリー名<br>
      <input type="text" name="categoryName" value="${category.categoryName}">
      <c:if test="${not empty errors.categoryName}">
        <div class="error">${errors.categoryName}</div>
      </c:if>
    </p>

    <p>
      <input type="submit" value="更新">
    </p>
  </form>

  <c:if test="${not empty errorMessage}">
    <div class="error">
      ${errorMessage}
    </div>
  </c:if>

  <form action="<%=request.getContextPath()%>/CategoryList" method="get">
    <p>
      <button type="submit">戻る</button>
    </p>
  </form>
</div>

</body>
</html>
