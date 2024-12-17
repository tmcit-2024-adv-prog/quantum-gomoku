package jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions;

public class MatchingFailedException extends Exception {
  public MatchingFailedException() {
    super("Matching failed");
  }
}
