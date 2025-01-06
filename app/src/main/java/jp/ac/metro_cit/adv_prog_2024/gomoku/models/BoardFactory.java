package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/** 盤のファクトリクラス */
public class BoardFactory {
  /**
   * 19x19の盤を生成する
   *
   * @return 生成された盤
   */
  public static Board create() {
    return new Board(new Vector2D(19, 19));
  }
}
