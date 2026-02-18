package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■CategoryDaoクラス■■■
 *概要：DAO
 *----------------------------------------------------------------------**/


public class CategoryDao {

	//-------------------------------------------
	//データベースへの接続情報
	//-------------------------------------------

	//JDBCドライバの相対パス
	String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

	//接続先のデータベース
	String JDBC_URL    = "jdbc:mysql://localhost/portfolio_db?characterEncoding=UTF-8&serverTimezone=JST&useSSL=false";

	//接続するユーザー名
	String USER_ID     = "test_user";

	//接続するユーザーのパスワード
	String USER_PASS   = "test_pass";

	//----------------------------------------------------------------
	//メソッド
	//----------------------------------------------------------------

	/**----------------------------------------------------------------------*
	 *■selectCategoryListメソッド
	 *概要  ：カテゴリー一覧を抽出する
	 *引数  ：なし
	 *戻り値：カテゴリー一覧（List<CategoryDto>型）
	 *----------------------------------------------------------------------**/

	public List<CategoryDto> selectCategoryList(){

		//-------------------------------------------
		//JDBCドライバのロード
		//-------------------------------------------
		try {
			Class.forName(DRIVER_NAME);       //JDBCドライバをロード＆接続先として指定
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		//-------------------------------------------
		//SQL発行
		//-------------------------------------------

		//JDBCの接続に使用するオブジェクトを宣言
		//※finallyブロックでも扱うためtryブロック内で宣言してはいけないことに注意
		Connection        con = null ;   // Connection（DB接続情報）格納用変数
		PreparedStatement ps  = null ;   // PreparedStatement（SQL発行用オブジェクト）格納用変数
		ResultSet         rs  = null ;   // ResultSet（SQL抽出結果）格納用変数

		//抽出データ（CategoryListDto型）格納用変数
		List<CategoryDto> list = new ArrayList<>();

		try {

			//-------------------------------------------
			//接続の確立（Connectionオブジェクトの取得）
			//-------------------------------------------
			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

			//-------------------------------------------
			//SQL文の送信 ＆ 結果の取得
			//-------------------------------------------

			//発行するSQL文の生成（SELECT）
			StringBuffer buf = new StringBuffer();
			buf.append(" SELECT                  ");
			buf.append("   	category_id,         ");
			buf.append("   	category_name,       ");
			buf.append("   	created_at,          ");
			buf.append("   	updated_at          ");
			buf.append(" FROM                    ");
			buf.append(" 	contact_categories   ");
			buf.append(" ORDER BY                ");
			buf.append("  	category_id asc      ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			while (rs.next()) {
				CategoryDto dto = new CategoryDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setCategoryId(   rs.getInt("category_id")   );					//カテゴリーID
				dto.setCategoryName(   rs.getString("category_name")   );			//カテゴリー名
				dto.setCreatedAt(   rs.getDate("created_at").toLocalDate()   );		//作成日時
				dto.setUpdatedAt(   rs.getDate("updated_at").toLocalDate()   );		//最終更新日時

				//listに追加
				list.add(dto);
			}

		} catch (Exception e){

			e.printStackTrace();

		} finally {
			//-------------------------------------------
			//接続の解除
			//-------------------------------------------

			//ResultSetオブジェクトの接続解除
			if (rs != null) {    //接続が確認できている場合のみ実施
				try {
					rs.close();  //接続の解除
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			//PreparedStatementオブジェクトの接続解除
			if (ps != null) {    //接続が確認できている場合のみ実施
				try {
					ps.close();  //接続の解除
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			//Connectionオブジェクトの接続解除
			if (con != null) {    //接続が確認できている場合のみ実施
				try {
					con.close();  //接続の解除
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return list;
	}


	/**----------------------------------------------------------------------*
	 *■editCategoryメソッド
	 *概要  ：編集対象アカウントを抽出する
	 *引数  ：userId
	 *戻り値：アカウント情報（CategoryDto型）
	 *----------------------------------------------------------------------**/

	public CategoryDto editCategory(String cid){

		//-------------------------------------------
		//JDBCドライバのロード
		//-------------------------------------------
		try {
			Class.forName(DRIVER_NAME);       //JDBCドライバをロード＆接続先として指定
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		//-------------------------------------------
		//SQL発行
		//-------------------------------------------

		//JDBCの接続に使用するオブジェクトを宣言
		//※finallyブロックでも扱うためtryブロック内で宣言してはいけないことに注意
		Connection        con = null ;   // Connection（DB接続情報）格納用変数
		PreparedStatement ps  = null ;   // PreparedStatement（SQL発行用オブジェクト）格納用変数
		ResultSet         rs  = null ;   // ResultSet（SQL抽出結果）格納用変数

		//抽出データ（CategoryDto型）格納用変数
		CategoryDto dto = new CategoryDto();

		try {

			//-------------------------------------------
			//接続の確立（Connectionオブジェクトの取得）
			//-------------------------------------------
			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

			//-------------------------------------------
			//SQL文の送信 ＆ 結果の取得
			//-------------------------------------------

			//発行するSQL文の生成（SELECT）
			StringBuffer buf = new StringBuffer();
			buf.append(" SELECT                  ");
			buf.append("   	category_id,         ");
			buf.append("   	category_name        ");
			buf.append(" FROM                    ");
			buf.append(" 	contact_categories   ");
			buf.append(" WHERE                   ");
			buf.append("  	is_deleted = 0       ");
			buf.append("  	AND                  ");
			buf.append("  	category_id = ?      ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//パラメータをセット
			ps.setString( 1, cid  );

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			if (rs.next()) {
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setCategoryId(   rs.getInt("category_id")   );				//カテゴリーID
				dto.setCategoryName(   rs.getString("category_name")   );		//カテゴリー名
			}

		} catch (Exception e){

			e.printStackTrace();

		} finally {
			//-------------------------------------------
			//接続の解除
			//-------------------------------------------

			//ResultSetオブジェクトの接続解除
			if (rs != null) {    //接続が確認できている場合のみ実施
				try {
					rs.close();  //接続の解除
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			//PreparedStatementオブジェクトの接続解除
			if (ps != null) {    //接続が確認できている場合のみ実施
				try {
					ps.close();  //接続の解除
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			//Connectionオブジェクトの接続解除
			if (con != null) {    //接続が確認できている場合のみ実施
				try {
					con.close();  //接続の解除
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return dto;
	}

    /**----------------------------------------------------------------------*
     *■updateCategoryメソッド
     *概要  ：カテゴリーを更新する
     *引数  ：カテゴリー情報（CategoryDto型）
     *戻り値：処理結果（true:成功 / false:失敗）
     *----------------------------------------------------------------------**/
    public boolean updateCategory(CategoryDto dto) {

        Connection con = null;
        PreparedStatement ps = null;

        boolean result = true;

        try {
            //-------------------------------------------
            // JDBCドライバのロード
            //-------------------------------------------
            Class.forName(DRIVER_NAME);

            //-------------------------------------------
            // 接続の確立
            //-------------------------------------------
            con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
            con.setAutoCommit(false);   // トランザクション開始

            //-------------------------------------------
            // contact_categorie テーブル UPDATE
            //-------------------------------------------
            StringBuffer buf = new StringBuffer();
            buf.append(" UPDATE contact_categories  ");
            buf.append(" SET  ");
            buf.append("   category_name = ?  ");
            buf.append(" WHERE  ");
            buf.append("   category_id = ? ");

            ps = con.prepareStatement(buf.toString());

            ps.setString(1, dto.getCategoryName());
            ps.setInt(2, dto.getCategoryId());

            ps.executeUpdate();


            //-------------------------------------------
            // コミット
            //-------------------------------------------
            con.commit();

        } catch (Exception e) {

            result = false;

            //-------------------------------------------
            // ロールバック
            //-------------------------------------------
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            //-------------------------------------------
            // 接続解除
            //-------------------------------------------
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**----------------------------------------------------------------------*
     *■insertCategoryメソッド
     *概要  ：カテゴリーを追加する
     *引数  ：カテゴリー情報（CategoryDto）
     *戻り値：処理結果（true:成功 / false:失敗）
     *----------------------------------------------------------------------**/
    public boolean insertCategory(CategoryDto dto) {

        Connection con = null;
        PreparedStatement ps = null;

        boolean result = true;

        try {
            //-------------------------------------------
            // JDBCドライバのロード
            //-------------------------------------------
            Class.forName(DRIVER_NAME);

            //-------------------------------------------
            // 接続の確立
            //-------------------------------------------
            con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
            con.setAutoCommit(false);   // トランザクション開始

            //-------------------------------------------
            // contact_categories テーブル INSERT
            //-------------------------------------------
            StringBuffer bufUser = new StringBuffer();
            bufUser.append(" INSERT INTO contact_categories ( ");
            bufUser.append("   category_name  ");
            bufUser.append(" ) VALUES ( ");
            bufUser.append("   ? ");
            bufUser.append(" ) ");

            ps = con.prepareStatement(bufUser.toString());

            ps.setString(1, dto.getCategoryName());


            ps.executeUpdate();

            //-------------------------------------------
            // コミット
            //-------------------------------------------
            con.commit();

        } catch (Exception e) {

            result = false;

            //-------------------------------------------
            // ロールバック
            //-------------------------------------------
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            //-------------------------------------------
            // 接続解除
            //-------------------------------------------
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**----------------------------------------------------------------------*
     *■selectActiveCategoryListメソッド
     *概要  ：削除されていないカテゴリー一覧（is_deleted=0）を抽出する
     *引数  ：なし
     *戻り値：カテゴリー一覧（List<CategoryDto>型）
     *----------------------------------------------------------------------**/
    public List<CategoryDto> selectActiveCategoryList(){

        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = null;
        PreparedStatement ps  = null;
        ResultSet rs  = null;

        List<CategoryDto> list = new ArrayList<>();

        try {
            con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

            StringBuffer buf = new StringBuffer();
            buf.append(" SELECT                ");
            buf.append("   category_id,        ");
            buf.append("   category_name       ");
            buf.append(" FROM                  ");
            buf.append("   contact_categories  ");
            buf.append(" WHERE                 ");
            buf.append("   is_deleted = 0      ");
            buf.append(" ORDER BY              ");
            buf.append("   category_id ASC     ");

            ps = con.prepareStatement(buf.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                CategoryDto dto = new CategoryDto();
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setCategoryName(rs.getString("category_name"));
                list.add(dto);
            }

        } catch (Exception e){
            e.printStackTrace();

        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return list;
    }


}
