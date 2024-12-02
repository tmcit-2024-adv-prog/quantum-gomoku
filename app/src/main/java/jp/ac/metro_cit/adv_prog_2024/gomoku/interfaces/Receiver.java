package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import java.io.IOException;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;

/**
 * 通信を受信する側のインターフェイス
 *
 * @author A Kokubo
 */
public interface Receiver {
  void onReceive(GameState gameState);

  void initReceiver() throws IOException;

  void startReceive();

  void disconnect() throws IOException;
}
