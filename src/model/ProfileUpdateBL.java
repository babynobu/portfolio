package model;

/**----------------------------------------------------------------------*
 *■■■ProfileUpdateBLクラス■■■
 *概要：ビジネスロジック（アカウントの更新）
 *----------------------------------------------------------------------**/

public class ProfileUpdateBL {

	/**----------------------------------------------------
	 * ■profileUpdateメソッド
	 * DBに接続し、プロフィールを更新する
	 * 引数	 ；プロフィール情報（AccountDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean profileUpdate(AccountDto dto){

		boolean result = true;

		//DB接続
		AccountDao dao = new AccountDao();
		result = dao.updateProfile(dto);

		return result;
	}

}
