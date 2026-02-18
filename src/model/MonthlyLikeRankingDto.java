package model;

public class MonthlyLikeRankingDto {

    private int rank;                 // 通し順位
    private int userId;
    private String userName;
    private String profileImagePath;
    private int likeCount;            // 今月いいね数

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
}
