package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Color;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;

/**
 * Represents the core logic of a Gomoku game. This class manages the game phase, players, board
 * state, and game flow.
 */
public class Game {
  private GamePhase phase;
  private Player blackPlayer;
  private Player whitePlayer;
  private Player currentPlayer;
  private Board board;

  /**
   * Creates a new Game instance with specified players and board.
   *
   * @param blackPlayer the player assigned to black stones
   * @param whitePlayer the player assigned to white stones
   * @param board the board to be used in the game
   */
  public Game(Player blackPlayer, Player whitePlayer, Board board) {
    this.phase = GamePhase.BEFORE_START;
    this.blackPlayer = new Player(blackPlayer.getColor(), blackPlayer.getName());
    this.whitePlayer = new Player(whitePlayer.getColor(), whitePlayer.getName());
    this.board = new Board(board.getSize());
  }

  /** Starts the game and setting the first turn. */
  public void startGame() {
    blackPlayer = new Player(Color.BLACK, blackPlayer.getName());
    whitePlayer = new Player(Color.WHITE, whitePlayer.getName());
    currentPlayer = blackPlayer;
    phase = GamePhase.BLACK_TURN;
  }

  /** Ends the game and transitions the phase to FINISHED. */
  void endGame() {
    phase = GamePhase.FINISHED;
  }

  /** the game phase to the next player's turn. Toggles between black and white turns. */
  public void nextPhase() {
    if (currentPlayer == blackPlayer) {
      currentPlayer = whitePlayer;
    } else {
      currentPlayer = blackPlayer;
    }

    phase = (currentPlayer.getColor() == Color.BLACK) ? GamePhase.BLACK_TURN : GamePhase.WHITE_TURN;
  }

  /**
   * Retrieves the current game phase.
   *
   * @return the current phase of the game
   */
  public GamePhase getPhase() {
    return phase;
  }

  /**
   * return current player of game.
   *
   * @return a copy of the current player
   */
  public Player getCurrentPlayer() {
    return new Player(currentPlayer.getColor(), currentPlayer.getName());
  }

  /**
   * Places a stone of the specified color at the given position on the board.
   *
   * @param color the color of the stone to place
   * @param pos the position to place the stone
   * @return true if the stone was placed successfully, false if the position is already occupied
   */
  public boolean putStone(Color color, Vector2D pos) {
    if (board.getStone(pos) != null) {
      return false;
    }
    Stone stone = new Stone(color, pos);
    board.setStone(pos, stone);
    return true;
  }

  /**
   * Checks if the last placed stone results in a victory condition. A player wins if five stones of
   * the same color are aligned in a row, column, or diagonal.
   *
   * @param pos the position of the last placed stone
   */
  public void checkWinner(Vector2D pos) {
    Color stoneColor = board.getStone(pos).getColor();
    Stone stone;

    // 横方向のチェック
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

    // 縦方向のチェック
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

    // 左斜めのチェック
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

    // 右斜めのチェック
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
  }
}
