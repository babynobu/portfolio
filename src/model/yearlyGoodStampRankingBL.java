package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■yearlyGoodStampRankingBLクラス■■■
 *概要：ビジネスロジック（ユーザーデータの抽出）
 *----------------------------------------------------------------------**/

public class yearlyGoodStampRankingBL {

	/**----------------------------------------------------
	 * ■yearlyStampRankingInfoメソッド
	 * DBに接続し、引数に紐づくいいね情報を抽出
	 * 引数	 ；なし
	 * 戻り値：userId,いいね数,（List<UserStampCountDto>型）
	 -----------------------------------------------------*/

	public List<UserStampCountDto> yearlyStampRankingInfo(){

		//戻り値用の型を生成
		List<UserStampCountDto> list = new ArrayList();

		//DB接続
		UserStampCountDao dao = new UserStampCountDao();
		list = dao.yearlyGetStampRanking();

		return list;
	}

}
