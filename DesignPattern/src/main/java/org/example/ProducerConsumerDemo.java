package org.example;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    producer -> available position in thr queue <- consumer
    if the  queue is full, the producer needs to wait
    each p/c represents a thread
*/

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        ProducerConsumerModel shareObject = new ProducerConsumerModel(); // queue
        List<Producer> producers = new ArrayList<>();
        List<Consumer> consumers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) producers.add(new Producer("producer-" + i, shareObject));
        for (int i = 1; i <= 5; i++) consumers.add(new Consumer("consumer-" + i, shareObject));

        for (Producer p : producers) p.start();
        for (Consumer c : consumers) c.start();
    }
}

// model class: queue
class ProducerConsumerModel {
    private Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 3;
    private Random random = new Random();
    Lock lock = new ReentrantLock();
    Condition queueNotFull = lock.newCondition();
    Condition queueNotEmpty = lock.newCondition();

    public void put() {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                System.out.println(Thread.currentThread().getName() + " is waiting, queue is full");
                queueNotFull.await();
            }
            int temp = random.nextInt(100);
            queue.offer(temp);
            System.out.println(Thread.currentThread().getName() + " put " + temp); // the queue is not empty now
            queueNotEmpty.signalAll(); // trigger all waiting threads
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            // make sure the lock is released after use
        }
    }

    public void take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " is waiting, queue is empty");
                queueNotEmpty.await();
            }
            int temp = queue.poll();
            System.out.println(Thread.currentThread().getName() + " take " + temp); // the queue is not full now
            queueNotFull.signalAll(); // trigger all waiting threads
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}

class PCModelSynchronized {
    private Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 3;
    private Random random = new Random();

    public synchronized void put() {
        try {
            while(queue.size() == capacity) {
                System.out.println(Thread.currentThread().getName() + " is waiting, queue is full");
                wait(); // wait until the queue is not full
            }
            int temp = random.nextInt(100);
            queue.offer(temp);
            System.out.println(Thread.currentThread().getName() + " puts " + temp);
            notifyAll(); // notify all producers
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void take() {
        try {
            while(queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " is waiting, queue is empty");
                wait(); // wait until the queue is not empty
            }
            int temp = queue.poll();
            System.out.println(Thread.currentThread().getName() + " takes " + temp);
            notifyAll(); // notify all consumers
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Producer extends Thread {
    private String name;
    ProducerConsumerModel pc;
//    PCModelSynchronized pc;

    public Producer(String name, ProducerConsumerModel sharedObject) {
        super(name);
        pc = sharedObject;
    }

    @Override
    public void run() {
        pc.put();
    }
}

class Consumer extends Thread {
    private String name;
    ProducerConsumerModel pc;

    public Consumer(String name, ProducerConsumerModel sharedObject) {
        super(name);
        pc = sharedObject;
    }

    @Override
    public void run() {
        pc.take();
    }
}