package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.shoot.CircleShoot;

public class FirePlusSupply extends BaseSupply {
    public FirePlusSupply(int x, int y, int sx, int sy) { super(x, y, sx, sy); }
    @Override
    public void active(HeroAircraft hero) {
        hero.activateFireSupply(new CircleShoot(), 10000);
        System.out.println("FirePlusSupply active! 切换环射，10秒后恢复。");
        vanish();
    }
}
