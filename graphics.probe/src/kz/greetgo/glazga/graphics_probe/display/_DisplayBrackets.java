package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.Bracket;
import kz.greetgo.glazga.graphics_probe.metric.BracketSide;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Graphics2D;
import java.util.function.Function;

public class _DisplayBrackets implements Display {
  private final Display in;
  private final Bracket bracket;
  private final DrawMetric drawMetric;
  private final Function<Graphics2D, Graphics2D> gp;

  public _DisplayBrackets(Display in, Bracket bracket, DrawMetric drawMetric, Function<Graphics2D, Graphics2D> gp) {
    this.in = in;
    this.bracket = bracket;
    this.drawMetric = drawMetric;
    this.gp = gp;
  }

  @Override
  public void paint(Graphics2D g, float x, float y) {
    FigArea inArea = in.area();

    DrawMetric.BracketDrawer left = drawMetric.drawerFor(bracket, BracketSide.LEFT);
    DrawMetric.BracketDrawer right = drawMetric.drawerFor(bracket, BracketSide.RIGHT);

    float widthLeft = left.widthForHeight(inArea.height());
//    float widthRight = right.widthForHeight(inArea.height());

    FigArea areaLeft = new FigArea(inArea).setWidth(widthLeft);
//    FigArea areaRight = new FigArea(inArea).setWidth(widthRight);

    {
      Graphics2D g1 = gp.apply(g);
      g1.fill(left.shapeIn(areaLeft, x, y));
      in.paint(g, x + widthLeft, y);
      g1.fill(right.shapeIn(areaLeft, x + widthLeft + inArea.width, y));
    }
  }

  @Override
  public FigArea area() {
    FigArea inArea = in.area();

    DrawMetric.BracketDrawer left = drawMetric.drawerFor(bracket, BracketSide.LEFT);
    DrawMetric.BracketDrawer right = drawMetric.drawerFor(bracket, BracketSide.RIGHT);

    float widthLeft = left.widthForHeight(inArea.height());
    float widthRight = right.widthForHeight(inArea.height());

    return new FigArea(widthLeft + inArea.width + widthRight, inArea.top, inArea.bottom);
  }
}
