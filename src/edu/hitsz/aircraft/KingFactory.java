package edu.hitsz.aircraft;

/**
 * 王牌敌机工厂：工厂方法模式，负责创建KingEnemy
 * @author hitsz
 */
public class KingFactory implements AircraftFactory {
    // 王牌敌机固定属性：血量/速度为所有敌机最高
    private static final int SPEED_X = 0;
    private static final int SPEED_Y = 6;
    private static final int HP = 150;

    @Override
    public AbstractAircraft createAircraft(int x, int y) {
        return new KingEnemy(x, y, SPEED_X, SPEED_Y, HP);
    }
}