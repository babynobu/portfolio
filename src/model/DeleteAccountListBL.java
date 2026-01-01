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
	 * 戻り値：（List<AccountListDto>型）
	 -----------------------------------------------------*/

	public List<AccountListDto> selectDeleteAccountList(){

		//戻り値用の型を生成
		List<AccountListDto> list = new ArrayList<>();

		//DB接続
		AccountListDao dao = new AccountListDao();
		list = dao.selectDeleteAccountList();

		return list;
	}

}
