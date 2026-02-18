<%@ page contentType="text/html; charset=UTF-8"%>

<%--概要：アカウント追加完了画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>アカウント追加完了画面</title>

  <style>
    .container {
      max-width: 760px;
      margin: 0 auto;
      padding: 16px;
    }

    .btn-link {
      display: inline-block;
      margin-top: 12px;
      padding: 10px 12px;
      border: 1px solid #aaa;
      border-radius: 10px;
      text-decoration: none;
      color: #000;
    }
  </style>
</head>

<body>
  <div class="container">
    <h2>アカウントを追加しました</h2>
    <a class="btn-link" href="<%= request.getContextPath() %>/AccountAdd">戻る</a>
  </div>
</
