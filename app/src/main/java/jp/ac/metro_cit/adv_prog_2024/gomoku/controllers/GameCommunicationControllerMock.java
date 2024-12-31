package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.util.HashMap;
import javax.annotation.Nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.GameStateCallback;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.IGameCommunicationController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;

public class GameCommunicationControllerMock implements IGameCommunicationController {
  private Player localPlayer;
  private Player remotePlayer;
  private GameStateCallback gameStatusCallback;
  private final HashMap<Vector2D, Stone> board = new HashMap<>();
  private int turnCount = 0;
  private GamePhase phase = GamePhase.BEFORE_START;
  @Nullable private Player winner = null;

  /**
   * GameCommunicationControllerコンストラクタ
   *
   * <p>localPlayerとremotePlayerの色が決定されている必要があり、それぞれのPlayerの色は異なる必要がある
   *
   * @param localPlayer LocalPlayer 色が決定されている必要がある
   * @param remotePlayer RemotePlayer 色が決定されている必要がある
   * @param sender Senderを実装したクラス
   * @param receiver Receiverを実装したクラス
   * @throws IllegalArgumentException
   */
  @SuppressFBWarnings(value = {"CT_CONSTRUCTOR_THROW"})
  public GameCommunicationControllerMock(
      Player localPlayer, Player remotePlayer, Sender s, Receiver r) {
    this(localPlayer, remotePlayer);
  }

  /**
   * GameCommunicationControllerコンストラクタ
   *
   * <p>localPlayerとremotePlayerの色が決定されている必要があり、それぞれのPlayerの色は異なる必要がある
   *
   * @param localPlayer LocalPlayer 色が決定されている必要がある
   * @param remotePlayer RemotePlayer 色が決定されている必要がある
   * @param sender Senderを実装したクラス
   * @param receiver Receiverを実装したクラス
   * @param game 試合のインスタンス
   * @throws IllegalArgumentException
   */
  @SuppressFBWarnings(value = {"CT_CONSTRUCTOR_THROW"})
  public GameCommunicationControllerMock(
      Player localPlayer, Player remotePlayer, Sender s, Receiver r, Game g)
      throws IllegalArgumentException {
    this(localPlayer, remotePlayer);
  }

  /**
   * GameCommunicationControllerコンストラクタ
   *
   * <p>localPlayerとremotePlayerの色が決定されている必要があり、それぞれのPlayerの色は異なる必要がある
   *
   * @param localPlayer LocalPlayer 色が決定されている必要がある
   * @param remotePlayer RemotePlayer 色が決定されている必要がある
   */
  @SuppressFBWarnings(value = {"CT_CONSTRUCTOR_THROW", "EI_EXPOSE_REP2"})
  public GameCommunicationControllerMock(Player localPlayer, Player remotePlayer) {
    if (localPlayer.getColor() == null || remotePlayer.getColor() == null) {
      throw new IllegalArgumentException("LocalPlayeryとRemotePlayerのいずれか、あるいは双方の色が決定されていません");
    }
    if (localPlayer.getColor() == remotePlayer.getColor()) {
      throw new IllegalArgumentException("LocalPlayerとRemotePlayerの色が同じです");
    }

    this.localPlayer = localPlayer;
    this.remotePlayer = remotePlayer;
  }

  /**
   * 試合開始メソッド　試合を開始する際に実行される関数
   *
   * @return 試合開始時のGameState
   */
  @Override
  public GameState startGame(GameStateCallback gameStateCallback) {
    this.gameStatusCallback = gameStateCallback;
    this.phase = GamePhase.BLACK_TURN;

    // localPlayerが白の場合、相手に行動させる
    if (this.localPlayer.getColor() == StoneColor.WHITE) {
      GameState state = this.putStoneInner(StoneColor.BLACK, new Vector2D(9, 9));
      this.gameStatusCallback.onGameStateChanged(state);
    }

    return new GameState(this.phase, this.localPlayer, this.remotePlayer, this.winner, this.board);
  }

  /**
   * putStoneメソッド　色と座標を受け取って石の設置を盤に反映
   *
   * @param color 色のenum
   * @param pos 石を設置する座標
   * @return 更新されたgameState
   * @throws PutStoneException 石を設置しようとしたときに異常が発生したときに返される
   */
  @Override
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public GameState putStone(StoneColor color, Vector2D v) throws PutStoneException {
    if (color == StoneColor.BLACK && this.phase != GamePhase.BLACK_TURN) {
      throw new PutStoneException(new IllegalArgumentException("黒の手番ではありません"));
    } else if (color == StoneColor.WHITE && this.phase != GamePhase.WHITE_TURN) {
      throw new PutStoneException(new IllegalArgumentException("白の手番ではありません"));
    }
    if (this.localPlayer.getColor() != color) {
      throw new PutStoneException(
          new IllegalArgumentException("配置しようとしている石の色とlocalPlayerの色が一致しません"));
    }

    GameState gameState = putStoneInner(color, v);
    // 相手番シミュレーション
    new Thread(
            () -> {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              GameState state = this.putStoneInner(color.oppsite(), v);
              this.gameStatusCallback.onGameStateChanged(state);
            })
        .start();

    return gameState;
  }

  private GameState putStoneInner(StoneColor color, Vector2D v) {
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
  @Override
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
  @Override
  public GameState getGameState() {
    return new GameState(this.phase, this.localPlayer, this.remotePlayer, this.winner, this.board);
  }
}
