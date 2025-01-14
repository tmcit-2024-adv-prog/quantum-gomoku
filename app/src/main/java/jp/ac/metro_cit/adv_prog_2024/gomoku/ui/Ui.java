package jp.ac.metro_cit.adv_prog_2024.gomoku.ui;

import java.awt.Color;
import javax.swing.JFrame;

import jp.ac.metro_cit.adv_prog_2024.gomoku.controllers.GameCommunicationController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.controllers.GameCommunicationControllerBuilder;
import jp.ac.metro_cit.adv_prog_2024.gomoku.controllers.MatchingController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePhaseException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.GamePage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.MatchingPage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.StartPage;

/**
 * * Windowの表示と各画面のルーティングを行うクラス
 *
 * @author 葛野
 */
public class Ui extends JFrame {
  private String localPlayerName;
  private Player localPlayer;
  private Player remotePlayer;
  private GameCommunicationControllerBuilder gccBuilder;
  private MatchingController matchingController;

  public void openStartWindow() {
    this.setVisible(true);
  }

  public Ui(MatchingController matchingController, GameCommunicationControllerBuilder gccBuilder) {
    this.gccBuilder = gccBuilder;
    this.matchingController = matchingController;
    setTitle("五目並べ");
    setSize(1280, 1024);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBackground(Color.WHITE);
    setLocationRelativeTo(null);
    showStartPage();
    setVisible(true);
  }

  public void showStartPage() {
    StartPage startPage = new StartPage(this);
    setContentPane(startPage);
    revalidate();
    repaint();
  }

  public void showMatchingPage() {
    MatchingPage matchingPage = new MatchingPage(this);
    setContentPane(matchingPage);
    revalidate();
    repaint();
  }

  public void showGamePage() throws GamePhaseException {
    GameCommunicationController gameCommunicationController =
        (GameCommunicationController) gccBuilder.build(localPlayer, remotePlayer);
    GamePage gamePage = new GamePage(this, gameCommunicationController);
    setContentPane(gamePage);
    revalidate();
    repaint();
  }

  public void setLocalPlayerName(String playerName) {
    this.localPlayerName = playerName;
  }

  public String getLocalPlayerName() {
    return localPlayerName;
  }

  public void setLocalPlayer(Player localPlayer) {
    this.localPlayer = localPlayer;
  }

  public void setRemotePlayer(Player remotePlayer) {
    this.remotePlayer = remotePlayer;
  }

  public Player getLocalPlayer() {
    return localPlayer;
  }

  public Player getRemotePlayer() {
    return remotePlayer;
  }

  public MatchingController getMatchingController() {
    return matchingController;
  }
}
