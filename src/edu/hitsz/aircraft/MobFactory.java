package edu.hitsz.aircraft;

/**
 * 普通敌机工厂：工厂方法模式，负责创建MobEnemy
 * @author hitsz
 */
public class MobFactory implements AircraftFactory {
    // 普通敌机固定属性：便于统一修改
    private static final int SPEED_X = 0;
    private static final int SPEED_Y = 10;
    private static final int HP = 30;

    @Override
    public AbstractAircraft createAircraft(int x, int y) {
        return new MobEnemy(x, y, SPEED_X, SPEED_Y, HP);
    }
}