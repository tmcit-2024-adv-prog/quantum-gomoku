package jp.ac.metro_cit.adv_prog_2024.gomoku.communication;

import java.io.IOException;

/**
 * 通信を受信する側のインターフェイス
 *
 * @author A Kokubo
 */
public interface Receiver {
  void onReceive(GameStatus gameStatus);
  void initReceiver() throws IOException;
  void startReceive();
  void disconnect() throws IOException;
}
