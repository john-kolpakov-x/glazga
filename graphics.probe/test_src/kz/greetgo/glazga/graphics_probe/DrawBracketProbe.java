package kz.greetgo.glazga.graphics_probe;

import kz.greetgo.glazga.graphics_probe.fonts.BracketDrawMetric;
import kz.greetgo.glazga.graphics_probe.fonts.Fonts;
import kz.greetgo.glazga.graphics_probe.model.FigArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import static kz.greetgo.glazga.graphics_probe.fonts.BracketDrawMetric.Bracket.CURLY;
import static kz.greetgo.glazga.graphics_probe.fonts.BracketDrawMetric.Bracket.PARENTHESIS;
import static kz.greetgo.glazga.graphics_probe.fonts.BracketDrawMetric.Bracket.SQUARE;
import static kz.greetgo.glazga.graphics_probe.fonts.BracketDrawMetric.Side.LEFT;
import static kz.greetgo.glazga.graphics_probe.fonts.BracketDrawMetric.Side.RIGHT;

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
        g.setColor(new Color(5, 5, 5));
        FigArea area = new FigArea(300, 100, 30);
        paintBracketsAround(g, area, 250, 180, PARENTHESIS);
      }
      {
        g.setColor(new Color(0xD10D0A));
        FigArea area = new FigArea(180, 80, 40);
        paintBracketsAround(g, area, 300, 340, SQUARE);
      }

      {
        g.setColor(new Color(0xD10D0A));
        FigArea area = new FigArea(180, 80, 40);
        paintBracketsAround(g, area, 700, 340, CURLY);
      }

      g.dispose();
    }

    File outputFile = new File("build/DrawBracketProbe.png");
    outputFile.getParentFile().mkdirs();
    ImageIO.write(image, "png", outputFile);
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private void paintBracketsAround(Graphics2D g, FigArea area, float x, float y, BracketDrawMetric.Bracket br) {
    drawFigAreaBounds(g, area, x, y);
    point(g, x, y, 2);

    BracketDrawMetric drawMetric = Fonts.With.Merriweather_Light.bracketDrawMetric();
    BracketDrawMetric.BracketDrawer bracketLeft = drawMetric.drawerFor(br, LEFT);
    BracketDrawMetric.BracketDrawer bracketRight = drawMetric.drawerFor(br, RIGHT);

    float widthLeft = bracketLeft.widthForHeight(area.height());
    float widthRight = bracketRight.widthForHeight(area.height());
    FigArea areaLeft = new FigArea(widthLeft, area.top, area.bottom);
    FigArea areaRight = new FigArea(widthRight, area.top, area.bottom);

    Shape shapeLeft = bracketLeft.shapeIn(areaLeft, x - areaLeft.width, y);
    Shape shapeRight = bracketRight.shapeIn(areaRight, x + area.width, y);

    g.fill(shapeLeft);
    g.fill(shapeRight);


  }

  @SuppressWarnings("SameParameterValue")
  private void point(Graphics2D g, float x, float y, int r) {
    for (int Y = Math.round(y) - r, Y2 = Math.round(y) + r; Y <= Y2; Y++) {
      g.drawLine(Math.round(x) - r, Y, Math.round(x) + r, Y);
    }
  }

  @SuppressWarnings("SameParameterValue")
  private void drawFigAreaBounds(Graphics2D g, FigArea a, float x, float y) {
    g.draw(new Rectangle2D.Float(x, y - a.top, a.width, a.top));
    g.draw(new Rectangle2D.Float(x, y, a.width, a.bottom));
  }
}
