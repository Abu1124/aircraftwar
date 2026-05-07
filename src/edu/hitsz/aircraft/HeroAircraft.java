package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shoot.DirectShoot;
import edu.hitsz.shoot.ShootStrategy;

import java.util.List;

public class HeroAircraft extends AbstractAircraft {


    private static final HeroAircraft INSTANCE = new HeroAircraft(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
            0, 0, 100, new DirectShoot()
    );

    // ============= 火力道具持续时间 =============
    /**
     * 火力道具激活标志。true 表示当前处于火力道具增益状态。
     */
    private volatile boolean fireSupplyActive = false;

    /**
     * 火力道具失效时间（毫秒），失效后自动恢复直射。
     */
    private volatile long fireSupplyEndTime = 0;

    /**
     * 激活火力道具效果，切换到指定射击策略，持续指定毫秒后自动恢复直射。
     *
     * @param strategy  要切换到的射击策略（散射/环射等）
     * @param durationMs 持续时间（毫秒）
     */
    public void activateFireSupply(ShootStrategy strategy, long durationMs) {
        setShootStrategy(strategy);
        this.fireSupplyActive = true;
        this.fireSupplyEndTime = System.currentTimeMillis() + durationMs;
    }

    /**
     * 检查火力道具是否仍在生效。
     */
    public boolean isFireSupplyActive() {
        return fireSupplyActive && System.currentTimeMillis() < fireSupplyEndTime;
    }

    /**
     * 每帧检测：火力道具超时则自动恢复为直射弹道。
     * 在游戏主循环的 action() 中调用。
     */
    public void checkAndRestoreFireSupply() {
        if (fireSupplyActive && System.currentTimeMillis() >= fireSupplyEndTime) {
            fireSupplyActive = false;
            fireSupplyEndTime = 0;
            setShootStrategy(new DirectShoot());
            System.out.println("火力道具效果结束，恢复直射状态。");
        }
    }

    // 私有构造，参数完全保留
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, DirectShoot strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    public static HeroAircraft getInstance() {
        return INSTANCE;
    }

    @Override
    public void forward() {
        // 英雄机鼠标控制，不变
    }

    // 射击交给策略，这里删掉原有射击逻辑
    @Override
    public List<BaseBullet> shoot() {
        return super.shoot();
    }

    // 重置方法不变
    public void reset() {
        this.setHp(100);
        this.setLocation(Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight());
        this.setShootStrategy(new DirectShoot());
        this.fireSupplyActive = false;
        this.fireSupplyEndTime = 0;
        this.isValid = true;  // 重置有效状态，英雄机不再"死亡"
    }
}
