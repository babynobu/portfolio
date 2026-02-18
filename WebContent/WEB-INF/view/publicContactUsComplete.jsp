<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ完了</title>

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
    <h2>お問い合わせを送信しました</h2>

    <form action="<%=request.getContextPath()%>/PublicHome" method="get">
      <button type="submit">トップへ戻る</button>
    </form>
  </div>
</body>
</html>
