package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.util.HashMap;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.PutStoneException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.GameStateCallback;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import jp.ac.metro_cit.adv_prog_2024.gomoku.services.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameCommunicationControllerMockTest {
  @Test
  @DisplayName("[正常系] 両プレイヤーの色が決定している場合、GameCommunicationControllerのインスタンスが生成される")
  public void testGameCommunicationController() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);

    // GCCのインスタンス生成
    assertDoesNotThrow(
        () ->
            new GameCommunicationControllerMock(localPlayer, remotePlayer, sender, receiver, game));
  }

  @Test
  @DisplayName("[異常系] 両プレイヤーの色が決定していない場合、IllegalArgumentExceptionをスローする")
  public void testGameCommunicationControllerWithNoColor() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer");
    Player remotePlayer = new Player("remotePlayer");

    // GCCのインスタンス生成
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new GameCommunicationControllerMock(
                    localPlayer, remotePlayer, sender, receiver, game));
    assertEquals("LocalPlayeryとRemotePlayerのいずれか、あるいは双方の色が決定されていません", e.getMessage());
  }

  @Test
  @DisplayName("[異常系] localPlayerの色が決定していない場合、IllegalArgumentExceptionをスローする")
  public void testGameCommunicationControllerWithNoLocalPlayerColor() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer");
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);

    // GCCのインスタンス生成
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new GameCommunicationControllerMock(
                    localPlayer, remotePlayer, sender, receiver, game));
    assertEquals("LocalPlayeryとRemotePlayerのいずれか、あるいは双方の色が決定されていません", e.getMessage());
  }

  @Test
  @DisplayName("[異常系] remotePlayerの色が決定していない場合、IllegalArgumentExceptionをスローする")
  public void testGameCommunicationControllerWithNoRemotePlayerColor() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer");

    // GCCのインスタンス生成
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new GameCommunicationControllerMock(
                    localPlayer, remotePlayer, sender, receiver, game));
    assertEquals("LocalPlayeryとRemotePlayerのいずれか、あるいは双方の色が決定されていません", e.getMessage());
  }

  @Test
  @DisplayName("[異常系] 両プレイヤーの色が同じ場合、IllegalArgumentExceptionをスローする")
  public void testGameCommunicationControllerWithSameColor() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.BLACK);

    // GCCのインスタンス生成
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new GameCommunicationControllerMock(
                    localPlayer, remotePlayer, sender, receiver, game));
    assertEquals("LocalPlayerとRemotePlayerの色が同じです", e.getMessage());
  }

  @Test
  @DisplayName("[正常系] startGameメソッドを実行した際にGamePhaseがBLACK_TURNになる")
  public void testStartGameWithGameStatusCallback() throws Throwable {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);

    // 期待されるGameState
    GameState expected =
        new GameState(GamePhase.BLACK_TURN, localPlayer, remotePlayer, null, new HashMap<>());

    // テスト対象のインスタンスを生成
    GameCommunicationControllerMock gcc =
        new GameCommunicationControllerMock(localPlayer, remotePlayer, sender, receiver, game);
    // startGameメソッドを実行
    GameState result = assertDoesNotThrow(() -> gcc.startGame(gameStatusCallback));

    // GameStateが返される
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[正常系] putStoneメソッドの実行時に黒の石を黒のターンに設置すると白のターンになる")
  public void testPutStoneBlackTurnBlackStone() throws Throwable {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    Game game = mock(Game.class);
    HashMap<Vector2D, Stone> dummyBoard = new HashMap<>();
    dummyBoard.put(new Vector2D(0, 0), new Stone(StoneColor.BLACK, new Vector2D(0, 0)));
    when(game.getBoard()).thenReturn(dummyBoard);
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);

    // 期待されるGameState
    // (0, 0)に黒の石が置かれた状態
    // 白のターン
    GameState expected =
        new GameState(GamePhase.WHITE_TURN, localPlayer, remotePlayer, null, dummyBoard);

    // テスト対象のインスタンスを生成
    GameCommunicationControllerMock gcc =
        new GameCommunicationControllerMock(localPlayer, remotePlayer, sender, receiver, game);
    // 試合を開始
    gcc.startGame(gameStatusCallback);
    // 黒の石を設置する
    GameState result = assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(0, 0)));

    // 期待通りのGameStateが返される
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[異常系] putStoneメソッドの実行時に白の石を黒のターンに設置しようとするとPutStoneExceptionをスローする")
  public void testPutStoneBlackTurnWhiteStone() throws Throwable {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);

    // テスト対象のインスタンスを生成
    GameCommunicationControllerMock gcc =
        new GameCommunicationControllerMock(localPlayer, remotePlayer, sender, receiver, game);
    // 試合を開始
    gcc.startGame(gameStatusCallback);

    // 白の石を設置しようとする
    PutStoneException e =
        assertThrows(
            PutStoneException.class, () -> gcc.putStone(StoneColor.WHITE, new Vector2D(0, 0)));

    // PutStoneExceptionがスローされる
    assertEquals("Failed to put stone.", e.getMessage());
  }

  @Test
  @DisplayName("[正常系] putStoneメソッドの実行時に白の石を白のターンに設置すると黒のターンになる")
  public void testPutStoneWhiteTurnWhiteStone() throws Throwable {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    Game game = mock(Game.class);
    HashMap<Vector2D, Stone> dummyBoard = new HashMap<>();
    dummyBoard.put(new Vector2D(0, 0), new Stone(StoneColor.BLACK, new Vector2D(0, 0)));
    dummyBoard.put(new Vector2D(18, 18), new Stone(StoneColor.WHITE, new Vector2D(18, 18)));
    when(game.getBoard()).thenReturn(dummyBoard);
    Player localPlayer = new Player("localPlayer", StoneColor.WHITE);
    Player remotePlayer = new Player("remotePlayer", StoneColor.BLACK);

    // 期待されるGameState
    // (0, 0)に白の石が置かれた状態
    // 黒のターン
    GameState expected =
        new GameState(GamePhase.BLACK_TURN, localPlayer, remotePlayer, null, dummyBoard);

    // テスト対象のインスタンスを生成
    GameCommunicationControllerMock gcc =
        new GameCommunicationControllerMock(localPlayer, remotePlayer, sender, receiver, game);
    // 試合を開始
    gcc.startGame(gameStatusCallback);
    // 白の石を設置する
    GameState result =
        assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(18, 18)));

    // 期待通りのGameStateが返される
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[異常系] putStoneメソッドの実行時に黒の石を白のターンに設置しようとするとPutStoneExceptionをスローする")
  public void testPutStoneWhiteTurnBlackStone() throws Throwable {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    Game game = mock(Game.class);
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);

    // テスト対象のインスタンスを生成
    GameCommunicationControllerMock gcc =
        new GameCommunicationControllerMock(localPlayer, remotePlayer, sender, receiver, game);
    // 試合を開始
    gcc.startGame(gameStatusCallback);
    // 黒の石を設置する
    gcc.putStone(StoneColor.BLACK, new Vector2D(0, 0));
    // 黒の石を設置しようとする
    PutStoneException e =
        assertThrows(
            PutStoneException.class, () -> gcc.putStone(StoneColor.BLACK, new Vector2D(1, 1)));

    // PutStoneExceptionがスローされる
    assertEquals("Failed to put stone.", e.getMessage());
  }
}
