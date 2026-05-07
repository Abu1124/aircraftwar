package edu.hitsz.service;

import edu.hitsz.dao.ScoreRecordDao;
import edu.hitsz.record.ScoreRecord;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 排行榜业务服务（将业务逻辑与 DAO 解耦）。
 */
public class ScoreRecordService {
    private final ScoreRecordDao dao;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ScoreRecordService(ScoreRecordDao dao) {
        this.dao = dao;
    }

    public void addRecord(ScoreRecord record) throws IOException {
        dao.add(record);
    }

    public List<ScoreRecord> getRankList() throws IOException {
        return dao.getAll();
    }

    public boolean deleteByRank(int rank) throws IOException {
        return dao.deleteByRank(rank);
    }

    public void printRankListToConsole() throws IOException {
        List<ScoreRecord> records = dao.getAll();
        System.out.println("======== SCORE RANK LIST ========");
        if (records.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (int i = 0; i < records.size(); i++) {
            ScoreRecord r = records.get(i);
            int rank = i + 1;
            System.out.printf("%d. %s\t%d\t%s%n",
                    rank,
                    r.getPlayerName(),
                    r.getScore(),
                    r.getTime().format(TIME_FORMATTER));
        }
    }
}

