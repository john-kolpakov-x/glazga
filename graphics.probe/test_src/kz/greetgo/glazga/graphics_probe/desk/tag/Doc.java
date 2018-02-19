package kz.greetgo.glazga.graphics_probe.desk.tag;

import kz.greetgo.glazga.graphics_probe.desk.Box;

import java.util.List;

public interface Doc {
  Tag first();

  List<Tag> roots();

  Box boxing();
}
