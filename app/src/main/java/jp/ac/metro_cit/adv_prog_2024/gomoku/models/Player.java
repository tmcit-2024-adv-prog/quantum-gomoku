package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/**
 * プレイヤークラス date: 2024/12/03
 *
 * @author 6306 hoshi
 * @version 1.0
 */
public class Player {
  private StoneColor color;
  private String name;

  /**
   * 引数(型)
   *
   * @param color プレイヤーが保有する石の色(enum)
   * @param name プレイヤー名(String)
   */
  public Player(StoneColor color, String name) {
    this.color = color;
    this.name = name;
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
   * プレイヤー命を返す。
   *
   * @return 対象のプレイヤー命
   */
  public String getName() {
    return name;
  }
}
