package kz.greetgo.glazga.graphics_probe.util.handler;

public class HandlerList extends HandlerList0<Handler> {
  public void fire() {
    toList().forEach(a -> {
      try {
        a.handle();
      } catch (Exception e) {
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        throw new RuntimeException(e);
      }
    });
  }
}
