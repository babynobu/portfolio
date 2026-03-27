package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.AccountDao;
import model.AccountDto;
import model.ManagerSettingUpdateBL;
import model.UserInfoDto;

@MultipartConfig
public class ExecuteManagerSetting extends HttpServlet {

    /**-----------------------------------------------
     * ■■■ExecuteManagerSettingクラス■■■
     * 概要：プロフィール編集処理
     -----------------------------------------------*/

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserInfoDto loginUser =
                (session != null) ? (UserInfoDto) session.getAttribute("LOGIN_INFO") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/LogIn");
            return;
        }

        if (loginUser.getRole() != 1) {
            response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/ManagerSetting");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 0) ログイン・権限チェック
        HttpSession session = request.getSession(false);
        UserInfoDto loginUser =
                (session != null) ? (UserInfoDto) session.getAttribute("LOGIN_INFO") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/LogIn");
            return;
        }

        if (loginUser.getRole() != 1) {
            response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
            return;
        }

        Map<String, String> errors = new HashMap<>();

        // 1) パラメータ取得
        String userIdParam = request.getParameter("userId");
        String loginId     = request.getParameter("loginId");
        String password    = request.getParameter("password");
        String name        = request.getParameter("name");
        String email       = request.getParameter("email");

        // 2) userId parse
        int userId = -1;
        if (userIdParam == null || userIdParam.trim().isEmpty()) {
            errors.put("userId", "ユーザーIDが取得できません。");
        } else {
            try {
                userId = Integer.parseInt(userIdParam);
                if (userId <= 0) {
                    errors.put("userId", "ユーザーIDが不正です。");
                }
            } catch (Exception e) {
                errors.put("userId", "ユーザーIDが不正です。");
            }
        }

        // 3) バリデーション
        if (loginId == null || loginId.trim().isEmpty()) {
            errors.put("loginId", "ログインIDは必須です。");
        } else if (loginId.length() > 255) {
            errors.put("loginId", "ログインIDは255文字以内で入力してください。");
        } else if (userId > 0) {
            AccountDao dao = new AccountDao();
            if (dao.existsLoginIdExceptSelf(loginId, userId)) {
                errors.put("loginId", "このログインIDはすでに使用されています。");
            }
        }

        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "パスワードは必須です。");
        } else if (!password.matches("^[a-zA-Z0-9_-]{8,32}$")) {
            errors.put("password", "パスワードは8〜32文字の半角英数字と「_」「-」のみ使用できます。");
        }

        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "名前は必須です。");
        } else if (name.length() > 255) {
            errors.put("name", "名前は255文字以内で入力してください。");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "メールアドレスは必須です。");
        } else if (email.length() > 255) {
            errors.put("email", "メールアドレスは255文字以内で入力してください。");
        } else if (!email.matches(".+@.+\\..+")) {
            errors.put("email", "メールアドレスの形式が正しくありません。");
        }

        // 4) エラーがあれば戻す
        if (!errors.isEmpty()) {
            forwardToForm(request, response, errors);
            return;
        }

        // 5) DTOへ詰める
        AccountDto dto = new AccountDto();
        dto.setUserId(userId);
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setName(name);
        dto.setEmail(email);

        // 6) 更新処理
        ManagerSettingUpdateBL logic = new ManagerSettingUpdateBL();

        try {
            boolean result = logic.managerSettingUpdate(dto);

            if (result) {
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/managerSettingComplete.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("errorMessage", "登録処理に失敗しました。");
                forwardToForm(request, response, errors);
            }

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("errorMessage", "システムエラーが発生しました。");
            RequestDispatcher rd =
                    request.getRequestDispatcher("/WEB-INF/view/systemError.jsp");
            rd.forward(request, response);
        }
    }

    private void forwardToForm(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String, String> errors)
            throws ServletException, IOException {

        request.setAttribute("errors", errors);

        HttpSession session = request.getSession(false);
        UserInfoDto loginUser =
                (session != null) ? (UserInfoDto) session.getAttribute("LOGIN_INFO") : null;

        if (loginUser != null) {
            AccountDao dao = new AccountDao();
            AccountDto dto = dao.selectEditAccount(String.valueOf(loginUser.getUserId()));
            request.setAttribute("managerSetting", dto);
        }

        RequestDispatcher rd =
                request.getRequestDispatcher("/WEB-INF/view/managerSetting.jsp");
        rd.forward(request, response);
    }
}