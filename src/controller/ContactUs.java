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

import model.ContactUsListBL;
import model.ContactUsListDao;
import model.ContactUsListDto;
import model.UserInfoDto;

public class ContactUs extends HttpServlet{

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

				//お問い合わせ一覧を取得
				//インスタンス化
				ContactUsListBL cul = new ContactUsListBL();
				List<ContactUsListDto> contactUsList = new ArrayList<>();
				//メソッド起動
				contactUsList = cul.selectContactUsList();
				//リクエストにアカウント情報を格納
				request.setAttribute("CONTACT_US_LIST", contactUsList);

				//お問い合わせ一覧画面にフォワード
				RequestDispatcher dispatch =
						request.getRequestDispatcher("/WEB-INF/view/contactUs.jsp");
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

	protected void doPost(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		HttpSession session = request.getSession(false);
		UserInfoDto userInfoOnSession =
				(session == null) ? null : (UserInfoDto) session.getAttribute("LOGIN_INFO");

		//レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");	//文字コードをUTF-8に指定

		if (userInfoOnSession != null) {
			//管理者
			if ( userInfoOnSession.getRole() == 1 ) {


				//お問い合わせ情報を更新
				int contactUsId = Integer.parseInt(request.getParameter("contactUsId"));
				String status = request.getParameter("status");
				boolean executeUpdate = true;
				ContactUsListDao dao = new ContactUsListDao();

				executeUpdate = dao.updateContactUs(contactUsId,status);

				if(executeUpdate) {
					//お問い合わせ一覧を取得
					//インスタンス化
					ContactUsListBL cul = new ContactUsListBL();
					List<ContactUsListDto> contactUsList = new ArrayList<>();
					//メソッド起動
					contactUsList = cul.selectContactUsList();
					//リクエストにアカウント情報を格納
					request.setAttribute("CONTACT_US_LIST", contactUsList);

					//お問い合わせ一覧画面にフォワード
					RequestDispatcher dispatch =
							request.getRequestDispatcher("/WEB-INF/view/contactUs.jsp");
					dispatch.forward(request, response);
				}else {
					//更新失敗
					// エラー画面にフォワード
					RequestDispatcher dispatch =
							request.getRequestDispatcher("/WEB-INF/view/errorContactUsUpdate.jsp");
					dispatch.forward(request, response);
				}
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
