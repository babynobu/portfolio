package model;

/**-----------------------------------------------
 * ■■■PublicContactUsDtoクラス■■■
 * 概要：公開お問い合わせDTO
 -----------------------------------------------*/
public class PublicContactUsDto {

    private int categoryId;
    private String body;
    private String email;

    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
