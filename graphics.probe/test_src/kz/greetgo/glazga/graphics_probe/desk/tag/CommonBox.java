package kz.greetgo.glazga.graphics_probe.desk.tag;

import kz.greetgo.glazga.graphics_probe.desk.Box;
import kz.greetgo.glazga.graphics_probe.desk.Target;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

class CommonBox implements Box {

  final String id, name;

  CommonBox(String id, String name) {
    this.id = id;
    this.name = name;
  }

  final List<CommonBox> children = new ArrayList<>();

  @Override
  public Target findAt(float x, float y) {
    return null;
  }

  @Override
  public void paint(Graphics2D g) {
    paintMe(g);
    children.forEach(b -> b.paint(g));
  }

  final List<DisplayTo> displayList = new ArrayList<>();

  private void paintMe(Graphics2D g) {
    displayList.forEach(d -> d.display.paint(g, d.x, d.y));
  }
}
