package jp.ac.metro_cit.adv_prog_2024.gomoku.controller;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.GameStateCallback;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Color;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;

public class GameCommunicationController {
  // メンバ変数
  private Game game;
  private Player controlPlayer;
  private Player remotePlayer;
  private Receiver receiver;
  private Sender sender;
  public GameStateCallback gameStatusCallback;

  // パブリックメソッド
  public Player createControlPlayer(String name) {
    this.controlPlayer = new Player(name);

    return this.controlPlayer;
  }

  public Player createRemotePlayer(String name) {
    this.remotePlayer = new Player(name);

    return this.remotePlayer;
  }

  public GameState putStone(Color color, int x, int y) throws PutStoneException {
    try {
      this.game.putStone(color, x, y);
      return this.game.gameState();
    } catch (PutStoneException e) {
      throw e;
    } catch (Exception e) {
      throw new PutStoneException("石を置くことができませんでした");
    }
  }

  public GameState surrender() {
    this.game.surrender();
    return this.game.gameState();
  }

  public GameState getGameStatus() {
    return this.game.gameState();
  }

  public void setGameState(GameState state) {
    this.game.setGameState(state);
  }

  private void startRecive() {
    this.receiver.startRecive();
  }

  private Color decideColor() {
    return this.controlPlayer.setColoer(Color.BLACK);
  }
}
