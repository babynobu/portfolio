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
import model.GeneralSettingUpdateBL;
import model.UserInfoDto;

@MultipartConfig
public class ExecuteGeneralSetting extends HttpServlet {

    /**-----------------------------------------------
     * ■■■ExecuteGeneralSettingクラス■■■
     * 概要：プロフィール編集処理
     -----------------------------------------------*/

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // セッションチェック
        HttpSession session = request.getSession(false);
        UserInfoDto loginUser =
                (session != null) ? (UserInfoDto) session.getAttribute("LOGIN_INFO") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/LogIn");
            return;
        }

        // 一般のみ許可
        if (loginUser.getRole() != 0) {
            response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/GeneralSetting");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> errors = new HashMap<>();

        // ===== パラメータ =====
        int userId     = Integer.parseInt(request.getParameter("userId"));
        String loginId     = request.getParameter("loginId");
        String password     = request.getParameter("password");
        String email    = request.getParameter("email");


        // ===== バリデーション =====
        String strUserId = String.valueOf(userId);
        if (strUserId == null || strUserId.isEmpty()) {
            throw new IllegalStateException("userId が未設定です");
        }

        if (loginId == null || loginId.isEmpty()) {
			errors.put("loginId", "ログインIDは必須です。");
		} else if (loginId.length() > 255) {
			errors.put("loginId", "ログインIDは255文字以内で入力してください。");
		} else {
			AccountDao dao = new AccountDao();
			if (dao.existsLoginIdExceptSelf(loginId, userId)) {
		        errors.put("loginId", "このログインIDはすでに使用されています。");
		    }
		}

        if (password == null || password.isEmpty()) {
            errors.put("password", "パスワードは必須です。");
        } else if (!password.matches("^[a-zA-Z0-9_-]{8,32}$")) {
            errors.put("password", "パスワードは8〜32文字の半角英数字と「_」「-」のみ使用できます。");
        }

        if (email == null || email.isEmpty()) {
            errors.put("email", "メールアドレスは必須です。");
        } else if (email.length() > 255) {
            errors.put("email", "メールアドレスは255文字以内で入力してください。");
        } else if (!email.matches(".+@.+\\..+")) {
            errors.put("email", "メールアドレスの形式が正しくありません。");
        }


        // ===== エラーがあれば戻す =====
        if (!errors.isEmpty()) {
            forwardToForm(request, response, errors);
            return;
        }

        // ===== ここまで来たら入力はOK =====



        AccountDto dto = new AccountDto();

        dto.setUserId(userId);
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setEmail(email);

        GeneralSettingUpdateBL logic = new GeneralSettingUpdateBL();

        try {
            boolean result = logic.generalSettingUpdate(dto);

            if (result) {
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/generalSettingComplete.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("errorMessage", "登録処理に失敗しました。");
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/generalSetting.jsp");
                rd.forward(request, response);
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
            request.setAttribute("generalSetting", dto);
        }
        RequestDispatcher rd =
                request.getRequestDispatcher("/WEB-INF/view/generalSetting.jsp");
        rd.forward(request, response);
    }
}
