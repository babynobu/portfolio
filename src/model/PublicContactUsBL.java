package model;

/**-----------------------------------------------
 * ■■■PublicContactUsBLクラス■■■
 * 概要：公開お問い合わせ 登録ビジネスロジック
 -----------------------------------------------*/
public class PublicContactUsBL {

    public boolean insert(PublicContactUsDto dto) throws Exception {

        PublicContactUsDao dao = new PublicContactUsDao();
        return dao.insertContactUs(dto);
    }
}
