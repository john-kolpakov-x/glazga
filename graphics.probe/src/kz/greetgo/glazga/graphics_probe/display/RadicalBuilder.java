package kz.greetgo.glazga.graphics_probe.display;

import kz.greetgo.glazga.graphics_probe.model.FigArea;
import kz.greetgo.glazga.graphics_probe.util.FloatSupplier;

import java.awt.Graphics2D;
import java.util.function.Function;

public class RadicalBuilder {
  private final Display radical;

  public float radicalHeight() {
    return radical.area().height();
  }

  private FloatSupplier radicalUpGap = () -> radicalHeight() * 0.04f;
  private FloatSupplier radicalLeftGap = radicalUpGap;
  private FloatSupplier radicalRightGap = radicalUpGap;

  @SuppressWarnings("unused")
  public RadicalBuilder setRadicalUpGap(FloatSupplier radicalUpGap) {
    this.radicalUpGap = radicalUpGap;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setRadicalLeftGap(FloatSupplier radicalLeftGap) {
    this.radicalLeftGap = radicalLeftGap;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setRadicalRightGap(FloatSupplier radicalRightGap) {
    this.radicalRightGap = radicalRightGap;
    return this;
  }

  private FloatSupplier radicalScionLength = radicalUpGap;

  @SuppressWarnings("unused")
  public RadicalBuilder setRadicalScionLength(FloatSupplier radicalScionLength) {
    this.radicalScionLength = radicalScionLength;
    return this;
  }

  private FloatSupplier bendHeight = this::radicalHeight;
  private FloatSupplier bendPit = () -> radicalHeight() * 0.33f;
  private FloatSupplier bendUpWidth = () -> radicalHeight() * 0.2f;
  private FloatSupplier bendDownWidth = bendUpWidth;

  @SuppressWarnings("unused")
  public RadicalBuilder setBendHeight(FloatSupplier bendHeight) {
    this.bendHeight = bendHeight;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setBendPit(FloatSupplier bendPit) {
    this.bendPit = bendPit;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setBendUpWidth(FloatSupplier bendUpWidth) {
    this.bendUpWidth = bendUpWidth;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setBendDownWidth(FloatSupplier bendDownWidth) {
    this.bendDownWidth = bendDownWidth;
    return this;
  }

  public RadicalBuilder(Display radical) {
    this.radical = radical;
  }

  private FloatSupplier indexDownGap = radicalUpGap;
  private FloatSupplier indexLeftGap = indexDownGap;
  private FloatSupplier indexRightGap = indexDownGap;
  private FloatSupplier indexScionLength = radicalUpGap;

  @SuppressWarnings("unused")
  public RadicalBuilder setIndexDownGap(FloatSupplier indexDownGap) {
    this.indexDownGap = indexDownGap;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setIndexLeftGap(FloatSupplier indexLeftGap) {
    this.indexLeftGap = indexLeftGap;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setIndexRightGap(FloatSupplier indexRightGap) {
    this.indexRightGap = indexRightGap;
    return this;
  }

  @SuppressWarnings("unused")
  public RadicalBuilder setIndexScionLength(FloatSupplier indexScionLength) {
    this.indexScionLength = indexScionLength;
    return this;
  }

  private Display index = null;

  @SuppressWarnings("unused")
  public RadicalBuilder setIndex(Display index) {
    this.index = index;
    return this;
  }

  private Function<Graphics2D, Graphics2D> radixPreparatory = Function.identity();

  public RadicalBuilder setRadixPreparatory(Function<Graphics2D, Graphics2D> radixPreparatory) {
    this.radixPreparatory = radixPreparatory;
    return this;
  }

  private boolean built = false;

  public Display build() {
    if (built) {
      throw new RuntimeException("Already built");
    }
    built = true;
    return new Display() {
      @Override
      public void paint(Graphics2D g, float x, float y) {
        paintInner(g, x, y);
      }

      @Override
      public FigArea area() {
        return getArea();
      }
    };
  }

  private FigArea area;
  private float radicalX, radicalY, indexX, indexY;
  private float Ax, Ay, Bx, By, Cx, Cy, Dx, Dy, Ex, Ey;

  private void calc() {
    float AB, DE;
    {
      float indexWidth = index == null ? 0f : index.area().width;
      float indexRightGap = this.indexRightGap.value();
      float AB1 = indexLeftGap.value() + indexWidth - indexRightGap;
      AB = AB1 < 0 ? 0 : AB1;
    }
    {
      DE = radicalLeftGap.value() + radical.area().width + radicalRightGap.value();
    }
    {
      float indexPeep = index == null ? 0 : index.area().height() - bendPit.value() + indexDownGap.value();
      if (indexPeep < 0) {
        indexPeep = 0;
      }

      area = new FigArea();

      area.top = radical.area().top + radicalUpGap.value() + indexPeep;

      float bottomCalc = bendHeight.value() - radical.area().top - radicalUpGap.value();

      area.bottom = Math.max(bottomCalc, radical.area().bottom);
    }
    {
      radicalX = AB + bendDownWidth.value() + bendUpWidth.value() + radicalLeftGap.value();
    }
    {
      Ax = 0;
      Ay = radical.area().top + radicalUpGap.value() - bendPit.value();

      Bx = AB;
      By = Ay;

      Cx = Bx + bendDownWidth.value();
      Cy = radical.area().top + radicalUpGap.value() - bendHeight.value();

      Dx = Cx + bendUpWidth.value();
      Dy = radical.area().top + radicalUpGap.value();

      radicalX = Dx + radicalLeftGap.value();
      radicalY = 0;

      Ex = Dx + DE;
      Ey = Dy;

      area.width = Ex;

      Ay = -Ay;
      By = -By;
      Cy = -Cy;
      Dy = -Dy;
      Ey = -Ey;
    }

    if (index != null) {
      indexX = indexLeftGap.value();
      indexY = Ay - indexDownGap.value() - index.area().bottom;
    }
  }

  private void paintInner(Graphics2D g, float x, float y) {
    calc();

    radical.paint(g, x + radicalX, y + radicalY);

    if (index != null) {
      index.paint(g, x + indexX, y + indexY);
    }

    paintRadical(radixPreparatory.apply(g), x, y);
  }

  private void paintRadical(Graphics2D g, float x, float y) {
    PolygonalСhain pc = new PolygonalСhain();

    if (indexScionLength != null && index != null) {
      pc.point(Ax, Ay + indexScionLength.value());
    }

    pc.point(Ax, Ay)
      .point(Bx, By)
      .point(Cx, Cy)
      .point(Dx, Dy)
      .point(Ex, Ey)
    ;

    if (radicalScionLength != null) {
      pc.point(Ex, Ey + radicalScionLength.value());
    }

    pc.drawTo(g, x, y);
  }

  private FigArea getArea() {
    calc();
    return area;
  }
}
