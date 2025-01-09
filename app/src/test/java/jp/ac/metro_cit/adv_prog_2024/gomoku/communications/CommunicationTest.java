package jp.ac.metro_cit.adv_prog_2024.gomoku.communications;

import java.util.HashMap;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GamePhase;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 通信周りのテストケース
 *
 * @author A. Kokubo
 */
public class CommunicationTest {

  /** レシーバー側のアドレスがnullの場合にエラーが発生することを確認する */
  @Test
  @DisplayName("[異常系] レシーバー側のアドレスにnullを指定された際にエラーが発生")
  void initReceiverWithNullAddress() {
    TransportSocketProps props = new TransportSocketProps(null, 5000, 5000, 5000);
    TransportSocket socket = new TransportSocket(props);

    // レシーバー側の初期化時にアドレスがnullを指定した場合にはIllegalArgumentExceptionが発生することを確認
    Assertions.assertThrows(IllegalArgumentException.class, socket::initReceiver);
    // 通信を確立する前にstartReceiveを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(IllegalStateException.class, socket::startReceive);
    // 通信を確立する前にsendを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(
        IllegalStateException.class, () -> socket.send(new GameMessage("data")));
  }

  /** 通信の確立前にデータの受信を開始した場合に{@link IllegalStateException}が発生することを確認する */
  @Test
  @DisplayName("[異常系] 通信を確立する前にデータの受信を開始した場合にエラーが発生")
  void callStartReceiveBeforeConnect() {
    TransportSocketProps props = new TransportSocketProps(null, 5001, 5001, 5001);
    TransportSocket socket = new TransportSocket(props);

    // 通信を確立する前にstartReceiveを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(IllegalStateException.class, socket::startReceive);
  }

  /** 通信の確立前にデータを送信した場合に{@link IllegalStateException}が発生することを確認する */
  @Test
  @DisplayName("[異常系] 通信を確立する前にデータを送信した際にエラーが発生")
  void callSendBeforeConnect() {
    TransportSocketProps props = new TransportSocketProps(null, 5002, 5002, 5002);
    TransportSocket socket = new TransportSocket(props);

    // 通信を確立する前にsendを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(
        IllegalStateException.class, () -> socket.send(new GameMessage("data")));
  }

  /** センダー側で正しくタイムアウトが発生することと接続前にデータの受信を開始した際に{@link IllegalStateException}が発生することを確認する */
  @Test
  @DisplayName("[正常系] センダー側の初期化が正常に行えることを検証")
  void initSender() {
    TransportSocketProps props = new TransportSocketProps(null, 5003, 5003, 5003);
    TransportSocket socket = new TransportSocket(props);
    // 通信の待受時にエラーが発生しないことを確認
    Assertions.assertDoesNotThrow(socket::initSender);
    // 通信を確立する前にsendを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(
        IllegalStateException.class, () -> socket.send(new GameMessage("data")));
  }

  /** レシーバー側で接続前にデータの受信を開始した際にエラーが発生することを確認する */
  @Test
  @DisplayName("[正常系] レシーバー側の初期化が正常に行えることを検証")
  void initReceiver() {
    TransportSocketProps props = new TransportSocketProps("127.0.0.1", 5004, 5004, 5004);
    TransportSocket socket = new TransportSocket(props);
    // 通信の開始時にエラーが発生しないことを確認する
    Assertions.assertDoesNotThrow(socket::initReceiver);
    // 通信が確立していない場合にstartReceiveを実行するとIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(IllegalStateException.class, socket::startReceive);
    // 通信を確立する前にsendを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(
        IllegalStateException.class, () -> socket.send(new GameMessage("data")));
  }

  /** センダー・レシーバー間でデータのやり取りが行えることを確認する */
  @Test
  @DisplayName("[正常系] 通信の確立後に通信が行えることを検証")
  void initCommunication() throws InterruptedException {
    TransportSocketProps senderProps = new TransportSocketProps(null, 5005, 5005, 5015);
    TransportSocket sender = new TransportSocket(senderProps);
    Assertions.assertDoesNotThrow(sender::initSender);

    TransportSocketProps receiverProps = new TransportSocketProps("127.0.0.1", 5005, 5015, 5005);
    TransportSocket receiver = new TransportSocket(receiverProps);
    System.out.println("Init Receiver");
    Assertions.assertDoesNotThrow(receiver::initReceiver);
    System.out.println("Waiting for connection");
    Assertions.assertDoesNotThrow(() -> Thread.sleep(3000));

    System.out.println("Start Receive");
    Assertions.assertDoesNotThrow(receiver::startReceive);
    Assertions.assertDoesNotThrow(sender::startReceive);

    GameState dummyState =
        new GameState(
            GamePhase.BEFORE_START, new Player("Alice"), new Player("Bob"), null, new HashMap<>());
    System.out.println("Check sender -> receiver");
    Assertions.assertDoesNotThrow(() -> sender.send(dummyState));
    Assertions.assertDoesNotThrow(() -> sender.send(new GameMessage("Hello")));

    System.out.println("Waiting for receive");
    Assertions.assertDoesNotThrow(() -> Thread.sleep(1000));

    Assertions.assertEquals(dummyState, receiver.receiveState());
    Assertions.assertEquals("Hello", receiver.receive().data());
    Assertions.assertNull((receiver.receive(3000)));

    System.out.println("Check receiver -> sender");
    Assertions.assertDoesNotThrow(() -> receiver.send(dummyState));
    Assertions.assertDoesNotThrow(() -> receiver.send(new GameMessage("Hello")));

    System.out.println("Waiting for receive");
    Assertions.assertDoesNotThrow(() -> Thread.sleep(1000));

    Assertions.assertEquals(dummyState, sender.receiveState());
    Assertions.assertEquals("Hello", sender.receive().data());
    Assertions.assertNull((sender.receive(3000)));

    System.out.println("Disconnect");
    Assertions.assertDoesNotThrow(receiver::disconnect);
  }

  /** ブロードキャストが行えることを検証する */
  @Test
  @DisplayName("[正常系] ブロードキャストを用いたデータの送受信が行えることを確認")
  void initBroadcast() throws InterruptedException {
    TransportSocketProps senderProps = new TransportSocketProps(null, 5006, 5008, 5007);
    TransportSocket sender = new TransportSocket(senderProps);
    Assertions.assertDoesNotThrow(sender::initSender);

    TransportSocketProps receiverProps = new TransportSocketProps("127.0.0.1", 5006, 5007, 5007);
    TransportSocket receiver = new TransportSocket(receiverProps);
    System.out.println("Init Receiver");
    Assertions.assertDoesNotThrow(receiver::initReceiver);

    System.out.println("Waiting for initializing receiver");
    Assertions.assertDoesNotThrow(() -> Thread.sleep(1000));

    Assertions.assertDoesNotThrow(receiver::startReceive);
    Assertions.assertDoesNotThrow(receiver::startReceiveBroadcast);
    Assertions.assertDoesNotThrow(sender::startReceiveBroadcast);

    System.out.println("Send broadcast message");
    Assertions.assertDoesNotThrow(() -> sender.broadcast(new GameMessage("Broadcasting")));

    System.out.println("Waiting for receive");
    Assertions.assertDoesNotThrow(() -> Thread.sleep(1000));

    Assertions.assertDoesNotThrow(
        () -> {
          GameMessage message = receiver.receive();
          receiver.reply(message, new GameMessage("Reply"));
        });

    System.out.println("Waiting for receive");
    Assertions.assertDoesNotThrow(() -> Thread.sleep(1000));

    Assertions.assertEquals("Reply", sender.receive().data());
  }
}
