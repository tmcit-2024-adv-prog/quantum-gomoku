package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.HashMap;

public class Board {
  /** 盤 */
  private HashMap<Vector2D, Stone> board;
  /** 盤のサイズ */
  private Vector2D size;

  /**
   * 盤の初期化
   * @param size 盤のサイズ
   */
  public Board(Vector2D size) {
    this.size = size;
    this.board = new HashMap<>();
    this.size = size;

    // 初期化時に盤面を空で設定、あらかじめ座標のキーを登録
    for (int x = 0; x < this.size.x; x++) {
      for (int y = 0; y < this.size.y; y++) {
        board.put(new Vector2D(x, y), null); // 空の位置を設定
      }
    }
  }

  /**
   * 指定した座標の石を入手
   * 
   * @param pos 指定した座標
   * @return 石の情報(Stone型) 
   * @throws IllegalArgumentException 座標が null の場合
   */
  public Stone getStone(Vector2D pos) {
    if (pos == null) {
      throw new IllegalArgumentException("Position cannot be null");
    }
    return board.get(pos); // null ならそのまま null を返す
  }

  /**
   * 指定した座標に石を置く
   * 
   * @param pos 指定した座標
   * @param stone 石
   * @throws IllegalArgumentException 指定した座標や石が null、または盤面外の場合
   * @throws IllegalStateException すでに石が置かれている座標に石を置こうとした場合
   */
  public void putStone(Vector2D pos, Stone stone) throws Exception {
    if (pos == null || stone == null) {
      throw new IllegalArgumentException("Position and stone cannot be null");
    }
    if (!board.containsKey(pos)) {
      throw new IllegalArgumentException("Position is out of bounds");
    }
    if (board.get(pos) != null) {
      throw new IllegalStateException("Position is already occupied");
    }
    board.put(pos, stone);
  }

  /**
   * 指定した座標を含め同じ石が5個並んでいるか確認する
   * 
   * @param pos 指定した座標
   * @return 5個並んでいればTrue、そうでなければFalse
   * @throws IllegalArgumentException 指定した座標が盤面外の場合
   */
  public boolean checkWinner(Vector2D pos) {
    if (!board.containsKey(pos)) {
      throw new IllegalArgumentException("Position is out of bounds");
    }  
    Stone currentStone = board.get(pos);
    if (currentStone == null) {
      return false; // 石が置かれていない場合は勝利条件を満たさない
    }
    StoneColor currentColor = currentStone.getColor();
    return (countStones(pos, 1, 0, currentColor) + countStones(pos, -1, 0, currentColor) >= 4) || // 横
    (countStones(pos, 0, 1, currentColor) + countStones(pos, 0, -1, currentColor) >= 4) || // 縦
    (countStones(pos, 1, 1, currentColor) + countStones(pos, -1, -1, currentColor) >= 4) || // 斜め（右下/左上）
    (countStones(pos, 1, -1, currentColor) + countStones(pos, -1, 1, currentColor) >= 4);  // 斜め（右上/左下）
  }

  /** 
   * (private)指定された方向に連続する石の数をカウント
   *
   */ 
  private int countStones(Vector2D pos, int dx, int dy, StoneColor currentColor) {
    int count = 0;
    int x = pos.x;
    int y = pos.y;
    while (true) {
      x += dx;
      y += dy;
      Vector2D nearpos = new Vector2D(x, y);
      Stone posStone = board.get(nearpos);
      if (posStone == null || posStone.getColor() != currentColor) {
        break;
      }
      count++;
    }
    return count;
  }

  /**
   * 現在の盤(board)の情報を取得する
   * @return 現在の盤
   */
  public HashMap<Vector2D, Stone> getBoard() {
    return this.board;
  }
}
