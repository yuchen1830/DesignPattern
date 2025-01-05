package org.example;

public class SingletonDemo {
    public static void main(String[] args) {

    }
}

// eager loading: may cause memory issues
class Singleton1 {
    private static final Singleton1 instance = new Singleton1(); // private for encapsulation
    // private constructor ensures the instance is only created here
    private Singleton1(){}
    public static Singleton1 getInstance() {
         return instance;
    }
}

// lazy loading, double check locking
class Singleton2 {
    private static volatile Singleton2 instance; // no assignment to `static`
    private Singleton2(){}

    // without synchronized, t1 and t2 might create 2 instances at a time
    public static Singleton2 getInstance(){ // static method synchronized is class level lock
        if(instance == null) {
            // we only need to lock this method at the first instantiation
            synchronized(Singleton2.class) {
                if(instance == null) {
                    instance = new Singleton2();
                    // what is happening here?
                    // 1. create instance reference
                    // 2. new the instance object in heap
                    // 3. assign the reference to the object
                }
            }
        }
        return instance;
    }
}
