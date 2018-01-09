package kz.greetgo.glazga.graphics_probe;

import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.model.FigArea;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawBracketProbe {
  public static void main(String[] args) throws Exception {
    new DrawBracketProbe().run();
  }

  private void run() throws Exception {
    BufferedImage image = new BufferedImage(1000, 550, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(255, 255, 255));

      Rectangle2D r = new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight());
      g.fill(r);


      {
        float x = 250, y = 180;
        g.setColor(new Color(5, 5, 5));
        FigArea figArea = new FigArea(300, 100, 30);
        drawTo(g, figArea, x, y);
        point(g, x, y, 2);
      }
      {
        float x = 300, y = 340;
        g.setColor(new Color(5, 5, 5));
        FigArea figArea = new FigArea(180, 80, 40);
        drawTo(g, figArea, x, y);
        point(g, x, y, 2);
      }

      g.dispose();
    }

    File outputFile = new File("build/DrawBracketProbe.png");
    outputFile.getParentFile().mkdirs();
    ImageIO.write(image, "png", outputFile);
  }

  @SuppressWarnings("SameParameterValue")
  private void point(Graphics2D g, float x, float y, int r) {
    for (int Y = Math.round(y) - r, Y2 = Math.round(y) + r; Y <= Y2; Y++) {
      g.drawLine(Math.round(x) - r, Y, Math.round(x) + r, Y);
    }
  }

  @SuppressWarnings("SameParameterValue")
  private void drawTo(Graphics2D g, FigArea a, float x, float y) {
    g.draw(new Rectangle2D.Float(x, y - a.top, a.width, a.top));
    g.draw(new Rectangle2D.Float(x, y, a.width, a.bottom));
  }
}
