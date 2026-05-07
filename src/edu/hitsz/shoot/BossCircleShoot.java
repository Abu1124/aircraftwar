package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import java.util.LinkedList;
import java.util.List;

public class BossCircleShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getImageHeight() / 2;
        int speed = 6;
        // 20发环射
        for (int i = 0; i < 20; i++) {
            double angle = Math.PI * 2 * i / 20;
            int sx = (int) (speed * Math.cos(angle));
            int sy = (int) (speed * Math.sin(angle));
            res.add(new EnemyBullet(x, y, sx, sy, 25));
        }
        return res;
    }
}