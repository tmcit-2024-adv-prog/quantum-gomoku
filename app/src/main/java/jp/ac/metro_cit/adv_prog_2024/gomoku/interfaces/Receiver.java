package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import java.io.IOException;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;

/**
 * 通信を受信する側のインターフェイス
 *
 * @author A Kokubo
 */
public interface Receiver {

  /**
   * 相手から送られてきた{@link GameMessage}を取得する
   *
   * <p>送られてきたデータがない場合はnullを返す
   *
   * @return 相手から送られてきたデータ
   */
  GameMessage receive();

  /**
   * 相手から送られてきた{@link GameState}を取得する
   *
   * <p>送られてきたデータがない場合はnullを返す
   *
   * @return 相手から送られてきたデータ
   */
  GameState receiveState();

  /**
   * Receiverを初期化し、通信を行う準備をする
   *
   * @throws IOException 通信時に発生したエラー
   */
  void initReceiver() throws IOException;

  /** 通信を開始する */
  void startReceive();

  /**
   * 通信を切断する
   *
   * @throws IOException 通信時に発生したエラー
   */
  void disconnect() throws IOException;
}
