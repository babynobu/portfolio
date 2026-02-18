<%@ page contentType="text/html; charset=UTF-8"%>

<%-- 概要：一般ダッシュボード画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ダッシュボード</title>

<style>
/* Aパターン：最低限のレスポンシブ対応 */
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

.count {
  display: block;
  margin-bottom: 6px;
}

a.link {
  display: inline-block;
  margin-top: 10px;
}

button {
  padding: 10px;
  font-size: 16px;
  width: 100%;
  max-width: 520px;
  margin-top: 12px;
}

@media (min-width: 481px) {
  button {
    width: auto;
    min-width: 160px;
  }
}
</style>
</head>

<body>
  <div class="area">
    <h2>ようこそ</h2>

    <a class="count">年間いいね数：${YEARLY_COUNT}</a>
    <a class="count">月間いいね数：${MONTHLY_COUNT}</a>

    <br>

    <a class="link" href="<%= request.getContextPath() %>/GeneralSetting">設定</a>
    <br><br>

    <a class="link" href="<%= request.getContextPath() %>/ProfileEdit">プロフィール編集</a>
    <br><br>

    <form action="<%= request.getContextPath() %>/LogOut" method="post">
      <button type="submit">ログアウト</button>
    </form>
  </div>
</body>
</html>
