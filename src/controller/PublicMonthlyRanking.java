package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MonthlyLikeRankingDto;
import model.MonthlyRankingBL;

public class PublicMonthlyRanking extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        int page = 1;
        int limit = 10;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try { page = Integer.parseInt(pageParam); }
            catch (NumberFormatException e) { page = 1; }
        }

        MonthlyRankingBL bl = new MonthlyRankingBL();

        int totalCount = bl.countMonthlyRankingTargets();
        int totalPage = (int) Math.ceil((double) totalCount / limit);

        if (totalPage > 0 && page > totalPage) page = totalPage;
        if (page < 1) page = 1;

        int offset = (page - 1) * limit;

        List<MonthlyLikeRankingDto> list = bl.selectMonthlyRanking(limit, offset);

        // 通し順位を付与（ページング込み）
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(offset + i + 1);
        }

        request.setAttribute("RANKING_LIST", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);

        RequestDispatcher dispatch =
                request.getRequestDispatcher("/WEB-INF/view/publicMonthlyRanking.jsp");
        dispatch.forward(request, response);
    }
}
