function checkName(){

	var loginId = document.getElementById("ID_LOGIN_ID").value;

	var errorMsg = document.getElementById("errorMsg");

	if (loginId === ""){
		errorMsg.innerHTML +=
			"ユーザーIDが未入力です。<br>";
		return false;
	} else if(loginId.length >= 255){
		errorMsg.innerHTML +=
			"ユーザーIDの文字数制限を超えています。<br>";
		return false;
	} else {
		return true;
	}


}