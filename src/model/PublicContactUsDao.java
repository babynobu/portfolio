package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**----------------------------------------------------------------------*
 *■■■PublicContactUsDaoクラス■■■
 *概要：公開お問い合わせ登録DAO
 *----------------------------------------------------------------------**/
public class PublicContactUsDao {

    // CategoryDaoと同じ
    String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    String JDBC_URL    = "jdbc:mysql://example-app-db-1:3306/portfolio_db?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo&useSSL=false&allowPublicKeyRetrieval=true";
    String USER_ID     = "test_user";
    String USER_PASS   = "test_pass";

    /**----------------------------------------------------------------------*
     *■insertContactUsメソッド
     *概要  ：contact_usへ登録（status='1'固定）
     *引数  ：PublicContactUsDto
     *戻り値：true:成功 / false:失敗
     *----------------------------------------------------------------------**/
    public boolean insertContactUs(PublicContactUsDto dto) {

        Connection con = null;
        PreparedStatement ps = null;

        boolean result = true;

        try {
            Class.forName(DRIVER_NAME);

            con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
            con.setAutoCommit(false);

            StringBuffer buf = new StringBuffer();
            buf.append(" INSERT INTO contact_us ( ");
            buf.append("   category_id, ");
            buf.append("   body, ");
            buf.append("   email, ");
            buf.append("   status ");
            buf.append(" ) VALUES ( ");
            buf.append("   ?, ?, ?, '1' ");
            buf.append(" ) ");

            ps = con.prepareStatement(buf.toString());
            ps.setInt(1, dto.getCategoryId());
            ps.setString(2, dto.getBody());
            ps.setString(3, dto.getEmail());

            ps.executeUpdate();
            con.commit();

        } catch (Exception e) {

            result = false;

            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
