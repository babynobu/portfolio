package model;

/**---------------------------------------------
 * ■■■UserInfoDtoクラス■■■
 * 概要：DTO
 --------------------------------------------**/

public class UserInfoDto {

	/*-----------------------
	 * フィールド
	 -----------------------*/

	private int userId ;
	private String loginId ;
	private String userName ;
	private String userPass ;
	private int role ;

	/*-----------------------
	 * getter/setter
	 -----------------------*/

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
}
