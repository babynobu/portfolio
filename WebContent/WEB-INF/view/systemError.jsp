<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--概要：アカウント追加失敗画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>アカウント追加失敗</title>

<style>
/* 基本 */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP","Hiragino Kaku Gothic ProN","Meiryo",sans-serif;
  box-sizing: border-box;
}
*, *::before, *::after { box-sizing: inherit; }

/* 画面の最大幅（PCで横に伸びすぎない）＋中央寄せ */
.area {
  max-width: 720px;
  margin: 0 auto;
}

/* エラー表示（長文でも崩れない） */
.error {
  color: #b00020;
  font-weight: bold;
  margin: 12px 0;
  white-space: pre-wrap;
  word-break: break-word;
  overflow-wrap: anywhere;
}

/* 戻るボタン（aタグをボタン化） */
a.button {
  display: inline-block;
  text-decoration: none;
  color: #000;
  border: 1px solid #aaa;
  background: #fff;
  padding: 10px 14px;
  font-size: 16px;
  text-align: center;
  cursor: pointer;
  width: 100%;          /* スマホで押しやすい */
  border-radius: 8px;
}

/* 481px以上はボタン幅を必要以上に伸ばさない */
@media (min-width: 481px) {
  a.button {
    width: auto;
    min-width: 220px;
  }
}
</style>
</head>

<body>
  <div class="area">
    <h2>アカウントの追加が失敗しました</h2>

    <c:if test="${not empty errorMessage}">
      <div class="error">${errorMessage}</div>
    </c:if>

    <a class="button" href="#"
      onclick="history.length > 1 ? history.back() : location.href='<%=request.getContextPath()%>/LogIn'; return false;">
      戻る
    </a>
  </div>
</body>
</html>
