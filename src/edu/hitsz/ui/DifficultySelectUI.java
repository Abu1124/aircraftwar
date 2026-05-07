package edu.hitsz.ui;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.record.Difficulty;

import javax.swing.*;
import java.awt.*;

/**
 * 难度选择界面。
 * 三个按钮对应三种难度，点击后启动对应难度的游戏。
 */
public class DifficultySelectUI {

    private final JFrame frame;

    public DifficultySelectUI() {
        frame = new JFrame("飞机大战");
        frame.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        frame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(
                ((int) screenSize.getWidth() - Main.WINDOW_WIDTH) / 2,
                ((int) screenSize.getHeight() - Main.WINDOW_HEIGHT) / 2,
                Main.WINDOW_WIDTH,
                Main.WINDOW_HEIGHT
        );
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 使用默认背景图（任意一张，因为选择前先显示）
        JPanel panel = new JPanel() {
            private final Image bg = ImageManager.BACKGROUND_IMAGE_EASY;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 标题
        JLabel titleLabel = new JLabel("飞机大战", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 60, 0));

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton easyBtn = createButton("简单模式");
        JButton normalBtn = createButton("普通模式");
        JButton hardBtn = createButton("困难模式");

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(easyBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(normalBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(hardBtn);
        buttonPanel.add(Box.createVerticalGlue());

        // 事件绑定
        easyBtn.addActionListener(e -> startGame(Difficulty.EASY));
        normalBtn.addActionListener(e -> startGame(Difficulty.NORMAL));
        hardBtn.addActionListener(e -> startGame(Difficulty.HARD));

        panel.add(titleLabel);
        panel.add(buttonPanel);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return btn;
    }

    private void startGame(Difficulty difficulty) {
        // 重置英雄机状态（单例模式，避免上一局血量残留）
        HeroAircraft.getInstance().reset();
        // 根据难度设置背景图
        ImageManager.setBackgroundByDifficulty(difficulty);
        // 启动游戏（通过工厂方法创建对应难度子类）
        Game game = Game.createGame(difficulty);
        frame.setContentPane(game);
        frame.revalidate();
        game.actionStart();
    }
}
