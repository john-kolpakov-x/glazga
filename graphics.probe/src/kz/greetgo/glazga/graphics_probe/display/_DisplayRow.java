package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Graphics2D;

class _DisplayRow implements Display {
  private final Display left;
  private final Display right;

  public _DisplayRow(Display left, Display right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public void paint(Graphics2D g, float x, float y) {
    left.paint(g, x, y);
    right.paint(g, x + left.area().width, y);
  }

  @Override
  public FigArea area() {
    FigArea rightArea = right.area();
    FigArea leftArea = left.area();
    float top = Math.max(leftArea.top, rightArea.top);
    float bottom = Math.max(leftArea.bottom, rightArea.bottom);
    float width = leftArea.width + rightArea.width;
    return new FigArea(width, top, bottom);
  }
}
