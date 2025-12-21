package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■AccountListDaoクラス■■■
 *概要：DAO
 *----------------------------------------------------------------------**/


public class AccountListDao {

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
	 *■selectAccountListメソッド
	 *概要  ：アカウント一覧を抽出する
	 *引数  ：なし
	 *戻り値：アカウント一覧（List<UserStampCountDto>型）
	 *----------------------------------------------------------------------**/

	public List<AccountListDto> selectAccountList(){

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

		//抽出データ（UserStampCountDto型）格納用変数
		List<AccountListDto> list = new ArrayList<>();

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
			buf.append(" SELECT             ");
			buf.append("   	user_id,        ");
			buf.append("   	user_name,      ");
			buf.append("   	user_name_kana, ");
			buf.append("   	email,          ");
			buf.append("   	status          ");
			buf.append(" FROM               ");
			buf.append(" 	users           ");
			buf.append(" WHERE              ");
			buf.append("  	role = 0        ");
			buf.append(" ORDER BY           ");
			buf.append("  	user_id asc     ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			while (rs.next()) {
				AccountListDto dto = new AccountListDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setUserId(   rs.getInt("user_id")   );					//ユーザーID
				dto.setUserName(   rs.getString("user_name")   );			//ユーザー名
				dto.setUserNameKana(   rs.getString("user_name_kana")   );  //カナ.
				dto.setEmail(  rs.getString("email")  );   					//メールアドレス
				dto.setStatus(  rs.getInt("status")  );   					//ステータス

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

}
