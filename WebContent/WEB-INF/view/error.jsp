<%@ page contentType="text/html; charset=UTF-8"%>

<%-- 概要：エラー画面（アカウント一覧が表示できない等） --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>エラー</title>

<style>
/* Aパターン：最小限のレスポンシブ対応 */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
}

h2 {
  margin-top: 0;
}

.area {
  max-width: 520px;
}

a.button {
  display: inline-block;
  margin-top: 12px;
  padding: 10px;
  text-decoration: none;
  color: #000;
  border: 1px solid #999;
  text-align: center;
  width: 100%;
  box-sizing: border-box;
}

@media (min-width: 481px) {
  a.button {
    width: auto;
    min-width: 160px;
  }
}
</style>
</head>

<body>
  <div class="area">
    <h2>エラーが発生しました</h2>

    <a class="button" href="<%= request.getContextPath() %>/LogIn">
      ログイン画面へ
    </a>

    <%-- AccountList.javaにてエラー時にセッション削除をしている --%>
  </div>
</body>
</html>
