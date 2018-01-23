package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.metric.HeightMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.model.ShapeArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

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

  public Display structure(Display it) {
    return new Display() {
      @Override
      public void paint(Graphics2D g, float x, float y) {
        final int r = 2;
        for (int Y = Math.round(y) - r, Y2 = Math.round(y) + r; Y <= Y2; Y++) {
          g.drawLine(Math.round(x) - r, Y, Math.round(x) + r, Y);
        }

        FigArea area = it.area();

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
        return it.area();
      }
    };
  }

  public Display div(Display top, Display bottom, int level) {
    final FloatSupplier savedBaseHeight = baseHeight;

    return new Display() {

      HeightMetric heightMetric() {
        float levelHeight = drawMetric.levelHeight(savedBaseHeight.value(), level);
        return drawMetric.heightMetric(levelHeight);
      }

      @Override
      public void paint(Graphics2D g, float x, float y) {
        HeightMetric heightMetric = heightMetric();

        FigArea topArea = top.area();
        FigArea bottomArea = bottom.area();

        float maxWidth = Math.max(topArea.width, bottomArea.width);

        float leftTop = (maxWidth - topArea.width) / 2f + heightMetric.lineGap / 2f;
        float leftBottom = (maxWidth - bottomArea.width) / 2f + heightMetric.lineGap / 2f;

        float yTop = y - heightMetric.middleLine - heightMetric.lineGap - topArea.bottom;
        float yBottom = y - heightMetric.middleLine + heightMetric.lineGap + bottomArea.top;

        float xTop = x + leftTop;
        float xBottom = x + leftBottom;

        Rectangle2D.Float middle = new Rectangle2D.Float();

        middle.x = x;
        middle.y = y - heightMetric.middleLine - heightMetric.underlineThickness / 2f;
        middle.width = maxWidth + heightMetric.lineGap;
        middle.height = heightMetric.underlineThickness;

        top.paint(g, xTop, yTop);
        bottom.paint(g, xBottom, yBottom);
        g.fill(middle);
      }

      @Override
      public FigArea area() {

        HeightMetric heightMetric = heightMetric();

        FigArea topArea = top.area();
        FigArea bottomArea = bottom.area();

        float simpleTop = topArea.height() + heightMetric.lineGap;
        float simpleBottom = bottomArea.height() + heightMetric.lineGap;

        float maxWidth = Math.max(topArea.width, bottomArea.width);

        {
          FigArea ret = new FigArea();
          ret.width = maxWidth + heightMetric.lineGap;
          ret.top = simpleTop + heightMetric.middleLine;
          ret.bottom = simpleBottom - heightMetric.middleLine;
          return ret;
        }
      }
    };
  }
}
