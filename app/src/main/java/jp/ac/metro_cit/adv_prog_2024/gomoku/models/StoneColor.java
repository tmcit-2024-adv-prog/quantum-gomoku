package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

public enum StoneColor {
  BLACK,
  WHITE;

  public StoneColor opposite() {
    return this == BLACK ? WHITE : BLACK;
  }
}
