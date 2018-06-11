package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.Bracket;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.Graphics2D;
import java.util.Objects;
import java.util.function.Function;

public class DisplayBuilder {
  private final DrawMetric drawMetric;
  private FloatSupplier baseHeight;

  public DisplayBuilder(DrawMetric drawMetric) {
    Objects.requireNonNull(drawMetric, "drawMetric == null");
    this.drawMetric = drawMetric;
    this.baseHeight = () -> 14f;
  }

  public DisplayBuilder baseHeight(FloatSupplier baseHeight) {
    Objects.requireNonNull(baseHeight, "baseHeight == null");
    this.baseHeight = baseHeight;
    return this;
  }

  public Display letter(final char c, final int level) {
    return new _DisplayLetter(c, level, baseHeight, drawMetric);
  }

  public Display row(Display left, Display right) {
    return new _DisplayRow(left, right);
  }

  private static final Display NONE = new Display() {
    @Override
    public void paint(Graphics2D g, float x, float y) {}

    @Override
    public FigArea area() {
      return new FigArea(0, 0, 0);
    }
  };

  public Display none() {
    return NONE;
  }

  public Display str(final String str, final int level) {
    if (str == null) return none();
    int length = str.length();
    if (length == 0) return none();
    Display ret = letter(str.charAt(0), level);
    for (int i = 1; i < length; i++) {
      ret = row(ret, letter(str.charAt(i), level));
    }
    return ret;
  }

  public Display structure(Display it) {
    return area(it.area(), Function.identity());
  }

  public Display area(FigArea area, Function<Graphics2D, Graphics2D> gp) {
    return new _DisplayArea(gp, area);
  }

  public Display area(float width, float top, float bottom, Function<Graphics2D, Graphics2D> gp) {
    return area(new FigArea(width, top, bottom), gp);
  }

  public Display div(Display top, Display bottom, int level) {
    return new _DisplayDiv(level, top, bottom, baseHeight, drawMetric);
  }

  public _PowerBuilder power(Display center) {
    Objects.requireNonNull(center);
    return new _PowerBuilder(null, new _PowerBuilder.SideRowDisplay(null, 0, center), baseHeight);
  }

  public Display brackets(Display in, Bracket bracket, Function<Graphics2D, Graphics2D> gp) {
    return new _DisplayBrackets(in, bracket, drawMetric, gp);
  }

  public Display brackets(Display in, Bracket bracket) {
    return brackets(in, bracket, Function.identity());
  }


  public RamaBuilder rama() {
    return new RamaBuilder(this);
  }
}
