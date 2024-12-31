package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.IOException;
import java.time.Duration;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingFailedException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingTimeoutException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessageType;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.utils.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("MatchingControllerのテスト")
public class MatchingControllerTest {

  @Test
  @DisplayName("[正常系] 能動的にマッチングを開始した場合にマッチングが成功する")
  public void testSuccessActiveMatching() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // 相手からはOfferメッセージとAckメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            new GameMessage(new MatchingMessage(MatchingMessageType.ACK)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[正常系] 受動的にマッチングを開始した場合にマッチングが成功する 自身の乱数値が相手より大きい場合は自身が黒になる")
  public void testSuccessPassiveMatchingOnRandomValueGreaterThanRemote() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.WHITE);
    // 相手からはDiscoverメッセージとRequestメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 0)),
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 1).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[正常系] 受動的にマッチングを開始した場合にマッチングが成功する 自身の乱数値が相手以下の場合は自身が白になる")
  public void testSuccessPassiveMatchingOnRandomValueLessThanAndEqualRemote() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // 相手からはDiscoverメッセージとRequestメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 0)),
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[正常系] 能動的にマッチングを開始した場合に期待されていないメッセージは無視される")
  public void testIgnoreUnexpectedMessageActiveMatching() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // 相手からOfferメッセージとAckメッセージ以外のメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            // 期待されるメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            // 期待されないメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            // 期待されないメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)),
            // 期待されるメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.ACK)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[正常系] 受動的にマッチングを開始した場合に期待されていないメッセージは無視される")
  public void testIgnoreUnexpectedMessagePassiveMatching() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    when(mockReceiver.receive())
        .thenReturn(
            // 期待されるメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            // 期待されないメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            // 期待されないメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.ACK)),
            // 期待されるメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName(
      "[異常系] Receiverからのメッセージの取得時にInterruptedExceptionが一定数以上発生した場合にMatchingFailedExceptionが発生する")
  public void testThrowMatchingFailedExceptionWhenReceiverThrowsInterruptedException()
      throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);

    // Receiverからのメッセージの取得時にInterruptedExceptionが発生するとき
    when(mockReceiver.receive()).thenThrow(InterruptedException.class);
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).setRetryCount(3).build();

    // マッチングを開始するとMatchingFailedExceptionが発生する
    assertThrows(MatchingFailedException.class, () -> matchingController.match("local"));
  }

  @Test
  @DisplayName("[異常系] Receiverからのメッセージの取得時にMatchingMessage以外のメッセージが受信された場合は無視される")
  public void testIgnoreNonMatchingMessage() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Receiverからのメッセージの取得時にMatchingMessage以外のメッセージが受信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            // MatchingMessage以外のメッセージ
            new GameMessage("dummy"),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Discoverメッセージに整数が含まれていない場合は無視される")
  public void testIgnoreDiscoverMessageWithoutInteger() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Offerメッセージに相手プレイヤーが含まれていないとき
    when(mockReceiver.receive())
        .thenReturn(
            // Discoverメッセージがnull
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER)),
            // OfferメッセージがIntegerでない
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, "dummy")),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            new GameMessage(new MatchingMessage(MatchingMessageType.ACK)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Offerメッセージに相手プレイヤーが含まれていない場合は無視される")
  public void testIgnoreOfferMessageWithoutPlayer() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Offerメッセージに相手プレイヤーが含まれていないとき
    when(mockReceiver.receive())
        .thenReturn(
            // Offerメッセージがnull
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER)),
            // OfferメッセージがPlayerでない
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER, "dummy")),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Offerメッセージに自分自身が含まれている場合は無視される")
  public void testIgnoreOfferMessageWithLocalPlayer() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    Player dummyLocalPlayer = new Player("local");
    // Offerメッセージに自分自身が含まれているとき
    when(mockReceiver.receive())
        .thenReturn(
            // Offerメッセージに自分自身が含まれている
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER, dummyLocalPlayer)),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            new GameMessage(new MatchingMessage(MatchingMessageType.ACK)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Offerメッセージの相手プレイヤーの色がnullの場合は無視される")
  public void testIgnoreOfferMessageWithNullColor() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Offerメッセージの相手プレイヤーの色がnullのとき
    when(mockReceiver.receive())
        .thenReturn(
            // Offerメッセージの相手プレイヤーの色がnull
            new GameMessage(
                new MatchingMessage(MatchingMessageType.OFFER, new Player("remote", null))),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Requestメッセージに相手プレイヤーが含まれていない場合は無視される")
  public void testIgnoreRequestMessageWithoutPlayer() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Requestメッセージに相手プレイヤーが含まれていないとき
    when(mockReceiver.receive())
        .thenReturn(
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            // Requestメッセージに相手プレイヤーが含まれていない
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST)),
            // Requestメッセージに相手プレイヤーが含まれていない
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, "dummy")),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Requestメッセージに自分自身が含まれている場合は無視される")
  public void testIgnoreRequestMessageWithLocalPlayer() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyLocalPlayer = new Player("local");
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Requestメッセージに自分自身が含まれているとき
    when(mockReceiver.receive())
        .thenReturn(
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            // Requestメッセージに自分自身が含まれている
            new GameMessage(
                new MatchingMessage(
                    MatchingMessageType.REQUEST, new Player("local", StoneColor.WHITE))),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Requestメッセージの相手プレイヤーの色がnullの場合は無視される")
  public void testIgnoreRequestMessageWithNullColor() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Requestメッセージの相手プレイヤーの色がnullのとき
    when(mockReceiver.receive())
        .thenReturn(
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            // Requestメッセージの相手プレイヤーの色がnull
            new GameMessage(
                new MatchingMessage(MatchingMessageType.REQUEST, new Player("remote", null))),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] Requestメッセージの相手プレイヤーの色が自身の色と同じ場合は無視される")
  public void testIgnoreRequestMessageWithSameColor() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player("remote", StoneColor.BLACK);
    // Requestメッセージの相手プレイヤーの色が自身の色と同じとき
    when(mockReceiver.receive())
        .thenReturn(
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)),
            // Requestメッセージの相手プレイヤーの色が自身の色と同じ
            new GameMessage(
                new MatchingMessage(
                    MatchingMessageType.REQUEST, new Player("remote", StoneColor.WHITE))),
            // 正常なメッセージ
            new GameMessage(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).build();

    // マッチングを開始すると
    Pair<Player, Player> result = matchingController.match("local");
    Player localPlayer = result.left();
    Player remotePlayer = result.right();

    // 相手のプレイヤーが返ってくる
    assertEquals(remotePlayer, dummyRemotePlayer);
    // 自分自身が返ってくる
    assertEquals(localPlayer.getName(), "local");
    assertEquals(localPlayer.getColor(), dummyRemotePlayer.getColor().opposite());
  }

  @Test
  @DisplayName("[異常系] retryLimit回以上sender.reply()がIOExceptionを投げた場合はMatchingFailedExceptionが発生する")
  public void testThrowMatchingFailedExceptionWhenSenderSendFailed() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    // sender.reply()が失敗するとき
    doAnswer(
            invocation -> {
              throw new IOException();
            })
        .when(mockSender)
        .reply(any(GameMessage.class), any(GameMessage.class));
    // receiover.receive()はDiscoverを返す
    when(mockReceiver.receive())
        .thenReturn(new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0).setRetryCount(3).build();

    // マッチングを開始するとMatchingFailedExceptionが発生する
    assertThrows(MatchingFailedException.class, () -> matchingController.match("local"));
    verify(mockSender, times(3)).reply(any(GameMessage.class), any(GameMessage.class));
  }

  @Test
  @DisplayName(
      "[異常系] retryLimit回以上sender.broadcast()がIOExceptionを投げた場合はMatchingTimeoutExceptionが発生する")
  public void testThrowMatchingTimeoutExceptionWhenSenderBroadcastFailed() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    // sender.broadcast()が失敗するとき
    doAnswer(
            invocation -> {
              throw new IOException();
            })
        .when(mockSender)
        .broadcast(any(GameMessage.class));
    // receiver.receive()はDiscoverを返す
    when(mockReceiver.receive())
        .thenReturn(new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER, 1)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0)
            .setRetryCount(3)
            .setTimeout(Duration.ofDays(1))
            .build();

    // マッチングを開始するとMatchingTimeoutExceptionが発生する
    assertThrows(MatchingTimeoutException.class, () -> matchingController.match("local"));
    verify(mockSender, times(3)).broadcast(any(GameMessage.class));
  }

  @Test
  @DisplayName("[異常系] timeout時間以内に相手プレイヤーが見つからない場合はMatchingTimeoutExceptionが発生する")
  public void testThrowMatchingTimeoutExceptionWhenTimeout() throws Throwable {
    // モックの作成
    Sender mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    // receiver.receive()はDiscoverを返さない
    when(mockReceiver.receive())
        .thenReturn(new GameMessage(new MatchingMessage(MatchingMessageType.OFFER)));
    MatchingController matchingController =
        new MatchingControllerBuilder(mockSender, mockReceiver, () -> 0)
            .setTimeout(Duration.ofSeconds(0))
            .build();

    // マッチングを開始するとMatchingTimeoutExceptionが発生する
    assertThrows(MatchingTimeoutException.class, () -> matchingController.match("local"));
  }
}
