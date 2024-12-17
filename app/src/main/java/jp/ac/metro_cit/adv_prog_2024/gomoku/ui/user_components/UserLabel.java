package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components;

import javax.swing.JLabel;

public class UserLabel {
  public static JLabel create(String text) {
    JLabel label = new JLabel(text);
    label.setFont(Constants.LABEL_FONT);
    label.setForeground(Constants.LABEL_FOREGROUND_COLOR);
    return label;
  }
}
