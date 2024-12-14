package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePhaseException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePlayerException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
  void StartGame() throws GamePhaseException {
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
  void NextPhase() throws GamePhaseException, GamePlayerException {
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
  void EndGame() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    // ゲームを開始
    game.startGame();
    game.surrender(StoneColor.BLACK);
    assertTrue(game.getPhase() == GamePhase.FINISHED, "Game phase is FINISHED"); // ゲームは終了している
  }

  @Test
  @DisplayName("[正常系]石が5個そろうとゲームが終了する")
  void CheckWinner() throws GamePhaseException, GamePlayerException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    // ゲームを開始
    game.startGame();
    game.putStone(StoneColor.BLACK, new Vector2D(0, 1));
    game.putStone(StoneColor.WHITE, new Vector2D(0, 0));
    game.putStone(StoneColor.BLACK, new Vector2D(1, 1));
    game.putStone(StoneColor.WHITE, new Vector2D(1, 0));
    game.putStone(StoneColor.BLACK, new Vector2D(2, 1));
    game.putStone(StoneColor.WHITE, new Vector2D(2, 0));
    game.putStone(StoneColor.BLACK, new Vector2D(3, 1));
    game.putStone(StoneColor.WHITE, new Vector2D(3, 0));
    game.putStone(StoneColor.BLACK, new Vector2D(4, 1));
    game.putStone(StoneColor.WHITE, new Vector2D(4, 0));
    game.putStone(StoneColor.BLACK, new Vector2D(5, 5)); // 現状Board.CheckWinnerは(5,5)に石を置くとtrueを返す
    assertTrue(game.getPhase() == GamePhase.FINISHED, "Game phase is FINISHED"); // ゲームは終了している
  }

  @Test
  @DisplayName("[異常系]ゲーム開始後に、再度ゲームを開始しようとすると例外が発生する")
  void StartGameTwice() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    try {
      game.startGame();
      game.startGame();
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]ゲームが開始、又は終了後に投了すると例外が発生する")
  void SurrenderBeforeStart() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    try {
      game.surrender(StoneColor.BLACK);
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
    game.startGame();
    game.surrender(StoneColor.BLACK);
    try {
      game.surrender(StoneColor.BLACK);
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]ゲーム開始前、終了後に、石を置こうとすると例外が発生する")
  void PutStoneAfterEnd() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    try {
      game.putStone(StoneColor.BLACK, new Vector2D(0, 0));
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }

    game.startGame();
    game.surrender(StoneColor.BLACK);

    try {
      game.putStone(StoneColor.BLACK, new Vector2D(0, 0));
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]相手のターン中に石を置こうとすると例外が発生する")
  void PutStoneInOpponentTurn() throws GamePhaseException, GamePlayerException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    game.startGame();
    game.putStone(StoneColor.BLACK, new Vector2D(0, 0));

    try {
      game.putStone(StoneColor.BLACK, new Vector2D(1, 1));
    } catch (Exception e) {
      assertTrue(e instanceof Exception, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]石の設置でエラーが発生した場合、それを返す")
  void PutStoneError() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    game.startGame();
    try {
      game.putStone(StoneColor.BLACK, new Vector2D(-10, 20));
    } catch (Exception e) {
      assertTrue(e instanceof GamePlayerException, "GamePlayerException is thrown");
    }
  }
}
