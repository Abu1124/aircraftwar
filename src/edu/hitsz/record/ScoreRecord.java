package edu.hitsz.record;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 得分记录（VO）。
 */
public final class ScoreRecord {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String playerName;
    private final int score;
    private final LocalDateTime time;

    public ScoreRecord(String playerName, int score, LocalDateTime time) {
        this.playerName = Objects.requireNonNull(playerName, "playerName");
        this.score = score;
        this.time = Objects.requireNonNull(time, "time");
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String toCsvLine() {
        return escapeCsv(playerName) + "," + score + "," + time.format(FORMATTER);
    }

    public static ScoreRecord fromCsvLine(String line) {
        // 约定格式：name,score,yyyy-MM-dd HH:mm:ss
        // name 中若包含逗号，用双引号包裹，双引号用 "" 转义
        String[] parts = splitCsv3(line);
        String name = unescapeCsv(parts[0].trim());
        int score = Integer.parseInt(parts[1].trim());
        LocalDateTime t = LocalDateTime.parse(parts[2].trim(), FORMATTER);
        return new ScoreRecord(name, score, t);
    }

    private static String escapeCsv(String s) {
        if (s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static String unescapeCsv(String s) {
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            String inner = s.substring(1, s.length() - 1);
            return inner.replace("\"\"", "\"");
        }
        return s;
    }

    private static String[] splitCsv3(String line) {
        // 只需要拆成 3 段：name,score,time
        String[] out = new String[3];
        int idx = 0;
        int i = 0;
        while (idx < 3 && i < line.length()) {
            if (line.charAt(i) == '"') {
                StringBuilder sb = new StringBuilder();
                sb.append('"');
                i++;
                while (i < line.length()) {
                    char c = line.charAt(i);
                    sb.append(c);
                    if (c == '"') {
                        if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                            sb.append('"');
                            i += 2;
                            continue;
                        }
                        i++;
                        break;
                    }
                    i++;
                }
                out[idx++] = sb.toString();
                if (i < line.length() && line.charAt(i) == ',') i++;
            } else {
                int j = i;
                while (j < line.length() && line.charAt(j) != ',') j++;
                out[idx++] = line.substring(i, j);
                i = j;
                if (i < line.length() && line.charAt(i) == ',') i++;
            }
        }
        if (idx != 3) {
            throw new IllegalArgumentException("Bad record line: " + line);
        }
        return out;
    }
}

