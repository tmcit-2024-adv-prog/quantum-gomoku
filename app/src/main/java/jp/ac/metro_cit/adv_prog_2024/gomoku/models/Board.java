package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.HashMap;

import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.BoardParamNullException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.BoardPositionException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.BoardPutStoneException;

public class Board {
  /** 盤 */
  private HashMap<Vector2D, Stone> board;

  /** 盤のサイズ */
  private Vector2D size;

  /**
   * 盤の初期化
   *
   * @param size 盤のサイズ
   */
  public Board(Vector2D size) {
    this.board = new HashMap<>();
    this.size = size;
  }

  /** (private)指定した座標が範囲(size)内にあるか確認 */
  private boolean checkOnBoard(Vector2D pos) {
    if (pos.x < 0 || pos.y < 0) {
      return false;
    }
    if (pos.x >= this.size.x || pos.y >= this.size.y) {
      return false;
    }
    return true;
  }

  /**
   * 指定した座標の石を入手
   *
   * @param pos 指定した座標
   * @return 石の情報(Stone型) @Nullable
   * @throws BoardParamNullException 座標が null の場合
   */
  @Nullable
  public Stone getStone(Vector2D pos) throws BoardParamNullException {
    if (pos == null) {
      throw new BoardParamNullException("Position cannot be null");
    }
    return board.get(pos); // null ならそのまま null を返す
  }

  /**
   * 指定した座標に石を置く
   *
   * @param pos 指定した座標
   * @param stone 石
   * @throws BoardParamNullException 指定した座標や石が null の場合
   * @throws BoardPositionException 指定した座標が範囲(size)外の場合
   * @throws BoardPutStoneException すでに石が置かれている座標に石を置こうとした場合
   */
  public void putStone(Vector2D pos, Stone stone)
      throws BoardParamNullException, BoardPositionException, BoardPutStoneException {
    if (pos == null || stone == null) {
      throw new BoardParamNullException("Position and stone cannot be null");
    }
    if (!this.checkOnBoard(pos)) {
      throw new BoardPositionException();
    }
    if (board.containsKey(pos)) {
      throw new BoardPutStoneException();
    }
    board.put(pos, stone);
  }

  /**
   * 指定した座標を含め同じ石が5個並んでいるか確認する
   *
   * @param pos 指定した座標
   * @return 5個並んでいればTrue、そうでなければFalse
   * @throws BoardPositionException 指定した座標が範囲(size)外の場合
   */
  public boolean checkWinner(Vector2D pos) throws BoardPositionException {
    if (!this.checkOnBoard(pos)) {
      throw new BoardPositionException();
    }
    if (!board.containsKey(pos)) {
      return false; // 石が置かれていない場合は勝利条件を満たさない
    }
    Stone currentStone = board.get(pos);
    StoneColor currentColor = currentStone.getColor();
    // 横
    if (countStones(pos, 1, 0, currentColor) + countStones(pos, -1, 0, currentColor) >= 4) {
      return true;
    }
    // 縦
    if (countStones(pos, 0, 1, currentColor) + countStones(pos, 0, -1, currentColor) >= 4) {
      return true;
    }
    // 斜め（右下/左上）
    if (countStones(pos, 1, 1, currentColor) + countStones(pos, -1, -1, currentColor) >= 4) {
      return true;
    }
    // 斜め（右上/左下）
    if (countStones(pos, 1, -1, currentColor) + countStones(pos, -1, 1, currentColor) >= 4) {
      return true;
    }
    return false;
  }

  /** (private)指定された方向に連続する石の数をカウント */
  private int countStones(Vector2D pos, int dx, int dy, StoneColor currentColor) {
    int count = 0;
    int x = pos.x;
    int y = pos.y;
    while (true) {
      x += dx;
      y += dy;
      Vector2D nearpos = new Vector2D(x, y);
      Stone posStone = board.get(nearpos);
      // 隣の石(posStone)が置かれてない(null)か、隣の石の色が異なるならカウント終了
      if (posStone == null || posStone.getColor() != currentColor) {
        break;
      }
      count++;
    }
    return count;
  }

  /**
   * 現在の盤(board)の情報を取得する
   *
   * @return 現在の盤
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
  public HashMap<Vector2D, Stone> getBoard() {
    return this.board;
  }
}
