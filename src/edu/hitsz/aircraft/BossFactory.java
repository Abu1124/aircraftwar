package edu.hitsz.aircraft;

public class BossFactory implements AircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int x, int y) {
        // 只创建对象，什么都不加！和你的 MobFactory 完全一样！
        return new BossEnemy(x, y, 0, 0, 500);
    }
}