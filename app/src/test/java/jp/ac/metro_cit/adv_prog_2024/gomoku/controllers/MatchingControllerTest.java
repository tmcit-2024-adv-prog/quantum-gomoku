package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessageType;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
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
    // 相手からはDiscoverメッセージとRequestメッセージが送信されるとき
    when(mockReceiver.receive())
        .thenReturn(
            ((Supplier<GameMessage<Serializable>>)
                    () -> {
                      MatchingMessage matchingMessage =
                          new MatchingMessage(MatchingMessageType.OFFER, dummyRemotePlayer);
                      GameMessage<Serializable> message =
                          new GameMessage<Serializable>(matchingMessage);

                      return message;
                    })
                .get(),
            ((Supplier<GameMessage<Serializable>>)
                    () -> {
                      MatchingMessage matchingMessage =
                          new MatchingMessage(MatchingMessageType.ACK);
                      GameMessage<Serializable> message =
                          new GameMessage<Serializable>(matchingMessage);

                      return message;
                    })
                .get());
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
            ((Supplier<GameMessage<Serializable>>)
                    () -> {
                      MatchingMessage matchingMessage =
                          new MatchingMessage(MatchingMessageType.DISCOVER);
                      GameMessage<Serializable> message =
                          new GameMessage<Serializable>(matchingMessage);

                      return message;
                    })
                .get(),
            ((Supplier<GameMessage<Serializable>>)
                    () -> {
                      MatchingMessage matchingMessage =
                          new MatchingMessage(MatchingMessageType.REQUEST, dummyRemotePlayer);
                      GameMessage<Serializable> message =
                          new GameMessage<Serializable>(matchingMessage);

                      return message;
                    })
                .get());
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
}
