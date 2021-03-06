package kz.greetgo.glazga.graphics_probe.metric;

import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.model.ShapeArea;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DrawMetric {

  public interface BracketDrawer {
    FigArea area();

    float widthForHeight(float height);

    Shape shapeIn(FigArea place, float x, float y);
  }

  public BracketDrawer drawerFor(Bracket bracket, BracketSide side) {
    final int glyphIndex = bracket.ordinal() * BracketSide.values().length + side.ordinal();
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
  private final Graphics2D graphics;

  public DrawMetric(Font font) {
    BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    graphics = image.createGraphics();
    graphics.setFont(font);

    StringBuilder s = new StringBuilder(Bracket.values().length * 2);
    for (Bracket bracket : Bracket.values()) {
      for (BracketSide side : BracketSide.values()) {
        s.append(bracket.forSide(side));
      }
    }

    GlyphVector glyphVector = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), s.toString());

    for (int i = 0, n = glyphVector.getNumGlyphs(); i < n; i++) {
      Shape shape = glyphVector.getGlyphOutline(i);
      Rectangle2D rect = glyphVector.getGlyphLogicalBounds(i).getBounds2D();
      glyphList.add(new Glyph(shape, rect));
    }

    glyphList.forEach(Glyph::killXY);
  }

  public ShapeArea letterShapeArea(char c) {
    GlyphVector glyphVector = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), new char[]{c});
    Shape glyphOutline = glyphVector.getGlyphOutline(0);
    Rectangle2D bounds = glyphVector.getGlyphLogicalBounds(0).getBounds2D();

    FigArea area = new FigArea();
    area.top = (float) -bounds.getY();
    area.bottom = (float) (bounds.getHeight() + bounds.getY());
    area.width = (float) bounds.getWidth();

    AffineTransform tx = new AffineTransform();
    tx.translate(-bounds.getMinX(), -bounds.getMinY());

    return new ShapeArea(tx.createTransformedShape(glyphOutline), area);
  }

  public float levelHeight(float baseHeight, int level) {
    if (level >= 1) while (level-- > 1) {
      baseHeight = baseHeight * 0.8f;
    }
    else while (level++ < 1) {
      baseHeight = baseHeight * 1.2f;
    }
    return baseHeight;
  }

  public HeightMetric heightMetric(float height) {
    LineMetrics lineMetrics = graphics.getFontMetrics().getLineMetrics("A", graphics);

    float itMiddleLine = Math.abs(lineMetrics.getStrikethroughOffset());
    float itMiddleThickness = lineMetrics.getStrikethroughThickness();
    float itHeight = lineMetrics.getHeight();
    float itLineGap = lineMetrics.getLeading();

//    if (itLineGap < 0.00001f) {
//      itLineGap = itHeight * 0.1f;
//    }

    float itUnderlineOffset = lineMetrics.getUnderlineOffset();
    float itUnderlineThickness = lineMetrics.getUnderlineThickness();

    float k = height / itHeight;

    {
      HeightMetric ret = new HeightMetric();
      ret.middleLine = itMiddleLine * k;
      ret.middleThickness = itMiddleThickness * k;
      ret.lineGap = itLineGap * k;
      ret.underlineOffset = itUnderlineOffset * k;
      ret.underlineThickness = itUnderlineThickness * k;
      return ret;
    }
  }
}
