package jp.ac.metro_cit.adv_prog_2024.gomoku;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
  @Test
  @DisplayName("[正常系]初期化後、ゲームクラスはnullではなく、フェーズはSETUPである")
  void InitGameClass() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    assertNotNull(game, "Create Game instance"); // ゲームインスタンスの生成
    assertTrue(game.getPhase() == GamePhase.BEFORE_START);
  }

  @Test
  @DisplayName("[正常系]ゲームを開始すると、現在のプレイヤーはnullではなく、BLACKである")
  void StartGame() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    // ゲームを開始
    game.startGame();
    assertNotNull(game.getCurrentPlayer(), "Current player is not null"); // 現在のプレイヤーはnullでない
    assertTrue(
        game.getCurrentPlayer().getColor() == StoneColor.BLACK,
        "Current player is not BLACK"); // 現在のプレイヤーはBLACK
  }

  @Test
  @DisplayName("[正常系]次のフェーズに移行すると、現在のプレイヤーが変わる")
  void NextPhase() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    game.startGame();
    Player currentPlayer = game.getCurrentPlayer();
    StoneColor currentColor = currentPlayer.getColor();
    game.putStone(StoneColor.BLACK, new Vector2D(0, 0)); // 石を置く
    assertTrue(
        game.getCurrentPlayer() != currentPlayer, "Current player is changed"); // 現在のプレイヤーが変わっている
    assertTrue(
        game.getCurrentPlayer().getColor() != currentColor,
        "Current player color is changed"); // 現在のプレイヤーの色が変わっている
  }

  @Test
  @DisplayName("[正常系]ゲームを終了すると、フェーズはFINISHEDである")
  void EndGame() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    // ゲームを開始
    game.startGame();
    game.endGame(StoneColor.BLACK);
    assertTrue(game.getPhase() == GamePhase.FINISHED, "Game phase is FINISHED"); // ゲームは終了している
  }

  @Test
  @DisplayName("[正常系]石が5個そろうとゲームが終了する")
  void CheckWinner() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    // ゲームを開始
    game.startGame();
    game.putStone(StoneColor.BLACK, new Vector2D(0, 0));
    assertFalse(game.getPhase() == GamePhase.FINISHED, "Game phase is not FINISHED"); // ゲームは終了していない
    game.putStone(StoneColor.BLACK, new Vector2D(1, 1));
    assertFalse(game.getPhase() == GamePhase.FINISHED, "Game phase is not FINISHED"); // ゲームは終了していない
    game.putStone(StoneColor.BLACK, new Vector2D(5, 5)); // 現状Board.CheckWinnerは(5,5)に石を置くとtrueを返す
    assertTrue(game.getPhase() == GamePhase.FINISHED, "Game phase is FINISHED"); // ゲームは終了している
  }
}
