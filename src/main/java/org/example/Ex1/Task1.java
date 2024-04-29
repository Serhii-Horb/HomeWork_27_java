package org.example.Ex1;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicInteger;

/* 1. Кассир в магазине. К нему подходят клиенты с товарoм отдают ему деньги и получают сдачу.
По окончанию работы кассир подсчитывает сумму в кассе и сдает выручку.
Кассир и каждый покупатель - это отдельные потоки. Сымитируйте данный процесс работы. Сумма оплаты и
сумма сдачи может быть сгенерирована случайным образом, чтобы сдача была всегда меньше чем оплата.
Какой синхронизатор с библиотеки concurrent Вы бы использовали для данного процесса? */

public class Task1 {
    public static void main(String[] args) {
        int totalAmount = 0;
        Exchanger<Integer> exchanger = new Exchanger<>();

        new Thread(new Client(totalAmount, exchanger)).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(new Cashier(totalAmount, "Тамара", exchanger)).start();
    }
}

class Cashier implements Runnable {
    int totalAmount;
    private String name;
    Exchanger<Integer> exchanger;

    public Cashier(int totalAmount, String name, Exchanger<Integer> exchanger) {
        this.totalAmount = totalAmount;
        this.name = name;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        System.out.println("Кассир по имени " + name + " начал свою работу");
        Random random = new Random();
        int sum = random.nextInt(1, 10);
        System.out.println("Кассир дал сдачи " + sum);
        try {
            int res = exchanger.exchange(sum);
            System.out.println("Кассиру заплатили " + res);
            totalAmount = res - sum;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Магазин заработала за сегодня " + totalAmount);
    }
}

class Client implements Runnable {
    int totalAmount;
    Exchanger<Integer> exchanger;

    public Client(int totalAmount, Exchanger<Integer> exchanger) {
        this.totalAmount = totalAmount;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        System.out.println("Клиент по имени " + Thread.currentThread().getName() + " выбрал покупки");
        Random random = new Random();
        int sum = random.nextInt(10, 100);
        System.out.println("Клиент заплатил " + Thread.currentThread().getName() + " " + sum);
        try {
            int res = exchanger.exchange(sum);
            Thread.sleep(200);
            System.out.println("Клиенту дали сдачи " + Thread.currentThread().getName() + " " + res);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}