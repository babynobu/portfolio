package model;

import java.time.LocalDate;

public class PublicAccountDetailDto {

    private int userId;
    private String userName;
    private String userNameKana;
    private int gender; // 1=男, 2=女 (あなたの仕様)
    private LocalDate birthday; // 年齢算出用
    private String introduction;
    private String profileImagePath;
    private int monthlyLikeCount; // 今月いいね数表示用

    private Integer age; // 算出結果（誕生日nullならnull）

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserNameKana() { return userNameKana; }
    public void setUserNameKana(String userNameKana) { this.userNameKana = userNameKana; }

    public int getGender() { return gender; }
    public void setGender(int gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

    public int getMonthlyLikeCount() { return monthlyLikeCount; }
    public void setMonthlyLikeCount(int monthlyLikeCount) { this.monthlyLikeCount = monthlyLikeCount; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
