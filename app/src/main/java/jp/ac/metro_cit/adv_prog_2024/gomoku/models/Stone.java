package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

public class Stone {

  private StoneColor color;
  private int posX;
  private int posY;

  public Stone(StoneColor color, int posX, int posY) {
    this.color = color;
    this.posX = posX;
    this.posY = posY;
  }

  public StoneColor getColor() {
    return color;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }
}
