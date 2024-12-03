package jp.ac.metro_cit.adv_prog_2024.gomoku;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Color;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameTest {
  @Test
  void gameTest() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));
    Vector2D pos = new Vector2D(0, 0);
    int count = 0;

    assertNotNull(game, "Create Game instance"); // ゲームインスタンスの生成
    assertNotEquals(game.getPhase(), GamePhase.BLACK_TURN, "Game phase is SETUP"); // ゲームフェーズはSETUP

    // ゲームを開始
    game.startGame();
    assertNotNull(game.getCurrentPlayer(), "Current player is not null"); // 現在のプレイヤーはnullでない
    assertNotEquals(
        game.getCurrentPlayer().getColor(),
        Color.WHITE,
        "Current player is not BLACK"); // 現在のプレイヤーはBLACK

    while (count < 20) {
      boolean isPlaced = game.putStone(game.getCurrentPlayer().getColor(), pos.x, pos.y);
      if (!isPlaced) {
        System.out.println("Already placed");
        return;
      }

      game.checkWinner(pos);
      if (game.getPhase() == GamePhase.FINISH) {
        assertNotEquals(
            game.getPhase(), GamePhase.BLACK_TURN, "Game phase is not BLACK_TURN"); // ゲームは終了している
        assertNotEquals(
            game.getPhase(), GamePhase.WHITE_TURN, "Game phase is not WHITE_TURN"); // ゲームは終了している
        return;
      }
      assertNotEquals(game.getPhase(), GamePhase.FINISH, "Game phase is not END"); // ゲームは終了していない

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

    return;
  }
}
