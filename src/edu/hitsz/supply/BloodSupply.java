package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 加血道具
 * 恢复英雄机血量，不超过初始最大值
 * @author hitsz
 */
public class BloodSupply extends BaseSupply {
    // 加血值
    private static final int BLOOD_ADD = 50;

    public BloodSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        // 计算新血量，不超过初始最大值
        int newHp = Math.min(heroAircraft.getHp() + BLOOD_ADD, heroAircraft.getMaxHp());
        heroAircraft.setHp(newHp);
        this.vanish(); // 道具生效后消失
    }
}