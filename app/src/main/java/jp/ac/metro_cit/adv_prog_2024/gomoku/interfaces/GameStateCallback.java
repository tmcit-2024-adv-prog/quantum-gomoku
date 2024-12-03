package jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;

public interface GameStateCallback {
  void onGameStateChanged(GameState state);
}
