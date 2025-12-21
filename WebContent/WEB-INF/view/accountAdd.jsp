<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>アカウント追加</title>

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

<h2>アカウント追加</h2>

<form action="<%=request.getContextPath()%>/ExecuteAccountAdd"
      method="post">

    <!-- ===== 権限選択 ===== -->
    <label>
        <input type="radio" name="role" value="admin"
            ${param.role == 'admin' ? 'checked' : ''}>
        管理者
    </label>
    <label>
        <input type="radio" name="role" value="general"
            ${param.role == 'general' ? 'checked' : ''}>
        一般
    </label>

    <c:if test="${not empty errors.role}">
        <div class="error">${errors.role}</div>
    </c:if>

    <!-- ===== 共通項目 ===== -->
    <h3>共通項目</h3>

    <p>
        ログインID<br>
        <input type="text" name="loginId" value="${param.loginId}">
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
        <input type="text" name="name" value="${param.name}">
    </p>
    <c:if test="${not empty errors.name}">
        <div class="error">${errors.name}</div>
    </c:if>

    <p>
        ふりがな<br>
        <input type="text" name="kana" value="${param.kana}">
    </p>
    <c:if test="${not empty errors.kana}">
        <div class="error">${errors.kana}</div>
    </c:if>

    <p>
        メールアドレス<br>
        <input type="email" name="email" value="${param.email}">
    </p>
    <c:if test="${not empty errors.email}">
        <div class="error">${errors.email}</div>
    </c:if>

    <!-- ===== 管理者用エリア ===== -->
    <div id="adminArea" class="role-area">
        <h3>管理者項目</h3>
        <p>※ 追加項目はありません</p>
    </div>

    <!-- ===== 一般用エリア ===== -->
    <div id="generalArea" class="role-area">
        <h3>一般項目</h3>

        <!-- 性別（先） -->
        <p>
            性別<br>
            <label>
                <input type="radio" name="gender" value="1"
                    ${param.gender == '1' ? 'checked' : ''}>
                男性
            </label>
            <label>
                <input type="radio" name="gender" value="2"
                    ${param.gender == '2' ? 'checked' : ''}>
                女性
            </label>
            <label>
                <input type="radio" name="gender" value="3"
                    ${param.gender == '3' ? 'checked' : ''}>
                その他
            </label>
        </p>
        <c:if test="${not empty errors.gender}">
            <div class="error">${errors.gender}</div>
        </c:if>

        <!-- 生年月日（後） -->
        <p>
            生年月日<br>
            <input type="date" name="birthday">
        </p>
        <c:if test="${not empty errors.birthday}">
            <div class="error">${errors.birthday}</div>
        </c:if>

        <p>
            自己紹介<br>
            <textarea name="introduction">${param.introduction}</textarea>
        </p>
        <c:if test="${not empty errors.introduction}">
            <div class="error">${errors.introduction}</div>
        </c:if>
    </div>

    <p>
        <input type="submit" value="登録">
    </p>
</form>

<form action="<%=request.getContextPath()%>/ManagerDashboard">
    <button type="submit">戻る</button>
</form>

<script>
const roleRadios  = document.querySelectorAll('input[name="role"]');
const adminArea   = document.getElementById('adminArea');
const generalArea = document.getElementById('generalArea');

function switchRole(role) {
    adminArea.style.display   = role === 'admin'   ? 'block' : 'none';
    generalArea.style.display = role === 'general' ? 'block' : 'none';
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
