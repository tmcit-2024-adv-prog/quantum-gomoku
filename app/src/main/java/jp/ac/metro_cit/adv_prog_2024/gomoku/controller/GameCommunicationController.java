/**
 * いい感じするあれ(Game Communication Controller)
 *
 * @author yanbaru1331
 * @version 1.0.0
 */
package jp.ac.metro_cit.adv_prog_2024.gomoku.controller;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.GameStateCallback;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Color;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;

public class GameCommunicationController {
  // メンバ変数
  /** メンバ変数一覧 */
  private Game game;

  private Player controlPlayer;
  private Player remotePlayer;
  private Receiver receiver;
  private Sender sender;
  public GameStateCallback gameStatusCallback;

  // パブリックメソッド

  // 名前を受け取ってこれを操作しているPlayerの実態を作成
  // retrunはPlayerクラスのインスタンス

  /**
   * createControlPlayerメソッド 名前を受け取ってこれを操作しているPlayerの実態を作成する
   *
   * @param name String
   * @return Playerの実態
   */
  public Player createControlPlayer(String name) {
    this.controlPlayer = new Player(name);

    return this.controlPlayer;
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
  public GameState surrender() {
    this.game.surrender();
    return this.game.gameState();
  }

  // ゲーム状態の取得関数
  // returnはGameStateクラスのインスタンス

  /**
   * getGameStatusメソッド　gameStateを取得したい際に使用する
   *
   * @return 現在のgameState
   */
  public GameState getGameStatus() {
    return this.game.gameState();
  }

  // ゲーム状態のセット関数
  // 更新したい状態を入力して更新されたGameStateを返却

  /**
   * setGameStateメソッド　gameStateを更新する成功すると特に何も返されない
   *
   * @param state 更新したいstate(enum)
   */
  public void setGameState(GameState state) {
    this.game.setGameState(state);
  }

  // 通信をするときのレシーバーの起動処理
  /** startReciveメソッド　リモートと通信をするときに立ち上げるメソッド */
  private void startRecive() {
    this.receiver.startRecive();
  }

  // private Color decideColor() {

  // }
}
