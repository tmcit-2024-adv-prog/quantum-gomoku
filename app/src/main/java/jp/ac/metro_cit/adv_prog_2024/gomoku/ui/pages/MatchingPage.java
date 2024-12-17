package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserButton;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserLabel;

public class MatchingPage extends JPanel {
  private Ui parentFrame;
  private JLabel playerNameLabel;
  private JLabel matchingMessageLabel;
  private JButton backButton;
  private JButton buttleButton;

  public MatchingPage(Ui parentFrame) {
    this.parentFrame = parentFrame;
    initializeComponents();
    setupLayout();
    setupEventHandlers();
  }

  private void initializeComponents() {
    matchingMessageLabel = UserLabel.create("対戦相手を探しています...");
    playerNameLabel = new JLabel();

    // ラベルの設定
    playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
    playerNameLabel.setVerticalAlignment(JLabel.CENTER);
    matchingMessageLabel.setHorizontalAlignment(JLabel.CENTER);
    matchingMessageLabel.setVerticalAlignment(JLabel.CENTER);

    // ボタンの作成
    backButton = UserButton.create("戻る");
    buttleButton = UserButton.create("バトル画面テスト");
  }

  private void setupLayout() {
    setLayout(new BorderLayout());
    add(matchingMessageLabel, BorderLayout.NORTH);
    add(playerNameLabel, BorderLayout.CENTER);
    add(backButton, BorderLayout.SOUTH);
    add(buttleButton, BorderLayout.EAST);
  }

  private void setupEventHandlers() {
    backButton.addActionListener(e -> parentFrame.showStartPage());
    buttleButton.addActionListener(e -> parentFrame.showGamePage());
  }
}
