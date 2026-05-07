package edu.hitsz.supply;

/**
 * 道具效果观察者接口。
 * 实现此接口的对象可以订阅道具效果通知（炸弹、冰冻）。
 * 注意：各观察者根据自身类型决定具体响应方式。
 *
 * @author hitsz
 */
public interface SupplyObserver {

    /**
     * 炸弹爆炸通知。
     * 各观察者根据自身类型决定响应方式：
     * - MobEnemy/EliteEnemy/AceEnemy: 坠毁
     * - KingEnemy: 掉血（不掉落道具）
     * - BossEnemy: 不受影响（空实现）
     * - EnemyBullet: 消失
     */
    void onBombExplode();

    /**
     * 冰冻效果通知。
     * 各观察者根据自身类型决定冰冻持续时间：
     * - MobEnemy: 永久冰冻（durationMs <= 0）
     * - EliteEnemy: 冰冻4秒
     * - AceEnemy: 冰冻3秒
     * - KingEnemy: 减速5秒
     * - BossEnemy: 不受影响（空实现）
     * - EnemyBullet: 静止5秒
     *
     * @param durationMs 冰冻持续时间（毫秒），<=0 表示永久冰冻
     */
    void onIceFreeze(long durationMs);

    /**
     * 解除冰冻效果通知。
     * 冰冻计时结束时由 Subject 通知各观察者。
     */
    void onIceUnfreeze();
}
