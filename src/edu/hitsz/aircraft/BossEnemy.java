package edu.hitsz.aircraft;

import edu.hitsz.shoot.BossCircleShoot;
import edu.hitsz.supply.SupplyObserver;

/**
 * Boss敌机：炸弹和冰冻道具均不受影响。
 * 实现 SupplyObserver 接口（空实现）。
 */
public class BossEnemy extends AbstractAircraft implements SupplyObserver {

    private boolean moveRight = true;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new BossCircleShoot());
    }

    @Override
    public void forward() {
        if (moveRight) {
            locationX += 3;
            if (locationX > 440) moveRight = false;
        } else {
            locationX -= 3;
            if (locationX < 40) moveRight = true;
        }
    }

    // ======== SupplyObserver 实现（Boss 不受炸弹和冰冻影响）=======

    @Override
    public void onIceFreeze(long durationMs) {
        // Boss 敌机不受冰冻影响
    }

    @Override
    public void onIceUnfreeze() {
        // Boss 敌机不受冰冻影响
    }

    @Override
    public void onBombExplode() {
        // Boss 敌机不受炸弹影响
    }
}
