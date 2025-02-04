package jp.ac.metro_cit.adv_prog_2024.gomoku;

import java.util.Random;

import jp.ac.metro_cit.adv_prog_2024.gomoku.communications.TransportSocket;
import jp.ac.metro_cit.adv_prog_2024.gomoku.communications.TransportSocketProps;
import jp.ac.metro_cit.adv_prog_2024.gomoku.controllers.GameCommunicationControllerBuilder;
import jp.ac.metro_cit.adv_prog_2024.gomoku.controllers.MatchingController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.controllers.MatchingControllerBuilder;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;

public class App {
  public static void main(String[] args) throws Throwable {
    TransportSocket socket =
        new TransportSocket(new TransportSocketProps(null, 58946, 13398, 13398));
    socket.initSender();
    socket.startReceiveBroadcast();
    Random random = new Random();
    MatchingController matchingController =
        new MatchingControllerBuilder(socket, socket, () -> random.nextInt(1024)).build();
    GameCommunicationControllerBuilder gccBuilder =
        new GameCommunicationControllerBuilder(socket, socket);
    Ui ui = new Ui(matchingController, gccBuilder);
    ui.openStartWindow();
  }
}
