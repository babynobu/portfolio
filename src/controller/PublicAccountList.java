package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.PublicAccountListBL;
import model.PublicAccountListDto;

public class PublicAccountList extends HttpServlet{

	protected void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{

		//レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");	//文字コードをUTF-8に指定


		//ページ番号取得（デフォルト1）
		int page = 1;
		int limit = 8;

		String pageParam = request.getParameter("page");
		if (pageParam != null && !pageParam.isEmpty()) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1; // 不正値なら1ページ目に戻す
			}
		}

		//アカウント一覧を取得
		PublicAccountListBL al = new PublicAccountListBL();

		//インスタンス化
		List<PublicAccountListDto> publicAccountList = new ArrayList<>();

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
		publicAccountList = al.selectAccountList(limit, offset);
		//リクエストにアカウント情報を格納
		request.setAttribute("PUBLIC_ACCOUNT_LIST", publicAccountList);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPage", totalPage);

		//アカウント一覧画面にフォワード
		RequestDispatcher dispatch =
				request.getRequestDispatcher("/WEB-INF/view/publicAccountList.jsp");
		dispatch.forward(request, response);

	}
}