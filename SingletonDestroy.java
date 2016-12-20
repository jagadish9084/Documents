import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by 1554435 on 12/7/2016.
 */
public class SingletonDestroy {

    public static void main(String[] arg) {

        SingletonDestroyTester.breakSingletonUsingReflection();
        SingletonDestroyTester.breakSingletonSerialization();
        SingletonDestroyTester.breakSingletonUsingCloning();
    }
}

/**
 * This class is to illustrate how singleton is being destroyed by using reflection
 */
class SingletonBP {

    private SingletonBP() {
    }

    private static final class SingletonHelper {
        private static final SingletonBP INSTANCE = new SingletonBP();
    }

    public static SingletonBP getInstance() {

        return SingletonHelper.INSTANCE;
    }
}


/**
 * This class is to illustrate how singleton is being destroyed by serialization and deserialization
 */
class SerializedSingleton implements Serializable {

    private static final long serialVersionUID = -1234567890123L;

    private SerializedSingleton() {
    }

    private static final class SingletonHelper {
        private static final SerializedSingleton INSTANCE = new SerializedSingleton();
    }

    public static SerializedSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}

/**
 * This class is to illustrate how singleton is being destroyed by cloning
 */
class ClonedSingleton implements Cloneable {

    private ClonedSingleton() {
    }

    private static final class SingletonHelper {
        private static final ClonedSingleton INSTANCE = new ClonedSingleton();
    }

    public static ClonedSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


/**
 * This class is to illustrate how singleton is being destroyed by various method
 */
class SingletonDestroyTester {


    /**
     * Breaking singleton using reflection
     */
    public static void breakSingletonUsingReflection() {
        SingletonBP singletonBP1 = SingletonBP.getInstance();
        SingletonBP singletonBP2 = null;
        Constructor[] constructors = SingletonBP.class.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            constructor.setAccessible(true);
            try {
                singletonBP2 = (SingletonBP) constructor.newInstance();
                break;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        System.out.println(singletonBP1 == singletonBP2);
    }

    /**
     * Breaking singleton using Serialization and Deserialization
     */
    public static void breakSingletonSerialization() {

        SerializedSingleton serializedSingleton1 = SerializedSingleton.getInstance();
        SerializedSingleton serializedSingleton2 = null;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("Resource.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serializedSingleton1);

            fileOutputStream.close();
            objectOutputStream.close();

            FileInputStream fileInputStream = new FileInputStream("Resource.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            serializedSingleton2 = (SerializedSingleton) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(serializedSingleton1 == serializedSingleton2);
    }


    /**
     * Breaking singleton using Cloning
     */
    public static void breakSingletonUsingCloning() {

        ClonedSingleton clonedSingleton1 = ClonedSingleton.getInstance();
        ClonedSingleton clonedSingleton2 = null;

        try {
            clonedSingleton2 = (ClonedSingleton) clonedSingleton1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println(clonedSingleton1 == clonedSingleton2);
    }
}



