package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

public class Board {
  Stone[][] stone;

  public Board(Vector2D size) {
    stone = new Stone[size.x][size.y];
    for (int y = 0; y < stone.length; y++) {
      for (int x = 0; x < stone[y].length; x++) {
        stone[x][y] = null;
      }
    }
  }

  public Stone getStone(Vector2D pos) {
    if (pos.x < 0 || pos.x >= stone.length || pos.y < 0 || pos.y >= stone[0].length) return null;
    return stone[pos.x][pos.y];
  }

  public Vector2D getSize() {
    return new Vector2D(stone.length, stone[0].length);
  }

  public void show() {
    for (int y = 0; y < stone.length; y++) {
      for (int x = 0; x < stone[y].length; x++) {
        if (stone[x][y] == null) System.out.print("_");
        else if (stone[x][y].color == Color.BLACK) System.out.print("B");
        else if (stone[x][y].color == Color.WHITE) System.out.print("W");
      }
      System.out.println();
    }
  }

  public void setStone(Vector2D pos, Stone stone) {
    this.stone[pos.x][pos.y] = stone;
  }
}
