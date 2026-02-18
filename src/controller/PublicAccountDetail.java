package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.PublicAccountDetailBL;
import model.PublicAccountDetailDto;

public class PublicAccountDetail extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String userIdParam = request.getParameter("userId");
        int userId;

        try {
            userId = Integer.parseInt(userIdParam);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/PublicAccountList");
            return;
        }

        PublicAccountDetailBL bl = new PublicAccountDetailBL();
        PublicAccountDetailDto dto = bl.selectDetail(userId);

        if (dto == null) {
            response.sendRedirect(request.getContextPath() + "/PublicAccountList");
            return;
        }

        request.setAttribute("ACCOUNT", dto);

        String from = request.getParameter("from");
        String backPage = request.getParameter("page");

        request.setAttribute("from", from);
        request.setAttribute("backPage", backPage);

        RequestDispatcher dispatch =
                request.getRequestDispatcher("/WEB-INF/view/publicAccountDetail.jsp");
        dispatch.forward(request, response);
    }
}
