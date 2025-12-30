package model;

/**----------------------------------------------------------------------*
 *■■■AccountAddBLクラス■■■
 *概要：ビジネスロジック（アカウントの追加）
 *----------------------------------------------------------------------**/

public class AccountEditBL {

	/**----------------------------------------------------
	 * ■accountAddメソッド
	 * DBに接続し、アカウントを追加する
	 * 引数	 ；アカウント情報（AccountAddDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean accountEdit(AccountCRUDDto dto){

		boolean result = true;

		//DB接続
		AccountCRUDDao dao = new AccountCRUDDao();
		result = dao.updateAccount(dto);

		return result;
	}

}
