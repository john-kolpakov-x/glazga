package kz.greetgo.glazga.graphics_probe.desk.tag;

import kz.greetgo.glazga.graphics_probe.desk.Box;

class DocBoxing {
  private DocReader.MyDoc doc;

  public DocBoxing(DocReader.MyDoc doc) {
    this.doc = doc;
  }

  public Box boxing() {
    CommonBox ret = new CommonBox("root", "ROOT");



    return ret;
  }
}
