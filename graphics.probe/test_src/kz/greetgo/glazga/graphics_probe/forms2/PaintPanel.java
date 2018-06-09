package kz.greetgo.glazga.graphics_probe.forms2;

import kz.greetgo.glazga.graphics_probe.display.Display;
import kz.greetgo.glazga.graphics_probe.display.DisplayBuilder;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.forms2.model.Operation;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationDiv;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationMinus;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationPlus;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationReadConst;
import kz.greetgo.glazga.graphics_probe.forms2.model.OperationReadVar;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;

import java.awt.Color;
import java.awt.Graphics2D;

public class PaintPanel {


  private DrawMetric drawMetric;
  private DisplayBuilder displayBuilder;

  Operation operation;

  public PaintPanel() {
    drawMetric = Fonts.With.Merriweather_Light.drawMetric();
    displayBuilder = new DisplayBuilder(drawMetric).baseHeight(() -> 80);

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
  }

  long startedAt;

  public void startPaint() {
    startedAt = System.currentTimeMillis();
  }

  public void paint(Graphics2D g, int width, int height) {
    g.setColor(new Color(239, 12, 19));
    g.drawLine(10, 100, 100, 10);

    Display display = operation.buildDisplay(displayBuilder);

    double seconds = (double) (System.currentTimeMillis() - startedAt) / 1000.0;

    int offset = (int) Math.round(100 * Math.sin(seconds));

    g.setColor(new Color(37, 32, 239));

    display.paint(g, 100, 250 + offset);
  }
}
