package controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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

import model.AccountCRUDDto;
import model.AccountEditBL;
import model.UserInfoDto;

@MultipartConfig
public class ExecuteAccountEdit extends HttpServlet {

    /**-----------------------------------------------
     * ■■■ExecuteAccountEditクラス■■■
     * 概要：アカウント編集処理
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

        // 管理者のみ許可
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

        // ===== 共通パラメータ =====
        String role     = request.getParameter("role");
        String userId     = request.getParameter("userId");
        String name     = request.getParameter("name");
        String kana     = request.getParameter("kana");
        String email    = request.getParameter("email");
        String status    = request.getParameter("status");

        // ===== general 用（後で使うので先に定義）=====
        String gender = null;
        String introduction = null;
        LocalDate parsedBirthday = null;
        String profileImagePath = null;

        // ===== role チェック =====
        if (role == null) {
            errors.put("role", "権限が選択されていません。");
        }

        // ===== 共通バリデーション =====

        if (name == null || name.isEmpty()) {
            errors.put("name", "名前は必須です。");
        } else if (name.length() > 255) {
            errors.put("name", "名前は255文字以内で入力してください。");
        }

        if (kana == null || kana.isEmpty()) {
            errors.put("kana", "ふりがなは必須です。");
        } else if (!kana.matches("^[ぁ-んー 　]+$")) {
            errors.put("kana", "ふりがなはひらがなのみで入力してください。");
        }

        if (email == null || email.isEmpty()) {
            errors.put("email", "メールアドレスは必須です。");
        } else if (email.length() > 255) {
            errors.put("email", "メールアドレスは255文字以内で入力してください。");
        } else if (!email.matches(".+@.+\\..+")) {
            errors.put("email", "メールアドレスの形式が正しくありません。");
        }
        //-----------------------------------------------------------------------------------
        if (!"1".equals(status) && !"0".equals(status)) {
            errors.put("status", "ステータスが不正です。");
        }

        // ===== 一般ユーザー専用 =====
        if ("0".equals(role)) {

            gender = request.getParameter("gender");
            String birthdayStr = request.getParameter("birthday");
            introduction = request.getParameter("introduction");

            // 性別（1 / 2 / 3）
            if (!"1".equals(gender) && !"2".equals(gender) && !"3".equals(gender)) {
                errors.put("gender", "性別が不正です。");
            }

            // 生年月日
            if (birthdayStr == null || birthdayStr.isEmpty()) {
                errors.put("birthday", "生年月日は必須です。");
            } else {
                try {
                    parsedBirthday = LocalDate.parse(birthdayStr);
                    if (parsedBirthday.isAfter(LocalDate.now())) {
                        errors.put("birthday", "生年月日は未来の日付にできません。");
                    }
                } catch (Exception e) {
                    errors.put("birthday", "生年月日の形式が正しくありません。");
                }
            }

            // 自己紹介
            if (introduction != null && introduction.length() > 1500) {
                errors.put("introduction", "自己紹介は1500文字以内で入力してください。");
            }

        } else if (!"1".equals(role)) {
            // 想定外（改ざん対策）
            errors.put("role", "不正な権限が指定されました。");
        }

        // ===== エラーがあれば戻す =====
        if (!errors.isEmpty()) {
            forwardToForm(request, response, errors);
            return;
        }

        // ===== ここまで来たら入力はOK =====

        // ===== プロフィール画像の受け取り・保存 =====

        Part imagePart = request.getPart("profileImage");   // input name="profileImage"

        if (imagePart != null && imagePart.getSize() > 0) {

            // ===== 2MB サイズチェック =====
            long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

            if (imagePart.getSize() > MAX_FILE_SIZE) {
                errors.put("profileImage", "プロフィール画像は2MB以内でアップロードしてください。");
                forwardToForm(request, response, errors);
                return;
            }

            String fileName = imagePart.getSubmittedFileName();

            // 保存先ディレクトリ
            String uploadDir =
                    getServletContext().getRealPath("/img/profile");

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 実際の保存パス
            String savePath = uploadDir + File.separator + fileName;
            imagePart.write(savePath);

            // DB に保存するパス
            profileImagePath = "/img/profile/" + fileName;
        }


        AccountCRUDDto dto = new AccountCRUDDto();

        dto.setRole(role);
        dto.setUserId(userId);
        dto.setName(name);
        dto.setKana(kana);
        dto.setEmail(email);
        dto.setStatus(status);

        if ("0".equals(role)) {
            dto.setGender(gender);
            dto.setBirthday(parsedBirthday);
            dto.setIntroduction(introduction);
            //画像を受け取る
            if (profileImagePath != null) {
                dto.setProfileImagePath(profileImagePath);
            }
        }

        AccountEditBL logic = new AccountEditBL();

        try {
            boolean result = logic.accountEdit(dto);

            if (result) {
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/accountEditComplete.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("errorMessage", "登録処理に失敗しました。");
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/accountEdit.jsp");
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
                request.getRequestDispatcher("/WEB-INF/view/accountEdit.jsp");
        rd.forward(request, response);
    }
}
