package kz.greetgo.glazga.graphics_probe.forms2.model;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayFactory;

public class OperationPlus extends Operation {
  public Operation left, right;

  @Override
  public Display buildDisplay(DisplayFactory builder) {
    Display leftDisplay = left.buildDisplay(builder);
    Display rightDisplay = right.buildDisplay(builder);
    Display leftPlus = builder.row(leftDisplay, builder.str("+", 1));
    return builder.row(leftPlus, rightDisplay);
  }
}
