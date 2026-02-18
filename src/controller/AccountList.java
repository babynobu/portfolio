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

import model.AccountDto;
import model.AccountListBL;
import model.UserInfoDto;

public class AccountList extends HttpServlet{

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

				//ページ番号取得（デフォルト1）
				int page = 1;
				int limit = 5;

				String pageParam = request.getParameter("page");
				if (pageParam != null && !pageParam.isEmpty()) {
				    try {
				        page = Integer.parseInt(pageParam);
				    } catch (NumberFormatException e) {
				        page = 1; // 不正値なら1ページ目に戻す
				    }
				}

				//アカウント一覧を取得
				AccountListBL al = new AccountListBL();

				//インスタンス化
				List<AccountDto> accountList = new ArrayList<>();

				//全件数取得
                int totalCount = al.countAccount();

                //総ページ数
                int totalPage = (int) Math.ceil((double) totalCount / limit);

                //ページ補正（削除後0件ページ対策）
                if (totalPage > 0 && page > totalPage) {
                    page = totalPage;
                }
                if (page < 1) {
                    page = 1;
                }

                int offset = (page - 1) * limit;

                //ページ対応の一覧取得
				//メソッド起動
				accountList = al.selectAccountList(limit, offset);
				//リクエストにアカウント情報を格納
				request.setAttribute("ACCOUNT_LIST", accountList);
				request.setAttribute("currentPage", page);
				request.setAttribute("totalPage", totalPage);

				//アカウント一覧画面にフォワード
				RequestDispatcher dispatch =
						request.getRequestDispatcher("/WEB-INF/view/accountList.jsp");
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
