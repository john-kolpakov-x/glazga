package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;

public class DisplayBuilder {
  final DrawMetric drawMetric;
  public float baseHeight;

  public DisplayBuilder(DrawMetric drawMetric, float baseHeight) {
    this.drawMetric = drawMetric;
    this.baseHeight = baseHeight;
  }

}
