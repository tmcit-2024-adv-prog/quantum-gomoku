package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import java.io.IOException;

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
