package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.metric.Bracket;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import kz.greetgo.glazga.graphics_probe.model.FigArea;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Function;

public class DisplayFactoryTest {

  private DrawMetric drawMetric;

  @BeforeMethod
  public void create_drawMetric() {
    drawMetric = Fonts.With.Merriweather_Light.drawMetric();
  }

  @Test
  public void str() throws Exception {
    DisplayFactory factory = new DisplayFactory(drawMetric).baseHeight(() -> 80);


    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    String str = "Жёлудь съеден!";

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));
      factory.str(str, 1).paint(g, 100, 200);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_str_" + str + ".png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  @Test
  public void div() throws Exception {
    DisplayFactory factory = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));

      Display a = factory.str("a + ", 1);
      Display top = factory.str("top", 1);
      Display bottom = factory.str("bottom", 1);

      Display div = factory.div(top, bottom, 1);

      Display main = factory.row(a, div);
      main.paint(g, 100, 200);

      g.setColor(new Color(0x2BF435));
      factory.structure(a).paint(g, 100, 200);
      factory.structure(main).paint(g, 100, 200);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_div.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  @Test
  public void power() throws Exception {
    DisplayFactory factory = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      Display center = factory.area(80, 75, 25, colorSetter(new Color(0x3636FF)));
      Display power = factory.area(40, 23, 8, colorSetter(new Color(0xFF43B8)));

      float x = 100, y = 200;
      g.setColor(new Color(0x000000));
      drawPointTo(g, x, y);

      Display main = factory
        .power(center)
        .left(1, +0.8f, power)
        .top(1, 0, power)
        .bottom(1, 0, power)
        .right(1, -0.8f, power)
        .create();

      g.setColor(new Color(0x2BF435));
      factory.structure(main).paint(g, x, y);

      main.paint(g, x, y);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_power.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  private void drawPointTo(Graphics2D g, float x1, float y1) {
    int x = Math.round(x1), y = Math.round(y1);
    g.drawLine(x - 9, y - 5, x, y);
    g.drawLine(x - 9, y + 5, x, y);
    g.drawLine(x - 9, y + 5, x - 9, y - 5);
  }

  private static Function<Graphics2D, Graphics2D> colorSetter(Color color) {
    return g -> {
      g.setColor(color);
      return g;
    };
  }

  @Test
  public void brackets() throws Exception {
    DisplayFactory factory = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));

      Display a = factory.str("a + ", 1);
      Display top = factory.str("top", 1);
      Display bottom = factory.str("bottom", 1);

      Display div = factory.div(top, bottom, 1);

      float x = 100, y = 200;

      Display a_div = factory.row(a, div);

      Display main = factory.brackets(a_div, Bracket.SQUARE);
      main.paint(g, x, y);

      g.setColor(new Color(0x2BF435));
      factory.structure(main).paint(g, x, y);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_brackets.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  @Test
  public void rama() throws Exception {
    DisplayFactory factory = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1500, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xD6D7D9));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));

      Display a1 = factory.area(new FigArea(100, 50, 50), Function.identity());
      Display a2 = factory.area(new FigArea(70, 50, 70), Function.identity());
      Display a3 = factory.area(new FigArea(130, 60, 30), Function.identity());
      Display a4 = factory.area(new FigArea(100, 50, 50), Function.identity());
      Display a5 = factory.area(new FigArea(10, 20, 20), Function.identity());
      Display a6 = factory.str("Hi", 1);

      {
        Display display = factory.rama()
          .window(a1, colorSetter(new Color(124, 79, 255)))
          .window(a6, colorSetter(new Color(158, 162, 41)))
          .window(a2, colorSetter(new Color(124, 79, 255)))
          .window(a3, colorSetter(new Color(124, 79, 255)))
          .window(a4, colorSetter(new Color(124, 79, 255)))
          .window(a5, colorSetter(new Color(158, 162, 41)))
          .setBorderPreparatory(colorSetter(new Color(203, 12, 18)))
          .create();

        display.paint(g, 30, 90);

//      g.setColor(new Color(0x1BC5C4));
//      builder.structure(display).paint(g, 30, 320);//+17);
      }

      Display _1 = factory.str("1", 1);
      Display _2 = factory.str("2", 2);
      Display _a = factory.str("a", 1);
      Display _b = factory.str("b", 1);
      Display _a2 = factory.power(_a).right(1, 0.5f, _2).create();
      Display _b2 = factory.power(_b).right(1, 0.5f, _2).create();
      Display space = factory.str(" ", 1);
      Display plus = factory.str("+", 1);
      Display minus = factory.str("-", 1);

      Display bottom1 = factory.row(_a2, plus, _b2);
      Display _a_plus_b = factory.brackets(factory.row(_a, plus, _b), Bracket.PARENTHESIS);
      Display bottom2 = factory.power(_a_plus_b).right(1, 0.5f, _2).create();

      Display _a_minus_b = factory.brackets(factory.row(_a, minus, _b), Bracket.PARENTHESIS);
      Display bottom3 = factory.power(_a_minus_b).right(1, 0.5f, _2).create();

      Display div1 = factory.div(_1, bottom1, 1);
      Display div2 = factory.div(_1, bottom2, 1);
      Display div3 = factory.div(_1, bottom3, 1);

      Display b1 = factory.str(" float ", 1);
      Display b2 = factory.str(" x ", 1);
      Display b3 = factory.str("←", 1);
      Display b4 = factory.row(space, div1, plus, div2, plus, div3, space);

      {
        Display display = factory.rama()
          .window(b1, colorSetter(new Color(124, 79, 255)))
          .window(b2, colorSetter(new Color(124, 79, 255)))
          .window(b3, colorSetter(new Color(124, 79, 255)))
          .window(b4, colorSetter(new Color(124, 79, 255)))
          .setBorderPreparatory(colorSetter(new Color(203, 12, 18)))
          .create();

        display.paint(g, 30, 300);
      }

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_rama.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  @Test
  public void radical() throws Exception {
    DisplayFactory factory = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1500, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xCBD5D9));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      Display radical = factory.area(80, 75, 25, colorSetter(new Color(0x3636FF)));
      Display index = factory.area(40, 37, 12, colorSetter(new Color(0x218329)));

      radical.paint(g, 10, 100);
      index.paint(g, 10, 300);

      Display display = factory.radical(radical)
        .setIndex(index)
        .setRadixPreparatory(colorSetter(new Color(0x831A81)))
        .build();

      display.paint(g, 200, 300);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_radical.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }
}