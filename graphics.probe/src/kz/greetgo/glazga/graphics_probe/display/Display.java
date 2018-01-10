package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Graphics2D;

public interface Display {
  void paint(Graphics2D g, float x, float y);

  FigArea area();
}
