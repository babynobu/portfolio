package model;

import java.sql.Timestamp;

/**---------------------------------------------
 * ■■■StampInfoDtoクラス■■■
 * 概要：DTO
 --------------------------------------------**/

public class StampInfoDto {

	/*-----------------------
	 * フィールド
	 -----------------------*/

	private int stampNumber ;
	private Timestamp stampAt ;

	/*-----------------------
	 * getter/setter
	 -----------------------*/

	public int getStampNumber() {
		return stampNumber;
	}
	public void setStampNumber(int stampNumber) {
		this.stampNumber = stampNumber;
	}
	public Timestamp getStampAt() {
		return stampAt;
	}
	public void setStampAt(Timestamp stampAt) {
		this.stampAt = stampAt;
	}



}
