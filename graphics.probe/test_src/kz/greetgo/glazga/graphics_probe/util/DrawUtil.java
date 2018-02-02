package kz.greetgo.glazga.graphics_probe.util;

import java.awt.Graphics2D;

public class DrawUtil {
  public static void point(Graphics2D g, float x, float y, int r) {
    for (int Y = Math.round(y) - r, Y2 = Math.round(y) + r; Y <= Y2; Y++) {
      g.drawLine(Math.round(x) - r, Y, Math.round(x) + r, Y);
    }
  }
}
