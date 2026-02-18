<%@ page contentType="text/html; charset=UTF-8"%>

<%-- 概要：プロフィール更新完了画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>プロフィール更新完了画面</title>

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
    min-width: 200px;
  }
}
</style>
</head>

<body>
  <div class="area">
    <h2>プロフィールを更新しました</h2>

    <a class="button" href="<%= request.getContextPath() %>/GeneralDashboard">
      ダッシュボードへ
    </a>
  </div>
</body>
</html>
