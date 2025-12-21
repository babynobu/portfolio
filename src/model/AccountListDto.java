package model;

/**---------------------------------------------
 * ■■■AccountListDtoクラス■■■
 * 概要：DTO
 --------------------------------------------**/

public class AccountListDto {

	/*-----------------------
	 * フィールド
	 -----------------------*/

	private int userId ;
	private String userName  ;
	private String userNameKana ;
	private String email ;
	private int status ;

	/*-----------------------
	 * getter/setter
	 -----------------------*/

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserNameKana() {
		return userNameKana;
	}
	public void setUserNameKana(String userNameKana) {
		this.userNameKana = userNameKana;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
