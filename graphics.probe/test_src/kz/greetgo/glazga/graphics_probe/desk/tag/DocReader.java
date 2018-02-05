package kz.greetgo.glazga.graphics_probe.desk.tag;

import kz.greetgo.glazga.graphics_probe.util.UtilIO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocReader {

  private static class MyDoc implements Doc {

    class Tag1 implements Tag {
      final String name;

      Tag1(String name) {this.name = name;}

      @Override
      public String name() {
        return name;
      }

      final Map<String, String> attrMap = new HashMap<>();

      @Override
      public String attr(String attrName) {
        return attrMap.get(attrName);
      }

      final List<Tag1> children = new ArrayList<>();

      @Override
      public List<Tag> children() {
        return new ArrayList<>(children);
      }

      public int readRest(int from, String text) {
        int i = from;
        while (i < text.length()) {

          int i2 = text.length() - 1;
          char c = 0;
          for (int ii = i; ii < text.length(); ii++) {
            char cc = text.charAt(ii);
            if (cc == '}' || cc == '{') {
              i2 = ii;
              c = cc;
              break;
            }
          }

          String t = text.substring(i, i2);
          i = i2 + 1;

          t = t.trim();
          Tag1 tag1 = createTag1(t);


          if (c == '{') {
            i = tag1.readRest(i, text);
            if (!tag1.isEmpty()) children.add(tag1);
          } else {
            if (!tag1.isEmpty()) children.add(tag1);
            return i;
          }
        }
        return i;
      }

      private boolean isEmpty() {
        return children.isEmpty() && attrMap.isEmpty() && name.isEmpty();
      }

      private Tag1 createTag1(String t) {
        int is1 = t.indexOf(' ');
        if (is1 < 0) return new Tag1(t);


        Tag1 tag1 = new Tag1(t.substring(0, is1));

        for (String pair : t.substring(is1).trim().split(",")) {
          {
            int sCount = 0;
            while (sCount < pair.length() && pair.charAt(sCount) == ' ') sCount++;
            pair = pair.substring(sCount);
          }

          int is2 = pair.indexOf(' ');
          if (is2 < 0) {
            tag1.attrMap.put(pair, null);
          } else {
            tag1.attrMap.put(pair.substring(0, is2), pair.substring(is2 + 1).trim());
          }
        }
        return tag1;

      }

      public void append(StringBuilder sb, int spaces) {
        for (int i = 0; i < spaces; i++) sb.append("  ");
        sb.append(name);
        String s = attrMap.entrySet().stream()
          .sorted(Comparator.comparing(Map.Entry::getKey))
          .map(e -> e.getKey() + ' ' + e.getValue())
          .collect(Collectors.joining(", "));
        sb.append(s.length() == 0 ? "" : " " + s);
        if (children.isEmpty()) {
          sb.append(" {}\n");
        } else {
          sb.append(" {\n");
          children.forEach(c -> c.append(sb, spaces + 1));
          for (int i = 0; i < spaces; i++) sb.append("  ");
          sb.append("}\n");
        }
      }
    }

    @Override
    public Tag first() {
      return roots().get(0);
    }

    final Tag1 top = new Tag1(null);

    @Override
    public List<Tag> roots() {
      return top.children();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      top.children.forEach(tag1 -> tag1.append(sb, 0));
      return sb.toString();
    }
  }

  private MyDoc doc = new MyDoc();

  public static Doc read(String text) {
    DocReader docReader = new DocReader();
    docReader.doc.top.readRest(0, text);
    return docReader.doc;
  }

  public static Doc read(InputStream inputStream) {
    try {
      return read(UtilIO.readAllAsStr(inputStream));
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        //noinspection ThrowFromFinallyBlock
        throw new RuntimeException(e);
      }
    }
  }
}
