package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.metric.HeightMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

class _DisplayDiv implements Display {
  private final FloatSupplier savedBaseHeight;
  private final int level;
  private final Display top;
  private final Display bottom;
  final DrawMetric drawMetric;

  _DisplayDiv(int level, Display top, Display bottom, FloatSupplier savedBaseHeight, DrawMetric drawMetric) {
    this.level = level;
    this.top = top;
    this.bottom = bottom;
    this.savedBaseHeight = savedBaseHeight;
    this.drawMetric = drawMetric;
  }

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
}
