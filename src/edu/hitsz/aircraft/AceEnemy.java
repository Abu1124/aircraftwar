package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.shoot.DoubleShoot;
import edu.hitsz.supply.SupplyObserver;

import java.util.Random;

/**
 * 精锐敌机：被冰冻后静止3秒后恢复。
 * 实现 SupplyObserver 接口，响应道具冰冻通知。
 */
public class AceEnemy extends AbstractAircraft implements SupplyObserver {

    private static final Random RANDOM = new Random();

    /** 水平漂移速度，每隔 DRIFT_INTERVAL 帧重新随机 */
    private int driftSpeedX = 0;
    /** 漂移计时器 */
    private int driftCounter = 0;
    /** 漂移间隔（帧），每 DRIFT_INTERVAL 帧重新随机漂移速度 */
    private static final int DRIFT_INTERVAL = 30;

    /** 是否处于冰冻状态 */
    private volatile boolean frozen = false;
    /** 冰冻失效时间（毫秒），0表示未冰冻 */
    private volatile long freezeEndTime = 0;

    public AceEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new DoubleShoot());
    }

    @Override
    public void forward() {
        // 冰冻状态下不移动
        if (frozen) {
            // 检查冰冻是否超时
            if (System.currentTimeMillis() >= freezeEndTime) {
                frozen = false;
                freezeEndTime = 0;
                System.out.println("精锐敌机冰冻结束，恢复移动！");
            } else {
                return;
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
     * 冰冻效果：精锐敌机静止3秒后恢复。
     */
    @Override
    public void onIceFreeze(long durationMs) {
        this.frozen = true;
        this.freezeEndTime = System.currentTimeMillis() + durationMs;
        System.out.println("精锐敌机被冰冻，" + (durationMs / 1000.0) + "秒后恢复！");
    }

    /**
     * 解除冰冻：精锐敌机3秒冰冻时间到后恢复移动。
     */
    @Override
    public void onIceUnfreeze() {
        this.frozen = false;
        this.freezeEndTime = 0;
    }

    /**
     * 炸弹爆炸：精锐敌机坠毁。
     */
    @Override
    public void onBombExplode() {
        this.vanish();
        System.out.println("精锐敌机被炸弹摧毁！");
    }
}
