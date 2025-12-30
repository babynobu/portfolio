<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>アカウント編集</title>

<style>
.role-area {
    display: none;
    border: 1px solid #ccc;
    padding: 15px;
    margin-top: 15px;
}
.error {
    color: red;
    font-size: 0.9em;
}
</style>
</head>

<body>

<h2>アカウント編集</h2>

<form action="<%=request.getContextPath()%>/ExecuteAccountEdit"
      method="post"
      enctype="multipart/form-data">
      <input type="hidden" name="userId" value="${account.userId}">

    <!-- ===== 権限選択 ===== -->
    <label>
        <input type="radio" name="role" value="1"
            ${(empty param.role ? account.role : param.role) == '1' ? 'checked' : ''}>
        管理者
    </label>
    <label>
        <input type="radio" name="role" value="0"
            ${(empty param.role ? account.role : param.role) == '0' ? 'checked' : ''}>
        一般
    </label>

    <c:if test="${not empty errors.role}">
        <div class="error">${errors.role}</div>
    </c:if>

    <!-- ===== 共通項目 ===== -->
    <h3>共通項目</h3>

    <p>
        名前<br>
        <input type="text" name="name" value="${empty param.name ? account.name : param.name}">
    </p>
    <c:if test="${not empty errors.name}">
        <div class="error">${errors.name}</div>
    </c:if>

    <p>
        ふりがな<br>
        <input type="text" name="kana" value="${empty param.kana ? account.kana : param.kana}">
    </p>
    <c:if test="${not empty errors.kana}">
        <div class="error">${errors.kana}</div>
    </c:if>

    <p>
        メールアドレス<br>
        <input type="email" name="email" value="${empty param.email ? account.email : param.email}">
    </p>
    <c:if test="${not empty errors.email}">
        <div class="error">${errors.email}</div>
    </c:if>

    <p>
        アカウントステータス<br>
        <label>
            <input type="radio" name="status" value="1"
                ${ (empty param.status ? account.status : param.status) == 1 ? 'checked' : '' }>
            有効
        </label>
        <label>
            <input type="radio" name="status" value="0"
                ${ (empty param.status ? account.status : param.status) == 0 ? 'checked' : '' }>
            無効
        </label>
    </p>
    <c:if test="${not empty errors.gender}">
        <div class="error">${errors.gender}</div>
    </c:if>


    <!-- ===== 管理者用エリア ===== -->
    <div id="adminArea" class="role-area">
        <h3>管理者項目</h3>
        <p>※ 追加項目はありません</p>
    </div>

    <!-- ===== 一般用エリア ===== -->
    <div id="generalArea" class="role-area">
        <h3>一般項目</h3>

        <!-- 性別 -->
        <p>
            性別<br>
            <label>
                <input type="radio" name="gender" value="1"
                    ${ (empty param.gender ? account.gender : param.gender) == 1 ? 'checked' : '' }>
                男性
            </label>
            <label>
                <input type="radio" name="gender" value="2"
                    ${ (empty param.gender ? account.gender : param.gender) == 2 ? 'checked' : '' }>
                女性
            </label>
            <label>
                <input type="radio" name="gender" value="3"
                    ${ (empty param.gender ? account.gender : param.gender) == 3 ? 'checked' : '' }>
                その他
            </label>
        </p>
        <c:if test="${not empty errors.gender}">
            <div class="error">${errors.gender}</div>
        </c:if>

        <!-- 生年月日 -->
        <p>
            生年月日<br>
            <input type="date" name="birthday" value="${empty param.birthday ? account.birthday : param.birthday}">
        </p>
        <c:if test="${not empty errors.birthday}">
            <div class="error">${errors.birthday}</div>
        </c:if>

		<!-- 自己紹介 -->
        <p>
            自己紹介<br>
            <textarea name="introduction">${empty param.introduction ? account.introduction : param.introduction}</textarea>
        </p>
        <c:if test="${not empty errors.introduction}">
            <div class="error">${errors.introduction}</div>
        </c:if>

        <!-- プロフィール画像 -->
        <p>
        	プロフィール画像<br>
        	<input type = "file" name ="profileImage">
        </p>
        <c:if test="not empty errors.profileImage">
        	<div class="error">${error.profileImage}</div>
        </c:if>
    </div>

    <p>
        <input type="submit" value="更新">
    </p>
</form>

<form action="<%=request.getContextPath()%>/AccountList">
    <button type="submit">戻る</button>
</form>

<script>
const roleRadios  = document.querySelectorAll('input[name="role"]');
const adminArea   = document.getElementById('adminArea');
const generalArea = document.getElementById('generalArea');

function switchRole(role) {
    adminArea.style.display   = role === '1'   ? 'block' : 'none';
    generalArea.style.display = role === '0' ? 'block' : 'none';
}

// 初期表示
window.addEventListener('DOMContentLoaded', () => {
    const checked = document.querySelector('input[name="role"]:checked');
    if (checked) {
        switchRole(checked.value);
    }
});

// 切り替え
roleRadios.forEach(radio => {
    radio.addEventListener('change', () => {
        switchRole(radio.value);
    });
});
</script>


</body>
</html>
