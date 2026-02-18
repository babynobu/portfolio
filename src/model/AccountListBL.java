package model;

import java.util.ArrayList;
import java.util.List;

/**----------------------------------------------------------------------*
 *■■■AccountListBLクラス■■■
 *概要：ビジネスロジック（アカウントリストの抽出）
 *----------------------------------------------------------------------**/

public class AccountListBL {

    /**----------------------------------------------------
     * ■selectAccountListメソッド（ページネーション用）
     * 概要：指定件数・開始位置で取得
     * 引数：limit  取得件数
     *       offset 開始位置
     -----------------------------------------------------*/
    public List<AccountDto> selectAccountList(int limit, int offset){

        List<AccountDto> list = new ArrayList<>();

        AccountDao dao = new AccountDao();
        list = dao.selectAccountList(limit, offset);

        return list;
    }

    /**----------------------------------------------------
     * ■countAccountメソッド
     * 概要：アカウント総件数を取得
     -----------------------------------------------------*/
    public int countAccount() {

        AccountDao dao = new AccountDao();
        return dao.countAccount();
    }

}
