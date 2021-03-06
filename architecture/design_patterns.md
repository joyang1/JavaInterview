# 设计模式
该章节主要是在读《Head First 设计模式》的总结。

## 策略模式
案例：鸭子应用（模拟鸭子游戏）。
游戏中需要设计各种鸭子，有戏水的、呱呱叫的。用学习过的OO技术，张三（鸭子游戏“首席架构师”）首先会设计一个鸭子超类，并让各种鸭子继承该超类。
![OO类图1](https://cdn.jsdelivr.net/gh/filess/img10@main/2021/04/06/1617640082714-8a14a0d7-73d1-471a-893f-a94fe3a5966f.jpg)

类图中，超类Duck实现了鸭子呱呱叫（quack）和游泳（swim），由于鸭子外观都不同，所以display()方法是抽象的，由子类去实现。
具体代码在该项目的位置：JavaInterview/architecture/src/main/java/cn.tommyyang.designpatterns.duckgame.Duck。

以上完成第一版鸭子应用。

由于鸭子游戏鸭子种类一直就那么几种，导致游戏用户增长上遇到了较大的瓶颈，所以急需创新。通过大家的头脑风暴，首先我们得让鸭子飞起来。这时，负责鸭子应用的开发人员张三接到了这个需求，认为只需在Duck超类上加上fly方法即可。设计如下：
![OO类图2](https://cdn.jsdelivr.net/gh/filess/img16@main/2021/04/06/1617640156038-1a4f4a43-5071-4ca9-a1a9-055d154629cd.jpg)

改完后，完成了游戏的第二次发版。改动点：
- 加入鸭子会飞的逻辑。
- 有鸭子的叫声不是呱呱叫，而是吱吱叫。

发布完成后，在公司内部开始进行试运行。试运行期间，同事们说，为什么橡皮鸭也会飞啊。张三收到了反馈后，他发现出问题了，不能直接在超类进行fly()方法的实现，或者在橡皮鸭内部覆盖fly方法，然后什么都不做。
如果后面有几万种鸭子，比如加入了诱饵鸭（是木头鸭），不会飞也不会叫，那怎么办，也去覆盖quack、fly方法？那这样继承这些方法有什么意义呢？
看到这里，不知道大家怎么想？

首先总结下利用继承来提供Duck的行为会导致哪些缺点：
- 代码在多个子类中重复。
- 运行时的行为不容易改变。
- 不能让鸭子跳舞。
- 很难知道所有鸭子的全部行为。
- 鸭子不能同时又飞又叫。
- 改变会牵一发动全身，造成其他鸭子不想要的改变。

通过在游戏的第二版发布过程中，张三认识到继承无法解决在当下游戏版本经常更新的情况。张三知道鸭子的规则会经常改变，每当游戏中新增新的鸭子时，就需要检查是否需要覆盖fly()/quack()......等方法，张三作为“首席架构师”，显然觉得这样的架构太low了，无法容忍。

故张三认为需要定义一些更清晰的接口，让“某些”（非全部）鸭子类型可飞/可叫......以后还可能扩展更多方法。
新的架构图如下：
![接口类图1](https://cdn.jsdelivr.net/gh/filess/img6@main/2021/04/06/1617640189105-a5fc6182-7ad7-41d1-be45-d88e3e642903.jpg)

以上是张三使用接口实现的，但是张三对着类图思考了一会，发现虽然Flyable和Quackable可以解决“一小部分”问题（不会出现会飞的橡皮鸭），但是却造成了大量重复代码（大量的重复飞，重复嘎嘎叫），但代码却无法复用，甚至，在会飞的鸭子中，飞行的动作还可能各种变化......

作为“首席架构师”，张三还是觉得否定了上述方案，深思许久后，总结道：
- 继承无法解决问题。
- Flyable、Quackable接口不错，但是需要让会飞的鸭子才继承Flybale。
- Java接口不具有实现代码（java8 default接口可以，但是不鼓励使用），所以继承接口无法达到代码的复用。

这时，张三突然想到从《Head First 设计模式》中看到的第一个设计模式中提到的三个原则：
- 找出应用中可能需要变化之处，把他们独立出来，不要和那些不需要变化的代码混在一起。
- 针对接口编程，而不是针对实现编程。
- 多用组合，少用继承。

通过上述三个原则，张三知道，Duck类中fly()和quack()会随着鸭子的不同而改变。那么需要把这两个行为从Duck类中取出来，建立一组新类来代表每个行为。及游戏中需要**鸭子类**和`鸭子行为类`。`鸭子行为类`专门用于提供某行为接口的实现，那么鸭子类就不需要了解行为的实现细节了。这就是`针对接口编程，而不是针对实现编程`。

了解到这些设计原则后，张三开始重新设计类图。
![策略模式类图](https://cdn.jsdelivr.net/gh/filess/img16@main/2021/04/06/1617640242705-5b21c883-4753-46b4-8aab-885fe5be2894.jpg)

通过以上设计，关键在于，鸭子将飞行和呱呱叫的动作“委托”（delegate）别人处理，而不是使用定义在Duck类（或子类）内的呱呱叫和飞行方法。
这样的好处，就是上述的三个原则，并且在运行时，可以随意修改其呱呱叫和飞行的行为，通过setFlyBehavior和setQuackBehavior来实现。

MallardDuck 的具体实现如下：

``` java

/**
 * 野鸭类
 *
 * @Author : TommyYang
 * @Time : 2021-04-05 16:00
 * @Software: IntelliJ IDEA
 * @File : MallardDuck.java
 */
public class MallardDuck extends Duck {

    public MallardDuck(FlyBehavior flyBehavior, QuackBehavior quackBehavior) {
        super(flyBehavior, quackBehavior);
    }

    /**
     * 具体实现鸭子外观
     */
    public void display() {
        System.out.println("绿头");
    }
}

```

在构造野鸭类的时候，完成 FlyBehavior 和 QuackBehavior 的定义。

最后，张三“首席架构师”，通过`策略模式（Strategy Pattern）`实现了鸭子游戏。

`策略模式`定义了算法簇，分别封装起来，让它们之间可以互相替换，此模式让算法的变化独立于使用算法的客户。

恭喜你，完成了第一个设计模式`策略模式`的学习，后续希望该模式在你的编程生涯中可以持续被使用。

## 观察者模式
张三由于通过使用`策略模式`完成鸭子游戏应用的重构后，在鸭子游戏的维护和更新上只需要投入少量的时间。
于是张三接到了下一个需求，*互联网气象观测站*的架构工作。

*互联网气象观测站*的三个组成部分：
- 气象站（获得实际气象数据的物理装置）
- WeatherData 对象（追踪来自气象站的数据，并更新布告板）
- 布告板（显示当前天气状况）给用户看

张三总结出系统用例如下：

张三认为，如果团队接受了这个项目，那工作就是建立一个应应用，利用 WeatherData 对象取得数据（由公司兄弟团队去和气象站对接），并更新三个布告板：当前状况、气象统计、天气预报。

张三团队开始了工作，第二天由公司兄弟团队提供的获取相关气象数据的 WeatherData 类，类图如下：

下面看一下实现的反面案例：针对具体实现编程。
具体代码在该项目的位置：[JavaInterview](https://github.com/joyang1/JavaInterview)；
JavaInterview/architecture/src/main/java/cn.tommyyang.designpatterns.observable.WeatherData。

```java

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

```
回故下上一篇讲解的一些设计原则：
- 找出应用中可能需要变化之处，把他们独立出来，不要和那些不需要变化的代码混在一起。
- 针对接口编程，而不是针对实现编程。
- 多用组合，少用继承。

不难发现上面的代码非常*垃圾*。

下面由首席架构师张三带大家使用观察者模式来实现该工程。

`观察者模式`：定义了对象之间的一对多依赖，这样一来，当一个对象改变状态时，它的所有依赖者都会收到通知并自动更新。
张三画了一张简易的观察者模式图如下：

## 装饰模式

## 工厂模式
### 简单工厂模式

### 工厂方法模式