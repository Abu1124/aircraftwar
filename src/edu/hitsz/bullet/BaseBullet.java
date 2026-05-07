package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.supply.SupplyObserver;

/**
 * 子弹基类
 * 实现 SupplyObserver 接口，响应道具冰冻通知。
 * @author hitsz
 */
public abstract class BaseBullet extends AbstractFlyingObject implements SupplyObserver {

    private int power = 0;

    /** 是否处于冰冻状态 */
    private volatile boolean frozen = false;
    /** 冰冻失效时间（毫秒），0表示未冰冻 */
    private volatile long freezeEndTime = 0;

    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        // 检查冰冻状态
        if (frozen) {
            if (System.currentTimeMillis() >= freezeEndTime) {
                frozen = false;
                freezeEndTime = 0;
                System.out.println("敌机子弹冰冻结束，恢复移动！");
            } else {
                // 冰冻状态下不移动
                return;
            }
        }

        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public int getPower() {
        return power;
    }

    // ======== SupplyObserver 实现 ========

    /**
     * 冰冻效果：敌机子弹静止5秒后恢复移动。
     */
    @Override
    public void onIceFreeze(long durationMs) {
        this.frozen = true;
        this.freezeEndTime = System.currentTimeMillis() + durationMs;
    }

    /**
     * 解除冰冻：5秒后子弹恢复移动。
     */
    @Override
    public void onIceUnfreeze() {
        this.frozen = false;
        this.freezeEndTime = 0;
    }

    /**
     * 炸弹爆炸：敌机子弹消失。
     */
    @Override
    public void onBombExplode() {
        this.vanish();
    }
}
