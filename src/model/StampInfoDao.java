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
 *■■■StampInfoDaoクラス■■■
 *概要：DAO（「good_stamp」テーブル）
 *----------------------------------------------------------------------**/


public class StampInfoDao {

	//-------------------------------------------
	//データベースへの接続情報
	//-------------------------------------------

	//JDBCドライバの相対パス
	String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

	//接続先のデータベース
	String JDBC_URL = "jdbc:mysql://touma-portfolio-db:3306/portfolio_db?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo&useSSL=false&allowPublicKeyRetrieval=true";

	//接続するユーザー名
	String USER_ID     = "test_user";

	//接続するユーザーのパスワード
	String USER_PASS   = "test_pass";

	//----------------------------------------------------------------
	//メソッド
	//----------------------------------------------------------------

	/**----------------------------------------------------------------------*
	 *■monthlyGetStampInfoメソッド
	 *概要  ：引数のユーザー情報に紐づく月間いいねデータを「good_stamp」テーブルから抽出する
	 *引数  ：ユーザーID
	 *戻り値：「good_stamp」テーブルから抽出したいいねデータ（List<StampInfoDto>型）
	 *----------------------------------------------------------------------**/

	public List<StampInfoDto> monthlyGetStampInfo(int userId){

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
		List<StampInfoDto> list = new ArrayList();

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
			buf.append("   stamp_number,         ");
			buf.append("   stamp_at              ");
			buf.append(" FROM                    ");
			buf.append("   good_atamp            ");
			buf.append(" WHERE                   ");
			buf.append("   target_user_id = ?    "); //第一パラメータ
			buf.append("   AND stamp_at >=  ?    "); //第二パラメータ
			buf.append("   AND stamp_at <  ?     "); //第三パラメータ
			buf.append(" ORDER BY                ");
			buf.append("   stamp_at ASC          ");


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
			ps.setInt( 1, userId   ); 	//ユーザーID
			ps.setTimestamp( 2, startTs   ); 	//対象期間開始
			ps.setTimestamp( 3, endTs   ); 	//対象期間終了

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			while (rs.next()) {
				StampInfoDto dto = new StampInfoDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setStampNumber(   rs.getInt("stamp_number")   );   	 //いいねNo.
				dto.setStampAt(  rs.getTimestamp("stamp_at")  );   		 //タイムスタンプ

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
	 *■yearlyGetStampInfoメソッド
	 *概要  ：引数のユーザー情報に紐づく年間いいねデータを「good_stamp」テーブルから抽出する
	 *引数  ：ユーザーID
	 *戻り値：「good_stamp」テーブルから抽出したいいねデータ（List<StampInfoDto>型）
	 *----------------------------------------------------------------------**/

	public List<StampInfoDto> yearlyGetStampInfo(int userId){

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
		List<StampInfoDto> list = new ArrayList();

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
			buf.append("   stamp_number,         ");
			buf.append("   stamp_at              ");
			buf.append(" FROM                    ");
			buf.append("   good_atamp            ");
			buf.append(" WHERE                   ");
			buf.append("   target_user_id = ?    "); //第一パラメータ
			buf.append("   AND stamp_at >= ?     "); //第二パラメータ
			buf.append("   AND stamp_at < ?      "); //第三パラメータ
			buf.append(" ORDER BY                ");
			buf.append("   stamp_at ASC          ");


			//PreparedStatement（SQL発行用オブジェクト）を生成＆発行するSQLをセット
			ps = con.prepareStatement(buf.toString());

			//対象期間
			int year = LocalDate.now().getYear();
			Timestamp startTs = Timestamp.valueOf(LocalDate.of(year, 1, 1).atStartOfDay());		//年初
			Timestamp endTs = Timestamp.valueOf(LocalDate.of(year + 1, 1, 1).atStartOfDay());	//年末

			//パラメータをセット
			ps.setInt( 1, userId   ); 	//ユーザーID
			ps.setTimestamp( 2, startTs   ); 	//対象期間開始
			ps.setTimestamp( 3, endTs   ); 	//対象期間終了

			//SQL文の送信＆戻り値としてResultSet（SQL抽出結果）を取得
			rs = ps.executeQuery();

			//--------------------------------------------------------------------------------
			//ResultSetオブジェクトからユーザーデータを抽出
			//--------------------------------------------------------------------------------
			while (rs.next()) {
				StampInfoDto dto = new StampInfoDto();
				//ResultSetから1行分のレコード情報をDTOへ登録
				dto.setStampNumber(   rs.getInt("stamp_number")   );   	 //いいねNo.
				dto.setStampAt(  rs.getTimestamp("stamp_at")  );   		 //タイムスタンプ

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
