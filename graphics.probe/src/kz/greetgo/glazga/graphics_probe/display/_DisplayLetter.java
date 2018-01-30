package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.model.ShapeArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

class _DisplayLetter implements Display {
  final FloatSupplier savedBaseHeight;
  final ShapeArea letterShapeArea;
  private final char c;
  private final int level;
  DrawMetric drawMetric;

   _DisplayLetter(char c, int level, FloatSupplier baseHeight, DrawMetric drawMetric) {
    this.c = c;
    this.level = level;
    this.drawMetric = drawMetric;
    savedBaseHeight = baseHeight;
    letterShapeArea = drawMetric.letterShapeArea(c);
  }

  @Override
  public void paint(Graphics2D g, float x, float y) {
    ShapeArea shapeArea = letterShapeArea.setHeight(drawMetric.levelHeight(savedBaseHeight.value(), level));

    Shape letterShape = AffineTransform
      .getTranslateInstance(x, y - shapeArea.area.top)
      .createTransformedShape(shapeArea.shape);

    g.fill(letterShape);
  }

  @Override
  public FigArea area() {
    return letterShapeArea.setHeight(savedBaseHeight.value()).area;
  }
}
