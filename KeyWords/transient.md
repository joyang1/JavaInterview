# 关键字-transient 

## 介绍
词义：短暂的

首先说说“序列化”，把一个对象的表示转化为字节流的过程称为串行化（也称为序列化，serialization），
从字节流中把对象重建出来称为反串行化（也称为为反序列化，deserialization）。
transient 为不应被串行化的数据提供了一个语言级的标记数据方法。

## 代码测试
```

public class TransientTest implements Serializable{

    private static final long serialVersionUID = -2670851086407643335L;

    private transient int a = 1; //不会被序列化(持久化)，将该对象保存到磁盘中的时候，该属性不会被保存

    private int b = 8;

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}

//测试类
public class RunTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //write Serializable object to file
        TransientTest test = new TransientTest();
        FileOutputStream fileOutputStream = new FileOutputStream("KeyWords/res/transienttest.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(test);
        objectOutputStream.flush();
        objectOutputStream.close();

        //get Serializable object from file
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("KeyWords/res/transienttest.txt"));
        TransientTest transientTest = (TransientTest) objectInputStream.readObject();
        System.out.println("a=" + transientTest.getA() + "\t" +"b=" + transientTest.getB());
    }

}

```

## transienttest.txt文件内容
[transienttest.txt](res/transienttest.txt)


## 结果

![如图](res/transient.png)
