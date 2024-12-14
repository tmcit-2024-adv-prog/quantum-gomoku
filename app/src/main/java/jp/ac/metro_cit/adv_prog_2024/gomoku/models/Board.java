package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

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
    if (pos.x < 0 || pos.x >= board.length || pos.y < 0 || pos.y >= board[0].length) return null;
    return board[pos.x][pos.y];
  }

  public Vector2D getSize() {
    return new Vector2D(board.length, board[0].length);
  }

  public void show() {
    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board[y].length; x++) {
        if (board[x][y] == null) System.out.print("_");
        else if (board[x][y].color == StoneColor.BLACK) System.out.print("B");
        else if (board[x][y].color == StoneColor.WHITE) System.out.print("W");
      }
      System.out.println();
    }
  }

  public boolean putStone(Vector2D pos, Stone stone) {
    if (pos.x < 0 || pos.x >= board.length || pos.y < 0 || pos.y >= board[0].length) return false;
    this.board[pos.x][pos.y] = stone;
    return true;
  }

  public boolean checkWinner(Vector2D pos) {
    if (pos.x == 5 && pos.y == 5) return true;
    return false;
  }
}
