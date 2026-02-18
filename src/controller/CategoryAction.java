package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CategoryActionDao;

public class CategoryAction extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        String action = request.getParameter("action");
        String categoryIdStr = request.getParameter("categoryId");

        PrintWriter out = response.getWriter();

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            CategoryActionDao dao = new CategoryActionDao();

            if ("status".equals(action)) {

                int newStatus = dao.toggleStatus(categoryId);
                String statusLabel = (newStatus == 1) ? "有効" : "無効";

                out.print(
                    "{"
                    + "\"result\":\"ok\","
                    + "\"newStatus\":\"" + statusLabel + "\""
                    + "}"
                );


            } else if ("restore".equals(action)) {

                dao.restore(categoryId);
                out.print("{\"result\":\"ok\"}");

            } else if ("delete".equals(action)) {

                dao.logicalDelete(categoryId);
                out.print("{\"result\":\"ok\"}");

            } else if ("physicsDelete".equals(action)) {

                dao.physicsDelete(categoryId);
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
