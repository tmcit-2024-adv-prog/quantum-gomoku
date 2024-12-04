package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import java.io.IOException;
import java.io.Serializable;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;

/**
 * 通信を送信する側のインターフェイス
 *
 * @author A Kokubo
 * @param <T>
 */
public interface Sender<T extends Serializable> {

  /** 送信側の初期化を行う */
  void initSender() throws IOException;

  /** ソケットの切断を行う */
  void disconnect() throws IOException;

  /**
   * ソケットを通じてデータを送信する
   *
   * @param gameState 送信するデータ
   */
  void send(GameState gameState) throws IOException;

  void send(GameMessage<T> message) throws IOException;
}
