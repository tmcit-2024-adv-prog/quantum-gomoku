package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import java.io.IOException;

/**
 * 通信を送信する側のインターフェイス
 *
 * @author A Kokubo
 */
public interface Sender {

  /**
   * 送信側の初期化を行う
   */
  void initSender() throws IOException;

  /**
   * ソケットの切断を行う
   */
  void disconnect() throws IOException;

  /**
   * ソケットを通じてデータを送信する
   * @param gameState 送信するデータ
   */
  void send(GameState gameState) throws IOException;
}
