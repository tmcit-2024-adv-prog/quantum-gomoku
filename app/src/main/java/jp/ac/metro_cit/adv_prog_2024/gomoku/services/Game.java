package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
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
    blackPlayer = new Player(StoneColor.BLACK, blackPlayer.getName());
    whitePlayer = new Player(StoneColor.WHITE, whitePlayer.getName());
    currentPlayer = blackPlayer;
    phase = GamePhase.BLACK_TURN;
  }

  /** Ends the game and transitions the phase to FINISHED. */
  public void endGame(StoneColor color) {
    phase = GamePhase.FINISHED;
  }

  /** the game phase to the next player's turn. Toggles between black and white turns. */
  void nextPhase() {
    if (phase == GamePhase.FINISHED) {
      return;
    }
    if (currentPlayer == blackPlayer) {
      currentPlayer = whitePlayer;
    } else {
      currentPlayer = blackPlayer;
    }

    phase =
        (currentPlayer.getColor() == StoneColor.BLACK)
            ? GamePhase.BLACK_TURN
            : GamePhase.WHITE_TURN;
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
  public boolean putStone(StoneColor color, Vector2D pos) {
    if (board.getStone(pos) != null) {
      return false;
    }
    Stone stone = new Stone(color, pos);
    board.setStone(pos, stone);
    if (!checkWinner(pos)) {
      nextPhase();
    }
    return true;
  }

  /**
   * Checks if the last placed stone results in a victory condition. A player wins if five stones of
   * the same color are aligned in a row, column, or diagonal.
   *
   * @param pos the position of the last placed stone
   */
  boolean checkWinner(Vector2D pos) {
    if (board.checkWinner(pos)) {
      endGame(currentPlayer.getColor());
      return true;
    }
    return false;
  }
}
