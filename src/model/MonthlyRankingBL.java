package model;

import java.util.List;

public class MonthlyRankingBL {

    public List<MonthlyLikeRankingDto> selectMonthlyRanking(int limit, int offset) {
        RankingDao dao = new RankingDao();
        return dao.selectMonthlyRanking(limit, offset);
    }

    public int countMonthlyRankingTargets() {
        RankingDao dao = new RankingDao();
        return dao.countMonthlyRankingTargets();
    }
}
