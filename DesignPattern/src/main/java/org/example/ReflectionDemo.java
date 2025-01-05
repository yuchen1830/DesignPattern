package org.example;

import java.beans.BeanProperty;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;

public class ReflectionDemo {
    public static void main(String[] args) throws Exception {
        RunningTestInstance obj = new RunningTestInstance();
        Class cls = obj.getClass(); // class is an object too
        System.out.println("the name of class is " + cls.getName());

        Constructor constructor = cls.getConstructor();
        System.out.println("the constructor is " + constructor.getName()); // without get to the real constructor in class definition

        System.out.println("get all public methods from class and parent class (object)");
        Method[] methods1 = cls.getMethods();
        for(Method m : methods1) {
            System.out.println(m);
        }

        System.out.println("get methods in another way");
        Method[] methods2 = cls.getDeclaredMethods();
        for(Method m : methods2) {
            System.out.println(m);
        }

//        obj.mtd2(1);
        System.out.println("invoke method through reflection");
        Method reflectMethod2 = cls.getDeclaredMethod("mtd2", int.class);
        reflectMethod2.invoke(obj,19);

        System.out.println("access the private method outside its class");
        Method reflectMethod3 = cls.getDeclaredMethod("mtd3");
        reflectMethod3.setAccessible(true); // set it as public
        reflectMethod3.invoke(obj);

        System.out.println("inspect annotations: ");
        Class testClass = RunningTestInstance.class;
        Method reflectMethod4 = testClass.getDeclaredMethod("mtd4");
        Annotation[] annotations = reflectMethod4.getAnnotations();
        for(Annotation a : annotations) {
            System.out.println(a.annotationType());
        }
        // apply logic on the annotations inspected by reflection
        // add additional capability to the method by add annotations rather than modify the method code

        System.out.println("class annotations: ");
        Annotation[] classAnnotations = testClass.getAnnotations();
        for(Annotation a : classAnnotations) {
            System.out.println(a.annotationType());
            MyAnnotation myAnnotation = (MyAnnotation) a;
            System.out.println(myAnnotation.name());
            System.out.println(myAnnotation.value());
        }

    }
}
@MyAnnotation(name = "name", value = "123")
class RunningTestInstance {
    private String val;
    public RunningTestInstance() {
        this.val = "12345";
    }

    public void mtd1() {
        System.out.println("the value is " + val);
    }

    public void mtd2(int n) {
        System.out.println("the number is " + n);
    }

    private void mtd3() {
        System.out.println("private method here");
    }

    @BeanProperty
    @Deprecated
    public void mtd4() {
        System.out.println("method with annotation");
    }
}

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    public String name();
    public String value();
}