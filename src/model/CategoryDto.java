package model;

import java.time.LocalDate;

/**---------------------------------------------
 * ■■■CategoryListDtoクラス■■■
 * 概要：DTO
 --------------------------------------------**/

public class CategoryDto {

	/*-----------------------
	 * フィールド
	 -----------------------*/

	private int categoryId ;
	private String categoryName ;
	private LocalDate createdAt ;
	private LocalDate updatedAt ;
	private LocalDate deletedAt ;

	/*-----------------------
	 * getter/setter
	 -----------------------*/

	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	public LocalDate getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(LocalDate deletedAt) {
		this.deletedAt = deletedAt;
	}

}
