package model;

/**----------------------------------------------------------------------*
 *■■■CategoryUpdateBLクラス■■■
 *概要：ビジネスロジック（アカウントの更新）
 *----------------------------------------------------------------------**/

public class CategoryUpdateBL {

	/**----------------------------------------------------
	 * ■categoryUpdateメソッド
	 * DBに接続し、カテゴリーを更新する
	 * 引数	 ；カテゴリー情報（CategoryDto型）
	 * 戻り値：処理結果（true/false）
	 -----------------------------------------------------*/

	public boolean categoryUpdate(CategoryDto dto){

		boolean result = true;

		//DB接続
		CategoryDao dao = new CategoryDao();
		result = dao.updateCategory(dto);

		return result;
	}

}
