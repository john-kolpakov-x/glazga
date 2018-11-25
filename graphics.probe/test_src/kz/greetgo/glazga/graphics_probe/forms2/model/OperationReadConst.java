package kz.greetgo.glazga.graphics_probe.forms2.model;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayFactory;

public class OperationReadConst extends Operation {
  public String constExpr;

  @Override
  public Display buildDisplay(DisplayFactory builder) {
    return builder.str(constExpr, 1);
  }
}
