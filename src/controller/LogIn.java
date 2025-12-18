package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UserInfoDto;

public class LogIn extends HttpServlet{

	protected void doGet(HttpServletRequest request,HttpServletResponse response)
	throws ServletException,IOException{

		//レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");	//文字コードをUTF-8に指定

		//リクエスト（受信データ）の文字コードを設定
		request.setCharacterEncoding("UTF-8");					//文字コードをUTF-8に指定

		//セッションからユーザーデータを取得
		HttpSession session = request.getSession();
		UserInfoDto userInfoOnSession = (UserInfoDto)session.getAttribute("LOGIN_INFO");

		//ログイン状態によって表示画面を振り分ける
		// ※ログイン状態はセッション上からユーザーデータを取得できたか否かで判断
		//	  ユーザーデータを取得できた　　　→既にログインされている
		//    ユーザーデータを取得できなかった→まだログインされていない

		if (userInfoOnSession != null) {
			//ログイン済 ダッシュボード画面へ転送
			if (userInfoOnSession.getRole() == 1) {
				response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
			}else {
				response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
			}

		}else {
			//ログイン画面にフォワード
			RequestDispatcher dispatch = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
			dispatch.forward(request, response);

		}

	}
}
