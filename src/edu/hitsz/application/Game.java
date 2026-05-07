package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.audio.AudioManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dao.impl.FileScoreRecordDaoImpl;
import edu.hitsz.record.Difficulty;
import edu.hitsz.record.ScoreRecord;
import edu.hitsz.service.ScoreRecordService;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.IceSupply;
import edu.hitsz.supply.SupplyFactory;
import edu.hitsz.supply.SupplyObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * 游戏主类（模板方法模式：抽象父类）。
 * action() 是模板方法，定义了游戏主循环的骨架，
 * 各种难度的具体行为由子类重写对应的钩子方法实现。
 */
public abstract class Game extends JPanel {
    private int backGroundTop = 0;

    /**
     * 可变帧率计时器（javax.swing.Timer）。
     * 记录每帧实际时间间隔（毫秒），用于精确控制游戏节奏。
     */
    private long cycleTime = 40;
    private long lastTime = 0;
    private javax.swing.Timer actionTimer;

    private final HeroAircraft heroAircraft = HeroAircraft.getInstance();
    protected final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseSupply> supplies;

    protected int enemyMaxNumber = 5;
    protected double enemySpawnCycle = 20;
    private int enemySpawnCounter = 0;

    protected double shootCycle = 20;
    private int shootCounter = 0;
    protected double eliteShootCycle = 25;
    private int eliteShootCounter = 0;

    protected int score = 0;
    private boolean gameOverFlag = false;
    private boolean rankSaved = false;

    protected final Difficulty difficulty;
    protected AudioManager audioManager;

    // ====================== BOSS 相关 ======================
    protected boolean hasBoss = false;
    protected int bossTriggerScore = 300;

    /**
     * 游戏时间计数器（每帧+1），用于难度递进
     */
    protected int gameTime = 0;

    public Game(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.enemyAircrafts = new LinkedList<>();
        this.heroBullets = new LinkedList<>();
        this.enemyBullets = new LinkedList<>();
        this.supplies = new LinkedList<>();
        new HeroController(this, heroAircraft);

        // 音效管理器
        this.audioManager = new AudioManager();

        // 根据难度设置背景图
        ImageManager.setBackgroundByDifficulty(difficulty);

        // 播放背景音乐
        audioManager.playBgm();

        // 使用固定帧率计时器，避免用上一帧时长设下一帧延迟导致级联变慢
        actionTimer = new javax.swing.Timer((int) cycleTime, e -> {
            // 仅用于诊断，不影响定时器延迟
            long currentTime = System.currentTimeMillis();
            if (lastTime != 0) {
                cycleTime = currentTime - lastTime;
            }
            lastTime = currentTime;

            action();
            repaint();
        });
    }

    // ==================== 模板方法 ====================

    /**
     * 游戏主循环（模板方法）。
     * 定义了游戏每帧执行的固定骨架，各步骤由具体子类重写。
     */
    public void action() {
        checkFireSupplyTimeout();     // 火力道具超时检测
        spawnEnemy();                  // 敌机生成（钩子方法）
        spawnBoss();                   // BOSS生成（钩子方法）
        shootAction();                 // 射击（固定）
        bulletsMoveAction();           // 子弹移动（固定）
        aircraftsMoveAction();         // 敌机移动（固定）
        suppliesMoveAction();          // 道具移动（固定）
        crashCheckAction();            // 碰撞检测（固定）
        supplyPickCheckAction();       // 道具拾取（固定）
        increaseDifficulty();         // 难度递进（钩子方法）
        postProcessAction();           // 清理无效对象（固定）
        checkResultAction();           // 结果判定（固定）

        gameTime++;
    }

    // ==================== 钩子方法（子类可重写） ====================

    /**
     * 敌机生成（钩子方法）。
     * 子类可重写以实现不同的敌机生成规则。
     */
    protected void spawnEnemy() {
        enemySpawnCounter++;
        if (enemySpawnCounter >= enemySpawnCycle) {
            enemySpawnCounter = 0;
            if (enemyAircrafts.size() < enemyMaxNumber) {
                double random = Math.random();
                int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
                int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
                AbstractAircraft enemy = createEnemyByRandom(random, x, y);
                if (enemy != null) {
                    enemyAircrafts.add(enemy);
                }
            }
        }
    }

    /**
     * 根据随机值和坐标创建敌机（被子类重写以修改敌机类型概率）。
     */
    protected AbstractAircraft createEnemyByRandom(double random, int x, int y) {
        if (random < 0.4) {
            return new MobFactory().createAircraft(x, y);
        } else if (random < 0.7) {
            return new EliteFactory().createAircraft(x, y);
        } else if (random < 0.9) {
            return new AceFactory().createAircraft(x, y);
        } else {
            return new KingFactory().createAircraft(x, y);
        }
    }

    /**
     * BOSS生成（钩子方法）。
     * 简单难度不能生成BOSS；普通/困难难度可生成。
     */
    protected void spawnBoss() {
        if (score >= bossTriggerScore && !hasBoss) {
            hasBoss = true;
            int x = Main.WINDOW_WIDTH / 2;
            int y = 80;
            AbstractAircraft boss = new BossFactory().createAircraft(x, y);
            enemyAircrafts.add(boss);
            audioManager.playBossBgm();
        }
    }

    /**
     * 难度递进（钩子方法）。
     * 子类重写以实现不同的难度递进逻辑。
     */
    protected void increaseDifficulty() {
        // 基础实现：无难度递进（适用于简单难度）
    }

    // ==================== 射击相关 ====================

    private void shootAction() {
        shootCounter++;
        eliteShootCounter++;

        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            heroBullets.addAll(heroAircraft.shoot());
        }

        if (eliteShootCounter >= eliteShootCycle) {
            eliteShootCounter = 0;
            for (AbstractAircraft enemy : enemyAircrafts) {
                if ((enemy instanceof EliteEnemy || enemy instanceof AceEnemy || enemy instanceof KingEnemy || enemy instanceof BossEnemy)
                        && !enemy.notValid()) {
                    enemyBullets.addAll(enemy.shoot());
                }
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) bullet.forward();
        for (BaseBullet bullet : enemyBullets) bullet.forward();
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) enemyAircraft.forward();
    }

    private void suppliesMoveAction() {
        for (BaseSupply supply : supplies) supply.forward();
    }

    // ==================== 碰撞与道具 ====================

    private void crashCheckAction() {
        if (heroAircraft.notValid()) return;

        // 敌机子弹打英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹打敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) continue;

                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    audioManager.playSound("bullet_hit");

                    if (enemyAircraft.notValid()) {
                        if (enemyAircraft instanceof MobEnemy) score += 10;
                        else if (enemyAircraft instanceof EliteEnemy) score += 20;
                        else if (enemyAircraft instanceof AceEnemy) score += 30;
                        else if (enemyAircraft instanceof KingEnemy) score += 50;
                        else if (enemyAircraft instanceof BossEnemy) score += 200;

                        int x = enemyAircraft.getLocationX();
                        int y = enemyAircraft.getLocationY();

                        if (enemyAircraft instanceof BossEnemy) {
                            for (int i = 0; i < 3; i++) {
                                supplies.add(SupplyFactory.createRandomSupply(x, y));
                            }
                        } else if (!(enemyAircraft instanceof MobEnemy)) {
                            if (Math.random() < 0.6) {
                                BaseSupply supply;
                                if (enemyAircraft instanceof AceEnemy) {
                                    supply = SupplyFactory.createRandomSupplyWithoutIce(x, y);
                                } else {
                                    supply = SupplyFactory.createRandomSupply(x, y);
                                }
                                supplies.add(supply);
                            }
                        }
                    }
                    break;
                }
            }
        }

        // 英雄机撞敌机
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) continue;
            if (enemyAircraft.crash(heroAircraft)) {
                enemyAircraft.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
                if (heroAircraft.notValid()) break;
            }
        }
    }

    private void supplyPickCheckAction() {
        if (heroAircraft.notValid()) return;
        var it = supplies.iterator();
        while (it.hasNext()) {
            BaseSupply supply = it.next();
            if (supply.notValid()) {
                it.remove();
                continue;
            }
            if (heroAircraft.crash(supply)) {
                audioManager.playSound("get_supply");
                if (supply instanceof BombSupply) {
                    audioManager.playSound("bomb_explosion");
                    registerObservers(supply);
                    supply.active(heroAircraft);
                    // 炸弹效果：获得所有坠毁敌机的分数
                    addBombScore();
                } else if (supply instanceof IceSupply) {
                    registerObservers(supply);
                    supply.active(heroAircraft);
                } else {
                    supply.active(heroAircraft);
                }
                it.remove();
            }
        }
    }

    /**
     * 将当前所有敌机和敌机子弹注册为指定道具的观察者。
     */
    private void registerObservers(BaseSupply supply) {
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy instanceof SupplyObserver) {
                supply.addObserver((SupplyObserver) enemy);
            }
        }
        for (BaseBullet bullet : enemyBullets) {
            if (bullet instanceof SupplyObserver) {
                supply.addObserver((SupplyObserver) bullet);
            }
        }
    }

    /**
     * 炸弹效果触发后，为所有坠毁的敌机增加分数。
     * Boss不受影响不加分，KingEnemy不坠落但掉血后死亡需加分。
     */
    private void addBombScore() {
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy) {
                continue;
            } else if (enemy instanceof KingEnemy) {
                // 王牌敌机掉血死亡，加分（不掉落道具）
                if (enemy.notValid()) {
                    score += 50;
                }
            } else {
                // 普通/精英/精锐敌机坠毁，加分
                if (enemy.notValid()) {
                    if (enemy instanceof MobEnemy) score += 10;
                    else if (enemy instanceof EliteEnemy) score += 20;
                    else if (enemy instanceof AceEnemy) score += 30;
                }
            }
        }
    }

    private void checkFireSupplyTimeout() {
        heroAircraft.checkAndRestoreFireSupply();
    }

    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supplies.removeIf(AbstractFlyingObject::notValid);
    }

    private void checkResultAction() {
        if (heroAircraft.getHp() <= 0) {
            actionTimer.stop();
            gameOverFlag = true;
            System.out.println("Game Over!");
            audioManager.stopAll();
            audioManager.playSound("game_over");
            saveAndPrintRankOnce();
        }
    }

    private void saveAndPrintRankOnce() {
        if (rankSaved) return;
        rankSaved = true;

        String playerName;
        while (true) {
            playerName = JOptionPane.showInputDialog(
                    null,
                    "游戏结束！\n您的得分：" + score + "\n请输入您的姓名：",
                    "成绩录入",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (playerName == null) {
                playerName = "匿名玩家";
            } else {
                playerName = playerName.trim();
                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "姓名不能为空，请重新输入！",
                            "输入错误",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }
            }
            break;
        }

        ScoreRecordService service = new ScoreRecordService(new FileScoreRecordDaoImpl(difficulty));
        try {
            service.addRecord(new ScoreRecord(playerName, score, LocalDateTime.now()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
        new edu.hitsz.ui.ScoreRankUI(difficulty);
    }

    /**
     * 启动游戏主循环
     */
    public void actionStart() {
        actionTimer.start();
    }

    /**
     * 工厂方法：根据难度创建对应的游戏实例。
     * 供 DifficultySelectUI 调用，避免直接 new 抽象类 Game。
     */
    public static Game createGame(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> new EasyGame();
            case NORMAL -> new NormalGame();
            case HARD -> new HardGame();
        };
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, backGroundTop, null);
        backGroundTop++;
        if (backGroundTop == Main.WINDOW_HEIGHT) backGroundTop = 0;

        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, supplies);

        g.drawImage(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        paintScoreAndLife(g);
    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) return;
        for (AbstractFlyingObject obj : objects) {
            BufferedImage img = obj.getImage();
            g.drawImage(img,
                    obj.getLocationX() - img.getWidth() / 2,
                    obj.getLocationY() - img.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + score, 10, 25);
        g.drawString("LIFE: " + heroAircraft.getHp(), 10, 45);
    }
}
