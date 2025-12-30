package model;

import java.time.LocalDate;

/**---------------------------------------------
 * ■■■AccountCRUDDaoクラス■■■
 * 概要：DTO
 --------------------------------------------**/

public class AccountCRUDDto {


	/*-----------------------
	 * フィールド
	 -----------------------*/

    // ===== 共通 =====
    private String role;
    private String userId;
    private String loginId;
    private String password;
    private String name;
    private String kana;
    private String email;
    private String status;			// 1 / 0

    // ===== 一般ユーザー用 =====
    private String gender;			// 1 / 2 / 3
    private LocalDate birthday;
    private String introduction;
    private String profileImagePath;


	/*-----------------------
	 * getter/setter
	 -----------------------*/

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKana() {
		return kana;
	}
	public void setKana(String kana) {
		this.kana = kana;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getProfileImagePath() {
		return profileImagePath;
	}
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}

}
