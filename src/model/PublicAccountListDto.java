package model;

/**---------------------------------------------
 * ■■■PublicAccountListDtoクラス■■■
 * 概要：公開プロフィール一覧表示用DTO
 --------------------------------------------**/
public class PublicAccountListDto {

    private int userId;
    private String profileImagePath;
    private int likeCount;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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

}
