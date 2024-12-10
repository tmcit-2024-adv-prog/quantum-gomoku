package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingFailedException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingTimeoutException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessageType;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("MatchingControllerのテスト")
public class MatchingControllerTest {
  @Test
  @DisplayName("[正常系] 能動的にマッチングを開始した場合にマッチングが成功する")
  @SuppressWarnings("unchecked")
  public void testSuccessAcriveMatching() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player();
    // 相手からはOfferメッセージとAckメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            new GameMessage<>(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            new GameMessage<>(new MatchingMessage(MatchingMessageType.ACK)));
    Player dummyLocalPlayer = new Player();
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    // マッチングを開始すると
    Player result = matchingController.match();

    // 相手のプレイヤーが返ってくる
    assertEquals(result, dummyRemotePlayer);
  }

  @Test
  @DisplayName("[正常系] 受動的にマッチングを開始した場合にマッチングが成功する")
  @SuppressWarnings("unchecked")
  public void testSuccessPassiveMatching() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player();
    // 相手からはDiscoverメッセージとRequestメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            new GameMessage<>(new MatchingMessage(MatchingMessageType.DISCOVER)),
            new GameMessage<>(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    Player dummyLocalPlayer = new Player();
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    // マッチングを開始すると
    Player result = matchingController.match();

    // 相手のプレイヤーが返ってくる
    assertEquals(result, dummyRemotePlayer);
  }

  @Test
  @DisplayName("[異常系] タイムアウト時間を過ぎた場合、MatchingTimeoutExceptionが発生する")
  @SuppressWarnings("unchecked")
  public void testThrowMatchingTimeoutExceptionOnTimeout() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyLocalPlayer = new Player();
    when(mockReceiver.receive())
        .thenReturn(new GameMessage<>(new MatchingMessage(MatchingMessageType.DISCOVER)));
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.empty(),
            Optional.of(Duration.ofNanos(0)));

    // マッチングを開始したときに、タイムアウト時間を過ぎるとMatchingTimeoutExceptionが発生する
    assertThrows(MatchingTimeoutException.class, () -> matchingController.match());
  }

  @Test
  @DisplayName("[異常系] SenderがIOExceptionをスローした場合、MatchingFailedExceptionが発生する")
  @SuppressWarnings("unchecked")
  public void testThrowMatchingFailedExceptionOnIOException() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyLocalPlayer = new Player();
    when(mockReceiver.receive())
        .thenReturn(new GameMessage<>(new MatchingMessage(MatchingMessageType.DISCOVER)));
    // SenderがIOExceptionをスローするとき
    doAnswer(
            invocation -> {
              throw new IOException();
            })
        .when(mockSender)
        .send(any(GameMessage.class));
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.of(1),
            Optional.empty());

    // マッチングを開始したときに、SenderがIOExceptionをスローするとMatchingFailedExceptionが発生する
    assertThrows(MatchingFailedException.class, () -> matchingController.match());
    verify(mockSender, atLeast(1)).send(any(GameMessage.class));
  }

  @Test
  @DisplayName("[異常系] ReceiverがInterruptedExceptionをスローした場合、MatchingFailedExceptionが発生する")
  @SuppressWarnings("unchecked")
  public void testThrowMatchingFailedExceptionOnInterruptedException() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyLocalPlayer = new Player();
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.of(1),
            Optional.empty());
    // ReceiverがInterruptedExceptionをスローするとき
    when(mockReceiver.receive()).thenThrow(new InterruptedException());

    // マッチングを開始したときに、ReceiverがInterruptedExceptionをretryLimitの回数だけスローするとMatchingFailedExceptionが発生する
    assertThrows(MatchingFailedException.class, () -> matchingController.match());
    verify(mockReceiver, atLeast(1)).receive();
  }

  @Test
  @DisplayName("[異常系] ReceiverがMatchingMessage以外のメッセージを受信した場合、それは無視される")
  @SuppressWarnings("unchecked")
  public void testIgnoreNonMatchingMessage() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player();
    // 相手からはOfferメッセージとAckメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            new GameMessage<>("non-matching message"),
            new GameMessage<>(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            new GameMessage<>(new MatchingMessage(MatchingMessageType.ACK)));
    Player dummyLocalPlayer = new Player();
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    // マッチングを開始すると
    Player result = matchingController.match();

    // 相手のプレイヤーが返ってくる
    assertEquals(result, dummyRemotePlayer);
  }

  @Test
  @DisplayName("[異常系] 能動的にマッチングを開始した場合に、不正なMatchingMessageが受信された場合、それは無視される")
  @SuppressWarnings("unchecked")
  public void testIgnoreActiveInvalidMatchingMessage() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player();
    Player dummyLocalPlayer = new Player();
    // 相手からはOfferメッセージとAckメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            // [不正] Playerを含まないOfferメッセージ
            new GameMessage<>(new MatchingMessage(MatchingMessageType.OFFER)),
            // [不正] 自身と同じPlayerを含むOfferメッセージ
            new GameMessage<>(new MatchingMessage(MatchingMessageType.OFFER, dummyLocalPlayer)),
            // [正常] 正しいOfferメッセージ
            new GameMessage<>(new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer)),
            new GameMessage<>(new MatchingMessage(MatchingMessageType.ACK)));
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    // マッチングを開始すると
    Player result = matchingController.match();

    // 相手のプレイヤーが返ってくる
    assertEquals(result, dummyRemotePlayer);
  }

  @Test
  @DisplayName("[異常系] 受動的にマッチングを開始した場合に、不正なMatchingMessageが受信された場合、それは無視される")
  @SuppressWarnings("unchecked")
  public void testIgnorePassiveInvalidMatchingMessage() throws Throwable {
    // モックの作成
    Sender<MatchingMessage> mockSender = mock(Sender.class);
    Receiver mockReceiver = mock(Receiver.class);
    Player dummyRemotePlayer = new Player();
    Player dummyLocalPlayer = new Player();
    // 相手からはDiscoverメッセージとRequestメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            new GameMessage<>(new MatchingMessage(MatchingMessageType.DISCOVER)),
            // [不正] RequestメッセージがPlayerを含まない
            new GameMessage<>(new MatchingMessage(MatchingMessageType.REQUEST)),
            // [不正] Requestメッセージが自身と同じPlayerを含む
            new GameMessage<>(new MatchingMessage(MatchingMessageType.REQUEST, dummyLocalPlayer)),
            // [正常] Requestメッセージ
            new GameMessage<>(new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer)));
    MatchingController matchingController =
        new MatchingController(
            dummyLocalPlayer,
            mockSender,
            mockReceiver,
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    // マッチングを開始すると
    Player result = matchingController.match();

    // 相手のプレイヤーが返ってくる
    assertEquals(result, dummyRemotePlayer);
  }
}
