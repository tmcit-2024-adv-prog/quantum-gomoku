package jp.ac.metro_cit.adv_prog_2024.gomoku;

import jp.ac.metro_cit.adv_prog_2024.gomoku.communications.TCPSocket;
import jp.ac.metro_cit.adv_prog_2024.gomoku.communications.TCPSocketProps;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 通信周りのテストケース
 *
 * @author A. Kokubo
 */
public class CommunicationTest {

  /** レシーバー側のアドレスがnullの場合にエラーが発生することを確認する */
  @Test
  void initReceiverWithNullAddress() {
    TCPSocketProps props = new TCPSocketProps(null, 5000);
    TCPSocket socket = new TCPSocket(props);

    // レシーバー側の初期化時にアドレスがnullを指定した場合にはIllegalArgumentExceptionが発生することを確認
    Assertions.assertThrows(IllegalArgumentException.class, socket::initReceiver);
    // 通信を確立する前にstartReceiveを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(IllegalStateException.class, socket::startReceive);
    // 通信を確立する前にsendを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> {
          socket.send(new GameState("data"));
        });
  }

  /** センダー側で正しくタイムアウトが発生することと接続前にデータの受信を開始した際にエラーが発生することを確認する */
  @Test
  void initSender() {
    TCPSocketProps props = new TCPSocketProps(null, 5001);
    TCPSocket socket = new TCPSocket(props);
    // 通信の待受時にエラーが発生しないことを確認
    Assertions.assertDoesNotThrow(socket::initSender);
    // 通信を確立する前にsendを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> {
          socket.send(new GameState("data"));
        });
  }

  /** レシーバー側で接続前にデータの受信を開始した際にエラーが発生することを確認する */
  @Test
  void initReceiver() {
    TCPSocketProps props = new TCPSocketProps("127.0.0.1", 5002);
    TCPSocket socket = new TCPSocket(props);
    // 通信の開始時にエラーが発生しないことを確認する
    Assertions.assertDoesNotThrow(socket::initReceiver);
    // 通信が確立していない場合にstartReceiveを実行するとIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(IllegalStateException.class, socket::startReceive);
    // 通信を確立する前にsendを呼び出した場合にIllegalStateExceptionが発生することを確認
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> {
          socket.send(new GameState("data"));
        });
  }

  /** センダー・レシーバー間でデータのやり取りが行えることを確認する */
  @Test
  void initCommunication() {
    TCPSocketProps senderProps = new TCPSocketProps(null, 5003);
    TCPSocket sender = new TCPSocket(senderProps);
    Assertions.assertDoesNotThrow(sender::initSender);

    TCPSocketProps receiverProps = new TCPSocketProps("127.0.0.1", 5003);
    TCPSocket receiver = new TCPSocket(receiverProps);
    System.out.println("Init Receiver");
    Assertions.assertDoesNotThrow(receiver::initReceiver);
    System.out.println("Waiting for connection");
    Assertions.assertDoesNotThrow(
        () -> {
          Thread.sleep(3000);
        });
    System.out.println("Start Receive");
    Assertions.assertDoesNotThrow(receiver::startReceive);
    Assertions.assertDoesNotThrow(sender::startReceive);

    System.out.println("Check sender -> receiver");
    Assertions.assertDoesNotThrow(
        () -> {
          sender.send(new GameState("Hello"));
        });

    System.out.println("Waiting for receive");
    Assertions.assertDoesNotThrow(
        () -> {
          Thread.sleep(1000);
        });

    Assertions.assertEquals("Hello", receiver.getLatestStatus().data());

    System.out.println("Check receiver -> sender");
    Assertions.assertDoesNotThrow(
        () -> {
          receiver.send(new GameState("Hello"));
        });

    System.out.println("Waiting for receive");
    Assertions.assertDoesNotThrow(
        () -> {
          Thread.sleep(1000);
        });

    Assertions.assertEquals("Hello", sender.getLatestStatus().data());

    System.out.println("Disconnect");
    Assertions.assertDoesNotThrow(receiver::disconnect);
  }
}
