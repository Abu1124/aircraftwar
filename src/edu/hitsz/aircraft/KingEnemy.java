package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.shoot.FanShoot;
import edu.hitsz.supply.SupplyObserver;

import java.util.Random;

/**
 * 王牌敌机：被冰冻后减速5秒后恢复。
 * 实现 SupplyObserver 接口，响应道具冰冻通知。
 */
public class KingEnemy extends AbstractAircraft implements SupplyObserver {

    private static final Random RANDOM = new Random();

    /** 水平漂移速度，每隔 DRIFT_INTERVAL 帧重新随机 */
    private int driftSpeedX = 0;
    /** 漂移计时器 */
    private int driftCounter = 0;
    /** 漂移间隔（帧），每 DRIFT_INTERVAL 帧重新随机漂移速度 */
    private static final int DRIFT_INTERVAL = 30;

    /** 原始 Y 轴速度（冰冻恢复时使用） */
    private final int originalSpeedY;

    /** 是否处于冰冻/减速状态 */
    private volatile boolean frozen = false;
    /** 冰冻失效时间（毫秒），0表示未冰冻 */
    private volatile long freezeEndTime = 0;

    public KingEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new FanShoot());
        this.originalSpeedY = speedY;
    }

    @Override
    public void forward() {
        // 冰冻/减速状态下减速
        if (frozen) {
            if (System.currentTimeMillis() >= freezeEndTime) {
                // 冰冻结束，恢复速度
                frozen = false;
                freezeEndTime = 0;
                speedY = originalSpeedY;
                System.out.println("王牌敌机减速效果结束，恢复正常速度！");
            } else {
                // 减速状态：Y速度降为1/3
                speedY = Math.max(1, originalSpeedY / 3);
            }
        }
        super.forward();
        // 逐渐水平漂移：每隔 DRIFT_INTERVAL 帧重新随机一个漂移速度
        driftCounter++;
        if (driftCounter >= DRIFT_INTERVAL) {
            driftCounter = 0;
            driftSpeedX = RANDOM.nextInt(7) - 3; // -3 到 +3
        }
        locationX += driftSpeedX;
        if (locationX < 0) {
            locationX = 0;
            driftSpeedX = Math.abs(driftSpeedX); // 碰边反向
        }
        if (locationX > Main.WINDOW_WIDTH) {
            locationX = Main.WINDOW_WIDTH;
            driftSpeedX = -Math.abs(driftSpeedX);
        }
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    // ======== SupplyObserver 实现 ========

    /**
     * 冰冻效果：王牌敌机减速5秒后恢复。
     */
    @Override
    public void onIceFreeze(long durationMs) {
        this.frozen = true;
        this.freezeEndTime = System.currentTimeMillis() + durationMs;
        System.out.println("王牌敌机被减速，" + (durationMs / 1000.0) + "秒后恢复！");
    }

    /**
     * 解除减速：王牌敌机5秒减速时间到后恢复正常速度。
     */
    @Override
    public void onIceUnfreeze() {
        this.frozen = false;
        this.freezeEndTime = 0;
        this.speedY = originalSpeedY;
    }

    /**
     * 炸弹爆炸：王牌敌机掉血（不掉落道具）。\n     */
    @Override
    public void onBombExplode() {
        this.decreaseHp(30);
        System.out.println("王牌敌机被炸弹炸伤，损失30点血量！");
    }
}
