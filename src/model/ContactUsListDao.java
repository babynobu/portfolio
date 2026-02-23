package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■ContactUsListDaoクラス■■■
 *概要：DAO
 *----------------------------------------------------------------------**/


public class ContactUsListDao {

	//-------------------------------------------
	//データベースへの接続情報
	//-------------------------------------------

	//JDBCドライバの相対パス
	String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

	//接続先のデータベース
	String JDBC_URL    = "jdbc:mysql://example-app-db-1:3306/portfolio_db?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo&useSSL=false&allowPublicKeyRetrieval=true";

	//接続するユーザー名
	String USER_ID     = "test_user";

	//接続するユーザーのパスワード
	String USER_PASS   = "test_pass";

	//----------------------------------------------------------------
	//メソッド
	//----------------------------------------------------------------

	/**----------------------------------------------------------------------*
	 *■selectContactUsListメソッド
	 *概要  ：お問い合わせ一覧を抽出する
	 *引数  ：なし
	 *戻り値：お問い合わせ一覧（List<ContactUsListDto>型）
	 *----------------------------------------------------------------------**/

	public List<ContactUsListDto> selectContactUsList(){

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

		//抽出データ（AccountListDto型）格納用変数
		List<ContactUsListDto> list = new ArrayList<>();

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
			buf.append(" SELECT                              ");
			buf.append("   	cu.contact_us_id,                ");
			buf.append("   	cc.category_name,                ");
			buf.append("   	cu.body,                         ");
			buf.append("   	cu.email,                        ");
			buf.append("   	cu.status,                       ");
			buf.append("   	cu.created_at,                   ");
			buf.append("   	cu.updated_at                    ");
			buf.append(" FROM                                ");
			buf.append(" 	contact_us cu                    ");
			buf.append(" INNER JOIN                          ");
			buf.append(" 	contact_categories cc            ");
			buf.append(" ON                                  ");
			buf.append("  	cu.category_id = cc.category_id  ");
			buf.append(" ORDER BY                            ");
			buf.append("  	cu.created_at asc                ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			while (rs.next()) {
				ContactUsListDto dto = new ContactUsListDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setContactUsId(   rs.getInt("contact_us_id")   );				//お問い合わせID
				dto.setCategoryName(   rs.getString("category_name")   );			//カテゴリー名
				dto.setBody(   rs.getString("body")   );  							//本文
				dto.setEmail(   rs.getString("email")   );  						//メールアドレス
				dto.setStatus(   rs.getInt("status")   );  							//ステータス 1:未対応 2:対応中 3:対応済み
				dto.setCreatedAt(  rs.getDate("created_at").toLocalDate()   );   	//作成日時
				dto.setUpdatedAt(  rs.getDate("updated_at").toLocalDate()   );   	//最終更新日時

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
	 *■editContactUsメソッド
	 *概要  ：編集対象のお問い合わせを抽出する
	 *引数  ：userId
	 *戻り値：お問い合わせ情報（ContactUsListDto型）
	 *----------------------------------------------------------------------**/

	public ContactUsListDto editContactUs(int cuid){

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

		//抽出データ（ContactUsListDto型）格納用変数
		ContactUsListDto dto = new ContactUsListDto();

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
			buf.append(" SELECT                              ");
			buf.append("   	cu.contact_us_id,                ");
			buf.append("   	cc.category_name,                ");
			buf.append("   	cu.body,                         ");
			buf.append("   	cu.email,                        ");
			buf.append("   	cu.status,                       ");
			buf.append("   	cu.created_at,                   ");
			buf.append("   	cu.updated_at                    ");
			buf.append(" FROM                                ");
			buf.append(" 	contact_us cu                    ");
			buf.append(" INNER JOIN                          ");
			buf.append(" 	contact_categories cc            ");
			buf.append(" ON                                  ");
			buf.append("  	cu.category_id = cc.category_id  ");
			buf.append(" WHERE                               ");
			buf.append("  	cu.contact_us_id = ?             ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//パラメータをセット
			ps.setInt( 1, cuid  );

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			if (rs.next()) {
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setContactUsId(   rs.getInt("contact_us_id")   );			//ユーザーID
				dto.setCategoryName(   rs.getString("category_name")   );		//ユーザー名
				dto.setBody(   rs.getString("body")   );  						//カナ.
				dto.setEmail(   rs.getString("email")   );  					//権限.
				dto.setStatus(  rs.getInt("status")  );   						//メールアドレス
				dto.setCreatedAt(  rs.getDate("created_at").toLocalDate()  );   //ステータス
				dto.setUpdatedAt(  rs.getDate("updated_at").toLocalDate()  );	//性別
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
	 *■updateContactUsメソッド
	 *概要  ：編集対象のお問い合わせを更新する
	 *引数  ：userId
	 *戻り値：boolean型 (true:更新 false:失敗)
	 *----------------------------------------------------------------------**/

	public boolean updateContactUs(int cuid, String st){

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

		//抽出データ（boolean型）格納用変数
		boolean executeUpdate = true;

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
			buf.append(" UPDATE                     ");
			buf.append(" 	contact_us              ");
			buf.append(" SET                        ");
			buf.append("   	status = ?              ");
			buf.append(" WHERE                      ");
			buf.append("  	contact_us_id = ?       ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//パラメータをセット
			ps.setString( 1, st );
			ps.setInt( 2, cuid  );

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			ps.executeUpdate();

		} catch (Exception e){

			e.printStackTrace();

			executeUpdate = false;

		} finally {
			//-------------------------------------------
			//接続の解除
			//-------------------------------------------

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
		return executeUpdate;
	}

}
