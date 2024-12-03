package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

public class Player {
  private Color color;
  private String name;

  public Player(String name) {
    this.name = name;
  }

  public Player(Color color, String name) {
    this.color = color;
    this.name = name;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    if (this.color == null) {
      this.color = color;
    }
  }

  public String getName() {
    return name;
  }
}
