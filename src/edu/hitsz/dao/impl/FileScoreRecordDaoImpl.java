package edu.hitsz.dao.impl;

import edu.hitsz.dao.ScoreRecordDao;
import edu.hitsz.record.Difficulty;
import edu.hitsz.record.ScoreRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 基于文件的排行榜 DAO 实现。
 * 每个难度对应一个文件，记录按“得分降序、时间降序”排序持久化。
 */
public class FileScoreRecordDaoImpl implements ScoreRecordDao {
    private final File file;

    public FileScoreRecordDaoImpl(Difficulty difficulty) {
        File baseDir = preferredProjectDir();
        File dataDir = new File(baseDir, "data");
        //noinspection ResultOfMethodCallIgnored
        dataDir.mkdirs();
        this.file = new File(dataDir, "rank_" + difficulty.name().toLowerCase() + ".csv");
    }

    private static File preferredProjectDir() {
        // 课程作业要求：data/ 生成在 d:\课程作业\中期检查 下
        File required = new File("d:\\课程作业\\中期检查");
        if (required.exists() && required.isDirectory()) {
            return required;
        }
        // 回退：运行时工作目录
        return new File(System.getProperty("user.dir"));
    }

    @Override
    public List<ScoreRecord> getAll() throws IOException {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        List<ScoreRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                records.add(ScoreRecord.fromCsvLine(line));
            }
        }
        records.sort(defaultComparator());
        return records;
    }

    @Override
    public void add(ScoreRecord record) throws IOException {
        List<ScoreRecord> records = getAll();
        records.add(record);
        records.sort(defaultComparator());
        writeAll(records);
    }

    @Override
    public boolean deleteByRank(int rank) throws IOException {
        if (rank <= 0) return false;
        List<ScoreRecord> records = getAll();
        int idx = rank - 1;
        if (idx < 0 || idx >= records.size()) return false;
        records.remove(idx);
        writeAll(records);
        return true;
    }

    private void writeAll(List<ScoreRecord> records) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) {
            for (ScoreRecord r : records) {
                bw.write(r.toCsvLine());
                bw.newLine();
            }
        }
    }

    private static Comparator<ScoreRecord> defaultComparator() {
        return Comparator
                .comparingInt(ScoreRecord::getScore).reversed()
                .thenComparing(ScoreRecord::getTime, Comparator.reverseOrder());
    }
}

