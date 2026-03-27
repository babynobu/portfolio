<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>アカウント編集</title>

<style>
.container {
	max-width: 760px;
	margin: 0 auto;
	padding: 16px;
}

.form-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 12px;
}

.field label.field-label {
	font-weight: bold;
	display: block;
	margin-bottom: 6px;
}

.field input[type="text"], .field input[type="password"], .field input[type="email"],
	.field input[type="number"], .field input[type="file"], .field textarea
	{
	width: 100%;
	padding: 8px;
	box-sizing: border-box;
}

.field textarea {
	min-height: 90px;
}

.role-area {
	display: none;
	border: 1px solid #ccc;
	padding: 15px;
	margin-top: 15px;
}

.error {
	color: red;
	font-size: 0.9em;
	margin-top: 6px;
}

.profile-img {
	max-width: 200px;
	max-height: 200px;
	width: 100%;
	height: auto;
	display: block;
	border: 1px solid #ddd;
	border-radius: 8px;
}

@media ( max-width : 768px) {
	.form-grid {
		grid-template-columns: 1fr;
	}
}
</style>
</head>

<body>
	<div class="container">

		<h2>アカウント編集</h2>

		<form action="<%=request.getContextPath()%>/ExecuteAccountEdit"
			method="post" enctype="multipart/form-data">

			<input type="hidden" name="userId" value="${account.userId}">

			<!-- ===== 権限 ===== -->
			<div class="field">
				<label class="field-label">権限</label>

				<c:set var="roleValue"
					value="${param.role != null ? param.role : account.role}" />

				<label> <input type="radio" name="role" value="1"
					${roleValue == '1' ? 'checked' : ''}> 管理者
				</label> <label> <input type="radio" name="role" value="0"
					${roleValue == '0' ? 'checked' : ''}> 一般
				</label>

				<c:if test="${not empty errors.role}">
					<div class="error">${errors.role}</div>
				</c:if>
			</div>

			<!-- ===== 共通項目 ===== -->
			<h3>共通項目</h3>

			<div class="form-grid">

				<div class="field">
					<label class="field-label">ログインID</label> <input type="text"
						name="loginId"
						value="${param.loginId != null ? param.loginId : account.loginId}">
					<c:if test="${not empty errors.loginId}">
						<div class="error">${errors.loginId}</div>
					</c:if>
				</div>

				<div class="field">
					<label class="field-label">パスワード</label> <input type="password"
						name="password">
					<c:if test="${not empty errors.password}">
						<div class="error">${errors.password}</div>
					</c:if>
				</div>

				<div class="field">
					<label class="field-label">名前</label> <input type="text"
						name="name"
						value="${param.name != null ? param.name : account.name}">
					<c:if test="${not empty errors.name}">
						<div class="error">${errors.name}</div>
					</c:if>
				</div>

				<div class="field">
					<label class="field-label">メールアドレス</label> <input type="email"
						name="email"
						value="${param.email != null ? param.email : account.email}">
					<c:if test="${not empty errors.email}">
						<div class="error">${errors.email}</div>
					</c:if>
				</div>

				<div class="field">
					<label class="field-label">アカウントステータス</label>

					<c:set var="statusValue"
						value="${param.status != null ? param.status : account.status}" />

					<label> <input type="radio" name="status" value="1"
						${statusValue == 1 ? 'checked' : ''}> 有効
					</label> <label> <input type="radio" name="status" value="0"
						${statusValue == 0 ? 'checked' : ''}> 無効
					</label>

					<c:if test="${not empty errors.status}">
						<div class="error">${errors.status}</div>
					</c:if>
				</div>

			</div>

			<!-- ===== 管理者 ===== -->
			<div id="adminArea" class="role-area">
				<h3>管理者項目</h3>
				<p>※ 追加項目はありません</p>
			</div>

			<!-- ===== 一般 ===== -->
			<div id="generalArea" class="role-area">
				<h3>一般項目</h3>

				<div class="form-grid">

					<div class="field">
						<label class="field-label">ふりがな</label> <input type="text"
							name="kana"
							value="${param.kana != null ? param.kana : account.kana}">
						<c:if test="${not empty errors.kana}">
							<div class="error">${errors.kana}</div>
						</c:if>
					</div>

					<div class="field">
						<label class="field-label">性別</label>

						<c:set var="genderValue"
							value="${param.gender != null ? param.gender : account.gender}" />

						<label> <input type="radio" name="gender" value="1"
							${genderValue == 1 ? 'checked' : ''}> 男性
						</label> <label> <input type="radio" name="gender" value="2"
							${genderValue == 2 ? 'checked' : ''}> 女性
						</label> <label> <input type="radio" name="gender" value="3"
							${genderValue == 3 ? 'checked' : ''}> その他
						</label>

						<c:if test="${not empty errors.gender}">
							<div class="error">${errors.gender}</div>
						</c:if>
					</div>

					<div class="field">
						<label class="field-label">年齢</label> <input type="number"
							name="age" min="0" max="999"
							value="${param.age != null ? param.age : account.age}">
						<c:if test="${not empty errors.age}">
							<div class="error">${errors.age}</div>
						</c:if>
					</div>

					<div class="field" style="grid-column: 1/-1;">
						<label class="field-label">自己紹介</label>
						<textarea name="introduction">${param.introduction != null ? param.introduction : account.introduction}</textarea>
						<c:if test="${not empty errors.introduction}">
							<div class="error">${errors.introduction}</div>
						</c:if>
					</div>

					<c:if test="${not empty account.profileImagePath}">
						<div class="field" style="grid-column: 1/-1;">
							<label class="field-label">現在のプロフィール画像</label> <img
								class="profile-img"
								src="<c:url value='${account.profileImagePath}' />">
						</div>
					</c:if>

					<div class="field" style="grid-column: 1/-1;">
						<label class="field-label">プロフィール画像</label> <input type="file"
							name="profileImage">
						<c:if test="${not empty errors.profileImage}">
							<div class="error">${errors.profileImage}</div>
						</c:if>
					</div>

				</div>
			</div>

			<p>
				<input type="submit" value="更新">
			</p>

		</form>

		<c:if test="${not empty errorMessage}">
			<div style="color: red;">${errorMessage}</div>
		</c:if>

		<form action="<%=request.getContextPath()%>/AccountList" method="get">
			<input type="hidden" name="page" value="${param.page}">
			<button type="submit">戻る</button>
		</form>

		<script>
const roleRadios  = document.querySelectorAll('input[name="role"]');
const adminArea   = document.getElementById('adminArea');
const generalArea = document.getElementById('generalArea');

function setDisabledInArea(areaEl, disabled) {
  const targets = areaEl.querySelectorAll('input, textarea, select');
  targets.forEach(el => {
    el.disabled = disabled;
  });
}

function switchRole(role) {
  const isAdmin = role === '1';

  adminArea.style.display   = isAdmin ? 'block' : 'none';
  generalArea.style.display = isAdmin ? 'none' : 'block';

  setDisabledInArea(generalArea, isAdmin);
}

window.addEventListener('DOMContentLoaded', () => {
  const checked = document.querySelector('input[name="role"]:checked');
  if (checked) {
    switchRole(checked.value);
  }
});

roleRadios.forEach(radio => {
  radio.addEventListener('change', () => {
    switchRole(radio.value);
  });
});
</script>

	</div>
</body>
</html>