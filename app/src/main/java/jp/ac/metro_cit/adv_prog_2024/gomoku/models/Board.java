package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.HashMap;

public class Board {
  Stone[][] board;

  public Board(Vector2D size) {
    board = new Stone[size.x][size.y];
    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board[y].length; x++) {
        board[x][y] = null;
      }
    }
  }

  public Stone getStone(Vector2D pos) {
    try {
      return board[pos.x][pos.y];
    } catch (Exception e) {
      return null;
    }
  }

  public void putStone(Vector2D pos, Stone stone) throws Exception {
    try {
      board[pos.x][pos.y] = stone;
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
