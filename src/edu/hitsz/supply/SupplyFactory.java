package edu.hitsz.supply;

/**
 * 道具简单工厂：负责所有道具的统一创建
 * @author hitsz
 */
public class SupplyFactory {
    // 道具类型常量
    public static final String TYPE_BLOOD = "blood";    // 加血
    public static final String TYPE_FIRE = "fire";      // 基础火力
    public static final String TYPE_FIRE_PLUS = "firePlus";// 超级火力
    public static final String TYPE_BOMB = "bomb";      // 炸弹
    public static final String TYPE_ICE = "ice";        // 冰冻

    // 道具移动速度：统一配置，便于修改
    private static final int SUPPLY_SPEED_X = 0;
    private static final int SUPPLY_SPEED_Y = 5;

    /**
     * 静态创建方法：创建指定类型的道具
     * @param type 道具类型（使用工厂常量）
     * @param x 生成X坐标（敌机坠毁位置）
     * @param y 生成Y坐标（敌机坠毁位置）
     * @return 对应类型的BaseSupply实例
     * @throws IllegalArgumentException 未知道具类型时抛出
     */
    public static BaseSupply createSupply(String type, int x, int y) {
        BaseSupply supply;
        switch (type) {
            case TYPE_BLOOD:
                supply = new BloodSupply(x, y, SUPPLY_SPEED_X, SUPPLY_SPEED_Y);
                break;
            case TYPE_FIRE:
                supply = new FireSupply(x, y, SUPPLY_SPEED_X, SUPPLY_SPEED_Y);
                break;
            case TYPE_FIRE_PLUS:
                supply = new FirePlusSupply(x, y, SUPPLY_SPEED_X, SUPPLY_SPEED_Y);
                break;
            case TYPE_BOMB:
                supply = new BombSupply(x, y, SUPPLY_SPEED_X, SUPPLY_SPEED_Y);
                break;
            case TYPE_ICE:
                supply = new IceSupply(x, y, SUPPLY_SPEED_X, SUPPLY_SPEED_Y);
                break;
            default:
                throw new IllegalArgumentException("未知的道具类型：" + type);
        }
        return supply;
    }

    /**
     * 重载方法：随机创建一种道具（供敌机坠毁时调用）
     * @param x 生成X坐标
     * @param y 生成Y坐标
     * @return 随机的BaseSupply实例
     */
    public static BaseSupply createRandomSupply(int x, int y) {
        // 随机生成0-4，对应5种道具
        int random = (int) (Math.random() * 5);
        BaseSupply supply;
        switch (random) {
            case 0:
                supply = createSupply(TYPE_BLOOD, x, y);
                break;
            case 1:
                supply = createSupply(TYPE_FIRE, x, y);
                break;
            case 2:
                supply = createSupply(TYPE_FIRE_PLUS, x, y);
                break;
            case 3:
                supply = createSupply(TYPE_BOMB, x, y);
                break;
            case 4:
                supply = createSupply(TYPE_ICE, x, y);
                break;
            default:
                throw new IllegalArgumentException("随机数超出范围：" + random);
        }
        return supply;
    }

    /**
     * 重载方法：随机创建非冰冻道具（供精锐敌机坠毁时调用，实验要求）
     * @param x 生成X坐标
     * @param y 生成Y坐标
     * @return 随机的非冰冻BaseSupply实例
     */
    public static BaseSupply createRandomSupplyWithoutIce(int x, int y) {
        // 随机生成0-3，对应4种非冰冻道具
        int random = (int) (Math.random() * 4);
        BaseSupply supply;
        switch (random) {
            case 0:
                supply = createSupply(TYPE_BLOOD, x, y);
                break;
            case 1:
                supply = createSupply(TYPE_FIRE, x, y);
                break;
            case 2:
                supply = createSupply(TYPE_FIRE_PLUS, x, y);
                break;
            case 3:
                supply = createSupply(TYPE_BOMB, x, y);
                break;
            default:
                throw new IllegalArgumentException("随机数超出范围：" + random);
        }
        return supply;
    }
}