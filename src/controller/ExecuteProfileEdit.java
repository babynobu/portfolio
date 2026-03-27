package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.AccountDao;
import model.AccountDto;
import model.AppConfig;
import model.ProfileUpdateBL;
import model.UserInfoDto;

@MultipartConfig
public class ExecuteProfileEdit extends HttpServlet {

	/**-----------------------------------------------
	 * ■■■ExecuteProfileEditクラス■■■
	 * 概要：プロフィール編集処理
	 -----------------------------------------------*/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		UserInfoDto loginUser =
				(session != null) ? (UserInfoDto) session.getAttribute("LOGIN_INFO") : null;

		if (loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/LogIn");
			return;
		}

		if (loginUser.getRole() != 0) {
			response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
			return;
		}

		response.sendRedirect(request.getContextPath() + "/GeneralDashboard");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		// 0) ログイン・権限チェック
		HttpSession session = request.getSession(false);
		UserInfoDto loginUser =
				(session != null) ? (UserInfoDto) session.getAttribute("LOGIN_INFO") : null;

		if (loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/LogIn");
			return;
		}

		if (loginUser.getRole() != 0) {
			response.sendRedirect(request.getContextPath() + "/ManagerDashboard");
			return;
		}

		Map<String, String> errors = new HashMap<>();

		// 1) パラメータ取得
		String userIdStr = request.getParameter("userId");
		String name = request.getParameter("name");
		String kana = request.getParameter("kana");
		String genderStr = request.getParameter("gender");
		String introduction = request.getParameter("introduction");
		String ageStr = request.getParameter("age");

		int userId = -1;
		int gender = 0;
		Integer age = null;
		String profileImagePath = null;

		// 2) userId
		if (userIdStr == null || userIdStr.trim().isEmpty()) {
			errors.put("userId", "ユーザーIDが取得できません。");
		} else {
			try {
				userId = Integer.parseInt(userIdStr);
				if (userId <= 0) {
					errors.put("userId", "ユーザーIDが不正です。");
				}
			} catch (Exception e) {
				errors.put("userId", "ユーザーIDが不正です。");
			}
		}

		// 3) 名前
		if (name == null || name.trim().isEmpty()) {
			errors.put("name", "名前は必須です。");
		} else if (name.length() > 255) {
			errors.put("name", "名前は255文字以内で入力してください。");
		}

		// 4) ふりがな
		if (kana == null || kana.trim().isEmpty()) {
			errors.put("kana", "ふりがなは必須です。");
		} else if (!kana.matches("^[ぁ-んー 　]+$")) {
			errors.put("kana", "ふりがなはひらがなのみで入力してください。");
		}

		// 5) 性別
		if (genderStr == null || genderStr.trim().isEmpty()) {
			errors.put("gender", "性別が選択されていません。");
		} else {
			try {
				gender = Integer.parseInt(genderStr);
				if (gender != 1 && gender != 2 && gender != 3) {
					errors.put("gender", "性別が不正です。");
				}
			} catch (Exception e) {
				errors.put("gender", "性別が不正です。");
			}
		}

		// 6) 年齢
		if (ageStr == null || ageStr.trim().isEmpty()) {
			errors.put("age", "年齢は必須です。");
		} else {
			try {
				int a = Integer.parseInt(ageStr);
				if (a < 0 || a > 999) {
					errors.put("age", "年齢は0〜999の範囲で入力してください。");
				} else {
					age = a;
				}
			} catch (Exception e) {
				errors.put("age", "年齢は数値で入力してください。");
			}
		}

		// 7) 自己紹介
		if (introduction != null && introduction.length() > 1500) {
			errors.put("introduction", "自己紹介は1500文字以内で入力してください。");
		}

		// 8) エラーがあれば戻す
		if (!errors.isEmpty()) {
			forwardToForm(request, response, errors);
			return;
		}

		// 9) プロフィール画像
		Part imagePart = request.getPart("profileImage");

		if (imagePart != null && imagePart.getSize() > 0) {

			long maxFileSize = 2 * 1024 * 1024; // 2MB
			if (imagePart.getSize() > maxFileSize) {
				errors.put("profileImage", "プロフィール画像は2MB以内でアップロードしてください。");
				forwardToForm(request, response, errors);
				return;
			}

			String uploadDir = AppConfig.UPLOAD_DIR;

			File dir = new File(uploadDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = imagePart.getSubmittedFileName();
			File saveFile = new File(dir, fileName);

			imagePart.write(saveFile.getAbsolutePath());

			profileImagePath = AppConfig.IMAGE_PATH_PREFIX + fileName;
		}

		// 10) DTO
		AccountDto dto = new AccountDto();
		dto.setUserId(userId);
		dto.setName(name);
		dto.setKana(kana);
		dto.setGender(gender);
		dto.setAge(age);
		dto.setIntroduction(introduction);

		if (profileImagePath != null) {
			dto.setProfileImagePath(profileImagePath);
		}

		// 11) 更新処理
		ProfileUpdateBL logic = new ProfileUpdateBL();

		try {
			boolean result = logic.profileUpdate(dto);

			if (result) {
				RequestDispatcher rd =
						request.getRequestDispatcher("/WEB-INF/view/profileEditComplete.jsp");
				rd.forward(request, response);
			} else {
				request.setAttribute("errorMessage", "登録処理に失敗しました。");
				forwardToForm(request, response, errors);
			}

		} catch (Exception e) {
			e.printStackTrace();

			request.setAttribute("errorMessage", "システムエラーが発生しました。");
			RequestDispatcher rd =
					request.getRequestDispatcher("/WEB-INF/view/systemError.jsp");
			rd.forward(request, response);
		}
	}

	private void forwardToForm(HttpServletRequest request,
			HttpServletResponse response,
			Map<String, String> errors)
			throws ServletException, IOException {

		request.setAttribute("errors", errors);

		String userId = request.getParameter("userId");
		if (userId != null && !userId.trim().isEmpty()) {
			AccountDao dao = new AccountDao();
			AccountDto dto = dao.selectEditAccount(userId);
			request.setAttribute("profileEdit", dto);
		}

		RequestDispatcher rd =
				request.getRequestDispatcher("/WEB-INF/view/profileEdit.jsp");
		rd.forward(request, response);
	}
}