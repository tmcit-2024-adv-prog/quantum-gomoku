package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import jp.ac.metro_cit.adv_prog_2024.gomoku.BuildConfig;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.IGameCommunicationController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;

public class GameCommunicationControllerFactory {
  public static IGameCommunicationController create(
      Player localPlayer, Player remotePlayer, Sender sender, Receiver receiver) {
    if (BuildConfig.ENV == "dev") {
      return new GameCommunicationControllerMock(localPlayer, remotePlayer, sender, receiver);
    } else {
      return new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    }
  }
}
