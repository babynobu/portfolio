package model;

/**----------------------------------------------------------------------*
 *■■■AccountAddBLクラス■■■
 *概要：ビジネスロジック（アカウントの追加）
 *----------------------------------------------------------------------**/

public class AccountAddBL {

	/**----------------------------------------------------
	 * ■accountAddメソッド
	 * DBに接続し、アカウントを追加する
	 * 引数	 ；アカウント情報（AccountAddDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean accountAdd(AccountAddDto dto){

		boolean result = true;

		//DB接続
		AccountAddDao dao = new AccountAddDao();
		result = dao.insertAccount(dto);

		return result;
	}

}
