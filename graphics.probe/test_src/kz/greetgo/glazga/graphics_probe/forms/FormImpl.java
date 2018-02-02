package kz.greetgo.glazga.graphics_probe.forms;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayBuilder;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.forms.events.Event;
import kz.greetgo.glazga.graphics_probe.forms.events.FormExit;
import kz.greetgo.glazga.graphics_probe.forms.events.MouseMove;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.util.DrawUtil;
import kz.greetgo.glazga.graphics_probe.util.handler.Detaching;
import kz.greetgo.glazga.graphics_probe.util.handler.Handler;
import kz.greetgo.glazga.graphics_probe.util.handler.HandlerList;

import java.awt.Color;
import java.awt.Graphics2D;

public class FormImpl implements Form {
  final DrawMetric drawMetric = Fonts.With.Merriweather_Light.drawMetric();
  final DisplayBuilder builder = new DisplayBuilder(drawMetric).baseHeight(() -> 80);

  private final HandlerList repaintHandlers = new HandlerList();

  public Detaching attachRepaint(Handler handler) {
    return repaintHandlers.attach(handler);
  }

  MouseMove mouseMoved = null;

  @Override
  public void goEvent(Event event) {
    if (event instanceof MouseMove) {
      mouseMoved = (MouseMove) event;
      repaintHandlers.fire();
      return;
    }
    if (event instanceof FormExit) {
      mouseMoved = null;
      repaintHandlers.fire();
      return;
    }
  }

  public void paint(Graphics2D g, float width, float height) {
    Fonts.get().applyHints(g);
    g.setColor(new Color(0xFFFFFF));
    g.fillRect(0, 0, Math.round(width), Math.round(height));
    g.setColor(new Color(0x00D200));
    g.drawRect(10, 10, Math.round(width - 20), Math.round(height - 20));

    g.setColor(new Color(0xAE2CBD));
    Display str = builder.str("Привет мир!!!", 1);
    str.paint(g, 100, 100);

    {
      MouseMove mouseMoved = this.mouseMoved;
      if (mouseMoved != null) {
        g.setColor(Color.BLUE);
        DrawUtil.point(g, mouseMoved.x, mouseMoved.y, 3);
      }
    }

  }
}
