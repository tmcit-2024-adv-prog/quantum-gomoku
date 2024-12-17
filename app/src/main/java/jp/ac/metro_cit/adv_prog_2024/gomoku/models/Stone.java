package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

public class Stone {
  StoneColor color;
  Vector2D pos;

  public Stone(StoneColor color, Vector2D pos) {
    this.color = color;
    this.pos = pos;
  }

  public StoneColor getColor() {
    return color;
  }

  public Vector2D getPos() {
    return pos;
  }
}
