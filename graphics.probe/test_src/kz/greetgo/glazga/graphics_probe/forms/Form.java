package kz.greetgo.glazga.graphics_probe.forms;

import kz.greetgo.glazga.graphics_probe.forms.events.Event;
import kz.greetgo.glazga.graphics_probe.util.handler.Detaching;
import kz.greetgo.glazga.graphics_probe.util.handler.Handler;

import java.awt.Graphics2D;

public interface Form {

  @SuppressWarnings("UnusedReturnValue")
  Detaching attachRepaint(Handler handler);

  void goEvent(Event event);

  void paint(Graphics2D g, float width, float height);
}
