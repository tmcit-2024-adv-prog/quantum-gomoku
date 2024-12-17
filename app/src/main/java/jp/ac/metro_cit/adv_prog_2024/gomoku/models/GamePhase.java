package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/** 試合の状態を表す列挙型 */
public enum GamePhase {
  /** 試合開始前 */
  BEFORE_START,

  /** 黒の手番 */
  BLACK_TURN,

  /** 白の手番 */
  WHITE_TURN,

  /** 試合終了 */
  FINISHED,
}
