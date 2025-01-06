package jp.ac.metro_cit.adv_prog_2024.gomoku.utils;

public record Pair<L, R>(L left, R right) {
  public static <L, R> Pair<L, R> of(L left, R right) {
    return new Pair<>(left, right);
  }
}
