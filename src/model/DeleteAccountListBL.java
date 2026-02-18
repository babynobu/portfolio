package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■DeleteAccountListBLクラス■■■
 *概要：ビジネスロジック（削除アカウントリストの抽出）
 *----------------------------------------------------------------------**/

public class DeleteAccountListBL {

	/**----------------------------------------------------
	 * ■selectDeleteAccountListメソッド
	 * DBに接続し、アカウントリストを抽出
	 * 引数	 ；なし
	 * 戻り値：（List<AccountDto>型）
	 -----------------------------------------------------*/

	public List<AccountDto> selectDeleteAccountList(){

		//戻り値用の型を生成
		List<AccountDto> list = new ArrayList<>();

		//DB接続
		AccountDao dao = new AccountDao();
		list = dao.selectDeleteAccountList();

		return list;
	}

}
