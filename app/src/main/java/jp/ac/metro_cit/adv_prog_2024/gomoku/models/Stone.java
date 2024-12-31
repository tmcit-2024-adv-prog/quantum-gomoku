package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.Objects;

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

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Stone) {
      Stone stone = (Stone) obj;
      return this.color == stone.color && this.pos.equals(stone.pos);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color, this.pos);
  }
}
