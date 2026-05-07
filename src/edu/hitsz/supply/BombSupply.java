package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 炸弹道具：通知所有已注册的观察者触发爆炸效果。
 * 采用观察者模式实现，各观察者响应方式不同：
 * - 普通敌机/精英敌机/精锐敌机：坠毁
 * - 王牌敌机：掉血（不掉落道具）
 * - Boss敌机：不受影响
 * - 敌机子弹：消失
 */
public class BombSupply extends BaseSupply {

    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active! 清除所有敌机！");
        // 通过观察者模式通知所有观察者炸弹爆炸
        notifyBombExplode();
        this.vanish();
    }
}