package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Practice {
    private static Integer num = 100; // task 1: +50; task 2: *2 -> 300
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        System.out.println("main thread starts");
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() ->{
                    num += 50;
                    return num;
                }).thenApply(val -> {return val *= 2; // same thread
                }).thenApplyAsync(val -> val + 100) // 400
                .thenAccept(val ->{
                    System.out.println("the main thread completes");
                    System.out.println("the final result: " + (val - 100)); // 300
                });
        System.out.println("task result: " + future.get()); // blocking
        System.out.println("the main thread completes");
    }
}
