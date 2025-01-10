package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.Objects;

/**
 * Stoneクラス
 *
 * @author 6306 hoshi
 * @version 1.2
 */
public class Stone {

  private StoneColor color;
  private Vector2D position;

  /**
   * 引数(型)
   *
   * @param color 石の色(StoneColor)
   * @param position 石の位置(Vector2D)
   */
  public Stone(StoneColor color, Vector2D position) {
    this.color = color;
    this.position = position;
  }

  /**
   * 石の色を返す。
   *
   * @return color プレイヤーに設定された石の色。
   */
  public StoneColor getColor() {
    return color;
  }

  /**
   * 石の座標を返す。
   *
   * @return position 石の座標
   */
  public Vector2D getPos() {
    return position;
  }

  /**
   * @author othermember
   * @param object
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof Stone) {
      Stone stone = (Stone) object;
      return this.color == stone.color && this.position.equals(stone.position);
    }

    return false;
  }

  /**
   * @author othermember
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.color, this.position);
  }
}
