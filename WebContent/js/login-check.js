function checkLogin() {

	//alert("checkLoginが呼ばれました");

	var errorMsg = document.getElementById("errorMsg");

	//前回のエラーを最初に全部消す
	errorMsg.innerHTML = "";

	var result1 = checkName();   // ユーザーIDチェック
	var result2 = checkPass();   // パスワードチェック
	// 両方OKなら true（送信される）
	return result1 && result2;
}
