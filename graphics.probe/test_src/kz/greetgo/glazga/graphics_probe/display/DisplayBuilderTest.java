package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.metric.DrawMetric;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DisplayBuilderTest {

  private DrawMetric drawMetric;

  @BeforeMethod
  public void create_drawMetric() {
    drawMetric = Fonts.With.Merriweather_Light.drawMetric();
  }

  @Test
  public void str() throws Exception {
    DisplayBuilder builder = new DisplayBuilder(drawMetric, () -> 80);

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

    File file = new File("build/DisplayBuilderTest_str_" + str + ".png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }
}