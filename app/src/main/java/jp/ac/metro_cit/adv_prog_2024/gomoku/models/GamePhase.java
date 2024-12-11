package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/**
 * Represents the different phases of a Gomoku game. Each phase indicates the current state of the
 * game.
 */
public enum GamePhase {
  /** The game has not started yet. */
  BEFORE_START,

  /** It is Black's turn to play. */
  BLACK_TURN,

  /** It is White's turn to play. */
  WHITE_TURN,

  /** The game has ended. */
  FINISHED,
}
