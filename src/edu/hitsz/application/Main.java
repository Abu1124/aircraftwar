package edu.hitsz.application;

import edu.hitsz.ui.DifficultySelectUI;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");
        // 启动难度选择界面
        new DifficultySelectUI();
    }
}
