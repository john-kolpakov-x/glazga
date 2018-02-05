package kz.greetgo.glazga.graphics_probe.desk;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayBuilder;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class DeskImpl implements Desk {

  final DrawMetric drawMetric = Fonts.With.Merriweather_Light.drawMetric();
  final DisplayBuilder builder = new DisplayBuilder(drawMetric).baseHeight(() -> 80);

  Target over = null;

  class Box1 implements Box, Target {

    final float x, y, width, top, bottom;

    Box1(float x, float y, float width, float top, float bottom) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.top = top;
      this.bottom = bottom;
    }

    @Override
    public Target findAt(float x, float y) {
      if (x < this.x) return null;
      if (y < this.y - top) return null;
      if (x > this.x + width) return null;
      if (y > this.y + bottom) return null;
      return this;
    }

    @Override
    public void paint(Graphics2D g) {
      Color color = over == this
        ? new Color(0x1AC81B)
        : new Color(0, 0, 0);

      Display area = builder.area(width, top, bottom, g1 -> {
        g1.setColor(color);
        return g1;
      });

      area.paint(g, x, y);
    }
  }

  final List<Box1> boxList = new ArrayList<>();

  {
    boxList.add(new Box1(50, 70, 100, 50, 50));
    boxList.add(new Box1(50 + 100 + 10, 70, 100, 50, 50));
  }

  @Override
  public Box getRootBox(float width, float height) {
    return new Box() {
      @Override
      public Target findAt(float x, float y) {
        for (Box1 b : boxList) {
          Target target = b.findAt(x, y);
          if (target != null) return target;
        }
        return null;
      }

      @Override
      public void paint(Graphics2D g) {
        boxList.forEach(b -> b.paint(g));
      }
    };
  }

  @Override
  public void setOver(Target target) {
    over = target;
  }
}
