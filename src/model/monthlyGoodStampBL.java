package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■monthlyGoodStampBLクラス■■■
 *概要：ビジネスロジック（ユーザーデータの抽出）
 *----------------------------------------------------------------------**/

public class monthlyGoodStampBL {

	/**----------------------------------------------------
	 * ■monthlyStampInfoメソッド
	 * DBに接続し、引数に紐づくいいね情報を抽出
	 * 引数	 ；ユーザーID（int型）
	 * 戻り値：いいねNo.、タイム情報（List<StampInfoDto>型）
	 -----------------------------------------------------*/

	public List<StampInfoDto> monthlyStampInfo(int id){

		//戻り値用の型を生成
		List<StampInfoDto> list = new ArrayList();

		//DB接続
		StampInfoDao dao = new StampInfoDao();
		list = dao.monthlyGetStampInfo(id);

		return list;
	}

}
