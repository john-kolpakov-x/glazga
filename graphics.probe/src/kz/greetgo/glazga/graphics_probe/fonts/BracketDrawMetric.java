package kz.greetgo.glazga.graphics_probe.fonts;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class BracketDrawMetric {

  public enum Side {
    LEFT, RIGHT;
  }

  public enum Bracket {
    /**
     * ()
     */
    PARENTHESIS('(', ')'),
    /**
     * []
     */
    SQUARE('[', ']'),
    /**
     * {}
     */
    CURLY('{', '}');

    public final char left;
    public final char right;

    public char forSide(Side side) {
      switch (side) {
        case LEFT:
          return left;
        case RIGHT:
          return right;
        default:
          throw new IllegalArgumentException("side = " + side);
      }
    }

    Bracket(char left, char right) {
      this.left = left;
      this.right = right;
    }
  }

  public float bracketWidth(float height, Bracket bracket, Side side) {
    int index = bracket.ordinal() * Side.values().length + side.ordinal();
    Shape glyphOutline = glyphVector.getGlyphOutline(index);
    Rectangle2D bounds = glyphOutline.getBounds2D();
    float width1 = (float) bounds.getWidth();
    float height1 = (float) bounds.getHeight();

  }

  public Shape shapeIn(FigArea figArea) {
    return null;
  }

  private final GlyphVector glyphVector;

  BracketDrawMetric(Font font) {
    BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();

    g.setFont(font);

    StringBuilder s = new StringBuilder(Bracket.values().length * 2);
    for (Bracket bracket : Bracket.values()) {
      for (Side side : Side.values()) {
        s.append(bracket.forSide(side));
      }
    }

    glyphVector = g.getFont().createGlyphVector(g.getFontRenderContext(), s.toString());
  }
}
