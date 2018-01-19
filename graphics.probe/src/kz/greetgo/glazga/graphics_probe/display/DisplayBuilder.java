package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.model.ShapeArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public class DisplayBuilder {
  final DrawMetric drawMetric;
  private FloatSupplier baseHeight;

  public DisplayBuilder(DrawMetric drawMetric, FloatSupplier baseHeight) {
    Objects.requireNonNull(drawMetric, "drawMetric == null");
    Objects.requireNonNull(baseHeight, "baseHeight == null");
    this.drawMetric = drawMetric;
    this.baseHeight = baseHeight;
  }

  public DisplayBuilder baseHeight(FloatSupplier baseHeight) {
    Objects.requireNonNull(baseHeight, "baseHeight == null");
    this.baseHeight = baseHeight;
    return this;
  }

  public Display letter(final char c, final int level) {
    final FloatSupplier savedBaseHeight = baseHeight;
    final ShapeArea letterShapeArea = drawMetric.letterShapeArea(c);
    return new Display() {
      @Override
      public void paint(Graphics2D g, float x, float y) {
        ShapeArea shapeArea = letterShapeArea.setHeight(drawMetric.levelHeight(savedBaseHeight.value(), level));

        Shape letterShape = AffineTransform
            .getTranslateInstance(x, y - shapeArea.area.top)
            .createTransformedShape(shapeArea.shape);

        g.fill(letterShape);
      }

      @Override
      public FigArea area() {
        return letterShapeArea.setHeight(savedBaseHeight.value()).area;
      }
    };
  }

  public Display row(Display left, Display right) {
    return new Display() {
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
    };
  }

  private static final Display NONE = new Display() {
    @Override
    public void paint(Graphics2D g, float x, float y) { }

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
}
