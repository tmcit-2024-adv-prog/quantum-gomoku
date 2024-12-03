package jp.ac.metro_cit.adv_prog_2024.gomoku;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;

public class GameTest {
  @Test
  void gameTest() {
    Game game = new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(9, 9)));
    Vector2D pos = new Vector2D(0, 0);
    int count = 0;

    assertNotNull(game, "Create Game instance");
    assertNotNull(game.getPhase() == GamePhase.SETUP, "Game phase is SETUP");

    // ゲーム開始
    game.startGame();
    while (count < 20) {
      System.out.println(game.getCurrentPlayer().getName() + " turn");

      try {
        game.putStone(game.getCurrentPlayer().getColor(), pos.x, pos.y);
      } catch (PutStoneException e) {
        System.out.println("Error: " + e.getMessage());
        return;
      }
      
      if (game.checkWinner(pos)) {
        System.out.println("Winner: " + game.getCurrentPlayer().getName());
        return;
      }

      // 設置場所の変更
      if (game.getPhase() == GamePhase.WHITE_TURN) {
        pos.x++;
        pos.y = 0;
      } else {
        pos.y = 1;
      }

      game.nextPhase();
      count++;
    }

    System.out.println("noWinner");
    return;
  }
}
