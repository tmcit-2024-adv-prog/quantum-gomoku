package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
  private StoneColor color;
  private String name;

  public Player(String name) {
    this.name = name;
  }

  public Player(String name, StoneColor color) {
    this(name);
    this.color = color;
  }

  public Player(StoneColor color, String name) {
    this.color = color;
    this.name = name;
  }

  public StoneColor getColor() {
    return color;
  }

  public void setColor(StoneColor color) {
    if (this.color == null) {
      this.color = color;
    }
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player) {
      Player player = (Player) obj;
      return this.name.equals(player.name) && this.color == player.color;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.color);
  }
}
