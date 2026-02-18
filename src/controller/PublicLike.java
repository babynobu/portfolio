package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.GoodStampDao;

public class PublicLike extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String userIdParam = request.getParameter("userId");
        try (PrintWriter out = response.getWriter()) {

            int userId = Integer.parseInt(userIdParam);

            GoodStampDao dao = new GoodStampDao();
            int likeCount = dao.addLikeAndGetCount(userId);

            out.print("{\"result\":\"ok\",\"likeCount\":" + likeCount + "}");

        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"result\":\"ng\"}");
            }
        }
    }
}
