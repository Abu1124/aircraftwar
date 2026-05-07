package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import java.util.LinkedList;
import java.util.List;

public class FanShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getImageHeight() / 2;
        int speedY = 10;
        res.add(new EnemyBullet(x - 15, y, -2, speedY, 20));
        res.add(new EnemyBullet(x, y, 0, speedY, 20));
        res.add(new EnemyBullet(x + 15, y, 2, speedY, 20));
        return res;
    }
}