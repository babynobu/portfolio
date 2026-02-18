package model;

/**----------------------------------------------------------------------*
 *■■■CategoryAddBLクラス■■■
 *概要：ビジネスロジック（アカウントの追加）
 *----------------------------------------------------------------------**/

public class CategoryAddBL {

	/**----------------------------------------------------
	 * ■categoryAddメソッド
	 * DBに接続し、アカウントを追加する
	 * 引数	 ；アカウント情報（CategoryDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean categoryAdd(CategoryDto dto){

		boolean result = true;

		//DB接続
		CategoryDao dao = new CategoryDao();
		result = dao.insertCategory(dto);

		return result;
	}

}
