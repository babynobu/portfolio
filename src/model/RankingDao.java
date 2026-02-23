package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RankingDao {

    String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    String JDBC_URL    = "jdbc:mysql://example-app-db-1:3306/portfolio_db?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo&useSSL=false&allowPublicKeyRetrieval=true";
    String USER_ID     = "test_user";
    String USER_PASS   = "test_pass";

    /** 今月いいねランキング（0も含む）10件ずつ取得 */
    public List<MonthlyLikeRankingDto> selectMonthlyRanking(int limit, int offset) {

        List<MonthlyLikeRankingDto> list = new ArrayList<>();

        // 今月 start/end を作成
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
        LocalDate firstDayOfNextMonth = firstDayOfThisMonth.plusMonths(1);
        Timestamp start = Timestamp.valueOf(firstDayOfThisMonth.atStartOfDay());
        Timestamp end   = Timestamp.valueOf(firstDayOfNextMonth.atStartOfDay());

        try {
            Class.forName(DRIVER_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        StringBuffer buf = new StringBuffer();
        buf.append(" SELECT ");
        buf.append("   u.user_id, ");
        buf.append("   u.user_name, ");
        buf.append("   up.profile_image_path, ");
        buf.append("   COALESCE(gs.like_count, 0) AS like_count ");
        buf.append(" FROM users u ");
        buf.append(" LEFT JOIN user_profiles up ");
        buf.append("   ON u.user_id = up.user_id ");
        buf.append(" LEFT JOIN ( ");
        buf.append("   SELECT target_user_id, COUNT(*) AS like_count ");
        buf.append("   FROM good_stamp ");
        buf.append("   WHERE stamp_at >= ? AND stamp_at < ? ");
        buf.append("   GROUP BY target_user_id ");
        buf.append(" ) gs ");
        buf.append("   ON u.user_id = gs.target_user_id ");
        buf.append(" WHERE u.is_deleted = 0 ");
        buf.append("   AND u.role = 0 ");
        buf.append(" ORDER BY like_count DESC, u.user_id ASC ");
        buf.append(" LIMIT ? OFFSET ? ");

        try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
             PreparedStatement ps = con.prepareStatement(buf.toString())) {

            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonthlyLikeRankingDto dto = new MonthlyLikeRankingDto();
                    dto.setUserId(rs.getInt("user_id"));
                    dto.setUserName(rs.getString("user_name"));
                    dto.setProfileImagePath(rs.getString("profile_image_path"));
                    dto.setLikeCount(rs.getInt("like_count"));
                    list.add(dto);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    /** ランキング母数（0いいねも含めるので users 件数） */
    public int countMonthlyRankingTargets() {

        try {
            Class.forName(DRIVER_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String sql = "SELECT COUNT(*) FROM users WHERE is_deleted = 0 AND role = 0";

        try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
