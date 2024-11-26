package jp.ac.metro_cit.adv_prog_2024.gomoku;

import jp.ac.metro_cit.adv_prog_2024.gomoku.communication.GameStatus;
import jp.ac.metro_cit.adv_prog_2024.gomoku.communication.TCPSocket;
import jp.ac.metro_cit.adv_prog_2024.gomoku.communication.TCPSocketProps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 通信周りのテストケース
 *
 * @author A. Kokubo
 */
public class CommunicationTest {

  @Test
  void initReceiverWithNullAddress() {
    TCPSocketProps props = new TCPSocketProps(null, 5000);
    TCPSocket socket = new TCPSocket(props);
    Assertions.assertThrows(IllegalArgumentException.class, socket::initReceiver);
    Assertions.assertThrows(IllegalStateException.class, socket::startReceive);
    Assertions.assertThrows(IllegalStateException.class, () -> {
      socket.send(new GameStatus("data"));
    });
  }

  @Test
  void initSender() {
    TCPSocketProps props = new TCPSocketProps(null, 5000);
    TCPSocket socket = new TCPSocket(props);
    Assertions.assertThrows(SocketTimeoutException.class, socket::initSender);
    Assertions.assertThrows(IllegalStateException.class, () -> {
      socket.send(new GameStatus("data"));
    });
  }

  @Test
  void initReceiver() {
    TCPSocketProps props = new TCPSocketProps("127.0.0.1", 5000);
    TCPSocket socket = new TCPSocket(props);
    Assertions.assertThrows(ConnectException.class, socket::initReceiver);
    Assertions.assertThrows(IllegalStateException.class, socket::startReceive);
    Assertions.assertThrows(IllegalStateException.class, () -> {
      socket.send(new GameStatus("data"));
    });
  }

  @Test
  void initCommunication() {
    TCPSocketProps senderProps = new TCPSocketProps(null, 5000);
    TCPSocket sender = new TCPSocket(senderProps);
    Assertions.assertDoesNotThrow(sender::initSender);

    TCPSocketProps receiverProps = new TCPSocketProps("127.0.0.1", 5000);
    TCPSocket receiver = new TCPSocket(receiverProps);
    System.out.println("Init Receiver");
    Assertions.assertDoesNotThrow(receiver::initReceiver);
    System.out.println("Waiting for connection");
    Assertions.assertDoesNotThrow(() -> {
      Thread.sleep(3000);
    });
    System.out.println("Start Receive");
    Assertions.assertDoesNotThrow(receiver::startReceive);
    Assertions.assertDoesNotThrow(sender::startReceive);

    Assertions.assertDoesNotThrow(() -> {
      sender.send(new GameStatus("Hello"));
    });

    System.out.println("Waiting for receive");
    Assertions.assertDoesNotThrow(() -> {
      Thread.sleep(1000);
    });

    Assertions.assertEquals("Hello", receiver.getLatestStatus().data());

    System.out.println("Disconnect");
    Assertions.assertDoesNotThrow(receiver::disconnect);
  }

}
