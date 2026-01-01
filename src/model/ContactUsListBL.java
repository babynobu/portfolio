package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■ContactUsListBLクラス■■■
 *概要：ビジネスロジック（お問い合わせ一覧の抽出）
 *----------------------------------------------------------------------**/

public class ContactUsListBL {

	/**----------------------------------------------------
	 * ■selectContactUsListメソッド
	 * DBに接続し、アカウントリストを抽出
	 * 引数	 ；なし
	 * 戻り値：（List<ContactUsListDto>型）
	 -----------------------------------------------------*/

	public List<ContactUsListDto> selectContactUsList(){

		//戻り値用の型を生成
		List<ContactUsListDto> list = new ArrayList<>();

		//DB接続
		ContactUsListDao dao = new ContactUsListDao();
		list = dao.selectContactUsList();

		return list;
	}

}
