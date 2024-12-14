package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/** 整数の2次元座標を表現するクラス。不変であり、変更は不可 */
public class Vector2D {
  /** x座標 */
  public final int x;

  /** y座標 */
  public final int y;

  /**
   * 2次元ベクトルを生成
   *
   * @param x x座標
   * @param y y座標
   */
  public Vector2D(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
