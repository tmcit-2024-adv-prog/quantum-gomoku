package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Game {
  public GamePhase phase;
  public Player player1;
  public Player player2;
  public Player currentPlayer;
  public Board board;
  Random r;

  public Game() {
    phase = GamePhase.SETUP;
    player1 = new Player();
    player2 = new Player();
    board = new Board();
    r = new Random();
  }

  public void StartGame() {
    boolean turn = r.nextBoolean();
    if (turn) {
      player1.stoneColor = Color.BLACK;
      player2.stoneColor = Color.WHITE;
      currentPlayer = player1;
    } else {
      player1.stoneColor = Color.WHITE;
      player2.stoneColor = Color.BLACK;
      currentPlayer = player2;
    }
    phase = GamePhase.BLACK_TURN;
  }

  public void NextPhase() {
    Color color = currentPlayer.stoneColor;
    if (color == Color.BLACK) phase = GamePhase.WHITE_TURN;
    else phase = GamePhase.BLACK_TURN;

    if (currentPlayer == player1) currentPlayer = player2;
    else currentPlayer = player1;
  }

  boolean WithinBoard(Point pos) {
    if (pos.x < 0 || pos.x > board.stone.length || pos.y < 0 || pos.y > board.stone[pos.x].length)
      return false;
    return true;
  }

  boolean CheckStone(Point pos) {
    if (!WithinBoard(pos)) return false;
    if (board.stone[pos.x][pos.y] == null) return false;
    return true;
  }

  boolean IsStone(Point pos) {
    if (board.stone[pos.x][pos.y] != null) return true;
    return false;
  }

  public boolean CheckWinner(Point pos) {
    Color stoneColor = board.stone[pos.x][pos.y].color;
    Point checkPos = new Point(0, 0);

    // 横方向の確認
    int stoneNum = 1;
    for (int x = pos.x - 1; x >= pos.x - 4; x--) {
      checkPos = new Point(x, pos.y);
      if (!CheckStone(checkPos) || board.stone[x][checkPos.y].color != stoneColor) break;
      stoneNum++;
    }
    for (int x = pos.x + 1; x <= pos.x + 4; x++) {
      checkPos = new Point(x, pos.y);
      if (!CheckStone(checkPos) || board.stone[x][pos.y].color != stoneColor) break;
      stoneNum++;
    }
    if (stoneNum >= 5) return true;

    // 縦方向の確認
    stoneNum = 1;
    for (int y = pos.y - 1; y >= pos.y - 4; y--) {
      checkPos = new Point(pos.x, y);
      if (!CheckStone(checkPos) || board.stone[pos.x][y].color != stoneColor) break;
      stoneNum++;
    }
    for (int y = pos.y + 1; y <= pos.y + 4; y++) {
      checkPos = new Point(pos.x, y);
      if (!CheckStone(checkPos) || board.stone[pos.x][y].color != stoneColor) break;
      stoneNum++;
    }
    if (stoneNum >= 5) return true;

    // 左斜め方向の確認
    stoneNum = 1;
    for (int x = pos.x - 1, y = pos.y - 1; x >= pos.x - 4 && y >= pos.y - 4; x--, y--) {
      checkPos = new Point(x, y);
      if (!CheckStone(checkPos) || board.stone[x][y].color != stoneColor) break;
      stoneNum++;
    }
    for (int x = pos.x + 1, y = pos.y + 1; x <= pos.x + 4 && y <= pos.y + 4; x++, y++) {
      checkPos = new Point(x, y);
      if (!CheckStone(checkPos) || board.stone[x][y].color != stoneColor) break;
      stoneNum++;
    }
    if (stoneNum >= 5) return true;

    // 右斜め方向の確認
    stoneNum = 1;
    for (int x = pos.x - 1, y = pos.y + 1; x >= pos.x - 4 && y <= pos.y + 4; x--, y++) {
      checkPos = new Point(x, y);
      if (!CheckStone(checkPos) || board.stone[x][y].color != stoneColor) break;
      stoneNum++;
    }
    for (int x = pos.x + 1, y = pos.y - 1; x <= pos.x + 4 && y >= pos.y - 4; x++, y--) {
      checkPos = new Point(x, y);
      if (!CheckStone(checkPos) || board.stone[x][y].color != stoneColor) break;
      stoneNum++;
    }
    if (stoneNum >= 5) return true;
    System.out.println("NUM:" + stoneNum);
    return false;
  }

  public Stone CreateStone(Color color) {
    return new Stone(color);
  }

  public boolean SetStone(Point pos, Stone stone) {
    if (!WithinBoard(pos) || IsStone(pos)) {
      System.err.println("Error: Already placed stone");
      return false;
    }
    board.SetStone(pos, stone);
    board.Show();
    return true;
  }
}

enum GamePhase {
  SETUP,
  BLACK_TURN,
  WHITE_TURN,
  END,
}

class Stone {
  public Color color;
  public Point pos;

  public Stone(Color color) {
    this.color = color;
  }
}

class Player {
  public Color stoneColor;
  public String name;
}

class Board {
  public Stone[][] stone;

  public Board() {
    stone = new Stone[9][9];
    for (int y = 0; y < stone.length; y++) {
      for (int x = 0; x < stone[y].length; x++) {
        stone[x][y] = null;
      }
    }
  }

  public void Show() {
    for (int y = 0; y < stone.length; y++) {
      for (int x = 0; x < stone[y].length; x++) {
        if (stone[x][y] == null) System.out.print("_");
        else if (stone[x][y].color == Color.BLACK) System.out.print("B");
        else if (stone[x][y].color == Color.WHITE) System.out.print("W");
      }
      System.out.println();
    }
  }

  public void SetStone(Point pos, Stone stone) {
    this.stone[pos.x][pos.y] = stone;
  }
}

