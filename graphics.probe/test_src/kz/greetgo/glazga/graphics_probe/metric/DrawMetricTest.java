package kz.greetgo.glazga.graphics_probe.metric;

import kz.greetgo.glazga.graphics_probe.display.ShapeArea;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawMetricTest {

  private DrawMetric drawMetric;


  @BeforeMethod
  public void create_drawMetric() throws Exception {
    drawMetric = Fonts.With.Merriweather_Light.drawMetric();
  }

  @DataProvider
  public Object[][] letterShapeArea_DP() {
    String s = "ЖЧц1ui_Щ!@";
    Object[][] ret = new Object[s.length()][];
    for (int i = 0, n = s.length(); i < n; i++) {
      ret[i] = new Object[]{s.charAt(i)};
    }
    return ret;
  }

  @Test(dataProvider = "letterShapeArea_DP")
  public void letterShapeArea(char c) throws Exception {
    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      ShapeArea shapeArea = drawMetric.letterShapeArea(c);

      g.setColor(new Color(0x4EFF2D));
      g.fill(shapeArea.shape);

      g.setColor(new Color(0x1F0EB8));
      g.draw(new Rectangle2D.Float(0, 0, shapeArea.area.width, shapeArea.area.height()));
      g.drawLine(0, shapeArea.area.topInt(), shapeArea.area.widthInt(), shapeArea.area.topInt());

      g.dispose();
    }

    File file = new File("build/DrawMetricTest_letterShapeArea_" + c + ".png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }
}