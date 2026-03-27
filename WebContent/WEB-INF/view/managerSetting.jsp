<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>設定</title>

<style>
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

input[type="text"],
input[type="password"],
input[type="email"] {
  width: 100%;
  max-width: 520px;
  padding: 8px;
  font-size: 16px;
  box-sizing: border-box;
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
    min-width: 140px;
  }
}
</style>
</head>

<body>
<h2>設定</h2>

<div class="form-area">
  <form action="<%=request.getContextPath()%>/ExecuteManagerSetting"
        method="post"
        enctype="multipart/form-data">

    <input type="hidden" name="userId" value="${managerSetting.userId}">

    <p>
      ログインID<br>
      <input type="text" name="loginId"
             value="${param.loginId != null ? param.loginId : managerSetting.loginId}">
    </p>
    <c:if test="${not empty errors.loginId}">
      <div class="error">${errors.loginId}</div>
    </c:if>

    <p>
      パスワード<br>
      <input type="password" name="password">
    </p>
    <c:if test="${not empty errors.password}">
      <div class="error">${errors.password}</div>
    </c:if>

    <p>
      名前<br>
      <input type="text" name="name"
             value="${param.name != null ? param.name : managerSetting.name}">
    </p>
    <c:if test="${not empty errors.name}">
      <div class="error">${errors.name}</div>
    </c:if>

    <p>
      メールアドレス<br>
      <input type="email" name="email"
             value="${param.email != null ? param.email : managerSetting.email}">
    </p>
    <c:if test="${not empty errors.email}">
      <div class="error">${errors.email}</div>
    </c:if>

    <p>
      <input type="submit" value="更新">
    </p>
  </form>

  <c:if test="${not empty errorMessage}">
    <div class="error">
      ${errorMessage}
    </div>
  </c:if>

  <form action="<%=request.getContextPath()%>/ManagerDashboard" method="get">
    <p>
      <button type="submit">戻る</button>
    </p>
  </form>
</div>

</body>
</html>