package jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions;

public class MatchingTimeoutException extends Exception {
  public MatchingTimeoutException() {
    super("Matching timeout");
  }
}
