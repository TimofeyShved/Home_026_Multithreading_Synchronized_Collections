package com.company;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger atomicInteger = new AtomicInteger(0); //атамарная переменная типа Int

    public static void main(String[] args) throws Exception {
        NameList nameList = new NameList();
        nameList.add("first");
        class MyThread extends Thread{
            @Override
            public void run(){
                System.out.println(nameList.removeFirst());
                atomicInteger.incrementAndGet(); // AtomicInteger++;
            }
        }
        MyThread myThread = new MyThread();
        myThread.setName("one");
        myThread.start();
        new MyThread().start();

        Thread.sleep(1000); // основной поток засыпает
        System.out.println(atomicInteger.get());
    }

    static class NameList{ // создаем класс, внутри которого есть ArrayList
        private List list = Collections.synchronizedList(new ArrayList<>()); // то что он синхронизирован, не дает нам ни каких гарантий
        // поэтому он просто будет атамарной переменной

        public synchronized  void  add(String name){ //поэтому мы синхронизируем методы
            list.add(name);
        }

        public synchronized String removeFirst(){ // и его тоже
            if (list.size()>0){
                if (Thread.currentThread().getName().equals("one")) {
                    Thread.yield(); // просип передать другому потоку, в этом может быть ошибка
                }
                return (String)list.remove(0);
            }
            return null;
        }
    }
}
