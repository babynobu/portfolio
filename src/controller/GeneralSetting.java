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

public class GeneralSetting extends HttpServlet{

	/**-----------------------------------------------
	 * ■■■GeneralSettingクラス■■■
	 * 概要；サーブレット
	 * 詳細：設定画面へ遷移
	 ------------------------------------------------*/

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

				//アカウント情報を取得
				int userId = userInfoOnSession.getUserId();
				int role =userInfoOnSession.getRole();

				AccountDto dto = new AccountDto();
				AccountDao dao = new AccountDao();

				if ( role == 0 ) {
					String stUserId = String.valueOf(userId);
					dto = dao.selectEditAccount(stUserId);
				}

				//アカウント情報をセット
				request.setAttribute("generalSetting", dto);

				//プロフィール編集画面にフォワード
				RequestDispatcher dispatch =
						request.getRequestDispatcher("/WEB-INF/view/generalSetting.jsp");
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