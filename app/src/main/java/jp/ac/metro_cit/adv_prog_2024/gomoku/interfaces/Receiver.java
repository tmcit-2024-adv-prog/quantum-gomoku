package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.Nullable;

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
   * @return 相手から送られてきたデータ
   */
  GameMessage<Serializable> receive() throws InterruptedException;

  /**
   * 相手から送られてきた{@link GameMessage}を取得する
   *
   * <p>指定されたタイムアウト(ミリ秒)以内に取得できない場合はnullを返す
   *
   * <p>送られてきたデータがない場合はnullを返す
   *
   * @param timeout 取得までのタイムアウト(ミリ秒)
   * @return 相手から送られてきたデータ
   */
  @Nullable
  GameMessage<Serializable> receive(long timeout) throws InterruptedException;

  /**
   * 相手から送られてきた{@link GameState}を取得する
   *
   * @return 相手から送られてきたデータ
   */
  GameState receiveState() throws InterruptedException;

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
