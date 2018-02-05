package kz.greetgo.glazga.graphics_probe.desk.tag;

import java.util.List;

public interface Tag {
  String name();

  String attr(String attrName);

  List<Tag> children();
}
