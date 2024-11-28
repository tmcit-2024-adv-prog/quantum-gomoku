package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.userConponents.Button;

public class MatchingPage extends JPanel {

  public MatchingPage(Ui parentFrame) {
    // パネルの設定
    setLayout(new BorderLayout());
    setOpaque(false);

    // 「戻る」ボタンを作成
    JButton backButton = Button.create("戻る");
    // ボタンがクリックされたときに StartPage に戻る
    backButton.addActionListener(e -> parentFrame.showStartPage());

    // ボタンを中央に配置
    add(backButton, BorderLayout.CENTER);
  }
}
