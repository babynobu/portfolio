package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;

public class GoodStampDao {

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
	public int addLikeAndGetCount(int targetUserId) {

		try {
			Class.forName(DRIVER_NAME);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS)) {

			LocalDate today = LocalDate.now();
			LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
			LocalDate firstDayOfNextMonth = firstDayOfThisMonth.plusMonths(1);

			Timestamp start = Timestamp.valueOf(firstDayOfThisMonth.atStartOfDay());
			Timestamp end   = Timestamp.valueOf(firstDayOfNextMonth.atStartOfDay());

			// 1) いいね追加
			String insertSql = "INSERT INTO good_stamp (target_user_id) VALUES (?)";
			try (PreparedStatement ps = con.prepareStatement(insertSql)) {
				ps.setInt(1, targetUserId);
				ps.executeUpdate();
			}

			// 2) 今月のカウント取得（stamp_at を今月範囲で絞る）
			String countSql =
					"SELECT COUNT(*) FROM good_stamp "
				  + "WHERE target_user_id = ? AND stamp_at >= ? AND stamp_at < ?";

			try (PreparedStatement ps = con.prepareStatement(countSql)) {
				ps.setInt(1, targetUserId);
				ps.setTimestamp(2, start); // 今月開始
				ps.setTimestamp(3, end);   // 来月開始

				try (ResultSet rs = ps.executeQuery()) {
					rs.next();
					return rs.getInt(1);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
