function checkform(){

	var userId = document.getElementById("ID_USER_ID").value;
	var password = document.getElementById("ID_PASSWORD").value;

	document.getElementById("errorMsg").textContent = "" ;

	if (userId === "" || password === "" ){
		document.getElementById("errorMsg").textContent =
			"未入力の項目があります。";
		return false;
	}

	return true;
}