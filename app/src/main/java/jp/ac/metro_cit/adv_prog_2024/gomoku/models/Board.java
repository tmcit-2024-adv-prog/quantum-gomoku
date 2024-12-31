package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.HashMap;

public class Board {
  HashMap<Vector2D, Stone> board;

  public Board(Vector2D size) {
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
    return new HashMap<Vector2D, Stone>();
  }
}
