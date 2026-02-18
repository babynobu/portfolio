<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>プロフィール編集</title>

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
  max-width: 720px;
}

.error {
  color: red;
  font-size: 0.9em;
  margin-top: 6px;
}

input[type="text"],
input[type="number"],
input[type="file"],
textarea {
  width: 100%;
  max-width: 720px;
  padding: 8px;
  font-size: 16px;
  box-sizing: border-box;
}

textarea {
  min-height: 120px;
  resize: vertical;
}

.gender-area label {
  margin-right: 12px;
}

img.profile {
  max-width: 200px;
  max-height: 200px;
  width: 100%;
  height: auto;
}

input[type="submit"], button {
  width: 100%;
  max-width: 720px;
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
<h2>プロフィール編集</h2>

<div class="form-area">
  <form action="<%=request.getContextPath()%>/ExecuteProfileEdit"
        method="post"
        enctype="multipart/form-data">
    <input type="hidden" name="userId" value="${profileEdit.userId}">

    <p>
      名前<br>
      <input type="text" name="name"
             value="${empty param.name ? profileEdit.name : param.name}">
    </p>
    <c:if test="${not empty errors.name}">
      <div class="error">${errors.name}</div>
    </c:if>

    <p>
      ふりがな<br>
      <input type="text" name="kana"
             value="${empty param.kana ? profileEdit.kana : param.kana}">
    </p>
    <c:if test="${not empty errors.kana}">
      <div class="error">${errors.kana}</div>
    </c:if>

    <p>
      性別<br>
      <span class="gender-area">
        <label>
          <input type="radio" name="gender" value="1"
            ${ (empty param.gender ? profileEdit.gender : param.gender) == 1 ? 'checked' : '' }>
          男性
        </label>
        <label>
          <input type="radio" name="gender" value="2"
            ${ (empty param.gender ? profileEdit.gender : param.gender) == 2 ? 'checked' : '' }>
          女性
        </label>
        <label>
          <input type="radio" name="gender" value="3"
            ${ (empty param.gender ? profileEdit.gender : param.gender) == 3 ? 'checked' : '' }>
          その他
        </label>
      </span>
    </p>
    <c:if test="${not empty errors.gender}">
      <div class="error">${errors.gender}</div>
    </c:if>

    <!-- ★ 生年月日 → 年齢 -->
    <p>
      年齢<br>
      <input type="number" name="age" min="0" max="999"
             value="${empty param.age ? profileEdit.age : param.age}">
    </p>
    <c:if test="${not empty errors.age}">
      <div class="error">${errors.age}</div>
    </c:if>

    <p>
      自己紹介<br>
      <textarea name="introduction">${empty param.introduction ? profileEdit.introduction : param.introduction}</textarea>
    </p>
    <c:if test="${not empty errors.introduction}">
      <div class="error">${errors.introduction}</div>
    </c:if>

    <c:if test="${not empty profileEdit.profileImagePath}">
      <p>
        現在のプロフィール画像<br>
        <img class="profile"
             src="<%= request.getContextPath() %>/${profileEdit.profileImagePath}"
             alt="プロフィール画像">
      </p>
    </c:if>

    <p>
      プロフィール画像<br>
      <input type="file" name="profileImage">
    </p>
    <c:if test="${not empty errors.profileImage}">
      <div class="error">${errors.profileImage}</div>
    </c:if>

    <p>
      <input type="submit" value="更新">
    </p>
  </form>

  <c:if test="${not empty errorMessage}">
    <div class="error">${errorMessage}</div>
  </c:if>

  <form action="<%=request.getContextPath()%>/GeneralDashboard" method="get">
    <p>
      <button type="submit">戻る</button>
    </p>
  </form>
</div>

</body>
</html>
