package com.hzk.boot.hook;

public class MyShutdownHook extends Thread {

    @Override
    public void run(){
        System.out.println("MyShutdownHook start------------");
        System.out.println("MyShutdownHook dosomething");
        try {
            Thread.currentThread().sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("MyShutdownHook end--------------");
    }

}
