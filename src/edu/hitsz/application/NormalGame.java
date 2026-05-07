package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.record.Difficulty;

/**
 * 普通难度游戏。
 * - 可以生成Boss敌机，每次召唤Boss不改变其血量
 * - 难度随游戏时间递进：敌机产生周期、速度、血量逐渐增加
 */
public class NormalGame extends Game {

    private static final int DIFFICULTY_INCREASE_INTERVAL = 600; // 每600帧（约24秒）递进一次
    private static final double SPAWN_CYCLE_DECREASE = 2.0;     // 每次递进敌机生成周期减少
    private static final double MIN_SPAWN_CYCLE = 10;           // 敌机生成周期下限

    private static final double ELITE_SHOOT_CYCLE_DECREASE = 1.5;
    private static final double MIN_ELITE_SHOOT_CYCLE = 10;

    private int lastDifficultyIncreaseTime = 0;

    public NormalGame() {
        super(Difficulty.NORMAL);
        // 普通难度初始参数
        this.enemyMaxNumber = 5;
        this.enemySpawnCycle = 20;
        this.shootCycle = 20;
        this.eliteShootCycle = 25;
        this.bossTriggerScore = 300;
    }

    @Override
    protected void spawnBoss() {
        if (score >= bossTriggerScore && !hasBoss) {
            hasBoss = true;
            int x = Main.WINDOW_WIDTH / 2;
            int y = 80;
            AbstractAircraft boss = new edu.hitsz.aircraft.BossFactory().createAircraft(x, y);
            enemyAircrafts.add(boss);
            audioManager.playBossBgm();
        }
    }

    @Override
    protected void increaseDifficulty() {
        if (gameTime - lastDifficultyIncreaseTime >= DIFFICULTY_INCREASE_INTERVAL) {
            lastDifficultyIncreaseTime = gameTime;

            // 敌机生成周期缩短
            if (enemySpawnCycle > MIN_SPAWN_CYCLE) {
                enemySpawnCycle = Math.max(MIN_SPAWN_CYCLE, enemySpawnCycle - SPAWN_CYCLE_DECREASE);
            }

            // 敌机射击周期缩短
            if (eliteShootCycle > MIN_ELITE_SHOOT_CYCLE) {
                eliteShootCycle = Math.max(MIN_ELITE_SHOOT_CYCLE, eliteShootCycle - ELITE_SHOOT_CYCLE_DECREASE);
            }

            System.out.println("[普通难度] 难度递进：敌机生成周期=" + enemySpawnCycle
                    + ", 敌机射击周期=" + eliteShootCycle);
        }
    }
}
