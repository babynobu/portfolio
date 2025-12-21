package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■UserStampCountDaoクラス■■■
 *概要：DAO（「good_stamp」テーブル）
 *----------------------------------------------------------------------**/


public class UserStampCountDao {

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
	 *■monthlyGetStampRankingメソッド
	 *概要  ：月間いいねデータを抽出する
	 *引数  ：なし
	 *戻り値：月間いいねランキング（List<UserStampCountDto>型）
	 *----------------------------------------------------------------------**/

	public List<UserStampCountDto> monthlyGetStampRanking(){

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
		List<UserStampCountDto> list = new ArrayList();

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
			buf.append(" SELECT                                                       ");
			buf.append("   	RANK() OVER (ORDER BY stamp_count DESC) AS ranking,       ");
			buf.append("   	target_user_id,                                           ");
			buf.append("   	user_name,                                                ");
			buf.append("   	stamp_count                                               ");
			buf.append(" FROM (                                                       ");
			buf.append(" 	SELECT                                                    ");
			buf.append("   		g.target_user_id,                                     ");
			buf.append("   		u.user_name,                                          ");
			buf.append("   		COUNT(*) AS stamp_count                               ");
			buf.append(" 	FROM                                                      ");
			buf.append("  		good_stamp g                                          ");
			buf.append("  	INNER JOIN                                                ");
			buf.append("  		users u                                               ");
			buf.append("  		ON g.target_user_id = u.user_id                       ");
			buf.append("  	WHERE                                                     ");
			buf.append("  		u.role = 0                                            ");
			buf.append("  		AND g.stamp_at >= ?                                   ");	//第一パラメータ
			buf.append("  		AND g.stamp_at < ?                                    ");	//第二パラメータ
			buf.append("  	GROUP BY                                                  ");
			buf.append("  		g.target_user_id,                                     ");
			buf.append("  		u.user_name                                           ");
			buf.append("  	) t                                                       ");
			buf.append("  	ORDER BY                                                  ");
			buf.append("  		ranking ASC                                           ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//対象期間
			//月初
			LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
			Timestamp startTs = Timestamp.valueOf(monthStart);
			//月末
			LocalDateTime nextMonthStart = LocalDate.now().withDayOfMonth(1).plusMonths(1).atStartOfDay();
			Timestamp endTs = Timestamp.valueOf(nextMonthStart);

			//パラメータをセット
			ps.setTimestamp( 1, startTs   ); 	//対象期間開始
			ps.setTimestamp( 2, endTs   ); 	//対象期間終了

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			while (rs.next()) {
				UserStampCountDto dto = new UserStampCountDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setRanking(   rs.getInt("ranking")   );				//ランキングNo
				dto.setUserId(   rs.getInt("target_user_id")   );		//ユーザーID
				dto.setUserName(   rs.getString("user_name")   );   	//ユーザーネーム.
				dto.setStampCount(  rs.getInt("stamp_count")  );   		//いいね数

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
	 *■yearlyGetStampRankinメソッド
	 *概要  ：年間いいねデータを抽出する
	 *引数  ：なし
	 *戻り値：年間いいねランキング（List<UserStampCountDto>型）
	 *----------------------------------------------------------------------**/

	public List<UserStampCountDto> yearlyGetStampRanking(){

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
		List<UserStampCountDto> list = new ArrayList();

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
			buf.append(" SELECT                                                       ");
			buf.append("   	RANK() OVER (ORDER BY stamp_count DESC) AS ranking,       ");
			buf.append("   	target_user_id,                                           ");
			buf.append("   	user_name,                                                ");
			buf.append("   	stamp_count                                               ");
			buf.append(" FROM (                                                       ");
			buf.append(" 	SELECT                                                    ");
			buf.append("   		g.target_user_id,                                     ");
			buf.append("   		u.user_name,                                          ");
			buf.append("   		COUNT(*) AS stamp_count                               ");
			buf.append(" 	FROM                                                      ");
			buf.append("  		good_stamp g                                          ");
			buf.append("  	INNER JOIN                                                ");
			buf.append("  		users u                                               ");
			buf.append("  		ON g.target_user_id = u.user_id                       ");
			buf.append("  	WHERE                                                     ");
			buf.append("  		u.role = 0                                            ");
			buf.append("  		AND g.stamp_at >= ?                                   ");	//第一パラメータ
			buf.append("  		AND g.stamp_at < ?                                    ");	//第二パラメータ
			buf.append("  	GROUP BY                                                  ");
			buf.append("  		g.target_user_id,                                     ");
			buf.append("  		u.user_name                                           ");
			buf.append("  	) t                                                       ");
			buf.append("  	ORDER BY                                                  ");
			buf.append("  		ranking ASC                                           ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//対象期間
			int year = LocalDate.now().getYear();
			Timestamp startTs = Timestamp.valueOf(LocalDate.of(year, 1, 1).atStartOfDay());		//年初
			Timestamp endTs = Timestamp.valueOf(LocalDate.of(year + 1, 1, 1).atStartOfDay());	//年末

			//パラメータをセット
			ps.setTimestamp( 1, startTs   ); 	//対象期間開始
			ps.setTimestamp( 2, endTs   ); 	//対象期間終了

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			while (rs.next()) {
				UserStampCountDto dto = new UserStampCountDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setRanking(   rs.getInt("ranking")   );				//ランキングNo
				dto.setUserId(   rs.getInt("target_user_id")   );		//ユーザーID
				dto.setUserName(   rs.getString("user_name")   );   	//ユーザーネーム.
				dto.setStampCount(  rs.getInt("stamp_count")  );   		//いいね数

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
