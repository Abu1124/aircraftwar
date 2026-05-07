package edu.hitsz.aircraft;

/**
 * 精锐敌机工厂：工厂方法模式，负责创建AceEnemy
 * @author hitsz
 */
public class AceFactory implements AircraftFactory {
    // 精锐敌机固定属性：血量/速度高于精英敌机
    private static final int SPEED_X = 0;
    private static final int SPEED_Y = 7;
    private static final int HP = 100;

    @Override
    public AbstractAircraft createAircraft(int x, int y) {
        return new AceEnemy(x, y, SPEED_X, SPEED_Y, HP);
    }
}