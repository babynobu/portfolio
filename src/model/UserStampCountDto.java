package model;

/**---------------------------------------------
 * ■■■UserStampCountDtoクラス■■■
 * 概要：DTO
 --------------------------------------------**/

public class UserStampCountDto {

	/*-----------------------
	 * フィールド
	 -----------------------*/

	private int ranking ;
	private int userId ;
	private String userName ;
	private int stampCount ;

	/*-----------------------
	 * getter/setter
	 -----------------------*/

	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
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
	public int getStampCount() {
		return stampCount;
	}
	public void setStampCount(int stampCount) {
		this.stampCount = stampCount;
	}

}
