package edu.hitsz.application;

import edu.hitsz.record.Difficulty;

/**
 * 简单难度游戏。
 * - 无法生成Boss敌机
 * - 难度不随游戏时间变化，全程保持初始设定
 */
public class EasyGame extends Game {

    public EasyGame() {
        super(Difficulty.EASY);
        // 简单难度初始参数
        this.enemyMaxNumber = 3;
        this.enemySpawnCycle = 30;
        this.shootCycle = 30;
        this.eliteShootCycle = 40;
        // Boss触发分数设为极大值，实际上不会生成
        this.bossTriggerScore = Integer.MAX_VALUE;
        this.hasBoss = true; // 直接设为true，阻止Boss生成
    }

    @Override
    protected void spawnBoss() {
        // 简单难度不生成Boss
    }

    @Override
    protected void increaseDifficulty() {
        // 简单难度无难度递进
    }
}
