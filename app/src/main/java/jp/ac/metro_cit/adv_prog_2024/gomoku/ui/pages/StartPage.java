package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.userConponents.Button;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.userConponents.Label;

public class StartPage extends JPanel {
  public StartPage(Ui parentFrame) {
    setLayout(new BorderLayout());
    setOpaque(false);

    JButton startButtlebutton = Button.create("はじめる");
    startButtlebutton.addActionListener(e -> parentFrame.showMatchingPage());

    JLabel pageTitle = Label.create("五目並べ");
    pageTitle.setHorizontalAlignment(JLabel.CENTER);
    pageTitle.setVerticalAlignment(JLabel.CENTER);

    JTextField playerNameInputField = new JTextField(20);
    playerNameInputField.setPreferredSize(new Dimension(100, 50));

    JPanel centerPanel = new JPanel();
    JPanel page_endPanel = new JPanel();

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(pageTitle, BorderLayout.CENTER);
    centerPanel.add(playerNameInputField, BorderLayout.SOUTH);
    page_endPanel.setLayout(new FlowLayout());
    page_endPanel.add(startButtlebutton);

    add(centerPanel, BorderLayout.CENTER);
    add(page_endPanel, BorderLayout.SOUTH);
  }
}
