package edu.hitsz.aircraft;

import edu.hitsz.shoot.DirectShoot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    private HeroAircraft hero;

    @BeforeEach
    void setUp() {
        hero = HeroAircraft.getInstance();
        hero.reset();
    }

    @Test
    @DisplayName("reset() should restore hp and strategy")
    void resetShouldRestoreHpAndStrategy() {
        hero.decreaseHp(30);
        assertTrue(hero.getHp() < hero.getMaxHp());

        hero.reset();
        assertEquals(100, hero.getHp());
        assertNotNull(hero.shoot());
    }

    @Test
    @DisplayName("decreaseHp() should not make hp negative")
    void decreaseHpShouldNotMakeHpNegative() {
        hero.decreaseHp(1000);
        assertEquals(0, hero.getHp());
        assertTrue(hero.notValid(), "hero should vanish when hp <= 0");
    }

    @Test
    @DisplayName("setShootStrategy() should take effect for shoot()")
    void setShootStrategyShouldWork() {
        hero.setShootStrategy(new DirectShoot());
        assertNotNull(hero.shoot());
        assertFalse(hero.shoot().isEmpty(), "direct shoot should produce bullets");
    }
}

