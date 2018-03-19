package transientkey;

import java.io.*;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class RunTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //write Serializable object to file
        TransientTest test = new TransientTest();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\transienttest.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(test);
        objectOutputStream.flush();
        objectOutputStream.close();

        //get Serializable object from file
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D:\\transienttest.txt"));
        TransientTest transientTest = (TransientTest) objectInputStream.readObject();
        System.out.println("a=" + transientTest.getA() + "\t" +"b=" + transientTest.getB());
    }

}
