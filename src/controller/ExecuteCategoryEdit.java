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

import model.CategoryDto;
import model.CategoryUpdateBL;
import model.UserInfoDto;

@MultipartConfig
public class ExecuteCategoryEdit extends HttpServlet {

    /**-----------------------------------------------
     * ■■■ExecuteCategoryEditクラス■■■
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

        response.sendRedirect(request.getContextPath() + "/CategoryList");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> errors = new HashMap<>();

        int categoryId     = Integer.parseInt(request.getParameter("categoryId"));
        String categoryName     = request.getParameter("categoryName");

        // ===== バリデーション =====

        if (categoryName == null || categoryName.isEmpty()) {
            errors.put("categoryName", "カテゴリー名は必須です。");
        } else if (categoryName.length() > 255) {
            errors.put("categoryName", "カテゴリー名は255文字以内で入力してください。");
        }

        // ===== エラーがあれば戻す =====
        if (!errors.isEmpty()) {
            forwardToForm(request, response, errors);
            return;
        }

        CategoryDto dto = new CategoryDto();

        dto.setCategoryId(categoryId);
        dto.setCategoryName(categoryName);

        CategoryUpdateBL logic = new CategoryUpdateBL();

        try {
            boolean result = logic.categoryUpdate(dto);

            if (result) {
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/categoryEditComplete.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("errorMessage", "登録処理に失敗しました。");
                RequestDispatcher rd =
                        request.getRequestDispatcher("/WEB-INF/view/categoryEdit.jsp");
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
                request.getRequestDispatcher("/WEB-INF/view/categoryEdit.jsp");
        rd.forward(request, response);
    }
}
