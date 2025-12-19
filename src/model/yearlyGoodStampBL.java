package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■yearlyGoodStampBLクラス■■■
 *概要：ビジネスロジック（ユーザーデータの抽出）
 *----------------------------------------------------------------------**/

public class yearlyGoodStampBL {

	/**----------------------------------------------------
	 * ■yearlyStampInfoメソッド
	 * DBに接続し、引数に紐づくいいね情報を抽出
	 * 引数	 ；ユーザーID（int型）
	 * 戻り値：タイム情報（List<StampInfoDto>型）
	 -----------------------------------------------------*/

	public List<StampInfoDto> yearlyStampInfo(int id){

		//戻り値用の型を生成
		List<StampInfoDto> list = new ArrayList();

		//DB接続
		StampInfoDao dao = new StampInfoDao();
		list = dao.yearlyGetStampInfo(id);

		return list;
	}

}
