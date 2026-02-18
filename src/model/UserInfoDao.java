package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**----------------------------------------------------------------------*
 *■■■UserInfoDaoクラス■■■
 *概要：DAO（「users」テーブル）
 *----------------------------------------------------------------------**/

public class UserInfoDao {
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
	 *■doSelectメソッド
	 *概要　：引数のユーザー情報に紐づくユーザーデータを「users」テーブルから抽出する
	 *引数①：ユーザーID（ユーザー入力）
	 *引数②：ユーザーパスワード（ユーザー入力）
	 *戻り値：「users」テーブルから抽出したユーザーデータ（UserInfoDto型）
	 *----------------------------------------------------------------------**/


	public UserInfoDto doSelect(String inputLoginId, String inputPassWord) {

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

		//抽出データ（UserInfoDto型）格納用変数
		UserInfoDto dto = null;

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
			buf.append(" SELECT               ");
			buf.append("   USER_ID,          ");
			buf.append("   LOGIN_ID,          ");
			buf.append("   USER_NAME,         ");
			buf.append("   PASSWORD,          ");
			buf.append("   ROLE               ");
			buf.append(" FROM                 ");
			buf.append("   USERS              ");
			buf.append(" WHERE                ");
			buf.append("   LOGIN_ID  = ? AND  ");  //第1パラメータ
			buf.append("   PASSWORD = ?       ");  //第2パラメータ
			buf.append("   AND STATUS = 1     ");  //1=有効、0=無効
			buf.append("   AND IS_DELETED = 0 ");

			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//パラメータをセット
			ps.setString( 1, inputLoginId   );  //第1パラメータ：ログインID（ユーザー入力）
			ps.setString( 2, inputPassWord );  //第2パラメータ：パスワード（ユーザー入力）

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			if (rs.next()) {
				dto = new UserInfoDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setUserId( rs.getInt("USER_ID"));			//ユーザーID
				dto.setLoginId( rs.getString("LOGIN_ID")   );	//ログインID
				dto.setUserName( rs.getString("USER_NAME") );	//ユーザー名
				dto.setUserPass( rs.getString("PASSWORD")  );	//パスワード
				dto.setRole( rs.getInt("ROLE")  );				//ユーザー権限
			}

		} catch (SQLException e) {
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

		//抽出したユーザーデータを戻す
		return dto;
	}

}
