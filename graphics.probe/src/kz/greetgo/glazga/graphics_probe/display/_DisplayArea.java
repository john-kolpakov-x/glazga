package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Graphics2D;
import java.util.function.Function;

class _DisplayArea implements Display {
  private final Function<Graphics2D, Graphics2D> gp;
  private final FigArea area;

   _DisplayArea(Function<Graphics2D, Graphics2D> gp, FigArea area) {
    this.gp = gp;
    this.area = area;
  }

  @Override
  public void paint(Graphics2D g1, float x, float y) {
    Graphics2D g = gp.apply(g1);
    final int r = 2;
    for (int Y = Math.round(y) - r, Y2 = Math.round(y) + r; Y <= Y2; Y++) {
      g.drawLine(Math.round(x) - r, Y, Math.round(x) + r, Y);
    }

    int X = Math.round(x), Y = Math.round(y);
    int top = Math.round(area.top), bottom = Math.round(area.bottom), width = Math.round(area.width);

    g.drawLine(X, Y - top, X + width, Y - top);
    g.drawLine(X + width, Y - top, X + width, Y + bottom);
    g.drawLine(X + width, Y + bottom, X, Y + bottom);
    g.drawLine(X, Y + bottom, X, Y - top);
    g.drawLine(X, Y, X + width, Y);
  }

  @Override
  public FigArea area() {
    return area;
  }
}
