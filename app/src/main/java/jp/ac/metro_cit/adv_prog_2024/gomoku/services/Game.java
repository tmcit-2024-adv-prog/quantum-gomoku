package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Color;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;

public class Game {
  public void putStone(Color color, int x, int y) throws PutStoneException {
    // ここに処理を書く
  }

  public void surrender() {
    // ここに処理を書く
  }

  public GameState gameState() {
    return gameState;
  }

  private GameState gameState;

  public void setGameState(GameState state) {}
}
