package jp.ac.metro_cit.adv_prog_2024.gomoku.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.metro_cit.adv_prog_2024.gomoku.controller.GameCommunicationController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.GameStateCallback;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.Ui;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserButton;
import jp.ac.metro_cit.adv_prog_2024.gomoku.ui.user_components.UserLabel;

/** Represents the game page panel in the Gomoku application. */
public class GamePage extends JPanel implements GameStateCallback {

  private Ui parentFrame;
  private static final int BOARD_SIZE = 19;
  private final JPanel northPanel = new JPanel(new BorderLayout());
  private final JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
  private final JPanel southPanel = new JPanel(new BorderLayout());
  private final JButton[][] boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
  private final JLabel currentPhaseLabel;
  private final JLabel playersNameLabel;
  private final JLabel coordinatesLabel;
  private final JButton surrenderButton;
  private final JButton backButton;
  private final GameCommunicationController gameController;

  /**
   * Constructs a new GamePage object.
   *
   * @param parentFrame Ui
   * @param gameController GameCommunicationController
   */
  public GamePage(Ui parentFrame, GameCommunicationController gameController) {
    this.parentFrame = parentFrame;
    this.gameController = gameController;
    this.gameController.setGameStateCallback(this);

    Player localPlayer = parentFrame.getLocalPlayer();
    Player remotePlayer = parentFrame.getRemotePlayer();
    GameState initialGameState = gameController.startGame(localPlayer, remotePlayer);

    currentPhaseLabel = UserLabel.create("黒のターン");
    playersNameLabel = UserLabel.create(localPlayer.getName() + " vs " + remotePlayer.getName());
    coordinatesLabel = UserLabel.create("");
    surrenderButton = UserButton.create("降参");
    surrenderButton.addActionListener(new SurrenderButtonActionListener());

    backButton = UserButton.create("戻る");
    backButton.addActionListener(new BackButtonActionListener());

    initializeBoardButtons();
    initializeLayout(initialGameState);
  }

  /** Initializes the board buttons. */
  private void initializeBoardButtons() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 50));
        button.addActionListener(new BoardButtonActionListener(i, j));
        boardButtons[i][j] = button;
        boardPanel.add(button);
      }
    }
  }

  /** Initializes the layout of the GamePage. */
  private void initializeLayout(GameState initialGameState) {
    setLayout(new BorderLayout());
    setOpaque(false);

    setupNorthPanel();
    setupSouthPanel();

    add(northPanel, BorderLayout.NORTH);
    add(boardPanel, BorderLayout.CENTER);
    add(southPanel, BorderLayout.SOUTH);

    updateBoardButtons(initialGameState);
  }

  /** Configures the north panel. */
  private void setupNorthPanel() {
    northPanel.add(currentPhaseLabel, BorderLayout.WEST);
    northPanel.add(coordinatesLabel, BorderLayout.EAST);
  }

  /** Configures the south panel. */
  private void setupSouthPanel() {
    southPanel.add(playersNameLabel, BorderLayout.WEST);
    southPanel.add(surrenderButton, BorderLayout.EAST);
  }

  /** Updates the phase label to reflect the current game phase. */
  private void updatePhaseLabel() {
    GameState currentGameState = gameController.getGameState();
    switch (currentGameState.phase()) {
      case BLACK_TURN -> currentPhaseLabel.setText("黒のターン");
      case WHITE_TURN -> currentPhaseLabel.setText("白のターン");
      case FINISHED -> currentPhaseLabel.setText("ゲーム終了");
      default -> currentPhaseLabel.setText("黒のターン");
    }
  }

  /** Updates the board buttons based on the current game state. */
  private void updateBoardButtons(GameState gameState) {
    HashMap<Vector2D, Stone> board = gameState.board();
    board.forEach(
        (pos, stone) -> {
          JButton button = boardButtons[stone.getPos().x][stone.getPos().y];
          button.setBackground(stone.getColor() == StoneColor.BLACK ? Color.BLACK : Color.WHITE);
          button.setEnabled(false);
        });
  }

  /** Disables all board buttons. */
  private void disableAllButtons() {
    for (JButton[] row : boardButtons) {
      for (JButton button : row) {
        button.setEnabled(false);
      }
    }
    surrenderButton.setEnabled(false);
  }

  /** Handles game state changes by updating the UI. */
  @Override
  public void onGameStateChanged(GameState state) {
    updatePhaseLabel();
    updateBoardButtons(state);
  }

  /** Inner class for handling board button actions. */
  private class BoardButtonActionListener implements ActionListener {

    private final int row;
    private final int col;

    public BoardButtonActionListener(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      GameState currentGameState = gameController.getGameState();
      try {
        StoneColor color =
            (currentGameState.phase() == GamePhase.BLACK_TURN)
                ? StoneColor.BLACK
                : StoneColor.WHITE;

        GameState updatedState = gameController.putStone(color, new Vector2D(row, col));

        updateBoardButtons(updatedState);
        coordinatesLabel.setText("選択された座標: (" + row + ", " + col + ")");
        updatePhaseLabel();

        if (updatedState.phase() == GamePhase.FINISHED) {
          String winnerName =
              updatedState.winner() == null ? "なし" : updatedState.winner().getName();
          currentPhaseLabel.setText("ゲーム終了! 勝者: " + winnerName);
          disableAllButtons();

          southPanel.add(backButton, BorderLayout.CENTER);
        }
      } catch (PutStoneException ex) {
        coordinatesLabel.setText("無効な操作: (" + row + ", " + col + ")");
      }
    }
  }

  private class SurrenderButtonActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      GameState updatedState = gameController.surrender();
      updateBoardButtons(updatedState);
      updatePhaseLabel();
      disableAllButtons();
    }
  }

  private class BackButtonActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      parentFrame.showStartPage();
    }
  }
}
