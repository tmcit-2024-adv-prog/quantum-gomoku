package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import java.util.HashMap;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePhaseException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.GamePlayerException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {
  @Test
  @DisplayName("[正常系]初期化後、ゲームクラスはnullではなく、フェーズはSETUPである")
  void initGameClass() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    assertNotNull(game, "Create Game instance"); // ゲームインスタンスの生成
    assertTrue(game.getPhase() == GamePhase.BEFORE_START);
  }

  @Test
  @DisplayName("[正常系]ゲームを開始すると、現在のプレイヤーはnullではなく、BLACKである")
  void startGame() throws GamePhaseException {
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
  void nextPhase() throws GamePhaseException, GamePlayerException, Exception {
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
  void endGame() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    // ゲームを開始
    game.startGame();
    game.surrender(StoneColor.BLACK);
    assertTrue(game.getPhase() == GamePhase.FINISHED, "Game phase is FINISHED"); // ゲームは終了している
  }

  @Test
  @DisplayName("[正常系]石が5個そろうとゲームが終了する")
  void checkWinner() throws GamePhaseException, GamePlayerException, Exception {
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
    game.putStone(StoneColor.BLACK, new Vector2D(5, 1));
    assertTrue(game.getPhase() == GamePhase.FINISHED, "Game phase is FINISHED"); // ゲームは終了している
  }

  @Test
  @DisplayName("[正常系]ゲームが終了した後、勝者を取得すると、勝者が取得できる")
  void getWinner() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    // ゲームを開始
    game.startGame();
    game.surrender(StoneColor.BLACK);
    assertTrue(
        game.getWinnerPlayer().getColor() == StoneColor.WHITE, "Winner is WHITE"); // 勝者はWHITE
  }

  @Test
  @DisplayName("[異常系]ゲーム開始後に、再度ゲームを開始しようとすると例外が発生する")
  void startGameTwice() {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    try {
      game.startGame();
      game.startGame();
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]ゲームが開始、又は終了後に投了すると例外が発生する")
  void surrenderBeforeStart() throws GamePhaseException {
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
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]ゲーム開始前に石を置こうとすると例外が発生する")
  void putStoneBeforeStart() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    try {
      game.putStone(StoneColor.BLACK, new Vector2D(0, 0));
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]ゲームが終了後に、石を置こうとすると例外が発生する")
  void putStoneAfterFinish() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    game.startGame();
    game.surrender(StoneColor.BLACK);

    try {
      game.putStone(StoneColor.BLACK, new Vector2D(0, 0));
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]石の設置でエラーが発生した場合、それを返す")
  void putStoneError() throws Throwable {
    Board board = mock(Board.class);
    doAnswer(
            invocation -> {
              throw new Exception("Error");
            })
        .when(board)
        .putStone(any(Vector2D.class), any(Stone.class));
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    Game game = new Game(localPlayer, remotePlayer, board);
    game.startGame();
    try {
      game.putStone(StoneColor.BLACK, new Vector2D(-10, 20));
      fail();
    } catch (Exception e) {
      assertEquals("The Stone cannot put.", e.getMessage());
    }
  }

  @Test
  @DisplayName("[異常系]相手のターン中に石を置くと例外が発生する")
  void notYourTurnError() throws GamePhaseException, GamePlayerException, Exception {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    game.startGame();
    game.putStone(StoneColor.BLACK, new Vector2D(0, 0));
    try {
      game.putStone(StoneColor.BLACK, new Vector2D(1, 0));
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof GamePlayerException, "GamePlayerException is thrown");
    }
  }

  @Test
  @DisplayName("[異常系]ゲームが終了していない状態で、勝者を取得しようとすると例外が発生する")
  void getWinnerBeforeEnd() throws GamePhaseException {
    Game game =
        new Game(new Player("player1"), new Player("player2"), new Board(new Vector2D(10, 10)));

    game.startGame();
    try {
      game.getWinnerPlayer();
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof GamePhaseException, "GamePhaseException is thrown");
    }
  }

  @Test
  @DisplayName(
      "[異常系] localPlayerの色として黒を指定したときに、intoメソッドでGameクラスからGameStateクラスへの変換が可能で、localPlayerの色が黒である")
  void intoGameStateOnLocalPlayerIsBlack() {
    // モックを作成する
    Board board = mock(Board.class);
    HashMap<Vector2D, Stone> map = new HashMap<>();
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    map.put(stone.getPos(), stone);
    when(board.getBoard()).thenReturn(map);

    // Gameクラスの実体が存在するとき
    Player blackPlayer = new Player("player1");
    blackPlayer.setColor(StoneColor.BLACK);
    Player whitePlayer = new Player("player2");
    whitePlayer.setColor(StoneColor.WHITE);
    Game game = new Game(blackPlayer, whitePlayer, board);
    // 期待されるGameState
    GameState expected = new GameState(GamePhase.BEFORE_START, blackPlayer, whitePlayer, null, map);
    // intoメソッドを実行
    GameState result = game.into(StoneColor.BLACK);

    assertEquals(expected, result);
  }

  @Test
  @DisplayName(
      "[異常系] localPlayerの色として白を指定したときに、intoメソッドでGameクラスからGameStateクラスへの変換が可能で、localPlayerの色が白である")
  void intoGameStateOnLocalPlayerIsWhite() {
    // モックを作成する
    Board board = mock(Board.class);
    HashMap<Vector2D, Stone> map = new HashMap<>();
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    map.put(stone.getPos(), stone);
    when(board.getBoard()).thenReturn(map);

    // Gameクラスの実体が存在するとき
    Player blackPlayer = new Player("player1");
    blackPlayer.setColor(StoneColor.BLACK);
    Player whitePlayer = new Player("player2");
    whitePlayer.setColor(StoneColor.WHITE);
    Game game = new Game(blackPlayer, whitePlayer, board);
    // 期待されるGameState
    GameState expected = new GameState(GamePhase.BEFORE_START, whitePlayer, blackPlayer, null, map);
    // intoメソッドを実行
    GameState result = game.into(StoneColor.WHITE);

    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[正常系] fromメソッドでGameStateをGameに反映可能")
  void fromGameState() {
    // モックを作成する
    Board board = mock(Board.class);
    HashMap<Vector2D, Stone> map = new HashMap<>();
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    map.put(stone.getPos(), stone);
    when(board.getBoard()).thenReturn(map);
    Player dummyBlackPlayer = new Player("player1", StoneColor.BLACK);
    Player dummyWhitePlayer = new Player("player2", StoneColor.WHITE);

    // Gameクラスの実体が存在するとき
    Game game = new Game(dummyBlackPlayer, dummyWhitePlayer, board);
    // 入力するGameState
    GameState gameState =
        new GameState(GamePhase.WHITE_TURN, dummyBlackPlayer, dummyWhitePlayer, null, map);

    // fromメソッドを実行
    assertDoesNotThrow(() -> game.from(gameState));
    // Gameクラスの状態がGameStateの状態になっているか確認
    assertEquals(gameState.phase(), game.getPhase());
    assertEquals(gameState.localPlayer(), game.getBlackPlayer());
    assertEquals(gameState.remotePlayer(), game.getWhitePlayer());
    assertEquals(gameState.board(), game.getBoard());
    GamePhaseException e = assertThrows(GamePhaseException.class, () -> game.getWinnerPlayer());
    assertEquals("The game has not finished.", e.getMessage());
  }

  @Test
  @DisplayName(
      "[異常系] fromメソッドでGameStateがに含まれるプレイヤー情報とGameのプレイヤー情報が一致しない場合、IllegalStateExceptionが発生")
  void fromGameStateWithIllegalStateException() {
    // モックを作成する
    Board board = mock(Board.class);
    HashMap<Vector2D, Stone> map = new HashMap<>();
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    map.put(stone.getPos(), stone);
    when(board.getBoard()).thenReturn(map);
    Player dummyBlackPlayer = new Player("player1", StoneColor.BLACK);
    Player dummyWhitePlayer = new Player("player2", StoneColor.WHITE);

    // Gameクラスの実体が存在するとき
    Game game = new Game(dummyBlackPlayer, dummyWhitePlayer, board);
    // 入力するGameState
    GameState gameState =
        new GameState(GamePhase.WHITE_TURN, new Player("dummy1"), new Player("dummy2"), null, map);

    // fromメソッドを実行
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.from(gameState));
    assertEquals("Players are not matched.", e.getMessage());
  }
}
