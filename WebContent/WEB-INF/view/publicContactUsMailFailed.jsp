<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ受付（メール送信失敗）</title>

<style>
/* 画面全体 */
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP","Hiragino Kaku Gothic ProN","Meiryo",sans-serif;
  box-sizing: border-box;
}
*, *::before, *::after { box-sizing: inherit; }

/* 中央寄せ + 最大幅（PCで横に伸びすぎない） */
.box {
  max-width: 720px;
  margin: 0 auto;
}

/* 見やすさ */
.warn {
  color: #b00020;
  font-weight: bold;
}

/* 長いエラーメッセージでも崩れない（URL/長文対策） */
.mailError {
  white-space: pre-wrap;
  word-break: break-word;
  overflow-wrap: anywhere;
}

/* ボタン：スマホは押しやすく100% */
button {
  padding: 10px 14px;
  font-size: 16px;
  width: 100%;
}

/* PC幅ではボタンを必要以上に伸ばさない */
@media (min-width: 481px) {
  button {
    width: auto;
    min-width: 220px;
  }
}
</style>
</head>

<body>
  <div class="box">
    <h2>お問い合わせを受け付けました</h2>

    <p class="warn">確認メールの送信に失敗しました。</p>

    <p class="mailError">
      ${mailErrorMessage}
    </p>

    <p>
      ※お問い合わせ内容はシステムに登録されています。管理者が順次対応します。
    </p>

    <form action="<%=request.getContextPath()%>/PublicHome" method="get">
      <button type="submit">ホームへ戻る</button>
    </form>
  </div>
</body>
</html>
