package jp.ac.metro_cit.adv_prog_2024.gomoku.ui;

import java.awt.Color;
import javax.swing.JFrame;

import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.MatchingPage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages.StartPage;

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
    revalidate(); // ページを更新
    repaint();
  }
}
