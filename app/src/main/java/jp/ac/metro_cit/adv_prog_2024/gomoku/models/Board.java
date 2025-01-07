package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.HashMap;

public class Board {
  HashMap<Vector2D, Stone> board;
  Vector2D size;

  public Board(Vector2D size) {
    this.size = size;
    this.board = new HashMap<>();
  }

  public Stone getStone(Vector2D pos) {
    try {
      return board.get(pos);
    } catch (Exception e) {
      return null;
    }
  }

  public void putStone(Vector2D pos, Stone stone) throws Exception {
    if (pos.x < 0 || pos.x >= size.x || pos.y < 0 || pos.y >= size.y) {
      throw new Exception("out of size"); // 範囲外の座標を指定した場合
    }
    try {
      board.put(pos, stone);
    } catch (Exception e) {
      throw new Exception("hoge error"); // 設置で何かしらのエラーが発生した場合
    }
  }

  public boolean checkWinner(Vector2D pos) {
    if (pos.x == 5 && pos.y == 1) return true;
    return false;
  }

  public HashMap<Vector2D, Stone> getBoard() {
    return board;
  }
}
