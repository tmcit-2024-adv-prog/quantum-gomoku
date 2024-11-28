package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.userConponents;

import java.awt.Dimension;
import javax.swing.JButton;

public class Button {

  public static JButton create(String title) {
    JButton btn = new JButton(title);
    btn.setFont(Constants.BUTTON_FONT);
    btn.setBackground(Constants.BUTTON_BACKGROUND_COLOR);
    btn.setPreferredSize(new Dimension(200, 50));
    return btn;
  }
}
