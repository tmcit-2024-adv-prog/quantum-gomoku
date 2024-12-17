package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserLabel;

public class GamePage extends JPanel {
  private static final int BOARD_SIZE = 11;
  private JPanel northPanel = new JPanel(new BorderLayout());
  private JButton[][] boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
  private JLabel currentPhaseLabel;
  private JLabel opponentNameLabel;
  private JLabel coordinatesLabel;
  private Ui parentFrame;

  public GamePage(Ui parentFrame) {
    this.parentFrame = parentFrame;
    initializeComponents();
    setupNorthPanel();
    setupLayout();
    updatePhaseLabel();
  }

  private void initializeComponents() {
    currentPhaseLabel = UserLabel.create("試合開始前");
    coordinatesLabel = UserLabel.create("選択された座標: (-1, -1)");
    initializeBoardButtons();
  }

  private void initializeBoardButtons() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        boardButtons[i][j] = new JButton();
        boardButtons[i][j].setPreferredSize(new Dimension(50, 50));
        boardButtons[i][j].addActionListener((ActionListener) new BoardButtonActionListener(i, j));
      }
    }
  }

  private void setupNorthPanel() {
    northPanel.add(currentPhaseLabel, BorderLayout.WEST);
    northPanel.add(opponentNameLabel, BorderLayout.CENTER);
    northPanel.add(coordinatesLabel, BorderLayout.EAST);
  }

  private void setupLayout() {
    setLayout(new BorderLayout());
    setOpaque(false);

    JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        boardPanel.add(boardButtons[i][j]);
      }
    }

    add(northPanel, BorderLayout.NORTH);
    add(boardPanel, BorderLayout.CENTER);
  }

  private void updatePhaseLabel() {
    switch (parentFrame.getPhase()) {
      case BLACK_TURN:
        currentPhaseLabel.setText("黒のターン");
        break;
      case WHITE_TURN:
        currentPhaseLabel.setText("白のターン");
        break;
      default:
        currentPhaseLabel.setText("");
        break;
    }
  }

  private class BoardButtonActionListener implements ActionListener {
    private int row;
    private int col;

    public BoardButtonActionListener(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JButton button = (JButton) e.getSource();
      if (parentFrame.getPhase() == GamePhase.BLACK_TURN) {
        button.setBackground(Color.BLACK);
      } else if (parentFrame.getPhase() == GamePhase.WHITE_TURN) {
        button.setBackground(Color.WHITE);
      }
      button.setEnabled(false);
      coordinatesLabel.setText("選択された座標: (" + row + ", " + col + ")");
    }
    // ゲームのロジックを呼び出す
  }
}
