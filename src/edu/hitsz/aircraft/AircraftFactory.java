package edu.hitsz.aircraft;

/**
 * 敌机抽象工厂：定义敌机创建的统一接口
 * 所有具体敌机工厂均实现此接口
 * @author hitsz
 */
public interface AircraftFactory {
    /**
     * 敌机创建方法：由具体工厂实现，自定义敌机属性
     * @param x 生成X坐标
     * @param y 生成Y坐标
     * @return 对应类型的AbstractAircraft实例
     */
    AbstractAircraft createAircraft(int x, int y);
}