package cn.tommyyang.designpatterns.observable;

import cn.tommyyang.designpatterns.observable.display.CurrentConditionsDisplay;

/**
 * 观察者模式
 * WeatherData 类
 *
 * @Author : TommyYang
 * @Time : 2021-04-18 15:51
 * @Software: IntelliJ IDEA
 * @File : WeatherData.java
 */
public class WeatherData {
    /**
     * 温度
     */
    private float temperature;

    /**
     * 湿度
     */
    private float humidity;

    /**
     * 气压
     */
    private float pressure;

    public float getTemperature() {
        return this.temperature;
    }

    public float getHumidity() {
        return this.humidity;
    }

    public float getPressure() {
        return pressure;
    }

    /**
     * 一旦气象测量更新，此方法会被调用
     */
    public void measurementsChanged() {
        float temp = getTemperature();
        float humidity = getHumidity();
        float pressure = getPressure();

        // 布告板数据更新
        // 针对具体的实现编程，导致后续增删布告板时必须修改程序
        CurrentConditionsDisplay currentConditionsDisplay = new CurrentConditionsDisplay();
        currentConditionsDisplay.update(temp, humidity, pressure);
    }

}
