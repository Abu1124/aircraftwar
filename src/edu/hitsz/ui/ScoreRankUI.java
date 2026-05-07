package edu.hitsz.ui;

import edu.hitsz.dao.impl.FileScoreRecordDaoImpl;
import edu.hitsz.record.Difficulty;
import edu.hitsz.record.ScoreRecord;
import edu.hitsz.service.ScoreRecordService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 得分排行榜界面。
 * 使用 JTable 展示得分记录，支持删除选中记录。
 */
public class ScoreRankUI {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JFrame frame;
    private final Difficulty difficulty;
    private final ScoreRecordService service;
    private JTable table;
    private DefaultTableModel tableModel;

    public ScoreRankUI(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.service = new ScoreRecordService(new FileScoreRecordDaoImpl(difficulty));

        frame = new JFrame("排行榜");
        frame.setSize(600, 450);
        frame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(
                ((int) screenSize.getWidth() - 600) / 2,
                ((int) screenSize.getHeight() - 450) / 2,
                600,
                450
        );
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initUI();
        loadData();

        frame.setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部标题区
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("排行榜", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        String difficultyText = switch (difficulty) {
            case EASY -> "EASY";
            case NORMAL -> "NORMAL";
            case HARD -> "HARD";
        };
        JLabel diffLabel = new JLabel("难度：" + difficultyText, SwingConstants.CENTER);
        diffLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        diffLabel.setForeground(Color.GRAY);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(diffLabel, BorderLayout.SOUTH);

        // 表格区
        String[] columnNames = {"排名", "玩家名", "得分", "记录时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(560, 300));

        // 底部按钮区
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton deleteBtn = new JButton("删除选中的记录");
        deleteBtn.setPreferredSize(new Dimension(180, 35));
        deleteBtn.addActionListener(e -> deleteSelectedRecord());

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.addActionListener(e -> loadData());

        JButton mainMenuBtn = new JButton("返回主菜单");
        mainMenuBtn.setPreferredSize(new Dimension(120, 35));
        mainMenuBtn.addActionListener(e -> {
            frame.dispose();
            new DifficultySelectUI();
        });

        bottomPanel.add(deleteBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(mainMenuBtn);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
    }

    /**
     * 从 DAO 加载数据并刷新表格
     */
    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<ScoreRecord> records = service.getRankList();
            int rank = 1;
            for (ScoreRecord r : records) {
                tableModel.addRow(new Object[]{
                        rank++,
                        r.getPlayerName(),
                        r.getScore(),
                        r.getTime().format(TIME_FORMATTER)
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                    "加载排行榜数据失败: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除选中的记录
     */
    private void deleteSelectedRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame,
                    "请先选中要删除的记录！",
                    "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String playerName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showOptionDialog(
                frame,
                "是否确定删除玩家「" + playerName + "」的记录？",
                "选择一个选项",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"是(Y)", "否(N)", "取消"},
                "否(N)"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // 排名 = selectedRow + 1
                boolean deleted = service.deleteByRank(selectedRow + 1);
                if (deleted) {
                    loadData();
                    JOptionPane.showMessageDialog(frame,
                            "删除成功！",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "删除失败！",
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "删除失败: " + e.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        // 否(N) 或 取消：不作任何操作
    }
}
