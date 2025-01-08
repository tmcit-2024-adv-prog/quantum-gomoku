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
   * objectの型がStoneであるとき、colorとpositionが同等かを判定し、その結果を返す。また、objectの型がStoneでないとき、falseを返す。
   *
   * @author 6301 tomoyuki
   * @param object 比較対象のオブジェクト
   * @return true or false objectの型がStoneであり、colorとpositionが同等であるとき、tureを返し、いずれかが異なるときはfalseを返す。
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
   * colorとpositionを元にハッシュコードを計算し、結果を返す。
   *
   * @author 6301 tomoyuki
   * @return hashcode ハッシュコードの計算結果
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.color, this.position);
  }
}
