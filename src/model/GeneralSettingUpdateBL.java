package model;

/**----------------------------------------------------------------------*
 *■■■GeneralSettingUpdateBLクラス■■■
 *概要：ビジネスロジック（プロフィールの更新）
 *----------------------------------------------------------------------**/

public class GeneralSettingUpdateBL {

	/**----------------------------------------------------
	 * ■generalSettingUpdateメソッド
	 * DBに接続し、アカウントを更新する
	 * 引数	 ；プロフィール情報（AccountDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean generalSettingUpdate(AccountDto dto){

		boolean result = true;

		//DB接続
		AccountDao dao = new AccountDao();
		result = dao.generalSettingUpdate(dto);

		return result;
	}

}
