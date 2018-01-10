package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;

public class DisplayBuilder {
  final DrawMetric drawMetric;
  public float baseSize;

  public DisplayBuilder(DrawMetric drawMetric, float baseSize) {
    this.drawMetric = drawMetric;
    this.baseSize = baseSize;
  }

}
