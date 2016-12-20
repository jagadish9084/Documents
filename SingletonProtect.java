import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by 1554435 on 12/13/2016.
 */
public class SingletonProtect {

    public static void main(String[] args) {
        SingletonProtectionTest.breakingSerializableSingletonTest();

        SingletonProtectionTest.singletonCloningTest();

        try {
            SingletonProtectionTest.singletonReflectionTest();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

/**
 * This class is to illustrate how singleton is protected from is being destroyed by Serialization
 */
class SerializableSingleton implements Serializable {

    private SerializableSingleton() {
    }

    private static final class SingletonHelper {
        private static final SerializableSingleton INSTANCE = new SerializableSingleton();
    }

    public static SerializableSingleton getInstance() {

        return SingletonHelper.INSTANCE;
    }

    private Object readResolve() {
        return getInstance();
    }

}

/**
 * This class is to illustrate how singleton is protected from is being destroyed by Cloning
 */
class SingletonClone implements Cloneable {

    private SingletonClone() {
    }

    private static final class SingletonHelper {
        private static final SingletonClone INSTANCE = new SingletonClone();
    }

    public static SingletonClone getInstance() {

        return SingletonHelper.INSTANCE;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("This instance can not support cloning");
    }
}

/**
 * This class is to illustrate how singleton is protected from is being destroyed by Reflection
 */
class SingletonReflection {

    private SingletonReflection() {
        if (null != SingletonHelper.INSTANCE) {
            throw new RuntimeException("Can not create more than one instance of that  class");
        }
    }

    private static final class SingletonHelper {
        private static final SingletonReflection INSTANCE = new SingletonReflection();
    }


    public static SingletonReflection getInstance() {

        return SingletonHelper.INSTANCE;
    }
}


/**
 * This class is to illustrate how singleton is being destroyed by various method
 */
class SingletonProtectionTest {

    public static void breakingSerializableSingletonTest() {

        SerializableSingleton serializableSingleton1 = SerializableSingleton.getInstance();
        SerializableSingleton serializableSingleton2 = null;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("Resource.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serializableSingleton1);

            fileOutputStream.close();
            objectOutputStream.close();

            FileInputStream fileInputStream = new FileInputStream("Resource.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            serializableSingleton2 = (SerializableSingleton) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(serializableSingleton1 == serializableSingleton2);
    }

    public static void singletonCloningTest() {

        SingletonClone singletonClone1 = SingletonClone.getInstance();
        SingletonClone singletonClone2 = null;

        try {
            singletonClone1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println(singletonClone1 == singletonClone2);
    }

    public static void singletonReflectionTest() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        SingletonReflection singletonReflection1 = SingletonReflection.getInstance();
        SingletonReflection singletonReflection2 = null;
        Constructor[] constructors = SingletonReflection.class.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            constructor.setAccessible(true);
            singletonReflection2 = (SingletonReflection) constructor.newInstance();
            break;
        }
        System.out.println(singletonReflection1 == singletonReflection2);
    }


}
