package kz.greetgo.glazga.graphics_probe.desk;

public interface Desk {
  Box getRootBox(float width, float height);

  void setOver(Target target);
}
