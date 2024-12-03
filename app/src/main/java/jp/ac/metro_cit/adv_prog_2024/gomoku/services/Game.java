package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import java.util.Random;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Color;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;

public class Game {
  private GamePhase phase;
  private Player player1;
  private Player player2;
  private Player currentPlayer;
  private Board board;
  private Random rand;

  public Game(Player player1, Player player2, Board board) {
    phase = GamePhase.SETUP;
    this.player1 = player1;
    this.player2 = player2;
    this.board = board;
    rand = new Random();
  }

  public void startGame() {
    boolean turn = rand.nextBoolean();
    if (turn) {
      player1 = new Player(Color.BLACK, player1.getName());
      player2 = new Player(Color.WHITE, player2.getName());
      currentPlayer = player1;
    } else {
      player1 = new Player(Color.WHITE, player1.getName());
      player2 = new Player(Color.BLACK, player2.getName());
      currentPlayer = player2;
    }
    phase = GamePhase.BLACK_TURN;
  }

  public void nextPhase() {
    if (currentPlayer == player1) {
      currentPlayer = player2;
    }
    else {
      currentPlayer = player1;
    }

    if (currentPlayer.getColor() == Color.BLACK) {
      phase = GamePhase.BLACK_TURN;
    }
    else {
      phase = GamePhase.WHITE_TURN;
    }
  }

  public GamePhase getPhase() {
    return phase;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  boolean containBoard(Vector2D pos) {
    if (pos.x < 0 || pos.x > board.GetSize().x || pos.y < 0 || pos.y > board.GetSize().y)
      return false;
    return true;
  }

  public void putStone(Color color, int x, int y) throws PutStoneException {
    Vector2D pos = new Vector2D(x, y);
    if (!containBoard(pos) || board.getStone(pos) != null) {
      System.err.println("Error: Already placed stone");
      return;
    }
    Stone stone = new Stone(color, pos);
    board.SetStone(pos, stone);
    board.Show();
    return;
  }

  public boolean checkWinner(Vector2D pos) {
    Color stoneColor = board.getStone(pos).getColor();
    Stone stone;

    // 横方向の確認
    int stoneNum = 1;
    for (int x = pos.x - 1; x >= pos.x - 4; x--) {
      stone = board.getStone(new Vector2D(x, pos.y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }

    for (int x = pos.x + 1; x <= pos.x + 4; x++) {
      stone = board.getStone(new Vector2D(x, pos.y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }
    if (stoneNum >= 5) return true;

    // 縦方向の確認
    stoneNum = 1;
    for (int y = pos.y - 1; y >= pos.y - 4; y--) {
      stone = board.getStone(new Vector2D(pos.x, y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }
    for (int y = pos.y + 1; y <= pos.y + 4; y++) {
      stone = board.getStone(new Vector2D(pos.x, y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }
    if (stoneNum >= 5) return true;

    // 左斜め方向の確認
    stoneNum = 1;
    for (int x = pos.x - 1, y = pos.y - 1; x >= pos.x - 4 && y >= pos.y - 4; x--, y--) {
      stone = board.getStone(new Vector2D(x, y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }
    for (int x = pos.x + 1, y = pos.y + 1; x <= pos.x + 4 && y <= pos.y + 4; x++, y++) {
      stone = board.getStone(new Vector2D(x, y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }
    if (stoneNum >= 5) return true;

    // 右斜め方向の確認
    stoneNum = 1;
    for (int x = pos.x - 1, y = pos.y + 1; x >= pos.x - 4 && y <= pos.y + 4; x--, y++) {
      stone = board.getStone(new Vector2D(x, y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }
    for (int x = pos.x + 1, y = pos.y - 1; x <= pos.x + 4 && y >= pos.y - 4; x++, y--) {
      stone = board.getStone(new Vector2D(x, y));
      if (stone == null || stone.getColor() != stoneColor) break;
      stoneNum++;
    }

    if (stoneNum >= 5) return true;
    System.out.println("NUM:" + stoneNum);
    return false;
  }

  public void surrender() {
    phase = GamePhase.END;
  }

  public GameState gameState() {
    return gameState;
  }

  private GameState gameState;

  public void setGameState(GameState state) {}
}
