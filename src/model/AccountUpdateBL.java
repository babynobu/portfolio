package model;

/**----------------------------------------------------------------------*
 *■■■AccountUpdateBLクラス■■■
 *概要：ビジネスロジック（アカウントの更新）
 *----------------------------------------------------------------------**/

public class AccountUpdateBL {

	/**----------------------------------------------------
	 * ■accountUpdateメソッド
	 * DBに接続し、アカウントを更新する
	 * 引数	 ；アカウント情報（AccountDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean accountUpdate(AccountDto dto){

		boolean result = true;

		//DB接続
		AccountDao dao = new AccountDao();
		result = dao.updateAccount(dto);

		return result;
	}

}
