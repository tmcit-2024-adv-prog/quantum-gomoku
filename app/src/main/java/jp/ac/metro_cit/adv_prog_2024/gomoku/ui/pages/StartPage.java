package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserButton;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserLabel;

public class StartPage extends JPanel {
  private JTextField mySideNameInputField;
  private Ui parentFrame;

  public StartPage(Ui parentFrame) {
    this.parentFrame = parentFrame;
    initializeComponents();
  }

  private void initializeComponents() {
    // ページ構成要素の設定
    JButton startButton = createStartButton();
    JLabel pageTitle = createPageTitle();
    mySideNameInputField = createMySideNameInputField();

    // レイアウトの表示設定
    JPanel centerPanel = createCenterPanel(pageTitle, mySideNameInputField);
    JPanel southPanel = createSouthPanel(startButton);

    // パネルの追加
    setLayout(new BorderLayout());
    add(centerPanel, BorderLayout.CENTER);
    add(southPanel, BorderLayout.SOUTH);
  }

  private JButton createStartButton() {
    JButton startButton = UserButton.create("はじめる");
    startButton.addActionListener(new StartButtonActionListener());
    return startButton;
  }

  private JLabel createPageTitle() {
    JLabel pageTitle = UserLabel.create("五目並べ");
    pageTitle.setHorizontalAlignment(SwingConstants.CENTER);
    pageTitle.setVerticalAlignment(SwingConstants.CENTER);
    return pageTitle;
  }

  private JTextField createMySideNameInputField() {
    JTextField inputField = new JTextField(20);
    inputField.setPreferredSize(new Dimension(100, 50));
    return inputField;
  }

  private JPanel createCenterPanel(JLabel pageTitle, JTextField inputField) {
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(pageTitle, BorderLayout.CENTER);
    centerPanel.add(inputField, BorderLayout.SOUTH);
    return centerPanel;
  }

  private JPanel createSouthPanel(JButton startButton) {
    JPanel southPanel = new JPanel(new FlowLayout());
    southPanel.add(startButton);
    return southPanel;
  }

  private class StartButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String mySideName = mySideNameInputField.getText();
      if (mySideName.isEmpty()) {
        return;
      }
      parentFrame.setLocalPlayerName(mySideName);
      parentFrame.showMatchingPage();
    }
  }
}
