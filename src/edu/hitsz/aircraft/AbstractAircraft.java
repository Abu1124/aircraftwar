package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.shoot.ShootStrategy;
import java.util.List;

public abstract class AbstractAircraft extends AbstractFlyingObject {
    protected int maxHp;
    protected int hp;
    protected ShootStrategy shootStrategy;

    // 构造方法完全保留你原来的参数！！
    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy strategy) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shootStrategy = strategy;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(this);
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * 获取飞机图片的高度（像素）。
     * 子类可重写以返回精确值，默认通过 ImageManager 查找。
     */
    public int getImageHeight() {
        var img = ImageManager.get(this);
        if (img != null) {
            return img.getHeight();
        }
        return 50; // 默认值
    }
}