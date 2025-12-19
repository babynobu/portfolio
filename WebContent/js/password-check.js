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