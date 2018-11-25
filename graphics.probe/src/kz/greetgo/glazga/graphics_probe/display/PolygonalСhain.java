package kz.greetgo.glazga.graphics_probe.display;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class PolygonalСhain {

  private final List<Float> xx = new ArrayList<>();
  private final List<Float> yy = new ArrayList<>();

  public PolygonalСhain point(float x, float y) {
    xx.add(x);
    yy.add(y);
    return this;
  }

  public void drawTo(Graphics2D g, float x, float y) {
    for (int i = 0, c = xx.size() - 1; i < c; i++) {

      float x1 = x + xx.get(i);
      float y1 = y + yy.get(i);
      float x2 = x + xx.get(i + 1);
      float y2 = y + yy.get(i + 1);

      g.drawLine(round(x1), round(y1), round(x2), round(y2));
    }
  }
}
