package org.example.BuilderPattern;

public class DepartmentDemo {
    public static void main(String[] args) {
        Department d = Department.builder().setName("JAVA").setId(1).build();
        // builder() returns DB object, with multiple setters
        // the chain is like different combination constructors
        // build(0 returns a Department object
    }
}
