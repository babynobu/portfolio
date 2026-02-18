package model;

public class PublicAccountDetailBL {

    public PublicAccountDetailDto selectDetail(int userId) {

        AccountDao dao = new AccountDao();
        PublicAccountDetailDto dto = dao.publicSelectAccountDetail(userId);

        if (dto == null) return null;

        // ★ age はDBの固定値なので、ここで計算しない
        return dto;
    }
}
