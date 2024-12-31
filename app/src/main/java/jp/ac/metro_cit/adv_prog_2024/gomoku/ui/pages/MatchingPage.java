package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.metro_cit.adv_prog_2024.gomoku.controller.GameMatchController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserButton;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserLabel;

public class MatchingPage extends JPanel {
  private Ui parentFrame;
  private JLabel playerNameLabel;
  private JLabel matchingMessageLabel;
  private JButton backButton;
  private JButton buttleButton;

  private Player localPlayer;
  private Player remotePlayer;

  public MatchingPage(Ui parentFrame) {
    this.parentFrame = parentFrame;

    initializeComponents();
    setupLayout();
    setupEventHandlers();
    matchingEvent();
  }

  private void initializeComponents() {
    matchingMessageLabel = UserLabel.create("対戦相手を探しています...");
    playerNameLabel = UserLabel.create(parentFrame.getLocalPlayerName());

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

  private void matchingEvent() {
    matchingMessageLabel.setText("対戦相手を探しています...");

    // 非同期処理を使ってマッチングを実行
    new Thread(
            () -> {
              GameMatchController gameMatchController = new GameMatchController();
              try {
                String localplayerName = parentFrame.getLocalPlayerName();
                Player[] players = gameMatchController.match(localplayerName);
                localPlayer = players[0];
                remotePlayer = players[1];
                parentFrame.setLocalPlayer(localPlayer);
                parentFrame.setRemotePlayer(remotePlayer);
                // マッチング成功時のUI更新をEDT(Event Dispatch Thread)で実行
                javax.swing.SwingUtilities.invokeLater(this::updateComponentsMatchinged);
              } catch (Exception e) {
                // マッチング失敗時のUI更新をEDTで実行
                javax.swing.SwingUtilities.invokeLater(
                    () -> {
                      matchingMessageLabel.setText("マッチングに失敗しました");
                      System.out.println(e.getMessage());
                    });
              }
            })
        .start();
  }

  private void updateComponentsMatchinged() {
    matchingMessageLabel.setText("対戦相手が見つかりました！");
    playerNameLabel.setText("対戦相手：" + remotePlayer.getName());
    try {
      Thread.sleep(5000);
      javax.swing.SwingUtilities.invokeLater(parentFrame::showGamePage);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void setupEventHandlers() {
    backButton.addActionListener(e -> parentFrame.showStartPage());
    buttleButton.addActionListener(e -> parentFrame.showGamePage());
  }
}
