package model;

/**----------------------------------------------------------------------*
 *■■■BusinessLogicクラス■■■
 *概要：ビジネスロジック（ユーザーデータの抽出）
 *----------------------------------------------------------------------**/

public class BusinessLogic {

	/**----------------------------------------------------
	 * ■userInfoExtractedクラス
	 * DBに接続し、引数に紐づくユーザー情報を抽出
	 * 引数①；ユーザーID
	 * 引数②：パスワード
	 * 戻り値：ユーザー情報（UserInfoDto型）
	 -----------------------------------------------------*/

	public UserInfoDto userInfoExtracted(String id, String pass) {

		//ユーザーデータ（UserInfoDto型）（戻り値用）
		UserInfoDto dto = new UserInfoDto();

		//DBに接続
		//Daoをインスタンス化＆メソッドを起動
		UserInfoDao dao = new UserInfoDao();
		return dao.doSelect(id,pass);
	}

}
