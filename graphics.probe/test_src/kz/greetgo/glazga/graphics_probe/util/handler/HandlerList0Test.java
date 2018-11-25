package kz.greetgo.glazga.graphics_probe.util.handler;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.fest.assertions.api.Assertions.assertThat;

public class HandlerList0Test {
  @Test
  public void test_detach_first() {
    HandlerList0<String> handlerList = new HandlerList0<>();

    Detaching asd = handlerList.attach("asd");
    Detaching dsa = handlerList.attach("dsa");

    assertThat(handlerList.toList()).containsExactly("asd", "dsa");

    asd.detach();

    assertThat(handlerList.toList()).containsExactly("dsa");

    dsa.detach();

    assertThat(handlerList.toList()).isEmpty();
  }

  @Test
  public void test_detach_second() {
    HandlerList0<String> handlerList = new HandlerList0<>();

    Detaching asd = handlerList.attach("asd");
    Detaching dsa = handlerList.attach("dsa");

    assertThat(handlerList.toList()).containsExactly("asd", "dsa");

    dsa.detach();

    assertThat(handlerList.toList()).containsExactly("asd");

    asd.detach();

    assertThat(handlerList.toList()).isEmpty();
  }

  private static Runnable createAttachAllRunnable(HandlerList0<Integer> handlerList,
                                                  BlockingDeque<Detaching> tunnel,
                                                  List<Integer> list) {
    return () -> {
      for (Integer value : list) {
        tunnel.add(handlerList.attach(value));
      }
    };
  }

  private static class ReadThread extends Thread {
    private int no;
    private HandlerList0<Integer> handlerList;
    private AtomicBoolean workMarker;

    public ReadThread(int no, HandlerList0<Integer> handlerList, AtomicBoolean workMarker) {
      this.no = no;
      this.handlerList = handlerList;
      this.workMarker = workMarker;
    }

    @Override
    public void run() {
      while (workMarker.get()) {
        doWork();
      }
    }

    long counts = 0;
    long breakCount = 0;
    long circles = 0;
    long sum = 0;

    private void doWork() {
      int count = 0;
      for (Integer value : handlerList.toList()) {
        counts++;
        sum += value;
        count++;
        if (count > 3000) {
          breakCount++;
          break;
        }
      }
      circles++;
    }

    public void printSummary() {
      double middleCounts = (double) counts / (double) circles;
      System.out.println("THREAD " + no
        + ": circles = " + circles
        + ", middleCounts = " + middleCounts
        + ", breakCount = " + breakCount
        + ", sum = " + sum);
    }

    public void doAsserts() {
      assertThat(breakCount).isZero();
    }
  }

  @Test
  public void multiThreadAble() throws Exception {

    List<Integer> list1 = new ArrayList<>();
    List<Integer> list2 = new ArrayList<>();
    List<Integer> list3 = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      list1.add(i);
    }
    for (int i = 0; i < 1000; i++) {
      list2.add(list1.size() + i);
    }
    for (int i = 0; i < 1000; i++) {
      list3.add(list1.size() + list2.size() + i);
    }
    Collections.shuffle(list1);
    Collections.shuffle(list2);
    Collections.shuffle(list3);

    HandlerList0<Integer> handlerList = new HandlerList0<>();

    List<ReadThread> readThreadList = new ArrayList<>();
    AtomicBoolean workMarker = new AtomicBoolean(true);
    for (int i = 1; i <= 5; i++) {
      readThreadList.add(new ReadThread(i, handlerList, workMarker));
    }
    readThreadList.forEach(Thread::start);

    BlockingDeque<Detaching> tunnel = new LinkedBlockingDeque<>();

    List<Thread> threadList = new ArrayList<>();

    threadList.add(new Thread(createAttachAllRunnable(handlerList, tunnel, list1)));
    threadList.add(new Thread(createAttachAllRunnable(handlerList, tunnel, list2)));
    threadList.add(new Thread(createAttachAllRunnable(handlerList, tunnel, list3)));

    Runnable r = () -> {
      for (int i = 0; i < 1500; i++) {
        try {
          tunnel.take().detach();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    };

    threadList.add(new Thread(r));
    threadList.add(new Thread(r));

    threadList.forEach(Thread::start);
    for (Thread thread : threadList) {
      thread.join();
    }

    workMarker.set(false);
    for (ReadThread thread : readThreadList) {
      thread.join();
    }
    readThreadList.forEach(ReadThread::printSummary);
    readThreadList.forEach(ReadThread::doAsserts);

    assertThat(handlerList.toList()).isEmpty();

    System.out.println("OK");
  }
}