package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

public class CircleShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() - aircraft.getImageHeight() / 2;
        int speed = 5;
        // 8向环射
        res.add(new HeroBullet(x, y, 0, -speed, 30));
        res.add(new HeroBullet(x, y, speed, -speed, 30));
        res.add(new HeroBullet(x, y, speed, 0, 30));
        res.add(new HeroBullet(x, y, speed, speed, 30));
        res.add(new HeroBullet(x, y, 0, speed, 30));
        res.add(new HeroBullet(x, y, -speed, speed, 30));
        res.add(new HeroBullet(x, y, -speed, 0, 30));
        res.add(new HeroBullet(x, y, -speed, -speed, 30));
        return res;
    }
}