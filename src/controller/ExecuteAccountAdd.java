package controller;

import java.io.File;
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
import javax.servlet.http.Part;

import model.AccountAddBL;
import model.AccountDao;
import model.AccountDto;
import model.UserInfoDto;

@MultipartConfig
public class ExecuteAccountAdd extends HttpServlet {

    /**-----------------------------------------------
     * ■■■ExecuteAccountAddクラス■■■
     * 概要：アカウント追加処理
     -----------------------------------------------*/

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

        response.sendRedirect(request.getContextPath() + "/AccountAdd");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> errors = new HashMap<>();

        // 1) パラメータ取得
        String roleStr   = request.getParameter("role");     // "admin" or "general"
        String loginId   = request.getParameter("loginId");
        String password  = request.getParameter("password");
        String name      = request.getParameter("name");
        String kana      = request.getParameter("kana");     // 一般のみ想定
        String email     = request.getParameter("email");
        String statusStr = request.getParameter("status");   // "1" or "0"

        // general 用
        int gender = 0;
        Integer age = null;                 // ★追加
        String introduction = null;
        String profileImagePath = null;

        // 2) roleチェック + DB用 intへ変換
        int role = -1;
        if (roleStr == null || roleStr.isEmpty()) {
            errors.put("role", "権限が選択されていません。");
        } else if ("admin".equals(roleStr)) {
            role = 1;
        } else if ("general".equals(roleStr)) {
            role = 0;
        } else {
            errors.put("role", "不正な権限が指定されました。");
        }

        // 3) status parse
        int status = -1;
        if (statusStr == null || statusStr.isEmpty()) {
            errors.put("status", "ステータスが選択されていません。");
        } else {
            try {
                status = Integer.parseInt(statusStr);
                if (status != 1 && status != 0) {
                    errors.put("status", "ステータスが不正です。");
                }
            } catch (Exception e) {
                errors.put("status", "ステータスが不正です。");
            }
        }

        // 4) 共通バリデーション
        if (loginId == null || loginId.isEmpty()) {
            errors.put("loginId", "ログインIDは必須です。");
        } else if (loginId.length() > 255) {
            errors.put("loginId", "ログインIDは255文字以内で入力してください。");
        } else {
            AccountDao dao = new AccountDao();
            if (dao.existsLoginId(loginId)) {
                errors.put("loginId", "このログインIDはすでに使用されています。");
            }
        }

        if (password == null || password.isEmpty()) {
            errors.put("password", "パスワードは必須です。");
        } else if (!password.matches("^[a-zA-Z0-9_-]{8,32}$")) {
            errors.put("password", "パスワードは8〜32文字の半角英数字と「_」「-」のみ使用できます。");
        }

        if (name == null || name.isEmpty()) {
            errors.put("name", "名前は必須です。");
        } else if (name.length() > 255) {
            errors.put("name", "名前は255文字以内で入力してください。");
        }

        if (email == null || email.isEmpty()) {
            errors.put("email", "メールアドレスは必須です。");
        } else if (email.length() > 255) {
            errors.put("email", "メールアドレスは255文字以内で入力してください。");
        } else if (!email.matches(".+@.+\\..+")) {
            errors.put("email", "メールアドレスの形式が正しくありません。");
        }

        // 5) 一般ユーザー専用バリデーション
        if ("general".equals(roleStr)) {

            if (kana == null || kana.isEmpty()) {
                errors.put("kana", "ふりがなは必須です。");
            } else if (!kana.matches("^[ぁ-んー 　]+$")) {
                errors.put("kana", "ふりがなはひらがなのみで入力してください。");
            }

            String genderStr = request.getParameter("gender");
            if (genderStr == null || genderStr.isEmpty()) {
                errors.put("gender", "性別が選択されていません。");
            } else {
                try {
                    gender = Integer.parseInt(genderStr);
                    if (gender != 1 && gender != 2 && gender != 3) {
                        errors.put("gender", "性別が不正です。");
                    }
                } catch (Exception e) {
                    errors.put("gender", "性別が不正です。");
                }
            }

            // ★ 年齢（固定）
            String ageStr = request.getParameter("age");
            if (ageStr == null || ageStr.isEmpty()) {
                errors.put("age", "年齢は必須です。");
            } else {
                try {
                    int a = Integer.parseInt(ageStr);
                    if (a < 0 || a > 999) {
                        errors.put("age", "年齢は0〜999の範囲で入力してください。");
                    } else {
                        age = a;
                    }
                } catch (Exception e) {
                    errors.put("age", "年齢は数値で入力してください。");
                }
            }

            introduction = request.getParameter("introduction");
            if (introduction != null && introduction.length() > 1500) {
                errors.put("introduction", "自己紹介は1500文字以内で入力してください。");
            }
        }

        // 6) エラーがあれば戻す
        if (!errors.isEmpty()) {
            forwardToForm(request, response, errors);
            return;
        }

        // 7) プロフィール画像（generalの時だけ）
        if ("general".equals(roleStr)) {

            Part imagePart = request.getPart("profileImage");

            if (imagePart != null && imagePart.getSize() > 0) {

                long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
                if (imagePart.getSize() > MAX_FILE_SIZE) {
                    errors.put("profileImage", "プロフィール画像は2MB以内でアップロードしてください。");
                    forwardToForm(request, response, errors);
                    return;
                }

                String fileName = imagePart.getSubmittedFileName();
                String uploadDir = getServletContext().getRealPath("/img/profile");

                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String savePath = uploadDir + File.separator + fileName;
                imagePart.write(savePath);

                profileImagePath = "/img/profile/" + fileName;
            }
        }

        // 8) DTOへ詰める
        AccountDto dto = new AccountDto();

        dto.setRole(role);
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setName(name);
        dto.setEmail(email);
        dto.setStatus(status);

        if ("general".equals(roleStr)) {
            dto.setKana(kana);
            dto.setGender(gender);
            dto.setAge(age); // ★追加
            dto.setIntroduction(introduction);
            dto.setProfileImagePath(profileImagePath);
        }

        // 9) 登録処理
        AccountAddBL logic = new AccountAddBL();

        try {
            boolean result = logic.accountAdd(dto);

            if (result) {
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/accountAddComplete.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("errorMessage", "登録処理に失敗しました。");
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/accountAdd.jsp");
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
        RequestDispatcher rd =
                request.getRequestDispatcher("/WEB-INF/view/accountAdd.jsp");
        rd.forward(request, response);
    }
}
