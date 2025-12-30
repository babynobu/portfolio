package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginBL;
import model.UserInfoDto;

public class ExecuteLogin extends HttpServlet{

	/**-----------------------------------------------
	 * ■■■ExecuteLoginクラス■■■
	 * 概要；サーブレット
	 * 詳細：ログイン結果から出力先を判別
	 ------------------------------------------------*/

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

		//既存セッションがあれば破棄（ユーザー切替対策）
		HttpSession oldSession = request.getSession(false);		//セッションを参照
		if (oldSession != null) {
			oldSession.invalidate();
		}

		//レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");
		//リクエスト（受信データ）の文字コードを設定
		request.setCharacterEncoding("UTF-8");

		//リクエストパラメータからユーザー入力値を取得
		String loginId = request.getParameter("LOGIN_ID");
		String userPass = request.getParameter("PASSWORD");

		//サーバー側バリデーション
	    boolean hasError = false;

	    if (loginId == null || loginId.isEmpty()) {
	        hasError = true;
	    } else if (loginId.length() > 255) {
	        hasError = true;
	    }

	    if (userPass == null || userPass.isEmpty()) {
	        hasError = true;
	    } else if (!userPass.matches("^[0-9a-zA-Z_-]{8,32}$")) {
	        hasError = true;
	    }

	    if (hasError) {
	        // 入力不正 → ログイン画面へ戻す
	        response.sendRedirect(request.getContextPath() + "/LogIn");
	        return;
	    }

		//DBに接続して合致するデータを出力
		LoginBL logic = new LoginBL();
		UserInfoDto dto  = logic.userInfoExtracted(loginId,userPass);

		//返ってきたらログイン
		if (dto != null && dto.getLoginId() != null) {

			//セッションにログイン情報を格納
			HttpSession session = request.getSession(true);
			session.setAttribute("LOGIN_INFO",dto);


			//管理者か一般か
			// role: 0 = 一般, 1 = 管理者
			if (dto.getRole() == 0 ) {
				//一般アカウント用ダッシュボード画面に転送
				response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
			}else {
				//管理者用ダッシュボード画面に転送
				response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
			}
		}else {
			//ユーザーデータの抽出に失敗：ログインNGとしてログイン画面へ転送
			response.sendRedirect(request.getContextPath() + "/LogIn");
		}
	}
}