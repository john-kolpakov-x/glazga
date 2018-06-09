package kz.greetgo.glazga.graphics_probe.forms2.model;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayBuilder;

public class OperationReadVar extends Operation {
  public String varName;

  @Override
  public Display buildDisplay(DisplayBuilder builder) {
    return builder.str(varName, 1);
  }
}
