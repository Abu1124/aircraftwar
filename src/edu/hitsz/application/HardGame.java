package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.record.Difficulty;

/**
 * 困难难度游戏。
 * - 可以生成Boss敌机，每次召唤Boss时提升其血量
 * - 难度随游戏时间递进：敌机产生周期、速度、血量、射击周期等逐渐增加
 */
public class HardGame extends Game {

    private static final int DIFFICULTY_INCREASE_INTERVAL = 600; // 每600帧（约24秒）递进一次
    private static final double SPAWN_CYCLE_DECREASE = 2.5;     // 每次递进敌机生成周期减少
    private static final double MIN_SPAWN_CYCLE = 6;            // 敌机生成周期下限

    private static final double ELITE_SHOOT_CYCLE_DECREASE = 2.0;
    private static final double MIN_ELITE_SHOOT_CYCLE = 8;

    private static final double HERO_SHOOT_CYCLE_DECREASE = 1.0;
    private static final double MIN_HERO_SHOOT_CYCLE = 10;

    private int lastDifficultyIncreaseTime = 0;

    public HardGame() {
        super(Difficulty.HARD);
        // 困难难度初始参数
        this.enemyMaxNumber = 7;
        this.enemySpawnCycle = 15;
        this.shootCycle = 18;
        this.eliteShootCycle = 20;
        this.bossTriggerScore = 200;
    }

    @Override
    protected void spawnBoss() {
        if (score >= bossTriggerScore && !hasBoss) {
            hasBoss = true;
            int x = Main.WINDOW_WIDTH / 2;
            int y = 80;
            // 困难难度每次召唤Boss提升血量
            AbstractAircraft boss = new edu.hitsz.aircraft.BossFactory().createAircraft(x, y);
            if (boss instanceof BossEnemy) {
                int currentHp = boss.getHp();
                int increasedHp = (int) (currentHp * 1.5);
                boss.setHp(increasedHp);
                System.out.println("[困难难度] Boss生成，血量提升至 " + increasedHp);
            }
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

            // 英雄机射击周期缩短
            if (shootCycle > MIN_HERO_SHOOT_CYCLE) {
                shootCycle = Math.max(MIN_HERO_SHOOT_CYCLE, shootCycle - HERO_SHOOT_CYCLE_DECREASE);
            }

            System.out.println("[困难难度] 难度递进：敌机生成周期=" + enemySpawnCycle
                    + ", 敌机射击周期=" + eliteShootCycle
                    + ", 英雄机射击周期=" + shootCycle);
        }
    }
}
