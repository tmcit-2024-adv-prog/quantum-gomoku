package jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions;

public class BoardPositionException extends Exception {
  public BoardPositionException() {
    super("Position is out of bounds");
  }

  public BoardPositionException(Throwable cause) {
    super("Position is out of bounds", cause);
  }
}
