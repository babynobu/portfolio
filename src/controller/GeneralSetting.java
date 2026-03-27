package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.AccountDao;
import model.AccountDto;
import model.UserInfoDto;

public class GeneralSetting extends HttpServlet {

	/**-----------------------------------------------
	 * ■■■GeneralSettingクラス■■■
	 * 概要：サーブレット
	 * 詳細：設定画面へ遷移
	 ------------------------------------------------*/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		UserInfoDto loginUser =
				(session == null) ? null : (UserInfoDto) session.getAttribute("LOGIN_INFO");

		response.setContentType("text/html;charset=UTF-8");

		// 未ログイン
		if (loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/LogIn");
			return;
		}

		// 一般ユーザー
		if (loginUser.getRole() == 0) {
			int userId = loginUser.getUserId();

			AccountDao dao = new AccountDao();
			AccountDto dto = dao.selectEditAccount(String.valueOf(userId));

			request.setAttribute("generalSetting", dto);

			RequestDispatcher dispatch =
					request.getRequestDispatcher("/WEB-INF/view/generalSetting.jsp");
			dispatch.forward(request, response);
			return;
		}

		// 管理者
		if (loginUser.getRole() == 1) {
			response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
			return;
		}

		// 想定外の権限値
		request.getSession().invalidate();
		RequestDispatcher dispatch =
				request.getRequestDispatcher("/WEB-INF/view/error.jsp");
		dispatch.forward(request, response);
	}
}