package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.shoot.NoShoot;
import edu.hitsz.supply.SupplyObserver;
import java.util.List;

/**
 * 普通敌机：被冰冻后永久静止（不消失），解除冰冻时无操作。
 * 实现 SupplyObserver 接口，响应道具冰冻通知。
 */
public class MobEnemy extends AbstractAircraft implements SupplyObserver {

    /** 是否处于冰冻状态 */
    private volatile boolean frozen = false;

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new NoShoot());
    }

    @Override
    public void forward() {
        // 冰冻状态下不移动
        if (frozen) {
            return;
        }
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    // ======== SupplyObserver 实现 ========

    /**
     * 冰冻效果：普通敌机永久静止。
     * durationMs <= 0 视为永久冰冻，onIceUnfreeze 不做处理。
     */
    @Override
    public void onIceFreeze(long durationMs) {
        this.frozen = true;
        System.out.println("普通敌机被冰冻，永久静止！");
    }

    /**
     * 解除冰冻：普通敌机被冰冻后永久静止，此方法不处理任何操作。
     */
    @Override
    public void onIceUnfreeze() {
        // 永久冰冻，不恢复
    }

    /**
     * 炸弹爆炸：普通敌机坠毁。
     */
    @Override
    public void onBombExplode() {
        this.vanish();
        System.out.println("普通敌机被炸弹摧毁！");
    }
}
