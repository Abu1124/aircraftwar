package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.shoot.SingleShoot;
import edu.hitsz.supply.SupplyObserver;

/**
 * 精英敌机：被冰冻后静止4秒后恢复。
 * 实现 SupplyObserver 接口，响应道具冰冻通知。
 */
public class EliteEnemy extends AbstractAircraft implements SupplyObserver {

    /** 是否处于冰冻状态 */
    private volatile boolean frozen = false;
    /** 冰冻失效时间（毫秒），0表示未冰冻 */
    private volatile long freezeEndTime = 0;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new SingleShoot());
    }

    @Override
    public void forward() {
        // 冰冻状态下不移动
        if (frozen) {
            // 检查冰冻是否超时
            if (System.currentTimeMillis() >= freezeEndTime) {
                frozen = false;
                freezeEndTime = 0;
                System.out.println("精英敌机冰冻结束，恢复移动！");
            } else {
                return;
            }
        }
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    // ======== SupplyObserver 实现 ========

    /**
     * 冰冻效果：精英敌机静止4秒后恢复。
     */
    @Override
    public void onIceFreeze(long durationMs) {
        this.frozen = true;
        this.freezeEndTime = System.currentTimeMillis() + durationMs;
        System.out.println("精英敌机被冰冻，" + (durationMs / 1000.0) + "秒后恢复！");
    }

    /**
     * 解除冰冻：精英敌机4秒冰冻时间到后恢复移动。
     */
    @Override
    public void onIceUnfreeze() {
        this.frozen = false;
        this.freezeEndTime = 0;
    }

    /**
     * 炸弹爆炸：精英敌机坠毁。
     */
    @Override
    public void onBombExplode() {
        this.vanish();
        System.out.println("精英敌机被炸弹摧毁！");
    }
}
