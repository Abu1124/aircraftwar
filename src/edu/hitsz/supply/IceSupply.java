package edu.hitsz.supply;

import edu.hitsz.aircraft.AceEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;

/**
 * 冰冻道具：通知所有已注册的观察者（敌机、子弹）进入冰冻状态。
 * 采用观察者模式实现，各观察者使用不同持续时间：
 * - 普通敌机：永久静止
 * - 精英敌机：静止4秒
 * - 精锐敌机：静止3秒
 * - 王牌敌机：减速5秒
 * - 敌机子弹：静止5秒
 */
public class IceSupply extends BaseSupply {

    private static final long DURATION_ELITE = 4000;   // 精英4秒
    private static final long DURATION_ACE = 3000;    // 精锐3秒
    private static final long DURATION_OTHERS = 5000;  // 王牌减速5秒，子弹静止5秒

    public IceSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        System.out.println("IceSupply active! 冰冻效果触发！");
        // 根据观察者类型使用不同冰冻持续时间
        for (SupplyObserver o : getObservers()) {
            long duration = getFreezeDuration(o);
            o.onIceFreeze(duration);
        }
        this.vanish();
    }

    /**
     * 根据观察者类型返回对应的冰冻持续时间
     */
    private long getFreezeDuration(SupplyObserver observer) {
        if (observer instanceof EliteEnemy) {
            return DURATION_ELITE;
        } else if (observer instanceof AceEnemy) {
            return DURATION_ACE;
        } else {
            // MobEnemy、王牌敌机、普通子弹 均使用5秒/永久
            return DURATION_OTHERS;
        }
    }
}