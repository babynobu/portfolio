package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■PublicAccountListBLクラス■■■
 *概要：ビジネスロジック（アカウントリストの抽出）
 *----------------------------------------------------------------------**/

public class PublicAccountListBL {

    /**----------------------------------------------------
     * ■selectAccountListメソッド（ページネーション用）
     * 概要：指定件数・開始位置で取得
     * 引数：limit  取得件数
     *       offset 開始位置
     -----------------------------------------------------*/
    public List<PublicAccountListDto> selectAccountList(int limit, int offset){

        List<PublicAccountListDto> list = new ArrayList<>();

        AccountDao dao = new AccountDao();
        list = dao.publicSelectAccountList(limit, offset);

        return list;
    }

    /**----------------------------------------------------
     * ■countAccountメソッド
     * 概要：アカウント総件数を取得
     -----------------------------------------------------*/
    public int countAccount() {

        AccountDao dao = new AccountDao();
        return dao.publicCountAccount();
    }

}
