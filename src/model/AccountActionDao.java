package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountActionDao {

    private static final String JDBC_URL =
    		"jdbc:mysql://example-app-db-1:3306/portfolio_db?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "test_user";
    private static final String DB_PASS = "test_pass";

    /**
     * ステータスを切り替えて、新しいステータスを返す
     */
    public int toggleStatus(int userId) throws Exception {

        int currentStatus;
        int newStatus;

        try (Connection con = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

            // ① 現在のステータス取得（未削除のみ）
            String selectSql =
                "SELECT status FROM users WHERE user_id = ? AND is_deleted = 0";

            try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    throw new Exception("対象ユーザーなし");
                }
                currentStatus = rs.getInt("status");
            }

            // ② 反転
            newStatus = (currentStatus == 1) ? 0 : 1;

            // ③ 更新
            String updateSql =
                "UPDATE users SET status = ? WHERE user_id = ? AND is_deleted = 0";

            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setInt(1, newStatus);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
        }

        return newStatus;
    }

    /**
     * アカウント復元
     */
    public void restore(int userId) throws Exception {

    	try (Connection con = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

    		String sql =
    			"UPDATE users SET is_deleted = 0 WHERE user_id = ? AND is_deleted= 1";

    		try (PreparedStatement ps = con.prepareStatement(sql)) {
    			ps.setInt(1, userId);
    			ps.executeUpdate();
    		}
    	}
    }

    /**
     * 論理削除
     */
    public void logicalDelete(int userId) throws Exception {

        try (Connection con = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

            String sql =
                "UPDATE users SET is_deleted = 1 WHERE user_id = ? AND is_deleted = 0";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }
        }
    }

    /**
     * 物理削除
     */
    public void physicsDelete(int userId) throws Exception {

        try (Connection con = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

            String sql =
                "DELETE u, p FROM users u LEFT JOIN user_profiles p ON u.user_id = p.user_id WHERE u.user_id = ? ";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }
        }
    }
}
