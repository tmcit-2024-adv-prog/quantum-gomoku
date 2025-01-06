package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import java.io.IOException;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;

/**
 * 通信を送信する側のインターフェイス
 *
 * @author A Kokubo
 */
public interface Sender {

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

  void send(GameMessage message) throws IOException;

  /**
   * データをブロードキャストで送信する
   *
   * @param message 送信するデータ
   */
  void broadcast(GameMessage message) throws IOException;

  /**
   * 送られてきたブロードキャストメッセージに返信する
   *
   * @param receivedMessage 返信対象の{@link GameMessage}
   * @param replyMessage 相手へ送信する{@link GameMessage}
   */
  void reply(GameMessage receivedMessage, GameMessage replyMessage) throws IOException;
}
