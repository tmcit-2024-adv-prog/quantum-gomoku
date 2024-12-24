package jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions;

public class PutStoneException extends Exception {
  public PutStoneException() {
    super("Failed to put stone.");
  }

  public PutStoneException(Throwable cause) {
    super("Failed to put stone.", cause);
  }
}
