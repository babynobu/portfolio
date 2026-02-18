package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CategoryDao;
import model.CategoryDto;
import model.UserInfoDto;

public class CategoryEdit extends HttpServlet{

	/**-----------------------------------------------
	 * ■■■CategoryEditクラス■■■
	 * 概要；サーブレット
	 * 詳細：カテゴリ一覧から編集画面へ遷移
	 ------------------------------------------------*/

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

				//カテゴリー情報を取得
				String categoryId = request.getParameter("categoryId");
				CategoryDto dto = new CategoryDto();
				CategoryDao dao = new CategoryDao();

				dto = dao.editCategory(categoryId);

				//アカウント情報をセット
				request.setAttribute("category", dto);

				//アカウント編集画面にフォワード
				RequestDispatcher dispatch =
						request.getRequestDispatcher("/WEB-INF/view/categoryEdit.jsp");
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