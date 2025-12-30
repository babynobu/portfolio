package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AccountActionDao;

public class AccountAction extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");

        PrintWriter out = response.getWriter();

        try {
            int userId = Integer.parseInt(userIdStr);
            AccountActionDao dao = new AccountActionDao();

            if ("status".equals(action)) {

                int newStatus = dao.toggleStatus(userId);
                String statusLabel = (newStatus == 1) ? "有効" : "無効";

                out.print(
                    "{"
                    + "\"result\":\"ok\","
                    + "\"newStatus\":\"" + statusLabel + "\""
                    + "}"
                );

            } else if ("delete".equals(action)) {

                dao.logicalDelete(userId);
                out.print("{\"result\":\"ok\"}");

            } else {
                out.print("{\"result\":\"ng\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"result\":\"ng\"}");
        }
    }
}
