package kz.greetgo.glazga.graphics_probe.model;

public class FigArea {
  public float width;
  public float top;
  public float bottom;

  public FigArea() {}

  public FigArea(float width, float top, float bottom) {
    this.width = width;
    this.top = top;
    this.bottom = bottom;
  }

  public float height() {
    return top + bottom;
  }
}
