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
    DisplayFactory builder = new DisplayFactory(drawMetric).baseHeight(() -> 80);


    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    String str = "Жёлудь съеден!";

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));
      builder.str(str, 1).paint(g, 100, 200);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_str_" + str + ".png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  @Test
  public void div() throws Exception {
    DisplayFactory builder = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));

      Display a = builder.str("a + ", 1);
      Display top = builder.str("top", 1);
      Display bottom = builder.str("bottom", 1);

      Display div = builder.div(top, bottom, 1);

      Display main = builder.row(a, div);
      main.paint(g, 100, 200);

      g.setColor(new Color(0x2BF435));
      builder.structure(a).paint(g, 100, 200);
      builder.structure(main).paint(g, 100, 200);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_div.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  @Test
  public void power() throws Exception {
    DisplayFactory builder = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      Display center = builder.area(80, 75, 25, colorSetter(new Color(0x3636FF)));
      Display power = builder.area(40, 23, 8, colorSetter(new Color(0xFF43B8)));

      float x = 100, y = 200;
      g.setColor(new Color(0x000000));
      drawPointTo(g, x, y);

      Display main = builder
        .power(center)
        .left(1, +0.8f, power)
        .top(1, 0, power)
        .bottom(1, 0, power)
        .right(1, -0.8f, power)
        .create();

      g.setColor(new Color(0x2BF435));
      builder.structure(main).paint(g, x, y);

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
    DisplayFactory builder = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));

      Display a = builder.str("a + ", 1);
      Display top = builder.str("top", 1);
      Display bottom = builder.str("bottom", 1);

      Display div = builder.div(top, bottom, 1);

      float x = 100, y = 200;

      Display a_div = builder.row(a, div);

      Display main = builder.brackets(a_div, Bracket.SQUARE);
      main.paint(g, x, y);

      g.setColor(new Color(0x2BF435));
      builder.structure(main).paint(g, x, y);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_brackets.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  @Test
  public void rama() throws Exception {
    DisplayFactory builder = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1500, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xD6D7D9));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      g.setColor(new Color(0x3636FF));

      Display a1 = builder.area(new FigArea(100, 50, 50), Function.identity());
      Display a2 = builder.area(new FigArea(70, 50, 70), Function.identity());
      Display a3 = builder.area(new FigArea(130, 60, 30), Function.identity());
      Display a4 = builder.area(new FigArea(100, 50, 50), Function.identity());
      Display a5 = builder.area(new FigArea(10, 20, 20), Function.identity());
      Display a6 = builder.str("Hi", 1);

      {
        Display display = builder.rama()
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

      Display _1 = builder.str("1", 1);
      Display _2 = builder.str("2", 2);
      Display _a = builder.str("a", 1);
      Display _b = builder.str("b", 1);
      Display _a2 = builder.power(_a).right(1, 0.5f, _2).create();
      Display _b2 = builder.power(_b).right(1, 0.5f, _2).create();
      Display space = builder.str(" ", 1);
      Display plus = builder.str("+", 1);
      Display minus = builder.str("-", 1);

      Display bottom1 = builder.row(_a2, plus, _b2);
      Display _a_plus_b = builder.brackets(builder.row(_a, plus, _b), Bracket.PARENTHESIS);
      Display bottom2 = builder.power(_a_plus_b).right(1, 0.5f, _2).create();

      Display _a_minus_b = builder.brackets(builder.row(_a, minus, _b), Bracket.PARENTHESIS);
      Display bottom3 = builder.power(_a_minus_b).right(1, 0.5f, _2).create();

      Display div1 = builder.div(_1, bottom1, 1);
      Display div2 = builder.div(_1, bottom2, 1);
      Display div3 = builder.div(_1, bottom3, 1);

      Display b1 = builder.str(" float ", 1);
      Display b2 = builder.str(" x ", 1);
      Display b3 = builder.str("←", 1);
      Display b4 = builder.row(space, div1, plus, div2, plus, div3, space);

      {
        Display display = builder.rama()
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
    DisplayFactory builder = new DisplayFactory(drawMetric).baseHeight(() -> 80);

    BufferedImage image = new BufferedImage(1500, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xD6D7D9));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      Display main = builder.area(80, 75, 25, colorSetter(new Color(0x3636FF)));
      Display power = builder.area(40, 37, 12, colorSetter(new Color(0x218329)));

      main.paint(g, 10, 100);
      power.paint(g, 10, 300);

      g.dispose();
    }

    File file = new File("build/" + getClass().getSimpleName() + "_radical.png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }
}