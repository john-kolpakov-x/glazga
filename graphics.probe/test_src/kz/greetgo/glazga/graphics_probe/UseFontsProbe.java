package kz.greetgo.glazga.graphics_probe;

import kz.greetgo.glazga.graphics_probe.fonts.Fonts;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UseFontsProbe {
  public static void main(String[] args) throws IOException {
    BufferedImage image = new BufferedImage(1000, 550, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      Fonts.get().applyHints(g);

      g.setColor(new Color(255, 255, 255));

      Rectangle2D r = new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight());
      g.fill(r);

      int y1 = 140, y2 = y1 + 160, y3 = y2 + 160;
      int x = 10;

      String s = "(Привет всем)";

      g.setColor(new Color(5, 5, 5));
      g.setFont(Fonts.With.Merriweather_Regular.font(120f));
      g.drawString(s, x, y1);
      int w1 = g.getFontMetrics().stringWidth(s);
      int ascent1 = g.getFontMetrics().getAscent();
      int descent1 = g.getFontMetrics().getDescent();
      GlyphVector glyphVector1 = g.getFont().createGlyphVector(g.getFontRenderContext(), s);

      g.setFont(Fonts.With.Merriweather_Black.font(120f));
      g.drawString(s, x, y2);
      int w2 = g.getFontMetrics().stringWidth(s);
      int ascent2 = g.getFontMetrics().getAscent();
      int descent2 = g.getFontMetrics().getDescent();
      GlyphVector glyphVector2 = g.getFont().createGlyphVector(g.getFontRenderContext(), s);

      g.setFont(Fonts.With.Merriweather_Light.font(120f));
      g.drawString(s, x, y3);
      int w3 = g.getFontMetrics().stringWidth(s);
      int ascent3 = g.getFontMetrics().getAscent();
      int descent3 = g.getFontMetrics().getDescent();
      GlyphVector glyphVector3 = g.getFont().createGlyphVector(g.getFontRenderContext(), s);

      AffineTransform tx1 = AffineTransform.getTranslateInstance(x, y1);
      AffineTransform tx2 = AffineTransform.getTranslateInstance(x, y2);
      AffineTransform tx3 = AffineTransform.getTranslateInstance(x, y3);

      g.setColor(new Color(0x65C4FF));

      //1
      {
//        Shape go = glyphVector1.getGlyphVisualBounds(0);
        Shape go = glyphVector1.getGlyphLogicalBounds(0);
        g.draw(tx1.createTransformedShape(go));
        g.draw(go.getBounds2D());
        Rectangle2D bounds2D = go.getBounds2D();
        System.out.println(bounds2D);
      }
      //2
      {
        Shape go = glyphVector2.getGlyphVisualBounds(1);
        g.draw(tx2.createTransformedShape(go));
      }
      {
        Shape go = glyphVector2.getGlyphVisualBounds(0);
        g.draw(tx2.createTransformedShape(go));
      }
      //3
      {
        Shape go = glyphVector3.getGlyphVisualBounds(0);
        g.draw(tx3.createTransformedShape(go));
      }
      {
        Shape go = glyphVector3.getGlyphVisualBounds(2);
        g.draw(tx3.createTransformedShape(go));
      }
      {
        Shape go = glyphVector3.getGlyphVisualBounds(11);
        g.draw(tx3.createTransformedShape(go));
      }

      g.setColor(new Color(0xA8A8A8));
      g.drawLine(0, y1, image.getWidth(), y1);
      g.drawLine(0, y2, image.getWidth(), y2);
      g.drawLine(0, y3, image.getWidth(), y3);

      g.drawLine(x, y1 + descent1, x, y1 - ascent1);
      g.drawLine(x + w1, y1 + descent1, x + w1, y1 - ascent1);

      g.drawLine(x, y2 + descent2, x, y2 - ascent2);
      g.drawLine(x + w2, y2 + descent2, x + w2, y2 - ascent2);

      g.drawLine(x, y3 + descent3, x, y3 - ascent3);
      g.drawLine(x + w3, y3 + descent3, x + w3, y3 - ascent3);

      g.setColor(new Color(0x181DEA));
      g.drawLine(x, y1 + descent1, x + w1, y1 + descent1);
      g.drawLine(x, y2 + descent2, x + w2, y2 + descent2);
      g.drawLine(x, y3 + descent3, x + w3, y3 + descent3);

      g.setColor(new Color(0xE81BEA));
      g.drawLine(x, y1 - ascent1, x + w1, y1 - ascent1);
      g.drawLine(x, y2 - ascent2, x + w2, y2 - ascent2);
      g.drawLine(x, y3 - ascent3, x + w3, y3 - ascent3);

      g.dispose();
    }


    File outputFile = new File("create/UseFontsProbe.png");
    outputFile.getParentFile().mkdirs();
    ImageIO.write(image, "png", outputFile);
  }
}
