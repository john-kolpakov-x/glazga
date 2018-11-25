package kz.greetgo.glazga.graphics_probe.forms2.model;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayFactory;

public class OperationDiv extends Operation {
  public Operation top, bottom;

  @Override
  public Display buildDisplay(DisplayFactory builder) {
    Display topDisplay = top.buildDisplay(builder);
    Display bottomDisplay = bottom.buildDisplay(builder);
    return builder.div(topDisplay, bottomDisplay, 1);
  }
}
