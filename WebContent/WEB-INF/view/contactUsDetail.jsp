<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
<title>お問い合わせ詳細</title>
</head>

<body>

<h2>お問い合わせ詳細</h2>

    <p>お問い合わせID<br>
    ${contactUs.contactUsId}
    </p>
    <br>
    <p>カテゴリー<br>
    ${contactUs.categoryName}
    </p>
    <br>
    <p>本文</p>
    <pre>${contactUs.body}</pre>
    <br>
    <p>メールアドレス<br>
    ${contactUs.email}
    </p>
    <br>
    <p>ステータス</p>
    <form action="<%=request.getContextPath()%>/ContactUs" method="post">

    <input type="hidden" name="contactUsId" value="${contactUs.contactUsId}">

    <select name="status">
        <option value="1" ${contactUs.status == 1 ? "selected" : ""}>未対応</option>
        <option value="2" ${contactUs.status == 2 ? "selected" : ""}>対応中</option>
        <option value="3" ${contactUs.status == 3 ? "selected" : ""}>対応済</option>
    </select>

    <button type="submit">ステータス更新</button>

	</form>

<form action="<%=request.getContextPath()%>/ContactUs">
    <button type="submit">戻る</button>
</form>
</body>
</html>
