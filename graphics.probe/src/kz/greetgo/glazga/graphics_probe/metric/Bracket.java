package kz.greetgo.glazga.graphics_probe.metric;

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

  public char forSide(BracketSide side) {
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
