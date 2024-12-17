package jp.ac.metro_cit.adv_prog_2024.gomoku.ui;

import java.awt.Color;
import javax.swing.JFrame;

import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.GamePage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.MatchingPage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.StartPage;

/**
 * * Windowの表示と各画面のルーティングを行うクラス
 *
 * @author 葛野
 */
public class Ui extends JFrame {

  public static void openStartWindow() {
    Ui frame = new Ui("五目並べ");
    frame.setVisible(true);
  }

  Ui(String title) {
    setTitle(title);
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

  public void showGamePage() {
    GamePage GamePage = new GamePage(this);
    setContentPane(GamePage);
    revalidate();
    repaint();
  }

  private String mySideName;

  public void setMySideName(String mySideName) {
    this.mySideName = mySideName;
  }

  public String getMySideName() {
    return mySideName;
  }

  private String OpponentName;

  public void setOpponentName(String OpponentName) {
    this.OpponentName = OpponentName;
  }

  public String getOpponentName() {
    return OpponentName;
  }

  // ここ後で変える
  private String currentTurn;

  public void setCurrentTurn(String currentTurn) {
    this.currentTurn = currentTurn;
  }

  public enum GamePhase {
    /** The game has not started yet. */
    BEFORE_START,

    /** It is Black's turn to play. */
    BLACK_TURN,

    /** It is White's turn to play. */
    WHITE_TURN,

    /** The game has ended. */
    FINISHED,
  }

  public GamePhase getPhase() {
    GamePhase phase = GamePhase.BLACK_TURN;
    return phase;
  }
}
