package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePhaseException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePlayerException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;

/** 試合を管理するクラス. */
public class Game {
  private GamePhase phase;
  private Player blackPlayer;
  private Player whitePlayer;
  private Player currentPlayer;
  private Player winnerPlayer = null;
  private Board board;

  /**
   * 試合のインスタンスを生成.
   *
   * @param blackPlayer 黒の碁石を置くプレイヤー
   * @param whitePlayer 白の碁石を置くプレイヤー
   * @param board 盤面
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public Game(Player blackPlayer, Player whitePlayer, Board board) {
    this.phase = GamePhase.BEFORE_START;
    this.blackPlayer = blackPlayer;
    this.whitePlayer = whitePlayer;
    this.blackPlayer.setColor(StoneColor.BLACK);
    this.whitePlayer.setColor(StoneColor.WHITE);
    this.currentPlayer = this.blackPlayer;
    this.board = board;
  }

  /** ゲームを開始 */
  public void startGame() throws GamePhaseException {
    if (phase != GamePhase.BEFORE_START) {
      throw new GamePhaseException("The game has already started.");
    }
    phase = GamePhase.BLACK_TURN;
  }

  /**
   * 試合を投了し、ゲームを終了
   *
   * @param color 投了するプレイヤーの色
   */
  public void surrender(StoneColor color) throws GamePhaseException {
    if (phase == GamePhase.FINISHED || phase == GamePhase.BEFORE_START) {
      throw new GamePhaseException("The game has not started or finished.");
    }
    finishGame(color == StoneColor.BLACK ? StoneColor.WHITE : StoneColor.BLACK);
  }

  /**
   * ゲームを終了する
   *
   * @param color 勝利したプレイヤーの色
   */
  void finishGame(StoneColor color) {
    if (color == StoneColor.BLACK) {
      winnerPlayer = blackPlayer;
    } else {
      winnerPlayer = whitePlayer;
    }
    phase = GamePhase.FINISHED;
  }

  /** 相手のターンに移行 */
  void nextPhase() {
    if (currentPlayer == blackPlayer) {
      currentPlayer = whitePlayer;
      phase = GamePhase.WHITE_TURN;
    } else {
      currentPlayer = blackPlayer;
      phase = GamePhase.BLACK_TURN;
    }
  }

  /**
   * 現在の試合フェーズを取得.
   *
   * @return 現在の試合フェーズ
   */
  public GamePhase getPhase() {
    return phase;
  }

  /**
   * 勝者のプレイヤーを取得.
   *
   * @return 勝者のプレイヤー
   */
  public Player getWinnerPlayer() throws GamePhaseException {
    if (phase != GamePhase.FINISHED) {
      throw new GamePhaseException("The game has not finished.");
    }
    return new Player(winnerPlayer.getColor(), winnerPlayer.getName());
  }

  /**
   * 現在のプレイヤーを取得.
   *
   * @return 現在のプレイヤー
   */
  public Player getCurrentPlayer() {
    return new Player(currentPlayer.getColor(), currentPlayer.getName());
  }

  /**
   * 碁盤に石を設置する.
   *
   * @param color 石の色
   * @param pos 石を設置する位置
   */
  public void putStone(StoneColor color, Vector2D pos)
      throws GamePhaseException, GamePlayerException, Exception {
    if (color != currentPlayer.getColor()) {
      throw new GamePlayerException("It is not your turn.");
    }
    if (phase == GamePhase.BEFORE_START || phase == GamePhase.FINISHED) {
      throw new GamePhaseException("The game has not started or finished.");
    }

    try {
      board.putStone(pos, new Stone(color, pos));
    } catch (Exception e) {
      throw new Exception("The Stone cannot puttted.");
    }

    if (board.checkWinner(pos)) {
      finishGame(currentPlayer.getColor());
    } else {
      nextPhase();
    }
  }

  public GameState into(StoneColor localPlayerColor) {
    if (localPlayerColor == StoneColor.BLACK) {
      return new GameState(
          this.phase, this.blackPlayer, this.whitePlayer, this.winnerPlayer, this.board.getBoard());
    } else {
      return new GameState(
          this.phase, this.whitePlayer, this.blackPlayer, this.winnerPlayer, this.board.getBoard());
    }
  }
}
