package kz.greetgo.glazga.graphics_probe.fonts;

import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Fonts {

  public void applyHints(Graphics2D g) {
    g.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    );
  }

  private enum WithType {
    TTF_1
  }

  @SuppressWarnings("unused")
  public enum With {
    Merriweather_Black(WithType.TTF_1),
    Merriweather_BlackItalic(WithType.TTF_1),
    Merriweather_Bold(WithType.TTF_1),
    Merriweather_BoldItalic(WithType.TTF_1),
    Merriweather_Italic(WithType.TTF_1),
    Merriweather_Light(WithType.TTF_1),
    Merriweather_LightItalic(WithType.TTF_1),
    Merriweather_Regular(WithType.TTF_1),

    //
    ;

    private final String resourceName;
    private final int fontType;

    With(WithType type) {
      String name = name();
      name = name.replace('_', '-');
      int index = name.indexOf('-');
      String group = name.substring(0, index);
      resourceName = group + "/" + name + ".ttf";
      fontType = Font.TRUETYPE_FONT;
    }

    private Font font = null;

    public Font font(float size) {
      if (font == null) {
        try (InputStream inputStream = Fonts.get().getClass().getResourceAsStream(resourceName)) {
          font = Font.createFont(fontType, inputStream);
        } catch (IOException | FontFormatException e) {
          throw new RuntimeException(e);
        }
      }

      return font.deriveFont(size);
    }

    private DrawMetric drawMetric;

    public DrawMetric drawMetric() {
      if (drawMetric != null) return drawMetric;
      DrawMetric x = new DrawMetric(font(300));
      return drawMetric = x;
    }
  }

  private Fonts() {}

  private static final Fonts instance = new Fonts();

  public static Fonts get() {
    return instance;
  }

  public static void main(String[] args) throws Exception {

    List<String> list = new ArrayList<>();
    list.add("Merriweather-Black.ttf");
    list.add("Merriweather-BlackItalic.ttf");
    list.add("Merriweather-Bold.ttf");
    list.add("Merriweather-BoldItalic.ttf");
    list.add("Merriweather-Italic.ttf");
    list.add("Merriweather-Light.ttf");
    list.add("Merriweather-LightItalic.ttf");
    list.add("Merriweather-Regular.ttf");

    for (String nameTTF : list) {
      InputStream in = Fonts.get().getClass().getResourceAsStream("Merriweather/" + nameTTF);
      Font font = Font.createFont(Font.TRUETYPE_FONT, in);
      System.out.println(nameTTF + " - " + font);
    }

  }
}
