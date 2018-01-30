package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class _PowerBuilder {

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

  static class SideRowDisplay {
    final SideRow sideRow;
    final float factor;
    final Display display;

    SideRowDisplay(SideRow sideRow, float factor, Display display) {
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

  private final _PowerBuilder parent;
  private final SideRowDisplay sideRowDisplay;
  private final FloatSupplier baseHeight;

  _PowerBuilder(_PowerBuilder parent, SideRowDisplay sideRowDisplay, FloatSupplier baseHeight) {
    this.parent = parent;
    this.sideRowDisplay = sideRowDisplay;
    this.baseHeight = baseHeight;
  }

  public _PowerBuilder add(PowerSide side, int row, float factor, Display display) {
    Objects.requireNonNull(side);
    Objects.requireNonNull(display);
    return new _PowerBuilder(this, new SideRowDisplay(new SideRow(side, row), factor, display), baseHeight);
  }

  public _PowerBuilder top(int row, float factor, Display display) {
    return add(PowerSide.TOP, row, factor, display);
  }

  public _PowerBuilder right(int row, float factor, Display display) {
    return add(PowerSide.RIGHT, row, factor, display);
  }

  public _PowerBuilder bottom(int row, float factor, Display display) {
    return add(PowerSide.BOTTOM, row, factor, display);
  }

  public _PowerBuilder left(int row, float factor, Display display) {
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
