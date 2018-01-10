package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Shape;

public class ShapeArea {
  public final Shape shape;
  public final FigArea area;

  public ShapeArea(Shape shape, FigArea area) {
    this.shape = shape;
    this.area = area;
  }
}
