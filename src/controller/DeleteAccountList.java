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

import model.AccountListDto;
import model.DeleteAccountListBL;
import model.UserInfoDto;

public class DeleteAccountList extends HttpServlet{

	protected void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		HttpSession session = request.getSession(false);
		UserInfoDto userInfoOnSession =
				(session == null) ? null : (UserInfoDto) session.getAttribute("LOGIN_INFO");

		//レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");	//文字コードをUTF-8に指定

		if (userInfoOnSession != null) {
			//管理者
			if ( userInfoOnSession.getRole() == 1 ) {

				//アカウント一覧を取得
				//インスタンス化
				DeleteAccountListBL al = new DeleteAccountListBL();
				List<AccountListDto> accountList = new ArrayList<>();
				//メソッド起動
				accountList = al.selectDeleteAccountList();
				//リクエストにアカウント情報を格納
				request.setAttribute("ACCOUNT_LIST", accountList);

				//アカウント一覧画面にフォワード
				RequestDispatcher dispatch =
						request.getRequestDispatcher("/WEB-INF/view/deleteAccountList.jsp");
				dispatch.forward(request, response);
				//一般
			}else if ( userInfoOnSession.getRole() == 0 ) {
				response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
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
