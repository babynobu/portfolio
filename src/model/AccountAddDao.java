package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**----------------------------------------------------------------------*
 *■■■AccountAddDaoクラス■■■
 *概要：アカウント追加用DAO
 *----------------------------------------------------------------------**/
public class AccountAddDao {

    //-------------------------------------------
    // データベースへの接続情報
    //-------------------------------------------
    String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    String JDBC_URL    = "jdbc:mysql://localhost/portfolio_db?characterEncoding=UTF-8&serverTimezone=JST&useSSL=false";
    String USER_ID     = "test_user";
    String USER_PASS   = "test_pass";

    //----------------------------------------------------------------
    // メソッド
    //----------------------------------------------------------------

    /**----------------------------------------------------------------------*
     *■insertAccountメソッド
     *概要  ：アカウントを追加する
     *引数  ：アカウント情報（AccountAddDto）
     *戻り値：処理結果（true:成功 / false:失敗）
     *----------------------------------------------------------------------**/
    public boolean insertAccount(AccountAddDto dto) {

        Connection con = null;
        PreparedStatement psUser    = null;
        PreparedStatement psProfile = null;
        ResultSet rs = null;

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
            // users テーブル INSERT
            //-------------------------------------------
            StringBuffer bufUser = new StringBuffer();
            bufUser.append(" INSERT INTO users ( ");
            bufUser.append("   login_id, ");
            bufUser.append("   password, ");
            bufUser.append("   user_name, ");
            bufUser.append("   user_name_kana, ");
            bufUser.append("   email, ");
            bufUser.append("   role ");
            bufUser.append(" ) VALUES ( ");
            bufUser.append("   ?, ?, ?, ?, ?, ? ");
            bufUser.append(" ) ");

            psUser = con.prepareStatement(
                    bufUser.toString(),
                    Statement.RETURN_GENERATED_KEYS
            );

            psUser.setString(1, dto.getLoginId());
            psUser.setString(2, dto.getPassword());
            psUser.setString(3, dto.getName());
            psUser.setString(4, dto.getKana());
            psUser.setString(5, dto.getEmail());

            // ===== ここが重要：role を int に変換 =====
            int roleInt;
            if ("admin".equals(dto.getRole())) {
                roleInt = 1;
            } else if ("general".equals(dto.getRole())) {
                roleInt = 0;
            } else {
                throw new IllegalArgumentException("不正なrole: " + dto.getRole());
            }

            psUser.setInt(6, roleInt);   // ← String ではなく int
            // ==========================================

            psUser.executeUpdate();

            //-------------------------------------------
            // 自動採番された user_id を取得
            //-------------------------------------------
            rs = psUser.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            //-------------------------------------------
            // 一般ユーザーの場合のみ user_profiles INSERT
            //-------------------------------------------
            if ("general".equals(dto.getRole())) {

                StringBuffer bufProfile = new StringBuffer();
                bufProfile.append(" INSERT INTO user_profiles ( ");
                bufProfile.append("   user_id, ");
                bufProfile.append("   gender, ");
                bufProfile.append("   birthday, ");
                bufProfile.append("   introduction ");
                bufProfile.append(" ) VALUES ( ");
                bufProfile.append("   ?, ?, ?, ? ");
                bufProfile.append(" ) ");

                psProfile = con.prepareStatement(bufProfile.toString());

                psProfile.setInt(1, userId);
                psProfile.setString(2, dto.getGender());
                psProfile.setDate(3, java.sql.Date.valueOf(dto.getBirthday()));
                psProfile.setString(4, dto.getIntroduction());

                psProfile.executeUpdate();
            }

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
                if (rs != null) rs.close();
                if (psProfile != null) psProfile.close();
                if (psUser != null) psUser.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
