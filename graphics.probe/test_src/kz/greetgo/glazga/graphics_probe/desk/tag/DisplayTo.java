package kz.greetgo.glazga.graphics_probe.desk.tag;

import kz.greetgo.glazga.graphics_probe.display.Display;

public class DisplayTo {
  public final float x, y;
  public final Display display;

  public DisplayTo(float x, float y, Display display) {
    this.x = x;
    this.y = y;
    this.display = display;
  }
}
