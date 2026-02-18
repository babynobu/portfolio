package model;

/**---------------------------------------------
 * ■■■AccountDtoクラス■■■
 * 概要：DTO
 --------------------------------------------**/
public class AccountDto {

    /*-----------------------
     * フィールド
     -----------------------*/

    // ===== 共通 =====
    private int role;
    private int userId;
    private String loginId;
    private String password;
    private String name;
    private String kana;
    private String email;
    private int status;            // 1 / 0

    // ===== 一般ユーザー用 =====
    private int gender;            // 1 / 2 / 3
    private Integer age;           // ★ birthday廃止 → 年齢（固定）
    private String introduction;
    private String profileImagePath;

    // ===== その他 =====
    private int likeCount;

    /*-----------------------
     * getter/setter
     -----------------------*/

    public int getRole() {
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKana() {
        return kana;
    }
    public void setKana(String kana) {
        this.kana = kana;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }

    // ★ age
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getProfileImagePath() {
        return profileImagePath;
    }
    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    //表示用メソッド
    public String getRoleLabel() {
        return (role == 1) ? "管理者" : "一般";
    }
    public String getStatusLabel() {
        return (status == 1) ? "有効" : "無効";
    }
}
