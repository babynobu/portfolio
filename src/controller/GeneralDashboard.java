package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.StampInfoDto;
import model.UserInfoDto;
import model.monthlyGoodStampBL;
import model.yearlyGoodStampBL;

public class GeneralDashboard extends HttpServlet{

	protected void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		HttpSession session = request.getSession(false);
		UserInfoDto userInfoOnSession =
				(session == null) ? null : (UserInfoDto) session.getAttribute("LOGIN_INFO");

		//レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");	//文字コードをUTF-8に指定

		if (userInfoOnSession != null) {
			//一般
			if ( userInfoOnSession.getRole() == 0 ) {

				//いいね件数を取得
				yearlyGoodStampBL yg = new yearlyGoodStampBL();
				monthlyGoodStampBL mg = new monthlyGoodStampBL();

				List<StampInfoDto> yearlyList = new ArrayList<>();
				List<StampInfoDto> monthlyList = new ArrayList<>();

				int userId = userInfoOnSession.getUserId();

				yearlyList = yg.yearlyStampInfo(userId);
				monthlyList = mg.monthlyStampInfo(userId);

				int yearlyCount = yearlyList.size();
				int monthlyCount = monthlyList.size();

				request.setAttribute("YEARLY_COUNT", yearlyCount);
				request.setAttribute("MONTHLY_COUNT", monthlyCount);

				//ダッシュボード画面にフォワード
				RequestDispatcher dispatch =
						request.getRequestDispatcher("/WEB-INF/view/GeneralDashboard.jsp");
				dispatch.forward(request, response);
				//管理者
			}else if ( userInfoOnSession.getRole() == 1 ) {
				response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
				// 想定外の値（0でも1でもない）
			}else {
				// セッション破棄
				request.getSession().invalidate();
				// エラー画面にフォワード
				RequestDispatcher dispatch =
						request.getRequestDispatcher("/WEB-INF/view/error.jsp");
				dispatch.forward(request, response);
			}
		} else {
			response.sendRedirect(request.getContextPath() + "/LogIn");
		}

	}

}
