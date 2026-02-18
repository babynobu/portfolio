package model;

/**----------------------------------------------------------------------*
 *■■■ManagerSettingUpdateBLクラス■■■
 *概要：ビジネスロジック（プロフィールの更新）
 *----------------------------------------------------------------------**/

public class ManagerSettingUpdateBL {

	/**----------------------------------------------------
	 * ■managerSettingUpdateメソッド
	 * DBに接続し、アカウントを更新する
	 * 引数	 ；プロフィール情報（AccountDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean managerSettingUpdate(AccountDto dto){

		boolean result = true;

		//DB接続
		AccountDao dao = new AccountDao();
		result = dao.managerSettingUpdate(dto);

		return result;
	}

}
