/**
 * いい感じするあれ(Game Communication Controller)
 *
 * @author yanbaru1331
 * @version 1.0.0
 */
package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePhaseException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePlayerException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;

public class GameCommunicationController {
  // メンバ変数
  /** メンバ変数一覧 */
  private Game game;

  private Player localPlayer;
  private Player remotePlayer;
  private Receiver receiver;
  private Sender sender;

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

  // パブリックメソッド

  // 名前を受け取ってこれを操作しているPlayerの実態を作成
  // retrunはPlayerクラスのインスタンス

  /**
   * createlocalPlayerメソッド 名前を受け取ってこれを操作しているPlayerの実態を作成する
   *
   * @param name String
   * @return Playerの実態
   */
  public Player createlocalPlayer(String name) {
    this.localPlayer = new Player(name);

    return this.localPlayer;
  }

  // 名前を受け取って対戦相手のPlayerの実態を作成
  // retrunはPlayerクラスのインスタンス

  /**
   * createRemotePlayerメソッド　名前を受け取ってリモートプレイヤーの実態を作成する
   *
   * @param name String
   * @return リモートプレイヤーの実態
   */
  public Player createRemotePlayer(String name) {
    this.remotePlayer = new Player(name);

    return this.remotePlayer;
  }

  // 石を受け取ってGameStateに反映
  // returnはGameStateクラスのインスタンス又は石を置けないエラー

  /**
   * putStoneメソッド　色と座標を受け取って石の設置を盤に反映
   *
   * @param color 色のenum
   * @param x x座標
   * @param y y座標
   * @return 更新されたgameState
   * @throws PutStoneException 石を設置しようとしたときに異常が発生したときに返される
   */
  public GameState putStone(Color color, int x, int y) throws PutStoneException {
    try {
      this.game.putStone(color, x, y);
      return this.game.gameState();
    } catch (PutStoneException e) {
      throw e;
    } catch (Exception e) {
      throw new PutStoneException("石を置くことができませんでした");
    }
  }

  // 投了・切断処理

  /**
   * GameStateメソッド これを実行した側が投了又は切断を行った際に実行される関数<br>
   * 相手に勝ちを通知するはず
   *
   * @return gameStateを返す
   */
  public GameState surrender(StoneColor color) throws GamePhaseException {
    this.game.surrender(color);
    return new GameState(
        this.game.getPhase(),
        this.game.getCurrentPlayer(),
        this.game.getRemotePlayer(),
        this.game.getWinnerPlayer(),
        this.game.getBoard());
    // return this.game.gameState();
  }

  // ゲーム状態の取得関数
  // returnはGameStateクラスのインスタンス

  /**
   * getGameStateメソッド　gameStateを取得したい際に使用する
   *
   * @return 現在のgameState
   */
  public GameState getGameState() {
    System.out.println("getGameState");
    // return this.game.gameState();
    return null;
  }

  // ゲーム状態のセット関数
  // 更新したい状態を入力して更新されたGameStateを返却

  /**
   * setGameStateメソッド　gameStateを更新する成功すると特に何も返されない
   *
   * @param state 更新したいstate(enum)
   */
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
    this.game = new Game(locPlayer, remPlayer, null);
    try {
      this.game = this.game.startGame(localPlayer, remotePlayer);
      return new GameState(
          this.game.getPhase(),
          this.game.getCurrentPlayer(),
          this.game.getRemotePlayer(),
          this.game.getWinnerPlayer(),
          this.game.getBoard());
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

  public void setGameStateCallback(GameState state) {
    this.setGameState(state);
    throw new UnsupportedOperationException("Unimplemented method 'setGameStateCallback'");
  }
}
