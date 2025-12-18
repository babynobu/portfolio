package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.BusinessLogic;
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
		response.setContentType("text/html;charset=UTF-8");	//文字コードをUTF-8に指定

		//リクエスト（受信データ）の文字コードを設定
		request.setCharacterEncoding("UTF-8");				//文字コードをUTF-8に指定



		//未ログイン

		//送信された情報をDBに接続して送る
		//返ってきたらログイン
		//エラーならログイン画面に戻る

		//リクエストパラメータからユーザー入力値を取得
		String userId = request.getParameter("USER_ID");		//リクエストパラメータ（USER_ID）
		String userPass = request.getParameter("PASSWORD");		//リクエストパラメータ（PASSWORD）

		//DBに接続して合致するデータを出力
		BusinessLogic logic = new BusinessLogic();
		UserInfoDto dto  = logic.userInfoExtracted(userId,userPass);

		//返ってきたらログイン
		if (dto != null && dto.getUserId() != null) {

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



/*
ログイン画面からデータを送信された場合→セッション情報を削除して、そのうえで送信されたデータに合致する情報をDBから抽出して、その結果に応じて管理者or一般orログイン失敗
URL直打ち→セッション情報を観て、その権限に応じて管理者or一般orログイン画面




 		//セッションからユーザーデータを取得
		HttpSession session = request.getSession(true);
		UserInfoDto userInfoOnSession = (UserInfoDto)session.getAttribute("LOGIN_INFO");

		//ログイン状態によって表示画面を振り分ける
		// ※ログイン状態はセッション上からユーザーデータを取得できたか否かで判断
		//	・ユーザーデータを取得できた	→既にログインされている
		//		→権限を判別し、転送するHTML（ダッシュボード画面）を分岐
		//	・ユーザーデータを取得できなかった→まだログインされていない
		//		→送信されたIDとPASSの照合と、ユーザーの権限を判別し、出力するHTML（ダッシュボード画面）
		if (userInfoOnSession != null) {

			//ログイン済

			//セッションからユーザーの権限情報を取得
			int role = userInfoOnSession.getRole();

			//権限情報から出力するHTML（ダッシュボード画面）を分岐
			// role: 0 = 一般, 1 = 管理者
			if ( role == 0 ) {
				//一般アカウント用ダッシュボード画面に転送
				response.sendRedirect(request.getContextPath() + "/GeneralDashboard");

			}else {
				//管理者用ダッシュボード画面に転送
				response.sendRedirect(request.getContextPath() + "/ManagerDashboard");

			}
		}else
 */
