package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

public class Stone {
  Color color;
  Vector2D pos;

  public Stone(Color color, Vector2D pos) {
    this.color = color;
    this.pos = pos;
  }

  public Color getColor() {
    return color;
  }

  public Vector2D GetPos() {
    return pos;
  }
}
