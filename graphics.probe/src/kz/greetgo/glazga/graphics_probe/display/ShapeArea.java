package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class ShapeArea {
  public final Shape shape;
  public final FigArea area;

  public ShapeArea(Shape shape, FigArea area) {
    this.shape = shape;
    this.area = area;
  }

  public ShapeArea setHeight(float height) {
    float K = height / area.height();
    float top = area.top * K;
    float bottom = area.bottom * K;
    float width = area.width * K;
    FigArea newArea = new FigArea(width, top, bottom);
    Shape newShape = AffineTransform.getScaleInstance(K, K).createTransformedShape(shape);
    return new ShapeArea(newShape, newArea);
  }
}
