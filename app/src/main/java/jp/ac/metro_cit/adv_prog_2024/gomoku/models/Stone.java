package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/**
 * 石クラス date: 2024/12/03
 *
 * @author 6306 hoshi
 * @version 1.0
 */
public class Stone {
  private StoneColor color;
  private int posX;
  private int posY;

  /**
   * 引数(型)
   *
   * @param color 石の色(enum)`
   * @param posX 石のX座標(int)
   * @param posY 石のY座標(int)
   */
  public Stone(StoneColor color, int posX, int posY) {
    this.color = color;
    this.posX = posX;
    this.posY = posY;
  }

  /**
   * 石の色を返す。
   *
   * @return 対象の石の色
   */
  public StoneColor getColor() {
    return color;
  }

  /**
   * 石のX座標を返す。
   *
   * @return 対象の石のX座標
   */
  public int getPosX() {
    return posX;
  }

  /**
   * 石のY座標を返す。
   *
   * @return 対象の石のY座標
   */
  public int getPosY() {
    return posY;
  }
}
