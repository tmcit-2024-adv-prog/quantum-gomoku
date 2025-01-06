/**
 * いい感じするあれ(Game Communication Controller)
 *
 * @author yanbaru1331
 * @version 1.0.0
 */
package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePhaseException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.GameStateCallback;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.IGameCommunicationController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.GameFactory;

public class GameCommunicationController implements IGameCommunicationController {
  public final Player localPlayer;
  public final Player remotePlayer;
  private final Game game;
  private final Receiver receiver;
  private final Sender sender;
  private GameStateCallback gameStatusCallback;

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
  @SuppressFBWarnings(value = {"CT_CONSTRUCTOR_THROW", "EI_EXPOSE_REP2"})
  public GameCommunicationController(
      Player localPlayer, Player remotePlayer, Sender sender, Receiver receiver)
      throws IllegalArgumentException {
    if (localPlayer.getColor() == null || remotePlayer.getColor() == null) {
      throw new IllegalArgumentException("LocalPlayeryとRemotePlayerのいずれか、あるいは双方の色が決定されていません");
    }
    if (localPlayer.getColor() == remotePlayer.getColor()) {
      throw new IllegalArgumentException("LocalPlayerとRemotePlayerの色が同じです");
    }

    this.localPlayer = localPlayer;
    this.remotePlayer = remotePlayer;
    this.receiver = receiver;
    this.sender = sender;
    Player blackPlayer;
    Player whitePlayer;
    if (localPlayer.getColor() == StoneColor.BLACK) {
      blackPlayer = localPlayer;
      whitePlayer = remotePlayer;
    } else {
      blackPlayer = remotePlayer;
      whitePlayer = localPlayer;
    }
    this.game = GameFactory.create(blackPlayer, whitePlayer);
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
  @SuppressFBWarnings(value = {"CT_CONSTRUCTOR_THROW", "EI_EXPOSE_REP2"})
  public GameCommunicationController(
      Player localPlayer, Player remotePlayer, Sender sender, Receiver receiver, Game game)
      throws IllegalArgumentException {
    if (localPlayer.getColor() == null || remotePlayer.getColor() == null) {
      throw new IllegalArgumentException("LocalPlayeryとRemotePlayerのいずれか、あるいは双方の色が決定されていません");
    }
    if (localPlayer.getColor() == remotePlayer.getColor()) {
      throw new IllegalArgumentException("LocalPlayerとRemotePlayerの色が同じです");
    }
    if (!localPlayer.equals(game.getBlackPlayer()) && !localPlayer.equals(game.getWhitePlayer())
        || !remotePlayer.equals(game.getBlackPlayer())
            && !remotePlayer.equals(game.getWhitePlayer())) {
      throw new IllegalArgumentException("LocalPlayerとRemotePlayerがGameのPlayerと一致しません");
    }

    this.localPlayer = localPlayer;
    this.remotePlayer = remotePlayer;
    this.receiver = receiver;
    this.sender = sender;
    this.game = game;
  }

  /**
   * startGameメソッド　ゲームを開始する
   *
   * @param callback GameStateCallbackを実装したクラス
   */
  @Override
  public GameState startGame(GameStateCallback callback) throws GamePhaseException {
    this.gameStatusCallback = callback;
    this.game.startGame();

    // 相手からの通信を待ち受けて、受信したらgameStateを更新
    // TODO: 適切にスレッドを解放する
    new Thread(
            () -> {
              while (true) {
                GameState state;
                try {
                  state = this.receiver.receiveState();
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
                if (state == null) {
                  continue;
                }
                this.setGameState(state);
              }
            })
        .start();

    return this.game.into(this.localPlayer.getColor());
  }

  /**
   * putStoneメソッド　色と座標を受け取って石の設置を盤に反映
   *
   * @param color 色のenum
   * @param pos 石を設置する座標
   * @return 更新されたgameState
   * @throws PutStoneException 石を設置しようとしたときに異常が発生したときに返される
   * @throws IOException 通信エラーが発生したときに返される
   */
  @Override
  public GameState putStone(StoneColor color, Vector2D pos) throws PutStoneException, IOException {
    try {
      this.game.putStone(color, pos);
    } catch (Exception e) {
      throw new PutStoneException(e);
    }
    // 相手に通知
    GameState state = this.game.into(this.localPlayer.getColor());
    this.sender.send(state);

    return state;
  }

  /**
   * GameStateメソッド これを実行した側が投了又は切断を行った際に実行される関数<br>
   * 相手に勝ちを通知するはず
   *
   * @return gameStateを返す
   * @throws GamePhaseException
   * @throws IOException 通信エラーが発生したときに返される
   */
  @Override
  public GameState surrender() throws GamePhaseException, IOException {
    StoneColor color = this.localPlayer.getColor();
    this.game.surrender(color);
    // 相手に通知
    GameState state = this.game.into(color);
    this.sender.send(state);

    return state;
  }

  /**
   * getGameStateメソッド　gameStateを取得したい際に使用する
   *
   * @return 現在のgameState
   */
  @Override
  public GameState getGameState() {
    return this.game.into(this.localPlayer.getColor());
  }

  /**
   * setGameStateメソッド　gameStateを更新する成功すると特に何も返されない
   *
   * @param state 更新したいstate
   */
  private void setGameState(GameState state) throws IllegalArgumentException {
    this.game.from(state);
    this.gameStatusCallback.onGameStateChanged(state);
  }
}
