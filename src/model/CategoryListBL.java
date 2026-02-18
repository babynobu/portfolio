package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■CategoryListBLクラス■■■
 *概要：ビジネスロジック（カテゴリー一覧の抽出）
 *----------------------------------------------------------------------**/

public class CategoryListBL {

	/**----------------------------------------------------
	 * ■selectCategoryListメソッド
	 * DBに接続し、カテゴリー一覧を抽出
	 * 引数	 ；なし
	 * 戻り値：（List<CategoryDto>型）
	 -----------------------------------------------------*/

	public List<CategoryDto> selectCategoryList(){

		//戻り値用の型を生成
		List<CategoryDto> list = new ArrayList<>();

		//DB接続
		CategoryDao dao = new CategoryDao();
		list = dao.selectCategoryList();

		return list;
	}

}
