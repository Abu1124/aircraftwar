package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.shoot.ScatterShoot;

public class FireSupply extends BaseSupply {
    public FireSupply(int x, int y, int sx, int sy) { super(x, y, sx, sy); }
    @Override
    public void active(HeroAircraft hero) {
        hero.activateFireSupply(new ScatterShoot(), 10000);
        System.out.println("FireSupply active! 切换散射，10秒后恢复。");
        vanish();
    }
}
