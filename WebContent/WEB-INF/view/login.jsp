<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- login.jsp 概要：ログイン画面 --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン画面</title>

<style>
body {
  margin: 0;
  padding: 16px;
  font-family: "Noto Sans JP", "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
}

h2 { margin-top: 0; }

.form-area { max-width: 520px; }

input[type="text"],
input[type="password"] {
  width: 100%;
  max-width: 520px;
  padding: 8px;
  font-size: 16px;
}

input[type="submit"] {
  width: 100%;
  max-width: 520px;
  padding: 10px;
  font-size: 16px;
  margin-top: 8px;
}

#errorMsg {
  color: red;
  font-size: 0.9em;
  margin-top: 6px;
}

@media (min-width: 481px) {
  input[type="submit"] {
    width: auto;
    min-width: 160px;
  }
}
</style>
</head>

<body>
  <h2>ログイン</h2>

  <div class="form-area">
    <form action="<%= request.getContextPath() %>/ExecuteLogin"
          method="post"
          onsubmit="return checkLogin();">

      <p>
        ログインID<br>
        <input type="text"
               name="LOGIN_ID"
               maxlength="255"
               id="ID_LOGIN_ID"
               value="<c:out value='${LOGIN_ID_VALUE}'/>">
      </p>

      <p>
        パスワード<br>
        <input type="password"
               name="PASSWORD"
               id="ID_PASSWORD">
      </p>

      <div id="errorMsg">
        <c:if test="${not empty errors}">
          <c:if test="${not empty errors.global}">
            <c:out value="${errors.global}"/><br>
          </c:if>
          <c:if test="${not empty errors.loginId}">
            <c:out value="${errors.loginId}"/><br>
          </c:if>
          <c:if test="${not empty errors.password}">
            <c:out value="${errors.password}"/><br>
          </c:if>
        </c:if>
      </div>

      <p>
        <input type="submit" value="送信">
      </p>
    </form>
  </div>

  <script type="text/javascript">
    function checkLogin() {

      var errorMsg = document.getElementById("errorMsg");

      // 前回のエラーを最初に全部消す
      errorMsg.innerHTML = "";

      var result1 = checkName();   // ユーザーIDチェック
      var result2 = checkPass();   // パスワードチェック

      // 両方OKなら true（送信される）
      return result1 && result2;
    }

    function checkName(){

      var loginId = document.getElementById("ID_LOGIN_ID").value;
      var errorMsg = document.getElementById("errorMsg");

      if (loginId === ""){
        errorMsg.innerHTML +=
          "ユーザーIDが未入力です。<br>";
        return false;

      } else if (loginId.length > 255){
        errorMsg.innerHTML +=
          "ユーザーIDの文字数制限を超えています。<br>";
        return false;

      } else {
        return true;
      }
    }

    function checkPass(){

      var password = document.getElementById("ID_PASSWORD").value;
      var errorMsg = document.getElementById("errorMsg");

      if (password === ""){
        errorMsg.innerHTML +=
          "パスワードが未入力です。<br>";
        return false;

      } else if(/^[\-_0-9a-zA-Z]{8,32}$/.test(password) ){
        return true;

      } else {
        errorMsg.innerHTML +=
          "パスワードは8～32文字の半角英数字と_-で入力してください。<br>";
        return false;
      }
    }
  </script>

</body>
</html>
