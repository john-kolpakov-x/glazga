package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class RamaBuilder {
  private Function<Graphics2D, Graphics2D> borderPreparatory = Function.identity();

  private static class Window {
    final Display display;
    final Function<Graphics2D, Graphics2D> displayPreparatory;

    public Window(Display display, Function<Graphics2D, Graphics2D> displayPreparatory) {
      this.display = display;
      this.displayPreparatory = displayPreparatory;
    }
  }

  private final List<Window> windowList = new ArrayList<>();

  @SuppressWarnings("unused")
  RamaBuilder(DisplayBuilder parent) {}

  private void check() {
    if (built) throw new RuntimeException("Cannot use builder after build");
  }

  public RamaBuilder window(Display display, Function<Graphics2D, Graphics2D> gp) {
    check();
    Objects.requireNonNull(display);
    Objects.requireNonNull(gp);
    windowList.add(new Window(display, gp));
    return this;
  }

  public RamaBuilder setBorderPreparatory(Function<Graphics2D, Graphics2D> borderPreparatory) {
    check();
    Objects.requireNonNull(borderPreparatory);
    this.borderPreparatory = borderPreparatory;
    return this;
  }

  private boolean built = false;

  public Display build() {
    built = true;
    return new Display() {
      @Override
      public void paint(Graphics2D g, float x, float y) {
        paintInner(g, x, y);
      }

      @Override
      public FigArea area() {
        return areaInner();
      }
    };
  }

  private Float cachedMaxTop = null;
  private Float cachedMaxBottom = null;
  private Float cachedWidthSum = null;
  private float[] cachedOffsetX = null;

  private void calcCaches() {
    float maxTop = 0;
    float maxBottom = 0;
    float widthSum = 0;

    cachedOffsetX = new float[windowList.size()];
    float offsetX = 1;

    for (int i = 0, c = windowList.size(); i < c; i++) {
      Window window = windowList.get(i);
      FigArea area = window.display.area();
      if (maxTop < area.top) maxTop = area.top;
      if (maxBottom < area.bottom) maxBottom = area.bottom;
      widthSum += area.width;
      cachedOffsetX[i] = offsetX;
      offsetX += area.width + 2;
    }

    cachedMaxTop = maxTop;
    cachedMaxBottom = maxBottom;
    cachedWidthSum = widthSum + windowList.size() * 2 ;
  }

  private float maxTop() {
    if (cachedMaxTop == null) calcCaches();
    return cachedMaxTop;
  }

  private float maxBottom() {
    if (cachedMaxBottom == null) calcCaches();
    return cachedMaxBottom;
  }

  private float widthSum() {
    if (cachedWidthSum == null) calcCaches();
    return cachedWidthSum;
  }

  private float[] offsetX() {
    if (cachedOffsetX == null) calcCaches();
    return cachedOffsetX;
  }

  private FigArea areaInner() {
    return new FigArea(widthSum(), maxTop() + 1, maxBottom() + 1);
  }

  private void paintInner(Graphics2D g, float x, float y) {
    float[] offsetX = offsetX();
    for (int i = 0, c = windowList.size(); i < c; i++) {
      Window window = windowList.get(i);
      window.display.paint(window.displayPreparatory.apply(g), x + offsetX[i], y);
    }

    {
      Graphics2D g1 = borderPreparatory.apply(g);
      int maxTop = Math.round(maxTop());
      int maxBottom = Math.round(maxBottom());
      int Y = Math.round(y);
      for (int i = 1, c = windowList.size(); i < c; i++) {
        int X = Math.round(x + offsetX[i]) - 1;
        g1.drawLine(X, Y - maxTop, X, Y + maxBottom);
      }

      {
        int X = Math.round(x);
        g1.drawRect(X, Y - maxTop - 1, Math.round(widthSum()), maxTop + maxBottom + 2);
      }
    }
  }
}
