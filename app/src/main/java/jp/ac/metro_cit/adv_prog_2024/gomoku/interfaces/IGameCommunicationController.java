package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import java.io.IOException;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePhaseException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;

public interface IGameCommunicationController {
  /**
   * 試合開始メソッド　試合を開始する際に実行される関数
   *
   * @param gameStateCallback GameStateCallbackを実装したクラス
   * @return 試合開始時のGameState
   */
  GameState startGame(GameStateCallback gameStateCallback) throws GamePhaseException;

  /**
   * putStoneメソッド　色と座標を受け取って石の設置を盤に反映
   *
   * @param color 色のenum
   * @param pos 石を設置する座標
   * @return 更新されたgameState
   * @throws PutStoneException 石を設置しようとしたときに異常が発生したときに返される
   */
  GameState putStone(StoneColor color, Vector2D v) throws PutStoneException, IOException;

  /**
   * GameStateメソッド これを実行した側が投了又は切断を行った際に実行される関数<br>
   * 相手に勝ちを通知するはず
   *
   * @return gameStateを返す
   */
  GameState surrender() throws GamePhaseException, IOException;

  /**
   * getGameStateメソッド　gameStateを取得したい際に使用する
   *
   * @return 現在のgameState
   */
  GameState getGameState();
}
