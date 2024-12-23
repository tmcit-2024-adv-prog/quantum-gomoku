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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class GameCommunicationControllerTest {
  @Test
  @DisplayName("[正常系] startGameメソッドの実行時にgameStateCallbackが設定されている場合、GameStateを返す")
  public void testStartGameWithGameStatusCallback() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer");
    Player remotePlayer = new Player("remotePlayer");
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    // 期待されるGameState
    GameState expected =
        new GameState(GamePhase.BLACK_TURN, localPlayer, remotePlayer, null, new HashMap<>());

    // gameStateCallbackを設定
    gcc.setGameStateCallback(gameStatusCallback);
    GameState result = gcc.startGame();

    // GameStateが返されることを確認
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[異常系] startGameメソッドの実行時にgameStateCallbackが設定されていない場合、IllegalStateExceptionをスローする")
  public void testStartGameWithoutGameStatusCallback() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);

    // gameStateCallbackを設定しない
    // IllegalStateExceptionがスローされることを確認
    Throwable exception = assertThrows(IllegalStateException.class, () -> gcc.startGame());
    assertEquals("gameStatusCallback is not set", exception.getMessage());
  }

  @Test
  @DisplayName("[正常系] putStoneメソッドの実行時に黒の石を黒のターンに設置すると白のターンになる")
  public void testPutStoneBlackTurnBlackStone() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    // 期待されるGameState
    HashMap<Vector2D, Stone> expectedBoard = new HashMap<>();
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    expectedBoard.put(stone.getPos(), stone);
    GameState expected =
        new GameState(GamePhase.WHITE_TURN, localPlayer, remotePlayer, null, expectedBoard);

    // 黒の石を設置する
    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    GameState result = assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(0, 0)));

    // GameStateが返されることを確認
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[異常系] putStoneメソッドの実行時に白の石を黒のターンに設置しようとするとPutStoneExceptionをスローする")
  public void testPutStoneBlackTurnWhiteStone() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);

    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    // 黒のターンに設定する
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    HashMap<Vector2D, Stone> board = new HashMap<>();
    board.put(stone.getPos(), stone);
    gcc.setGameState(new GameState(GamePhase.BLACK_TURN, localPlayer, remotePlayer, null, board));

    // 白の石を設置しようとする
    assertThrows(PutStoneException.class, () -> gcc.putStone(StoneColor.WHITE, new Vector2D(0, 0)));
  }

  @Test
  @DisplayName("[正常系] putStoneメソッドの実行時に白の石を白のターンに設置すると黒のターンになる")
  public void testPutStoneWhiteTurnWhiteStone() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    // 期待されるGameState
    HashMap<Vector2D, Stone> expectedBoard = new HashMap<>();
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    expectedBoard.put(stone.getPos(), stone);
    stone = new Stone(StoneColor.WHITE, new Vector2D(18, 18));
    expectedBoard.put(stone.getPos(), stone);
    GameState expected =
        new GameState(GamePhase.BLACK_TURN, localPlayer, remotePlayer, null, expectedBoard);

    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    // 白のターンに設定する
    stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    HashMap<Vector2D, Stone> board = new HashMap<>();
    board.put(stone.getPos(), stone);
    gcc.setGameState(new GameState(GamePhase.WHITE_TURN, localPlayer, remotePlayer, null, board));
    // 白の石を設置する
    GameState result =
        assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(18, 18)));

    // GameStateが返されることを確認
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[異常系] putStoneメソッドの実行時に黒の石を白のターンに設置しようとするとPutStoneExceptionをスローする")
  public void testPutStoneWhiteTurnBlackStone() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);

    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    // 白のターンに設定する
    Stone stone = new Stone(StoneColor.BLACK, new Vector2D(0, 0));
    HashMap<Vector2D, Stone> board = new HashMap<>();
    board.put(stone.getPos(), stone);
    gcc.setGameState(new GameState(GamePhase.WHITE_TURN, localPlayer, remotePlayer, null, board));

    // 黒の石を設置しようとする
    assertThrows(PutStoneException.class, () -> gcc.putStone(StoneColor.BLACK, new Vector2D(0, 0)));
  }

  @Test
  @DisplayName("[正常系] 黒い石を5回設置すると黒の勝利となる。localPlayerが黒の場合、返り値のwinnerがlocalPlayerになる")
  public void testPutStoneBlackWin() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    // 期待されるGameState
    HashMap<Vector2D, Stone> expectedBoard = new HashMap<>();
    for (int i = 0; i < 5; i++) {
      Stone stone = new Stone(StoneColor.BLACK, new Vector2D(i, i));
      expectedBoard.put(stone.getPos(), stone);
    }
    for (int i = 0; i < 4; i++) {
      Stone stone = new Stone(StoneColor.WHITE, new Vector2D(18 - i, 18 - i));
      expectedBoard.put(stone.getPos(), stone);
    }
    GameState expected =
        new GameState(GamePhase.FINISHED, localPlayer, remotePlayer, localPlayer, expectedBoard);

    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    // 黒の石を5回設置する
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(0, 0)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(18, 18)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(1, 1)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(17, 17)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(2, 2)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(16, 16)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(3, 3)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(15, 15)));
    GameState result = assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(4, 4)));

    // GameStateが返されることを確認
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[正常系] 黒い石を5回設置すると黒の勝利となる。localPlayerが白の場合、返り値のwinnerがremotePlayerになる")
  public void testPutStoneBlackWinWithWhiteLocalPlayer() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.WHITE);
    Player remotePlayer = new Player("remotePlayer", StoneColor.BLACK);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    // 期待されるGameState
    HashMap<Vector2D, Stone> expectedBoard = new HashMap<>();
    for (int i = 0; i < 5; i++) {
      Stone stone = new Stone(StoneColor.BLACK, new Vector2D(i, i));
      expectedBoard.put(stone.getPos(), stone);
    }
    for (int i = 0; i < 4; i++) {
      Stone stone = new Stone(StoneColor.WHITE, new Vector2D(18 - i, 18 - i));
      expectedBoard.put(stone.getPos(), stone);
    }
    GameState expected =
        new GameState(GamePhase.FINISHED, localPlayer, remotePlayer, remotePlayer, expectedBoard);

    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    // 黒の石を5回設置する
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(0, 0)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(18, 18)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(1, 1)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(17, 17)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(2, 2)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(16, 16)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(3, 3)));
    assertDoesNotThrow(() -> gcc.putStone(StoneColor.WHITE, new Vector2D(15, 15)));
    GameState result = assertDoesNotThrow(() -> gcc.putStone(StoneColor.BLACK, new Vector2D(4, 4)));

    // GameStateが返される
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[正常系] surrenderメソッドを実行するとフェーズが終了済みになり、相手側が勝利する")
  public void testSurrender() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    // 期待されるGameState
    GameState expected =
        new GameState(GamePhase.FINISHED, localPlayer, remotePlayer, remotePlayer, new HashMap<>());

    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    // 相手側が勝利する
    GameState result = assertDoesNotThrow(() -> gcc.surrender());

    // GameStateが返されることを確認
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("[正常系] setGameStateメソッドを実行するとGameStateが更新される。getGameStateメソッドで取得できる")
  public void testSetAndGetGameState() {
    // モックの作成
    Sender sender = mock(Sender.class);
    Receiver receiver = mock(Receiver.class);
    GameStateCallback gameStatusCallback = mock(GameStateCallback.class);
    // テスト対象のインスタンスを生成
    Player localPlayer = new Player("localPlayer", StoneColor.BLACK);
    Player remotePlayer = new Player("remotePlayer", StoneColor.WHITE);
    GameCommunicationController gcc =
        new GameCommunicationController(localPlayer, remotePlayer, sender, receiver);
    // 期待されるGameState
    GameState expected =
        new GameState(GamePhase.FINISHED, localPlayer, remotePlayer, remotePlayer, new HashMap<>());

    gcc.setGameStateCallback(gameStatusCallback);
    gcc.startGame();
    // GameStateを更新する
    gcc.setGameState(expected);
    GameState result = gcc.getGameState();

    // GameStateが取得できることを確認
    assertEquals(expected, result);
  }
}
