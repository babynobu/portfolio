package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CategoryDao;
import model.CategoryDto;

/**-----------------------------------------------
 * ■■■PublicContactUsクラス■■■
 * 概要：公開お問い合わせフォーム表示
 -----------------------------------------------*/
public class PublicContactUs extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // カテゴリー一覧（is_deleted=0）を取得
        CategoryDao categoryDao = new CategoryDao();
        List<CategoryDto> categories = categoryDao.selectActiveCategoryList();

        request.setAttribute("CATEGORIES", categories);

        RequestDispatcher rd =
                request.getRequestDispatcher("/WEB-INF/view/publicContactUs.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 直接POSTされてもフォームへ戻す
        response.sendRedirect(request.getContextPath() + "/PublicContactUs");
    }
}
