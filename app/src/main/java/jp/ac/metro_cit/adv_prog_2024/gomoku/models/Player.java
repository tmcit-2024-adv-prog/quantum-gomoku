package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;
import javax.annotation.Nullable;

/**
 * プレイヤークラス date: 2024/12/03
 *
 * @author 6306 hoshi
 * @version 1.0
 */
public class Player implements Serializable {
  private StoneColor color = null;
  private String name;

  /**
   * 引数(型) colorなし
   *
   * @param name プレイヤー名(String)
   */
  public Player(String name) {
    this.name = name;
  }

  /**
   * 引数(型)
   *
   * @param color 色(StoneColor)
   * @param name プレイヤー名(String)
   */
  public Player(StoneColor color, String name) {
    this.color = color;
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
   * @return 対象のプレイヤーが保有する石の色。まだ色が設定されていない場合はnullを返す。
   */
  @Nullable
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
