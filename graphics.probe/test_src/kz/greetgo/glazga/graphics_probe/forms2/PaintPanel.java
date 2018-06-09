package kz.greetgo.glazga.graphics_probe.forms2;

import java.awt.Color;
import java.awt.Graphics2D;

public class PaintPanel {
  public void startPaint() {

  }

  public void paint(Graphics2D g, int width, int height) {
    g.setColor(new Color(239, 12, 19));
    g.drawLine(10, 100, 100, 10);
  }
}
