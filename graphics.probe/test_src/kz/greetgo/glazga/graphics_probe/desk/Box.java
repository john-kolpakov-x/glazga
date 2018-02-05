package kz.greetgo.glazga.graphics_probe.desk;

import java.awt.Graphics2D;

public interface Box {
  Target findAt(float x, float y);

  void paint(Graphics2D g);
}
