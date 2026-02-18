package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginBL;
import model.UserInfoDto;

public class ExecuteLogin extends HttpServlet {

    /**-----------------------------------------------
     * ■■■ExecuteLoginクラス■■■
     * 概要；サーブレット
     * 詳細：ログイン結果から出力先を判別
     ------------------------------------------------*/

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // セッション情報を取得（※ session が null の可能性があるので安全に）
        HttpSession session = request.getSession(false);
        UserInfoDto userInfoOnSession =
                (session == null) ? null : (UserInfoDto) session.getAttribute("LOGIN_INFO");

        // セッション情報によって、転送する画面を分ける
        if (userInfoOnSession == null) {
            response.sendRedirect(request.getContextPath() + "/LogIn"); // 未ログイン
        } else {
            // role: 0 = 一般, 1 = 管理者
            if (userInfoOnSession.getRole() == 0) {
                response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 既存セッションがあれば破棄（ユーザー切替対策）
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // レスポンス（出力データ）の文字コードを設定
        response.setContentType("text/html;charset=UTF-8");
        // リクエスト（受信データ）の文字コードを設定
        request.setCharacterEncoding("UTF-8");

        // リクエストパラメータからユーザー入力値を取得
        String loginId = request.getParameter("LOGIN_ID");
        String userPass = request.getParameter("PASSWORD");

        // ==========================
        // サーバー側バリデーション（JSと完全一致させる）
        // ==========================
        Map<String, String> errors = new HashMap<>();

        // loginId: 必須 + 255文字以内
        if (loginId == null || loginId.isEmpty()) {
            errors.put("loginId", "ユーザーIDが未入力です。");
        } else if (loginId.length() > 255) {
            errors.put("loginId", "ユーザーIDの文字数制限を超えています。");
        }

        // password: 必須 + 8～32 半角英数字と _-
        if (userPass == null || userPass.isEmpty()) {
            errors.put("password", "パスワードが未入力です。");
        } else if (!userPass.matches("^[0-9a-zA-Z_-]{8,32}$")) {
            errors.put("password", "パスワードは8～32文字の半角英数字と_-で入力してください。");
        }

        // バリデーションNG → login.jspへフォワード（エラーと入力値を渡す）
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("LOGIN_ID_VALUE", loginId); // 入力保持
            RequestDispatcher dispatch = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
            dispatch.forward(request, response);
            return;
        }

        // DBに接続して合致するデータを出力
        LoginBL logic = new LoginBL();
        UserInfoDto dto = logic.userInfoExtracted(loginId, userPass);

        // ログイン成功
        if (dto != null && dto.getLoginId() != null) {

            HttpSession session = request.getSession(true);
            session.setAttribute("LOGIN_INFO", dto);

            // role: 0 = 一般, 1 = 管理者
            if (dto.getRole() == 0) {
                response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
            }

        } else {
            // ログイン失敗（存在しない / パスワード違い / 無効ステータス 等を区別しない）
            errors.put("global", "ログインに失敗しました。入力内容をご確認ください。");
            request.setAttribute("errors", errors);
            request.setAttribute("LOGIN_ID_VALUE", loginId);
            RequestDispatcher dispatch = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
            dispatch.forward(request, response);
        }

    }
}
