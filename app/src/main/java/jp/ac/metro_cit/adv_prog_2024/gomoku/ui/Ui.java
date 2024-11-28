package jp.ac.metro_cit.adv_prog_2024.gomoku.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Ui {

  public static void openWindow() {

    JFrame frame = new JFrame("五目並べ");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1280, 1024);
    frame.setLocationRelativeTo(null);

    JLabel label = new JLabel("Hello World");
    frame.add(label);

    frame.setVisible(true);
  }
}
