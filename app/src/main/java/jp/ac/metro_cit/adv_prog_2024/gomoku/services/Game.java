package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Color;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;

public class Game {
  private GamePhase phase;
  private Player player1;
  private Player player2;
  private Player currentPlayer;
  private Board board;
  private GameState gameState;

  public Game(Player blackPlayer, Player whitePlayer, Board board) {
    phase = GamePhase.SETUP;
    gameState = new GameState("Game is not started yet");
    this.player1 = new Player(blackPlayer.getColor(), blackPlayer.getName());
    this.player2 = new Player(whitePlayer.getColor(), whitePlayer.getName());
    this.board = new Board(board.getSize());
  }

  public void startGame() {
    player1 = new Player(Color.BLACK, player1.getName());
    player2 = new Player(Color.WHITE, player2.getName());
    currentPlayer = player1;
    phase = GamePhase.BLACK_TURN;
  }

  void endGame() {
    phase = GamePhase.END;
  }

  public void nextPhase() {
    if (currentPlayer == player1) {
      currentPlayer = player2;
    } else {
      currentPlayer = player1;
    }

    if (currentPlayer.getColor() == Color.BLACK) {
      phase = GamePhase.BLACK_TURN;
    } else {
      phase = GamePhase.WHITE_TURN;
    }
  }

  public GamePhase getPhase() {
    return phase;
  }

  public Player getCurrentPlayer() {
    return new Player(currentPlayer.getColor(), currentPlayer.getName());
  }

  public void putStone(Color color, int x, int y) throws PutStoneException {
    Vector2D pos = new Vector2D(x, y);
    if (board.getStone(pos) != null) {
      System.err.println("Error: Already placed stone");
      return;
    }
    Stone stone = new Stone(color, pos);
    board.setStone(pos, stone);
    board.show();
    return;
  }

  public void checkWinner(Vector2D pos) {
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
    if (stoneNum >= 5) endGame();

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
    if (stoneNum >= 5) endGame();

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
    if (stoneNum >= 5) endGame();

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

    if (stoneNum >= 5) endGame();
    return;
  }

  public void surrender() {
    phase = GamePhase.END;
  }

  public GameState gameState() {
    return gameState;
  }

  public void setGameState(GameState state) {}
}
