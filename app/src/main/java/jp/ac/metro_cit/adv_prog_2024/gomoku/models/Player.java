package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/**
 * プレイヤークラス date: 2024/12/03
 *
 * @author 6306 hoshi
 * @version 1.0
 */
public class Player {
  private StoneColor color = null;
  private String name;

  /**
   * 引数(型)
   *
   * @param name プレイヤー名(String)
   */
  public Player(String name) {
    this.name = name;
  }

  /**
   * プレイヤーの色を設定する。
   *
   * @param color プレイヤーに設定する石の色
   * @throws IllegalStateException 色が既に設定されている場合
   */
  public void setColor(StoneColor color) {
    if (this.color != null) {
      throw new IllegalStateException("既に色が与えられているため、上書きできません。");
    }
    this.color = color;
  }

  /**
   * プレイヤーが保有する石の色を返す。
   *
   * @return 対象のプレイヤーが保有する石の色
   */
  public StoneColor getColor() {
    return color;
  }

  /**
   * プレイヤー名を返す。
   *
   * @return 対象のプレイヤー名
   */
  public String getName() {
    return name;
  }
}
