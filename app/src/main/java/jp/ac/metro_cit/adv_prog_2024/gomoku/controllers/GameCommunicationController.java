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
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.GameFactory;

public class GameCommunicationController implements IGameCommunicationController {
  private Game game;
  private Player localPlayer;
  private Player remotePlayer;
  private Receiver receiver;
  private Sender sender;
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

  // コンストラクタ

  /**
   * GameCommunicationControllerコンストラクタ
   *
   * @param sender Sender
   * @param receiver Receiver
   */
  public GameCommunicationController(
      Game game, Player localPlayer, Player remotePlayer, Sender sender, Receiver receiver) {

    this.localPlayer = localPlayer;
    this.remotePlayer = remotePlayer;
    this.receiver = receiver;
    this.sender = sender;
    this.game = game;
    this.gameState =
        new GameState(GamePhase.BEFORE_START, localPlayer, remotePlayer, null, game.getBoard());

    // this.setGameStateCallback();
  }

  // コンストラクタ

  /**
   * GameCommunicationControllerコンストラクタ
   *
   * @param sender Sender
   * @param receiver Receiver
   */
  public GameCommunicationController(
      Game game, Player localPlayer, Player remotePlayer, Sender sender, Receiver receiver) {

    this.localPlayer = localPlayer;
    this.remotePlayer = remotePlayer;
    this.receiver = receiver;
    this.sender = sender;
    this.game = game;
    this.gameState =
        new GameState(GamePhase.BEFORE_START, localPlayer, remotePlayer, null, game.getBoard());

    // this.setGameStateCallback();
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
    this.game.startGame(localPlayer, remotePlayer);

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
   * @return gameStateを返す <<<<<<< HEAD =======
   * @throws GamePhaseException
   * @throws IOException 通信エラーが発生したときに返される >>>>>>> a7a5b90 (perf: :zap:
   *     GameCommunicationControllerの実装修正)
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
   * @param state 更新したいstate(enum)
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public void setGameState(GameState state) {
    // stateからgameを更新
    //     GamePhase phase,
    // Player blackPlayer,
    // Player whitePlayer,
    // @Nullable Player winner,
    // HashMap<Vector2D, Stone> board)

  }

  // 通信をするときのレシーバーの起動処理
  /** startReciveメソッド　リモートと通信をするときに立ち上げるメソッド */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  private void startRecive() {
    this.receiver.startReceive();
  }

  // private Color decideColor() {
  //   return this.localPlayer.setColoer(Color.BLACK);
  // }

  /**
   * startGameメソッド　ゲームを開始する
   *
   * @param locPlayer これを操作しているプレイヤー
   * @param remPlayer　相手のプレイヤー
   */
  public GameState startGame(Player locPlayer, Player remPlayer)
      throws GamePhaseException, GamePlayerException, Exception {
    if (this.gameStatusCallback == null) {
      throw new IllegalStateException("gameStatusCallback is not set");
    }
    this.game = new Game(locPlayer, remPlayer, null);
    try {
      this.game = this.game.startGame(localPlayer, remotePlayer);
      return this.game.into(localPlayer.getColor());
    } catch (GamePhaseException e) {
      throw e;
    }
  }

  // public GameState into(StoneColor localPlayerColor) {
  //   if (localPlayerColor == StoneColor.BLACK) {
  //     return new GameState(
  //         this.phase, this.blackPlayer, this.whitePlayer, this.winnerPlayer,
  // this.board.getBoard());
  //   } else {
  //     return new GameState(
  //         this.phase, this.whitePlayer, this.blackPlayer, this.winnerPlayer,
  // this.board.getBoard());
  //   }
  // }

  public void setGameStateCallback(GameStateCallback callback) {
    this.gameStatusCallback = callback;
  }
}
