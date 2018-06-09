package kz.greetgo.glazga.graphics_probe.forms2.model;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayBuilder;

public class OperationReadConst extends Operation {
  public String constExpr;

  @Override
  public Display buildDisplay(DisplayBuilder builder) {
    return builder.str(constExpr, 1);
  }
}
