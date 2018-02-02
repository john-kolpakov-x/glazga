package kz.greetgo.glazga.graphics_probe.util.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HandlerList0<T> {

  private static class Dot<T> {
    final T t;
    Dot<T> next, prev;

    private Dot(T t) {this.t = t;}
  }

  final Object sync = new Object();

  private Dot<T> last = null;

  public Detaching attach(T t) {

    final Dot<T> dot = new Dot<>(t);

    synchronized (sync) {
      if (last == null) {
        last = dot.next = dot.prev = dot;
      } else {
        dot.next = last.next;
        dot.prev = last;
        last.next.prev = dot;
        last.next = dot;
        last = dot;
      }
    }

    return () -> {
      synchronized (sync) {
        if (last == dot) {
          if (last.next == last) {
            last = null;
          } else {
            last = last.prev;
          }
        }
        dot.prev.next = dot.next;
        dot.next.prev = dot.prev;
      }
    };
  }

  private Iterator<T> createIterator() {
    final Dot<T> theLast = last;

    if (theLast == null) return new Iterator<T>() {
      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public T next() {
        throw new UnsupportedOperationException();
      }
    };

    return new Iterator<T>() {

      Dot<T> current = null;

      @Override
      public boolean hasNext() {
        return current != theLast;
      }

      @Override
      public T next() {
        return (current = (current == null ? theLast : current).next).t;
      }
    };
  }

  public List<T> toList() {
    List<T> ret = new ArrayList<>();
    synchronized (sync) {
      Iterator<T> iterator = createIterator();
      while (iterator.hasNext()) {
        ret.add(iterator.next());
      }
    }
    return ret;
  }
}
