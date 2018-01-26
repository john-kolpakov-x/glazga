package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.metric.HeightMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.model.ShapeArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;
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
    return area(it.area(), Function.identity());
  }

  public Display area(FigArea area, Function<Graphics2D, Graphics2D> gp) {
    return new Display() {
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
    };
  }

  public Display area(float width, float top, float bottom, Function<Graphics2D, Graphics2D> gp) {
    return area(new FigArea(width, top, bottom), gp);
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

  public enum PowerSide {
    TOP, RIGHT, BOTTOM, LEFT;
  }

  private static class SideRow {
    final PowerSide side;
    final int row;

    public SideRow(PowerSide side, int row) {
      this.side = side;
      this.row = row;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SideRow sideRow = (SideRow) o;
      return row == sideRow.row &&
          side == sideRow.side;
    }

    @Override
    public int hashCode() {
      return Objects.hash(side, row);
    }
  }

  private static class SideRowDisplay {
    final SideRow sideRow;
    final float factor;
    final Display display;

    private SideRowDisplay(SideRow sideRow, float factor, Display display) {
      this.sideRow = sideRow;
      this.factor = factor;
      this.display = display;
    }
  }

  private class DeltaDisplay {
    final Display display;
    final float dx, dy;

    public DeltaDisplay(Display display, float dx, float dy) {
      this.dx = dx;
      this.dy = dy;
      this.display = display;
    }
  }

  public class PowerBuilder {
    private final PowerBuilder parent;
    private final SideRowDisplay sideRowDisplay;

    public PowerBuilder(PowerBuilder parent, SideRowDisplay sideRowDisplay) {
      this.parent = parent;
      this.sideRowDisplay = sideRowDisplay;
    }

    public PowerBuilder add(PowerSide side, int row, float factor, Display display) {
      Objects.requireNonNull(side);
      Objects.requireNonNull(display);
      return new PowerBuilder(this, new SideRowDisplay(new SideRow(side, row), factor, display));
    }

    public PowerBuilder top(int row, float factor, Display display) {
      return add(PowerSide.TOP, row, factor, display);
    }

    public PowerBuilder right(int row, float factor, Display display) {
      return add(PowerSide.RIGHT, row, factor, display);
    }

    public PowerBuilder bottom(int row, float factor, Display display) {
      return add(PowerSide.BOTTOM, row, factor, display);
    }

    public PowerBuilder left(int row, float factor, Display display) {
      return add(PowerSide.LEFT, row, factor, display);
    }

    Display fillDisplayList(List<DeltaDisplay> displayList, Map<SideRow, Float> distanceMap) {
      if (parent == null) return sideRowDisplay.display;
      Display center = parent.fillDisplayList(displayList, distanceMap);
      FigArea centerArea = center.area();

      Float distance = distanceMap.get(sideRowDisplay.sideRow);
      if (distance == null) distance = 0f;

      FigArea area = sideRowDisplay.display.area();
      float factor = sideRowDisplay.factor;

      PowerSide side = sideRowDisplay.sideRow.side;

      float dx;

      switch (side) {
        case TOP:
        case BOTTOM:
          if (factor >= 0) {
            float x1 = centerArea.width / 2 - area.width / 2;
            float x2 = centerArea.width;
            dx = x1 + (x2 - x1) * factor;
          } else {
            float x1 = centerArea.width / 2 - area.width / 2;
            float x2 = -area.width;
            dx = x1 + (x1 - x2) * factor;
          }
          break;

        case LEFT:
          dx = -area.width - distance;
          distance += area.width;
          break;

        case RIGHT:
          dx = centerArea.width + distance;
          distance += area.width;
          break;

        default:
          throw new RuntimeException("Unknown value " + side);
      }

      float dy;

      switch (side) {
        case LEFT:
        case RIGHT:
          if (factor >= 0) {
            float y1 = 0;
            float y2 = -centerArea.top - area.bottom;
            dy = y1 + (y2 - y1) * factor;
          } else {
            float y1 = 0;
            float y2 = centerArea.bottom + area.top;
            dy = y1 + (y1 - y2) * factor;
          }
          break;

        case TOP:
          dy = -centerArea.top - area.bottom - distance;
          distance += area.height();
          break;

        case BOTTOM:
          dy = centerArea.bottom + area.top + distance;
          distance += area.height();
          break;

        default:
          throw new RuntimeException("Unknown value " + side);
      }

      distanceMap.put(sideRowDisplay.sideRow, distance);

      displayList.add(new DeltaDisplay(sideRowDisplay.display, dx, dy));

      return center;
    }

    public Display create() {
      final FloatSupplier savedBaseHeight = baseHeight;
      return new Display() {
        List<DeltaDisplay> displayList = null;

        float baseHeightValue;

        FigArea area = null;

        void prepare() {
          if (displayList != null && baseHeightValue == savedBaseHeight.value()) return;
          baseHeightValue = savedBaseHeight.value();

          List<DeltaDisplay> displayList = new ArrayList<>();
          Map<SideRow, Float> distanceMap = new HashMap<>();

          Display center = fillDisplayList(displayList, distanceMap);

          float dx = 0;
          for (DeltaDisplay dd : displayList) {
            if (dx > dd.dx) dx = dd.dx;
          }

          dx = -dx;

          List<DeltaDisplay> tmp = new ArrayList<>();
          for (DeltaDisplay dd : displayList) {
            tmp.add(new DeltaDisplay(dd.display, dd.dx + dx, dd.dy));
          }
          tmp.add(new DeltaDisplay(center, dx, 0));

          this.displayList = tmp;

          calcArea();
        }

        private void calcArea() {
          float width = 0, top = 0, bottom = 0;

          for (DeltaDisplay dd : displayList) {
            FigArea area = dd.display.area();
            float width1 = area.width + dd.dx;
            float top1 = area.top - dd.dy;
            float bottom1 = area.bottom + dd.dy;

            if (width < width1) width = width1;
            if (top < top1) top = top1;
            if (bottom < bottom1) bottom = bottom1;
          }

          area = new FigArea(width, top, bottom);
        }

        @Override
        public void paint(Graphics2D g, float x, float y) {
          prepare();
          displayList.forEach(dd -> dd.display.paint(g, x + dd.dx, y + dd.dy));
        }

        @Override
        public FigArea area() {
          prepare();
          return area;
        }
      };
    }
  }

  public PowerBuilder power(Display center) {
    Objects.requireNonNull(center);
    return new PowerBuilder(null, new SideRowDisplay(null, 0, center));
  }

}
