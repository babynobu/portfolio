package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CategoryDao;
import model.CategoryDto;
import model.PublicContactUsBL;
import model.PublicContactUsDto;
import util.MailUtil;

/**-----------------------------------------------
 * ■■■ExecutePublicContactUsクラス■■■
 * 概要：公開お問い合わせ 送信処理（バリデーション→登録→メール送信）
 -----------------------------------------------*/
public class ExecutePublicContactUs extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/PublicContactUs");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> errors = new HashMap<>();

        // 1) パラメータ取得
        String email = request.getParameter("email");
        String categoryIdStr = request.getParameter("categoryId");
        String body = request.getParameter("body");

        // 2) 再表示用の入力値を先にセット
        request.setAttribute("inputEmail", email);
        request.setAttribute("inputBody", body);

        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            try {
                categoryId = Integer.valueOf(categoryIdStr);
            } catch (Exception e) {
                // 不正値は後続バリデーションでエラーにする
            }
        }
        request.setAttribute("inputCategoryId", categoryId);

        // 3) バリデーション
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "返信用メールアドレスは必須です。");
        } else if (email.length() > 255) {
            errors.put("email", "返信用メールアドレスは255文字以内で入力してください。");
        } else if (!email.matches(".+@.+\\..+")) {
            errors.put("email", "返信用メールアドレスの形式が正しくありません。");
        }

        if (categoryId == null) {
            errors.put("categoryId", "問い合わせカテゴリーを選択してください。");
        }

        if (body == null || body.trim().isEmpty()) {
            errors.put("body", "本文は必須です。");
        } else if (body.length() > 1000) {
            errors.put("body", "本文は1000文字以内で入力してください。");
        }

        // 4) エラーがあれば戻す
        if (!errors.isEmpty()) {
            forwardToForm(request, response, errors);
            return;
        }

        // 5) 登録DTO
        PublicContactUsDto dto = new PublicContactUsDto();
        dto.setEmail(email);
        dto.setCategoryId(categoryId);
        dto.setBody(body);

        PublicContactUsBL logic = new PublicContactUsBL();

        try {
            boolean result = logic.insert(dto);

            if (result) {
                String categoryName = resolveCategoryName(categoryId);
                String subject = "【受付完了】お問い合わせを受け付けました";
                String mailBody = buildReceiptMailText(email, categoryName, body);

                try {
                    MailUtil.sendContactReceipt(email, subject, mailBody);

                    RequestDispatcher rd =
                            request.getRequestDispatcher("/WEB-INF/view/publicContactUsComplete.jsp");
                    rd.forward(request, response);

                } catch (Exception mailEx) {
                    mailEx.printStackTrace();

                    request.setAttribute("mailErrorMessage",
                            "お問い合わせは受け付けましたが、確認メールの送信に失敗しました。"
                          + "入力したメールアドレスをご確認のうえ、必要であれば再度お問い合わせください。");

                    RequestDispatcher rd =
                            request.getRequestDispatcher("/WEB-INF/view/publicContactUsMailFailed.jsp");
                    rd.forward(request, response);
                }

            } else {
                request.setAttribute("errorMessage", "送信処理に失敗しました。");
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

        CategoryDao categoryDao = new CategoryDao();
        List<CategoryDto> categories = categoryDao.selectActiveCategoryList();
        request.setAttribute("CATEGORIES", categories);

        request.setAttribute("errors", errors);

        RequestDispatcher rd =
                request.getRequestDispatcher("/WEB-INF/view/publicContactUs.jsp");
        rd.forward(request, response);
    }

    /** カテゴリーIDから名称を引く（メール本文用） */
    private String resolveCategoryName(Integer categoryId) {
        try {
            CategoryDao dao = new CategoryDao();
            List<CategoryDto> categories = dao.selectActiveCategoryList();
            for (CategoryDto c : categories) {
                if (c.getCategoryId() == categoryId) {
                    return c.getCategoryName();
                }
            }
        } catch (Exception e) {
            // 失敗してもメールは送れるようにする
        }
        return "（不明）";
    }

    /** 受付メールの本文を作る */
    private String buildReceiptMailText(String email, String categoryName, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("このメールは自動送信です。\n");
        sb.append("\n");
        sb.append("お問い合わせを受け付けました。\n");
        sb.append("\n");
        sb.append("■メールアドレス\n");
        sb.append(email).append("\n");
        sb.append("\n");
        sb.append("■カテゴリー\n");
        sb.append(categoryName).append("\n");
        sb.append("\n");
        sb.append("■本文\n");
        sb.append(body).append("\n");
        sb.append("\n");
        sb.append("※本メールに心当たりがない場合は破棄してください。\n");
        return sb.toString();
    }
}