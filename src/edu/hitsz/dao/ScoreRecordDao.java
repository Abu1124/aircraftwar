package edu.hitsz.dao;

import edu.hitsz.record.ScoreRecord;

import java.io.IOException;
import java.util.List;

/**
 * 排行榜数据访问对象（DAO）接口。
 */
public interface ScoreRecordDao {
    List<ScoreRecord> getAll() throws IOException;

    void add(ScoreRecord record) throws IOException;

    /**
     * 按排行榜序号删除（从 1 开始）。
     */
    boolean deleteByRank(int rank) throws IOException;
}

