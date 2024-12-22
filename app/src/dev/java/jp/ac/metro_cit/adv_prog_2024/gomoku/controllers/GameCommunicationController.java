package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.util.HashMap;
import javax.annotation.Nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.GameStateCallback;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;

public class GameCommunicationController {
  private final Player localPlayer;
  private final Player remotePlayer;
  @Nullable private GameStateCallback gameStatusCallback;
  private HashMap<Vector2D, Stone> board = new HashMap<>();
  private int turnCount = 0;
  private GamePhase phase = GamePhase.BEFORE_START;
  @Nullable private Player winner = null;

  /**
   * コンストラクタ
   *
   * @param localPlayer ローカルプレイヤー
   * @param remotePlayer リモートプレイヤー
   * @param sender Senderを実装したクラス
   * @param receiver Receiverを実装したクラス
   */
  @SuppressFBWarnings(value = {"EL_EXPOSE_REP", "EI_EXPOSE_REP2"})
  public GameCommunicationController(
      Player localPlayer, Player remotePlayer, Sender sender, Receiver receiver) {
    this.localPlayer = localPlayer;
    this.remotePlayer = remotePlayer;
  }

  /**
   * 試合開始メソッド　試合を開始する際に実行される関数
   *
   * @return 試合開始時のGameState
   * @throws IllegalStateException gameStatusCallbackが設定されていない場合に返される
   */
  public GameState startGame() throws IllegalStateException {
    if (this.gameStatusCallback == null) {
      throw new IllegalStateException("gameStatusCallback is not set");
    }

    this.phase = GamePhase.BLACK_TURN;

    return new GameState(this.phase, this.localPlayer, this.remotePlayer, this.winner, this.board);
  }

  /**
   * putStoneメソッド　色と座標を受け取って石の設置を盤に反映
   *
   * @param color 色のenum
   * @param x x座標
   * @param y y座標
   * @return 更新されたgameState
   * @throws PutStoneException 石を設置しようとしたときに異常が発生したときに返される
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public GameState putStone(StoneColor color, int x, int y) throws PutStoneException {
    if (color == StoneColor.BLACK && this.phase != GamePhase.BLACK_TURN) {
      throw new PutStoneException();
    } else if (color == StoneColor.WHITE && this.phase != GamePhase.WHITE_TURN) {
      throw new PutStoneException();
    }

    Vector2D pos;
    if (color == StoneColor.BLACK) {
      pos = new Vector2D(this.turnCount / 2, this.turnCount / 2);
    } else {
      pos = new Vector2D(18 - this.turnCount / 2, 18 - this.turnCount / 2);
    }
    Stone stone = new Stone(color, pos);
    this.board.put(pos, stone);
    this.turnCount += 1;
    this.phase = color == StoneColor.BLACK ? GamePhase.WHITE_TURN : GamePhase.BLACK_TURN;

    if (this.turnCount > 8) {
      if (this.localPlayer.getColor() == StoneColor.BLACK) {
        this.winner = this.localPlayer;
      } else {
        this.winner = this.remotePlayer;
      }
      this.phase = GamePhase.FINISHED;
    }

    return new GameState(this.phase, this.localPlayer, this.remotePlayer, this.winner, this.board);
  }

  /**
   * GameStateメソッド これを実行した側が投了又は切断を行った際に実行される関数<br>
   * 相手に勝ちを通知するはず
   *
   * @return gameStateを返す
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public GameState surrender() {
    this.phase = GamePhase.FINISHED;
    this.winner = this.remotePlayer;

    return new GameState(
        GamePhase.FINISHED, this.localPlayer, this.remotePlayer, this.remotePlayer, this.board);
  }

  /**
   * getGameStateメソッド　gameStateを取得したい際に使用する
   *
   * @return 現在のgameState
   */
  public GameState getGameState() {
    return new GameState(this.phase, this.localPlayer, this.remotePlayer, this.winner, this.board);
  }

  /**
   * setGameStateメソッド　gameStateを更新する成功すると特に何も返されない
   *
   * @param state GameState
   */
  public void setGameState(GameState state) {
    this.phase = state.phase();
    this.winner = state.winner();
    this.board = state.board();

    // gameStatusCallbackが設定されていない場合にsetGameStateを実行することはシステムロジック上はありえない
    if (this.gameStatusCallback != null) {
      this.gameStatusCallback.onGameStateChanged(
          new GameState(this.phase, this.localPlayer, this.remotePlayer, this.winner, this.board));
    }
  }

  /**
   * setGameStateCallbackメソッド　GameStateCallbackを設定する
   *
   * @param callback GameStateCallbackを実装したクラス
   */
  public void setGameStateCallback(GameStateCallback callback) {
    this.gameStatusCallback = callback;
  }
}
