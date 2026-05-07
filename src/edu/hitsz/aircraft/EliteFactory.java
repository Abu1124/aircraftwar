package edu.hitsz.aircraft;

/**
 * 精英敌机工厂：工厂方法模式，负责创建EliteEnemy
 * @author hitsz
 */
public class EliteFactory implements AircraftFactory {
    // 精英敌机固定属性
    private static final int SPEED_X = 0;
    private static final int SPEED_Y = 8;
    private static final int HP = 60;

    @Override
    public AbstractAircraft createAircraft(int x, int y) {
        return new EliteEnemy(x, y, SPEED_X, SPEED_Y, HP);
    }
}