package kz.greetgo.glazga.graphics_probe.fonts;

import kz.greetgo.glazga.graphics_probe.model.FigArea;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

  public interface BracketDrawer {
    FigArea area();

    float widthForHeight(float height);

    Shape shapeIn(FigArea place, float x, float y);
  }

  public BracketDrawer drawerFor(Bracket bracket, Side side) {
    final int glyphIndex = bracket.ordinal() * Side.values().length + side.ordinal();
    return new BracketDrawer() {

      @Override
      public FigArea area() {
        return FigArea.from(glyphList.get(glyphIndex).rect);
      }

      @Override
      public float widthForHeight(float height) {
        FigArea a = area();
        return height / a.height() * a.width;
      }

      @Override
      public Shape shapeIn(FigArea place, float x, float y) {
        Glyph glyph = glyphList.get(glyphIndex);
        AffineTransform tx = new AffineTransform();
        tx.translate(x, y - place.top);
        tx.concatenate(area().resizeTo(place));
        return tx.createTransformedShape(glyph.shape);
      }
    };
  }

  private class Glyph {
    Shape shape;
    final Rectangle2D rect;

    public Glyph(Shape shape, Rectangle2D rect) {
      this.shape = shape;
      this.rect = rect;
    }

    public void killXY() {
      AffineTransform tx = new AffineTransform();
      tx.translate(-rect.getMinX(), -rect.getMinY());
      shape = tx.createTransformedShape(shape);
      rect.setRect(0, 0, rect.getWidth(), rect.getHeight());
    }
  }

  private final List<Glyph> glyphList = new ArrayList<>();

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

    GlyphVector glyphVector = g.getFont().createGlyphVector(g.getFontRenderContext(), s.toString());
    for (int i = 0, n = glyphVector.getNumGlyphs(); i < n; i++) {
      Shape shape = glyphVector.getGlyphOutline(i);
      glyphList.add(new Glyph(shape, shape.getBounds2D()));
    }

    glyphList.forEach(Glyph::killXY);
  }

}

