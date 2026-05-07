package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import java.util.LinkedList;
import java.util.List;

public class DoubleShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getImageHeight() / 2;
        int speedY = aircraft.getSpeedY() + 5;
        res.add(new EnemyBullet(x - 10, y, 0, speedY, 15));
        res.add(new EnemyBullet(x + 10, y, 0, speedY, 15));
        return res;
    }
}