package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UserInfoDto;

public class LogOut extends HttpServlet{

	protected void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		//セッション情報を取得
		HttpSession session = request.getSession(false);
		UserInfoDto userInfoOnSession = (UserInfoDto)session.getAttribute("LOGIN_INFO");

		//セッション情報によって、転送する画面を分ける
		if ( userInfoOnSession == null ) {
			response.sendRedirect( request.getContextPath() + "/LogIn" );	//未ログイン
		}else {
			// role: 0 = 一般, 1 = 管理者
			if ( userInfoOnSession.getRole() == 0 ) {
				//一般アカウント用ダッシュボード画面に転送
				response.sendRedirect( request.getContextPath() + "/GeneralDashboard" );
			}else {
				//管理者用ダッシュボード画面に転送
				response.sendRedirect( request.getContextPath() + "/ManagerDashboard" );
			}
		}

	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		//既存セッションを破棄
		HttpSession oldSession = request.getSession(false);		//セッションを参照
		if (oldSession != null) {
			oldSession.invalidate();
			//ログアウト画面へフォワード
			RequestDispatcher dispatch =
					request.getRequestDispatcher("/WEB-INF/view/logout.jsp");
			dispatch.forward(request, response);
		}
		// 想定外
		else {
			// エラーページへ
			response.sendRedirect(request.getContextPath() + "/ErrorPage");
		}

	}
}

/*

セッション情報によって出力画面を変える

直打ち
・未ログイン→ログイン
・管理者ログイン→管理者ダッシュボード
・一般ログイン→一般ダッシュボード

ログアウトボタン押下→ログアウトしました画面

 */
