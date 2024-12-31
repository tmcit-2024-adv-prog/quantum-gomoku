package jp.ac.metro_cit.adv_prog_2024.gomoku.controller;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;

public class GameMatchController {
  public Player[] match(String localPlayerName) {
    Player localPlayer = new Player(localPlayerName);
    Player remotePlayer = new Player("相手");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return new Player[] {localPlayer, remotePlayer};
  }
}
