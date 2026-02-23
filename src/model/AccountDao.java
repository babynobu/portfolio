package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■AccountDaoクラス■■■
 *概要：アカウント用DAO
 *----------------------------------------------------------------------**/
public class AccountDao {

	//-------------------------------------------
	// データベースへの接続情報
	//-------------------------------------------
	String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	String JDBC_URL    = "jdbc:mysql://example-app-db-1:3306/portfolio_db?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo&useSSL=false&allowPublicKeyRetrieval=true";
	String USER_ID     = "test_user";
	String USER_PASS   = "test_pass";

	//----------------------------------------------------------------
	// メソッド
	//----------------------------------------------------------------

	/**----------------------------------------------------------------------*
	 *■existsLoginIdメソッド
	 *概要  ：login_id の重複を確認
	 *引数  ：ログインID（String）
	 *戻り値：存在する場合 true
	 *----------------------------------------------------------------------**/
	public boolean existsLoginId(String loginId) {

		String sql = "SELECT COUNT(*) FROM users WHERE login_id = ?";

		try (
				Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
				PreparedStatement ps = con.prepareStatement(sql)
		) {
			ps.setString(1, loginId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**----------------------------------------------------------------------*
	 *■existsLoginIdExceptSelfメソッド
	 *概要  ：login_id の重複を確認(自分のIDは除外)
	 *引数  ：ログインID（String）, userId
	 *戻り値：存在する場合 true
	 *----------------------------------------------------------------------**/
	public boolean existsLoginIdExceptSelf(String loginId, int userId) {

		String sql =
				"SELECT COUNT(*) " +
				"FROM users " +
				"WHERE login_id = ? " +
				"  AND user_id <> ?";

		try (
				Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
				PreparedStatement ps = con.prepareStatement(sql)
		) {
			ps.setString(1, loginId);
			ps.setInt(2, userId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**----------------------------------------------------------------------*
	 *■insertAccountメソッド
	 *概要  ：アカウントを追加する
	 *引数  ：アカウント情報（AccountDto）
	 *戻り値：処理結果（true:成功 / false:失敗）
	 *----------------------------------------------------------------------**/
	public boolean insertAccount(AccountDto dto) {

	    Connection con = null;
	    PreparedStatement psUser    = null;
	    PreparedStatement psProfile = null;
	    ResultSet rs = null;

	    boolean result = true;

	    try {
	        Class.forName(DRIVER_NAME);

	        con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
	        con.setAutoCommit(false);

	        //-------------------------------------------
	        // users INSERT（※ user_name_kana なし）
	        //-------------------------------------------
	        StringBuffer bufUser = new StringBuffer();
	        bufUser.append(" INSERT INTO users ( ");
	        bufUser.append("   login_id, ");
	        bufUser.append("   password, ");
	        bufUser.append("   user_name, ");
	        bufUser.append("   email, ");
	        bufUser.append("   status, ");
	        bufUser.append("   role ");
	        bufUser.append(" ) VALUES ( ");
	        bufUser.append("   ?, ?, ?, ?, ?, ? ");
	        bufUser.append(" ) ");

	        psUser = con.prepareStatement(bufUser.toString(), Statement.RETURN_GENERATED_KEYS);

	        psUser.setString(1, dto.getLoginId());
	        psUser.setString(2, dto.getPassword());
	        psUser.setString(3, dto.getName());
	        psUser.setString(4, dto.getEmail());
	        psUser.setInt(5, dto.getStatus());
	        psUser.setInt(6, dto.getRole());

	        psUser.executeUpdate();

	        //-------------------------------------------
	        // 自動採番 user_id 取得
	        //-------------------------------------------
	        rs = psUser.getGeneratedKeys();
	        int userId = 0;
	        if (rs.next()) {
	            userId = rs.getInt(1);
	        }

	        //-------------------------------------------
	        // 一般ユーザー(role=0)のみ user_profiles INSERT
	        //-------------------------------------------
	        if (dto.getRole() == 0) {

	            StringBuffer bufProfile = new StringBuffer();
	            bufProfile.append(" INSERT INTO user_profiles ( ");
	            bufProfile.append("   user_id, ");
	            bufProfile.append("   gender, ");
	            bufProfile.append("   age, ");              // ★ birthday → age
	            bufProfile.append("   user_name_kana, ");
	            bufProfile.append("   introduction, ");
	            bufProfile.append("   profile_image_path ");
	            bufProfile.append(" ) VALUES ( ");
	            bufProfile.append("   ?, ?, ?, ?, ?, ? ");
	            bufProfile.append(" ) ");

	            psProfile = con.prepareStatement(bufProfile.toString());

	            psProfile.setInt(1, userId);
	            psProfile.setInt(2, dto.getGender());

	            // age は一般ユーザー必須想定
	            if (dto.getAge() == null) {
	                throw new IllegalStateException("age が未設定です");
	            }
	            psProfile.setInt(3, dto.getAge());

	            psProfile.setString(4, dto.getKana());
	            psProfile.setString(5, dto.getIntroduction());
	            psProfile.setString(6, dto.getProfileImagePath());

	            psProfile.executeUpdate();
	        }

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


	/**----------------------------------------------------------------------*
	 *■updateAccountメソッド
	 *概要  ：アカウントを更新する（管理側の更新想定）
	 *引数  ：アカウント情報（AccountDto）
	 *戻り値：処理結果（true:成功 / false:失敗）
	 *----------------------------------------------------------------------**/
	public boolean updateAccount(AccountDto dto) {

	    Connection con = null;
	    PreparedStatement psUser    = null;
	    PreparedStatement psProfile = null;

	    boolean result = true;

	    try {
	        Class.forName(DRIVER_NAME);

	        con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
	        con.setAutoCommit(false);

	        //-------------------------------------------
	        // users UPDATE（※ user_name_kana なし）
	        //-------------------------------------------
	        StringBuffer bufUser = new StringBuffer();
	        bufUser.append(" UPDATE users  ");
	        bufUser.append(" SET  ");
	        bufUser.append("   login_id = ?, ");
	        bufUser.append("   password = ?, ");
	        bufUser.append("   user_name = ?, ");
	        bufUser.append("   email = ?, ");
	        bufUser.append("   status = ?, ");
	        bufUser.append("   role = ? ");
	        bufUser.append(" WHERE  ");
	        bufUser.append("   user_id = ? ");

	        psUser = con.prepareStatement(bufUser.toString());

	        psUser.setString(1, dto.getLoginId());
	        psUser.setString(2, dto.getPassword());
	        psUser.setString(3, dto.getName());
	        psUser.setString(4, dto.getEmail());
	        psUser.setInt(5, dto.getStatus());
	        psUser.setInt(6, dto.getRole());
	        psUser.setInt(7, dto.getUserId());

	        psUser.executeUpdate();

	        //-------------------------------------------
	        // 一般ユーザー(role=0)のみ user_profiles UPDATE/INSERT
	        //-------------------------------------------
	        if (dto.getRole() == 0) {

	            int updated = 0;

	            // age 必須
	            if (dto.getAge() == null) {
	                throw new IllegalStateException("age が未設定です");
	            }

	            if (dto.getProfileImagePath() != null) {
	                StringBuffer bufProfile = new StringBuffer();
	                bufProfile.append(" UPDATE user_profiles SET ");
	                bufProfile.append("   gender = ?, ");
	                bufProfile.append("   age = ?, ");                 // ★ birthday → age
	                bufProfile.append("   user_name_kana = ?, ");
	                bufProfile.append("   introduction = ?, ");
	                bufProfile.append("   profile_image_path = ? ");
	                bufProfile.append(" WHERE user_id = ? ");

	                psProfile = con.prepareStatement(bufProfile.toString());
	                psProfile.setInt(1, dto.getGender());
	                psProfile.setInt(2, dto.getAge());
	                psProfile.setString(3, dto.getKana());
	                psProfile.setString(4, dto.getIntroduction());
	                psProfile.setString(5, dto.getProfileImagePath());
	                psProfile.setInt(6, dto.getUserId());

	                updated = psProfile.executeUpdate();

	            } else {
	                StringBuffer bufProfile = new StringBuffer();
	                bufProfile.append(" UPDATE user_profiles SET ");
	                bufProfile.append("   gender = ?, ");
	                bufProfile.append("   age = ?, ");                 // ★ birthday → age
	                bufProfile.append("   user_name_kana = ?, ");
	                bufProfile.append("   introduction = ? ");
	                bufProfile.append(" WHERE user_id = ? ");

	                psProfile = con.prepareStatement(bufProfile.toString());
	                psProfile.setInt(1, dto.getGender());
	                psProfile.setInt(2, dto.getAge());
	                psProfile.setString(3, dto.getKana());
	                psProfile.setString(4, dto.getIntroduction());
	                psProfile.setInt(5, dto.getUserId());

	                updated = psProfile.executeUpdate();
	            }

	            // UPDATEが0件なら INSERT
	            if (updated == 0) {
	                if (psProfile != null) {
	                    psProfile.close();
	                    psProfile = null;
	                }

	                StringBuffer bufIns = new StringBuffer();
	                bufIns.append(" INSERT INTO user_profiles ( ");
	                bufIns.append("   user_id, gender, age, user_name_kana, introduction, profile_image_path ");
	                bufIns.append(" ) VALUES ( ");
	                bufIns.append("   ?, ?, ?, ?, ?, ? ");
	                bufIns.append(" ) ");

	                psProfile = con.prepareStatement(bufIns.toString());
	                psProfile.setInt(1, dto.getUserId());
	                psProfile.setInt(2, dto.getGender());
	                psProfile.setInt(3, dto.getAge());
	                psProfile.setString(4, dto.getKana());
	                psProfile.setString(5, dto.getIntroduction());
	                psProfile.setString(6, dto.getProfileImagePath());

	                psProfile.executeUpdate();
	            }
	        }

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
	            if (psProfile != null) psProfile.close();
	            if (psUser != null) psUser.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return result;
	}


	/**----------------------------------------------------------------------*
	 *■updateProfileメソッド
	 *概要  ：プロフィールを更新する（本人更新想定）
	 *引数  ：プロフィール情報（AccountDto）
	 *戻り値：処理結果（true:成功 / false:失敗）
	 *----------------------------------------------------------------------**/
	public boolean updateProfile(AccountDto dto) {

	    Connection con = null;
	    PreparedStatement psUser    = null;
	    PreparedStatement psProfile = null;

	    boolean result = true;

	    try {
	        Class.forName(DRIVER_NAME);

	        con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
	        con.setAutoCommit(false);

	        //-------------------------------------------
	        // users UPDATE（名前のみ）
	        //-------------------------------------------
	        StringBuffer bufUser = new StringBuffer();
	        bufUser.append(" UPDATE users  ");
	        bufUser.append(" SET  ");
	        bufUser.append("   user_name = ? ");
	        bufUser.append(" WHERE  ");
	        bufUser.append("   user_id = ? ");

	        psUser = con.prepareStatement(bufUser.toString());
	        psUser.setString(1, dto.getName());
	        psUser.setInt(2, dto.getUserId());
	        psUser.executeUpdate();

	        //-------------------------------------------
	        // user_profiles UPDATE（カナ含む）
	        //-------------------------------------------
	        int updated = 0;

	        // age 必須
	        if (dto.getAge() == null) {
	            throw new IllegalStateException("age が未設定です");
	        }

	        if (dto.getProfileImagePath() != null) {
	            StringBuffer bufProfile = new StringBuffer();
	            bufProfile.append(" UPDATE user_profiles SET ");
	            bufProfile.append("   gender = ?, ");
	            bufProfile.append("   age = ?, ");                 // ★ birthday → age
	            bufProfile.append("   user_name_kana = ?, ");
	            bufProfile.append("   introduction = ?, ");
	            bufProfile.append("   profile_image_path = ? ");
	            bufProfile.append(" WHERE user_id = ? ");

	            psProfile = con.prepareStatement(bufProfile.toString());
	            psProfile.setInt(1, dto.getGender());
	            psProfile.setInt(2, dto.getAge());
	            psProfile.setString(3, dto.getKana());
	            psProfile.setString(4, dto.getIntroduction());
	            psProfile.setString(5, dto.getProfileImagePath());
	            psProfile.setInt(6, dto.getUserId());

	            updated = psProfile.executeUpdate();

	        } else {
	            StringBuffer bufProfile = new StringBuffer();
	            bufProfile.append(" UPDATE user_profiles SET ");
	            bufProfile.append("   gender = ?, ");
	            bufProfile.append("   age = ?, ");                 // ★ birthday → age
	            bufProfile.append("   user_name_kana = ?, ");
	            bufProfile.append("   introduction = ? ");
	            bufProfile.append(" WHERE user_id = ? ");

	            psProfile = con.prepareStatement(bufProfile.toString());
	            psProfile.setInt(1, dto.getGender());
	            psProfile.setInt(2, dto.getAge());
	            psProfile.setString(3, dto.getKana());
	            psProfile.setString(4, dto.getIntroduction());
	            psProfile.setInt(5, dto.getUserId());

	            updated = psProfile.executeUpdate();
	        }

	        // UPDATEが0件なら INSERT
	        if (updated == 0) {
	            if (psProfile != null) {
	                psProfile.close();
	                psProfile = null;
	            }

	            StringBuffer bufIns = new StringBuffer();
	            bufIns.append(" INSERT INTO user_profiles ( ");
	            bufIns.append("   user_id, gender, age, user_name_kana, introduction, profile_image_path ");
	            bufIns.append(" ) VALUES ( ");
	            bufIns.append("   ?, ?, ?, ?, ?, ? ");
	            bufIns.append(" ) ");

	            psProfile = con.prepareStatement(bufIns.toString());
	            psProfile.setInt(1, dto.getUserId());
	            psProfile.setInt(2, dto.getGender());
	            psProfile.setInt(3, dto.getAge());
	            psProfile.setString(4, dto.getKana());
	            psProfile.setString(5, dto.getIntroduction());
	            psProfile.setString(6, dto.getProfileImagePath());

	            psProfile.executeUpdate();
	        }

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
	            if (psProfile != null) psProfile.close();
	            if (psUser != null) psUser.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return result;
	}

	/**----------------------------------------------------------------------*
	 *■selectEditAccountメソッド
	 *概要  ：編集対象アカウントを抽出する（1件）
	 *引数  ：uid（String）
	 *戻り値：アカウント情報（AccountDto型）
	 *----------------------------------------------------------------------**/
	public AccountDto selectEditAccount(String uid){

	    Connection con = null;
	    PreparedStatement ps  = null;
	    ResultSet rs  = null;

	    AccountDto dto = new AccountDto();

	    try {
	        Class.forName(DRIVER_NAME);
	        con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

	        StringBuffer buf = new StringBuffer();
	        buf.append(" SELECT                    ");
	        buf.append("   u.user_id,              ");
	        buf.append("   u.login_id,             ");
	        buf.append("   u.password,             ");
	        buf.append("   u.user_name,            ");
	        buf.append("   u.role,                 ");
	        buf.append("   u.email,                ");
	        buf.append("   u.status,               ");
	        buf.append("   p.user_name_kana,       ");
	        buf.append("   p.gender,               ");
	        buf.append("   p.age,                  "); // ★ birthday → age
	        buf.append("   p.introduction,         ");
	        buf.append("   p.profile_image_path    ");
	        buf.append(" FROM users u              ");
	        buf.append(" LEFT JOIN user_profiles p ");
	        buf.append("   ON u.user_id = p.user_id");
	        buf.append(" WHERE u.is_deleted = 0    ");
	        buf.append("   AND u.user_id = ?       ");

	        ps = con.prepareStatement(buf.toString());
	        ps.setString(1, uid);

	        rs = ps.executeQuery();

	        if (rs.next()) {
	            dto.setUserId(rs.getInt("user_id"));
	            dto.setLoginId(rs.getString("login_id"));
	            dto.setPassword(rs.getString("password"));
	            dto.setName(rs.getString("user_name"));
	            dto.setRole(rs.getInt("role"));
	            dto.setEmail(rs.getString("email"));
	            dto.setStatus(rs.getInt("status"));

	            dto.setKana(rs.getString("user_name_kana"));
	            dto.setGender(rs.getInt("gender"));

	            // ★ LEFT JOINで NULL を潰さない（管理者はprofile無しのことがある）
	            Integer ageObj = (Integer) rs.getObject("age");
	            dto.setAge(ageObj);

	            dto.setIntroduction(rs.getString("introduction"));
	            dto.setProfileImagePath(rs.getString("profile_image_path"));
	        }

	    } catch (Exception e){
	        e.printStackTrace();

	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return dto;
	}

	/**----------------------------------------------------------------------*
	 *■selectDeleteAccountListメソッド
	 *概要  ：アカウント削除一覧を抽出する
	 *引数  ：なし
	 *戻り値：アカウント削除一覧（List<AccountDto>型）
	 *
	 *【修正点】
	 * - user_name_kana は user_profiles から取得（LEFT JOIN）
	 *----------------------------------------------------------------------**/
	public List<AccountDto> selectDeleteAccountList(){

		Connection con = null;
		PreparedStatement ps  = null;
		ResultSet rs  = null;

		List<AccountDto> list = new ArrayList<>();

		try {
			Class.forName(DRIVER_NAME);
			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

			StringBuffer buf = new StringBuffer();
			buf.append(" SELECT                 ");
			buf.append("   u.user_id,           ");
			buf.append("   u.user_name,         ");
			buf.append("   p.user_name_kana,    ");
			buf.append("   u.role,              ");
			buf.append("   u.email,             ");
			buf.append("   u.status             ");
			buf.append(" FROM users u           ");
			buf.append(" LEFT JOIN user_profiles p ");
			buf.append("   ON u.user_id = p.user_id ");
			buf.append(" WHERE u.is_deleted = 1 ");
			buf.append(" ORDER BY u.user_id ASC ");

			ps = con.prepareStatement(buf.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				AccountDto dto = new AccountDto();
				dto.setUserId(rs.getInt("user_id"));
				dto.setName(rs.getString("user_name"));
				dto.setKana(rs.getString("user_name_kana"));
				dto.setRole(rs.getInt("role"));
				dto.setEmail(rs.getString("email"));
				dto.setStatus(rs.getInt("status"));
				list.add(dto);
			}

		} catch (Exception e){
			e.printStackTrace();

		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**----------------------------------------------------------------------*
	 *■managerSettingUpdateメソッド
	 *概要  ：管理者設定を更新する
	 *引数  ：AccountDto
	 *戻り値：処理結果（true:成功 / false:失敗）
	 *----------------------------------------------------------------------**/
	public boolean managerSettingUpdate(AccountDto dto) {

		Connection con = null;
		PreparedStatement psUser = null;

		boolean result = true;

		try {
			Class.forName(DRIVER_NAME);

			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
			con.setAutoCommit(false);

			StringBuffer bufUser = new StringBuffer();
			bufUser.append(" UPDATE users  ");
			bufUser.append(" SET  ");
			bufUser.append("   login_id = ?, ");
			bufUser.append("   password = ?, ");
			bufUser.append("   user_name = ?, ");
			bufUser.append("   email = ?  ");
			bufUser.append(" WHERE  ");
			bufUser.append("   user_id = ? ");

			psUser = con.prepareStatement(bufUser.toString());

			psUser.setString(1, dto.getLoginId());
			psUser.setString(2, dto.getPassword());
			psUser.setString(3, dto.getName());
			psUser.setString(4, dto.getEmail());
			psUser.setInt(5, dto.getUserId());

			psUser.executeUpdate();

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
				if (psUser != null) psUser.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**----------------------------------------------------------------------*
	 *■generalSettingUpdateメソッド
	 *概要  ：一般ユーザー設定（login/password/email）を更新する
	 *引数  ：AccountDto
	 *戻り値：処理結果（true:成功 / false:失敗）
	 *----------------------------------------------------------------------**/
	public boolean generalSettingUpdate(AccountDto dto) {

		Connection con = null;
		PreparedStatement psUser = null;

		boolean result = true;

		try {
			Class.forName(DRIVER_NAME);

			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
			con.setAutoCommit(false);

			StringBuffer bufUser = new StringBuffer();
			bufUser.append(" UPDATE users  ");
			bufUser.append(" SET  ");
			bufUser.append("   login_id = ?, ");
			bufUser.append("   password = ?, ");
			bufUser.append("   email = ?  ");
			bufUser.append(" WHERE  ");
			bufUser.append("   user_id = ? ");

			psUser = con.prepareStatement(bufUser.toString());

			psUser.setString(1, dto.getLoginId());
			psUser.setString(2, dto.getPassword());
			psUser.setString(3, dto.getEmail());
			psUser.setInt(4, dto.getUserId());

			psUser.executeUpdate();

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
				if (psUser != null) psUser.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**----------------------------------------------------------------------*
	 *■selectAccountListメソッド（ページネーション用）
	 *概要  ：アカウント一覧を指定件数・開始位置で抽出する（論理削除除外）
	 *引数  ：limit 取得件数 / offset 開始位置
	 *戻り値：アカウント一覧（List<AccountDto>型）
	 *
	 *【修正点】
	 * - user_name_kana は user_profiles から取得（LEFT JOIN）
	 *----------------------------------------------------------------------**/
	public List<AccountDto> selectAccountList(int limit, int offset){

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AccountDto> list = new ArrayList<>();

		try {
			Class.forName(DRIVER_NAME);
			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

			StringBuffer buf = new StringBuffer();
			buf.append(" SELECT                 ");
			buf.append("   u.user_id,           ");
			buf.append("   u.user_name,         ");
			buf.append("   p.user_name_kana,    ");
			buf.append("   u.role,              ");
			buf.append("   u.email,             ");
			buf.append("   u.status             ");
			buf.append(" FROM users u           ");
			buf.append(" LEFT JOIN user_profiles p ");
			buf.append("   ON u.user_id = p.user_id ");
			buf.append(" WHERE u.is_deleted = 0 ");
			buf.append(" ORDER BY u.user_id ASC ");
			buf.append(" LIMIT ? OFFSET ?       ");

			ps = con.prepareStatement(buf.toString());
			ps.setInt(1, limit);
			ps.setInt(2, offset);

			rs = ps.executeQuery();

			while (rs.next()) {
				AccountDto dto = new AccountDto();
				dto.setUserId(rs.getInt("user_id"));
				dto.setName(rs.getString("user_name"));
				dto.setKana(rs.getString("user_name_kana"));
				dto.setRole(rs.getInt("role"));
				dto.setEmail(rs.getString("email"));
				dto.setStatus(rs.getInt("status"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**----------------------------------------------------------------------*
	 *■countAccountメソッド
	 *概要  ：アカウント総件数を取得する（論理削除除外）
	 *----------------------------------------------------------------------**/
	public int countAccount() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int count = 0;

		try {
			Class.forName(DRIVER_NAME);
			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

			String sql = "SELECT COUNT(*) FROM users WHERE is_deleted = 0";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	/**----------------------------------------------------------------------*
	 *■publicSelectAccountListメソッド（ページネーション用）
	 *概要  ：公開ページ用アカウント一覧を指定件数・開始位置で抽出する
	 *引数  ：limit 取得件数 / offset 開始位置
	 *戻り値：公開用一覧（List<PublicAccountListDto>型）
	 *----------------------------------------------------------------------**/
	public List<PublicAccountListDto> publicSelectAccountList(int limit, int offset){

	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    List<PublicAccountListDto> list = new ArrayList<>();

	    try {
	        Class.forName(DRIVER_NAME);
	        con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

	        StringBuffer buf = new StringBuffer();
	        buf.append(" SELECT ");
	        buf.append("   u.user_id, ");
	        buf.append("   up.profile_image_path, ");
	        buf.append("   COALESCE(gs.like_count, 0) AS like_count ");
	        buf.append(" FROM users u ");
	        buf.append(" INNER JOIN user_profiles up ");
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
	        buf.append(" ORDER BY u.user_id ASC ");
	        buf.append(" LIMIT ? OFFSET ? ");

	        java.time.LocalDate today = java.time.LocalDate.now();
	        java.time.LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
	        java.time.LocalDate firstDayOfNextMonth = firstDayOfThisMonth.plusMonths(1);

	        Timestamp start = Timestamp.valueOf(firstDayOfThisMonth.atStartOfDay());
	        Timestamp end   = Timestamp.valueOf(firstDayOfNextMonth.atStartOfDay());

	        ps = con.prepareStatement(buf.toString());
	        ps.setTimestamp(1, start);
	        ps.setTimestamp(2, end);
	        ps.setInt(3, limit);
	        ps.setInt(4, offset);

	        rs = ps.executeQuery();

	        while (rs.next()) {
	            PublicAccountListDto dto = new PublicAccountListDto();
	            dto.setUserId(rs.getInt("user_id"));
	            dto.setProfileImagePath(rs.getString("profile_image_path"));
	            dto.setLikeCount(rs.getInt("like_count"));
	            list.add(dto);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return list;
	}

	/**----------------------------------------------------------------------*
	 *■publicCountAccountメソッド
	 *概要  ：公開ページ用アカウント総件数を取得する（論理削除除外）
	 *----------------------------------------------------------------------**/
	public int publicCountAccount() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int count = 0;

		try {
			Class.forName(DRIVER_NAME);
			con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

			String sql = "SELECT COUNT(*) FROM users WHERE is_deleted = 0 AND role = 0";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	/**----------------------------------------------------------------------*
	 *■publicSelectAccountDetailメソッド
	 *概要  ：公開ページ用 詳細取得
	 *----------------------------------------------------------------------**/
	public PublicAccountDetailDto publicSelectAccountDetail(int userId) {

	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    PublicAccountDetailDto dto = null;

	    try {
	        Class.forName(DRIVER_NAME);
	        con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);

	        // 今月の範囲
	        java.time.LocalDate today = java.time.LocalDate.now();
	        java.time.LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
	        java.time.LocalDate firstDayOfNextMonth = firstDayOfThisMonth.plusMonths(1);
	        Timestamp start = Timestamp.valueOf(firstDayOfThisMonth.atStartOfDay());
	        Timestamp end   = Timestamp.valueOf(firstDayOfNextMonth.atStartOfDay());

	        StringBuffer buf = new StringBuffer();
	        buf.append(" SELECT ");
	        buf.append("   u.user_id, ");
	        buf.append("   u.user_name, ");
	        buf.append("   up.user_name_kana, ");
	        buf.append("   up.gender, ");
	        buf.append("   up.age, ");                // ★ birthday → age
	        buf.append("   up.introduction, ");
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
	        buf.append(" WHERE u.user_id = ? ");
	        buf.append("   AND u.is_deleted = 0 ");
	        buf.append("   AND u.role = 0 ");

	        ps = con.prepareStatement(buf.toString());
	        ps.setTimestamp(1, start);
	        ps.setTimestamp(2, end);
	        ps.setInt(3, userId);

	        rs = ps.executeQuery();

	        if (rs.next()) {
	            dto = new PublicAccountDetailDto();
	            dto.setUserId(rs.getInt("user_id"));
	            dto.setUserName(rs.getString("user_name"));
	            dto.setUserNameKana(rs.getString("user_name_kana"));
	            dto.setGender(rs.getInt("gender"));

	            Integer ageObj = (Integer) rs.getObject("age");
	            dto.setAge(ageObj);

	            dto.setIntroduction(rs.getString("introduction"));
	            dto.setProfileImagePath(rs.getString("profile_image_path"));
	            dto.setMonthlyLikeCount(rs.getInt("like_count"));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (con != null) con.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return dto;
	}
}
