package org.example.FactoryPattern;

// client side: only focus on how to use the class
public class PhoneDemo {
    public static void main(String[] args) {
        PhoneFactory factory = new PhoneFactory();
        Phone iphone = factory.getPhone("iphone");
        Phone sony = factory.getPhone("sony");
    }
}
