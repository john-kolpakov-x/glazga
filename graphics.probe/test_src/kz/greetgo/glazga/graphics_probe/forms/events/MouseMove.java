package kz.greetgo.glazga.graphics_probe.forms.events;

public class MouseMove implements Event {
  public final float x;
  public final float y;

  public MouseMove(float x, float y) {
    this.x = x;
    this.y = y;
  }
}
