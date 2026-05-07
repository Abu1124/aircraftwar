package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.application.Main;

import java.util.LinkedList;
import java.util.List;

/**
 * 道具基类（同时作为观察者模式中的抽象观察目标）
 * @author hitsz
 */
public abstract class BaseSupply extends AbstractFlyingObject {

    /** 订阅本道具效果的所有观察者 */
    private final List<SupplyObserver> observers = new LinkedList<>();

    public BaseSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 道具向下飞行，出界则消失
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    /**
     * 道具生效方法
     * 不同道具实现不同生效逻辑
     * @param heroAircraft 英雄机
     */
    public abstract void active(HeroAircraft heroAircraft);

    // ==================== 观察者模式：抽象观察目标 ====================

    /**
     * 注册观察者
     */
    public void addObserver(SupplyObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * 注销观察者
     */
    public void removeObserver(SupplyObserver observer) {
        observers.remove(observer);
    }

    /**
     * 获取所有已注册的观察者（子类可访问，用于自定义通知逻辑）
     */
    protected List<SupplyObserver> getObservers() {
        return observers;
    }

    /**
     * 通知所有观察者冰冻效果（统一持续时间）
     */
    protected void notifyIceFreeze(long durationMs) {
        for (SupplyObserver o : observers) {
            o.onIceFreeze(durationMs);
        }
    }

    /**
     * 通知所有观察者解除冰冻
     */
    protected void notifyIceUnfreeze() {
        for (SupplyObserver o : observers) {
            o.onIceUnfreeze();
        }
    }

    /**
     * 通知所有观察者炸弹爆炸
     */
    protected void notifyBombExplode() {
        for (SupplyObserver o : observers) {
            o.onBombExplode();
        }
    }
}