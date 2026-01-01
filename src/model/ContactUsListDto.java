package model;

import java.time.LocalDate;

/**---------------------------------------------
 * ■■■ContactUsListDtoクラス■■■
 * 概要：DTO
 --------------------------------------------**/

public class ContactUsListDto {

	/*-----------------------
	 * フィールド
	 -----------------------*/

	private int contactUsId ;
	private String categoryName  ;
	private String body ;
	private String email;
	private int status;
	private LocalDate createdAt ;
	private LocalDate updatedAt ;

	/*-----------------------
	 * getter/setter
	 -----------------------*/

	public int getContactUsId() {
		return contactUsId;
	}
	public void setContactUsId(int contactUsId) {
		this.contactUsId = contactUsId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDate getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}
	//表示用メソッド
	public String getStatusLabel() {
		String st ;
		if (status == 1) {
			st = "未対応";
		}else if(status == 2){
			st = "対応中";
		}else if(status == 3) {
			st = "対応済";
		}else {
			st = "※不正なステータスです。";
		}
	    return st;
	}

}
