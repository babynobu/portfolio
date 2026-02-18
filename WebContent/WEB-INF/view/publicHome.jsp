<%@ page contentType="text/html; charset=UTF-8"%>

<%-- 概要：公開ページホーム画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ホーム画面</title>

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

a.link {
  display: inline-block;
  margin-top: 10px;
}

button {
  width: 100%;
  max-width: 520px;
  padding: 10px;
  font-size: 16px;
  margin-top: 12px;
}

@media (min-width: 481px) {
  button {
    width: auto;
    min-width: 200px;
  }
}
</style>
</head>

<body>
  <div class="area">
    <h2>ようこそ</h2>

    <a class="link" href="<%= request.getContextPath() %>/PublicAccountList">アカウント一覧</a>
    <br><br>

    <a class="link" href="<%= request.getContextPath() %>/PublicMonthlyRanking">月間いいねランキング</a>
    <br><br>

    <a class="link" href="<%=request.getContextPath()%>/PublicContactUs">お問い合わせはこちら</a>
  </div>
</body>
</html>
