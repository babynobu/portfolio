<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ詳細</title>

<style>
/* Aパターン：最低限のレスポンシブ対応（詳細） */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
}

h2 {
  margin-top: 0;
}

.area {
  max-width: 720px;
}

pre {
  white-space: pre-wrap;      /* スマホで横にはみ出さない */
  word-break: break-word;
  border: 1px solid #999;
  padding: 10px;
  margin: 0;
}

select {
  width: 100%;
  max-width: 520px;
  padding: 8px;
  font-size: 16px;
}

button {
  padding: 10px;
  font-size: 16px;
  margin-top: 8px;
  width: 100%;
  max-width: 520px;
}

@media (min-width: 481px) {
  button {
    width: auto;
    min-width: 160px;
  }
  select {
    width: auto;
    min-width: 220px;
  }
}
</style>
</head>

<body>
  <div class="area">
    <h2>お問い合わせ詳細</h2>

    <p>お問い合わせID<br>
      ${contactUs.contactUsId}
    </p>
    <br>

    <p>カテゴリー<br>
      ${contactUs.categoryName}
    </p>
    <br>

    <p>本文</p>
    <pre>${contactUs.body}</pre>
    <br>

    <p>メールアドレス<br>
      ${contactUs.email}
    </p>
    <br>

    <p>ステータス</p>
    <form action="<%=request.getContextPath()%>/ContactUs" method="post">
      <input type="hidden" name="contactUsId" value="${contactUs.contactUsId}">

      <select name="status">
        <option value="1" ${contactUs.status == 1 ? "selected" : ""}>未対応</option>
        <option value="2" ${contactUs.status == 2 ? "selected" : ""}>対応中</option>
        <option value="3" ${contactUs.status == 3 ? "selected" : ""}>対応済</option>
      </select>

      <br>
      <button type="submit">ステータス更新</button>
    </form>

    <form action="<%=request.getContextPath()%>/ContactUs" method="get">
      <button type="submit">戻る</button>
    </form>
  </div>
</body>
</html>
