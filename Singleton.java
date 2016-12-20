/**
 * Created by 1554435 on 12/6/2016.
 */
public class Singleton {


    public static void main(String[] args) {

        //Eager Initialization
        SingletonEagerInit singletonEagerInit1 = SingletonEagerInit.getInstance();
        SingletonEagerInit singletonEagerInit2 = SingletonEagerInit.getInstance();
        System.out.println(singletonEagerInit1 == singletonEagerInit2);

        //Eager Initialization
        SingletonStaticBlockInit singletonStaticBlockInit1 = SingletonStaticBlockInit.getInstance();
        SingletonStaticBlockInit singletonStaticBlockInit2 = SingletonStaticBlockInit.getInstance();
        System.out.println(singletonStaticBlockInit1 == singletonStaticBlockInit2);

        //Eager Initialization
        SingletonLazyInit singletonLazyInit1 = SingletonLazyInit.getInstance();
        SingletonLazyInit singletonLazyInit2 = SingletonLazyInit.getInstance();
        System.out.println(singletonLazyInit1 == singletonLazyInit2);

        //Double check locking Initialization
        SingletonDoubleCheckLocking singletonDoubleCheckLocking1 = SingletonDoubleCheckLocking.getInstance();
        SingletonDoubleCheckLocking singletonDoubleCheckLocking2 = SingletonDoubleCheckLocking.getInstance();
        System.out.println(singletonDoubleCheckLocking1 == singletonDoubleCheckLocking2);

        //Bill Pugh Singleton
        SingletonBillPugh singletonBillPugh1 = SingletonBillPugh.getInstance();
        SingletonBillPugh singletonBillPugh2 = SingletonBillPugh.getInstance();
        System.out.println(singletonBillPugh1 == singletonBillPugh2);

        SingletonEnum singletonEnum1 = SingletonEnum.getInstance();
        SingletonEnum singletonEnum2 = SingletonEnum.getInstance();
        System.out.println(singletonEnum1 == singletonEnum2);
        SingletonEnum.A.doSomething();
    }
}

/**
 * Singleton eager initialization
 */
class SingletonEagerInit {

    private static final SingletonEagerInit INSTANCE = new SingletonEagerInit();

    private SingletonEagerInit() {
    }

    public static SingletonEagerInit getInstance() {
        return INSTANCE;
    }

}

/**
 * Singleton static initialization
 */
class SingletonStaticBlockInit {

    private static SingletonStaticBlockInit instance;

    static {
        instance = new SingletonStaticBlockInit();
    }

    private SingletonStaticBlockInit() {
    }

    public static SingletonStaticBlockInit getInstance() {
        return instance;
    }
}

/**
 * Singleton lazy initialization
 */
class SingletonLazyInit {

    private static SingletonLazyInit instance;

    private SingletonLazyInit() {
    }

    public static SingletonLazyInit getInstance() {

        if (null != instance) {
            instance = new SingletonLazyInit();
        }
        return instance;
    }
}

/**
 * Singleton double checked locking
 * getInstance() method checks two times to see whether INSTANCE is null or not
 * and that’s why it’s called double checked locking pattern,
 * remember that double checked locking is broker before Java 5 but
 * with the guranteed of volatile variable in Java 5 memory model, it should work perfectly.
 */
class SingletonDoubleCheckLocking {

    private static SingletonDoubleCheckLocking singletonDoubleCheckLocking;

    private SingletonDoubleCheckLocking() {
    }

    public static SingletonDoubleCheckLocking getInstance() {

        if (null == singletonDoubleCheckLocking) {
            synchronized (SingletonDoubleCheckLocking.class) {

                if (null == singletonDoubleCheckLocking) {
                    singletonDoubleCheckLocking = new SingletonDoubleCheckLocking();
                }
            }
        }
        return singletonDoubleCheckLocking;
    }
}

/**
 * Singleton Bill Pugh
 */
class SingletonBillPugh {

    private SingletonBillPugh() {
    }

    private static final class SingletonHelper {

        private static final SingletonBillPugh INSTANCE = new SingletonBillPugh();
    }

    public static SingletonBillPugh getInstance() {

        return SingletonHelper.INSTANCE;
    }

}

/**
 * Enum has implicit private constructor
 * Enum Singleton are easy to write
 */
enum SingletonEnum {
    INSTANCE,
    A("text1"),
    B("text2"),
    C("text3"),
    D("text4");


    SingletonEnum(String text1) {

    }


    SingletonEnum() {

    }

    public void doSomething() {
    }

    // Static getter
    public static SingletonEnum getInstance() {
        return INSTANCE;
    }

}
