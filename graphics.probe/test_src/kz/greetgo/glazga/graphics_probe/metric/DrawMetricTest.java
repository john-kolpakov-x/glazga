package kz.greetgo.glazga.graphics_probe.metric;

import kz.greetgo.glazga.graphics_probe.DrawUtil;
import kz.greetgo.glazga.graphics_probe.display.ShapeArea;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

  public static class P {
    final float x, y;

    public P(float x, float y) {
      this.x = x;
      this.y = y;
    }
  }

  @Test
  public void writeStr() throws Exception {
    BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);

    String str = "Жёлудь съеден!";
    float x = 100f, y = 100f, height = 50;

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(0xFFFFFF));
      g.fill(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));

      ShapeArea shapeArea1 = drawMetric.letterShapeArea(str.charAt(0));

      shapeArea1 = shapeArea1.setHeight(height);


      g.setColor(new Color(0x00C500));
      g.draw(new Rectangle2D.Float(0, 0, shapeArea1.area.width, shapeArea1.area.height()));
      g.drawLine(0, shapeArea1.area.topInt(), shapeArea1.area.widthInt(), shapeArea1.area.topInt());

      g.setColor(new Color(0x3636FF));
      g.fill(shapeArea1.shape);

      List<P> points = new ArrayList<>();

      g.setColor(new Color(0xFF31F0));
      points.add(new P(x, y));
      x = drawStr(g, str, height, x, y);
      points.add(new P(x, y));

      x = 100f; y = 200f; height = 80;
      points.add(new P(x, y));
      x = drawStr(g, str, height, x, y);
      points.add(new P(x, y));

      x = 100f; y = 300f; height = 30;
      points.add(new P(x, y));
      x = drawStr(g, str, height, x, y);
      points.add(new P(x, y));

      x = 100f; y = 360f; height = 20;
      points.add(new P(x, y));
      x = drawStr(g, str, height, x, y);
      points.add(new P(x, y));

      g.setColor(new Color(0x000000));
      points.forEach(p -> DrawUtil.point(g, p.x, p.y, 2));

      g.dispose();
    }

    File file = new File("build/DrawMetricTest_writeStr_" + str + ".png");
    file.getParentFile().mkdirs();
    ImageIO.write(image, "png", file);
  }

  private float drawStr(Graphics2D g, String str, float height, float x, float y) {
    for (int i = 0; i < str.length(); i++) {
      ShapeArea shapeArea = drawMetric.letterShapeArea(str.charAt(i)).setHeight(height);
      Shape letterShape = AffineTransform
          .getTranslateInstance(x, y - shapeArea.area.top)
          .createTransformedShape(shapeArea.shape);

      g.fill(letterShape);
      x += shapeArea.area.width;
    }
    return x;
  }
}