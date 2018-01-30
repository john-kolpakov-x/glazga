package kz.greetgo.glazga.graphics_probe.model;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

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

  public FigArea(double width, double top, double bottom) {
    this((float) width, (float) top, (float) bottom);
  }

  public FigArea(FigArea area) {
    this(area.width, area.top, area.bottom);
  }

  public float height() {
    return top + bottom;
  }

  public static FigArea from(Rectangle2D rect) {
    return new FigArea(rect.getWidth(), -rect.getY(), rect.getY() + rect.getHeight());
  }

  public AffineTransform resizeTo(FigArea to) {
    AffineTransform ret = new AffineTransform();
    ret.scale(to.width / width, to.height() / height());
    return ret;
  }

  @Override
  public String toString() {
    return "FigArea{" +
      "width=" + width +
      ", top=" + top +
      ", bottom=" + bottom +
      '}';
  }

  public int topInt() {
    return Math.round(top);
  }

  public int widthInt() {
    return Math.round(width);
  }

  public FigArea setWidth(float newWidth) {
    width = newWidth;
    return this;
  }

  public FigArea setTop(float newTop) {
    top = newTop;
    return this;
  }

  public FigArea setBottom(float newBottom) {
    bottom = newBottom;
    return this;
  }
}
