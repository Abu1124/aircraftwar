package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import java.util.List;

/**
 * 射击策略接口（抽象策略）
 */
public interface ShootStrategy {
    List<BaseBullet> shoot(AbstractAircraft aircraft);
}