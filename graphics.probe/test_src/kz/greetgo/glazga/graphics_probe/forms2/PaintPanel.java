package kz.greetgo.glazga.graphics_probe.forms2;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayBuilder;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.forms2.model.Assignment;
import kz.greetgo.glazga.graphics_probe.forms2.model.Operation;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationDiv;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationMinus;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationPlus;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationReadConst;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationReadVar;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class PaintPanel {


  @SuppressWarnings("FieldCanBeLocal")
  private DrawMetric drawMetric;
  private DisplayBuilder displayBuilder;

  Operation operation;

  Assignment assignment;

  public PaintPanel() {
    drawMetric = Fonts.With.Merriweather_Light.drawMetric();
    displayBuilder = new DisplayBuilder(drawMetric).baseHeight(() -> 30);

    OperationReadVar varHello = new OperationReadVar();
    varHello.varName = "hello";

    OperationReadConst const23_34 = new OperationReadConst();
    const23_34.constExpr = "23.34";

    OperationPlus topPlus = new OperationPlus();
    topPlus.left = varHello;
    topPlus.right = const23_34;

    OperationMinus bottomMinus = new OperationMinus();
    bottomMinus.left = const23_34;
    bottomMinus.right = varHello;

    OperationDiv div = new OperationDiv();
    div.top = topPlus;
    div.bottom = bottomMinus;

    OperationReadConst const1 = new OperationReadConst();
    const1.constExpr = "1";

    OperationPlus mainPlus = new OperationPlus();
    mainPlus.left = const1;
    mainPlus.right = div;

    operation = mainPlus;

    assignment = new Assignment();
    assignment.type = "Str";
    assignment.varName = "radius";
    assignment.operation = mainPlus;
    assignment.position = new Point(100, 100);
  }


  public void startPaint() {}

  public void paint(Graphics2D g, int width, int height) {
    g.setColor(new Color(239, 12, 19));
    g.drawLine(10, 100, 100, 10);

    {
      Display display = operation.buildDisplay(displayBuilder);
      g.setColor(new Color(37, 32, 239));
      display.paint(g, 100, 250);
    }

    {
      Display display = assignment.buildDisplay(displayBuilder);
      g.setColor(new Color(35, 195, 29));
      display.paint(g, assignment.position.x, assignment.position.y);
    }
  }
}
