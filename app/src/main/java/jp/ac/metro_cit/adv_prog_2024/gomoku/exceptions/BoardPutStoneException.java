package jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions;

public class BoardPutStoneException extends Exception {
  public BoardPutStoneException() {
    super("Position is already occupied by stone");
  }

  public BoardPutStoneException(Throwable cause) {
    super("Position is already occupied by stone", cause);
  }
}
