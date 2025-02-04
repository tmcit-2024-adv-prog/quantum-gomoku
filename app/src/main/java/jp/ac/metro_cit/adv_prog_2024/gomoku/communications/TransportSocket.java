package jp.ac.metro_cit.adv_prog_2024.gomoku.communications;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.BroadcastWrapper;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.TransportTarget;

/**
 * トランスポート層のプロトコルで通信を行うためのクラス
 *
 * <p>本クラスを使用する際にはセンダー側(通信を待ち受ける側)およびレシーバー側(接続する側)にそれぞれ以下のように記載します。
 *
 * <p>センダー側
 *
 * <pre>{@code
 *  public void someSenderSideMethod() {
 *     // 接続を最初に受け待つ側の処理
 *     TransportSocket transportSocket = new TransportSocket(new TransportSocketProps(null, 5050));
 *     // ソケットの初期化
 *     transportSocket.initSender();
 *     // 接続相手からの通信を待ち受ける
 *     transportSocket.startReceive();
 * }
 * }</pre>
 *
 * レシーバー側
 *
 * <pre>{@code
 * public void someReceiverSideMethod() {
 *     // 接続を最初に受け待つ側の処理
 *     TransportSocket transportSocket = new TransportSocket(new TransportSocketProps("192.168.100.1", 5050));
 *     // ソケットの初期化
 *     transportSocket.initReceiver();
 *     // 接続相手からの通信を待ち受ける
 *     transportSocket.startReceive();
 * }
 * }</pre>
 *
 * @author A Kokubo
 */
public class TransportSocket implements Sender, Receiver {

  public static final int PACKET_BUFFER_SIZE = 1024;

  private final TransportSocketProps props;
  private ServerSocket serverSocket = null;
  private Socket socket = null;
  private ObjectOutputStream oos = null;
  private ObjectInputStream ois = null;
  private final DatagramSocket serverDatagramSocket;
  private DatagramSocket receiverDatagramSocket = null;
  private final HashMap<GameMessage, TransportTarget> messageCache = new HashMap<>();

  // スレッドセーフなQueueで送られてきたデータを保持する
  private final LinkedBlockingQueue<GameState> gameStates = new LinkedBlockingQueue<>();
  private final LinkedBlockingQueue<GameMessage> messages = new LinkedBlockingQueue<>();

  private String targetAddress = null;
  private int targetPort = 0;

  @SuppressFBWarnings(value = {"CT_CONSTRUCTOR_THROW"})
  public TransportSocket(TransportSocketProps props) {
    this.props = props;

    // ブロードキャスト送信用のUDPソケットを作成
    try {
      this.serverDatagramSocket = new DatagramSocket();
      this.serverDatagramSocket.setBroadcast(true);
    } catch (SocketException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void initSender() throws IOException {
    // 引数から紐付けるアドレスとポートを取得
    String address = props.address();
    int port = props.port();
    int subPort = props.subPort();
    if (address == null) {
      // アドレスがnullの場合は全てのIPアドレスで通信を待ち受ける
      this.serverSocket = new ServerSocket(port);
      this.receiverDatagramSocket = new DatagramSocket(subPort);
    } else {
      // その他の場合は指定されたアドレスで通信を待ち受ける
      this.serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
      this.receiverDatagramSocket = new DatagramSocket(subPort, InetAddress.getByName(address));
    }
    this.receiverDatagramSocket.setBroadcast(true);

    // タイムアウトを30秒に設定
    // 30秒間コネクションがなかった場合はエラーを投げる
    this.serverSocket.setSoTimeout(30000);
    // 別のスレッドで通信の待受を行う
    new Thread(
            () -> {
              try {
                // Socketが開いている間は通信の待受をし続ける
                while (!serverSocket.isClosed()) {
                  Socket socket = this.serverSocket.accept();
                  this.socket = socket;
                  // Objectの受け渡しを行うため、ObjectInput/OutputStreamに変換してグローバル変数に代入
                  oos = new ObjectOutputStream(socket.getOutputStream());
                  oos.flush();
                  ois = new ObjectInputStream(socket.getInputStream());
                }
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
        .start();
  }

  @Override
  public void initReceiver() throws IOException {
    // 引数から紐付けるアドレスとポートを取得
    int port = props.port();
    int subPort = props.subPort();
    // レシーバーの場合は宛先のアドレスが必要なため、nullの場合にエラーを出す
    if (targetAddress == null) {
      throw new IllegalArgumentException();
    } else {
      try {
        if (this.receiverDatagramSocket == null) {
          this.receiverDatagramSocket = new DatagramSocket(subPort);
        }
        this.socket = new Socket(targetAddress, targetPort);
        new Thread(
                () -> {
                  try {
                    // Objectの受け渡しを行うため、ObjectInput/OutputStreamに変換してグローバル変数に代入
                    socket.getInputStream();
                    this.oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.flush();
                    this.ois = new ObjectInputStream(socket.getInputStream());
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                })
            .start();
      } catch (ConnectException e) {
        messages.add(new GameMessage(("Connection refused")));
      }
    }
  }

  /**
   * 通信相手のアドレスとポートを設定する
   *
   * @param address 通信相手のアドレス
   * @param port 通信相手のポート
   */
  public void setTargetAddress(String address, int port) {
    this.targetAddress = address;
    this.targetPort = port;
  }

  public void startReceiveBroadcast() {
    // UDPの通信(ブロードキャスト)を待ち受ける
    new Thread(
            () -> {
              byte[] buffer = new byte[PACKET_BUFFER_SIZE];
              DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
              try {
                while (!this.receiverDatagramSocket.isClosed()) {
                  this.receiverDatagramSocket.receive(packet);
                  ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
                  ObjectInputStream ois = new ObjectInputStream(bis);
                  Serializable next = (Serializable) ois.readObject();
                  if (next
                      instanceof
                      BroadcastWrapper(int sourcePort, int replyPort, GameMessage message)) {
                    if (sourcePort == props.targetPort()
                        && packet
                            .getAddress()
                            .getHostAddress()
                            .equals(InetAddress.getLocalHost().getHostAddress())) {
                      continue;
                    }
                    messageCache.put(message, new TransportTarget(packet.getAddress(), replyPort));
                    messages.add(message);
                  } else if (next instanceof GameMessage nextGameMessage) {
                    nextGameMessage.setSource(socket.getInetAddress().getHostAddress());
                    nextGameMessage.setSourcePort(socket.getPort());
                    nextGameMessage.setTarget(socket.getLocalAddress().getHostAddress());
                    nextGameMessage.setTargetPort(socket.getLocalPort());
                    messages.add(nextGameMessage);
                  }
                }
              } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
              }
            })
        .start();
  }

  @Override
  public void startReceive() {
    if (ois == null) {
      // 通信が確立していないタイミングではエラーを出す
      throw new IllegalStateException();
    }
    // スレッドを作成し、別プロセスとしてデータを待ち受ける
    new Thread(
            () -> {
              try {
                while (socket != null && !socket.isClosed()) {
                  // データを受け取ったら処理を引き渡す
                  Serializable next = (Serializable) ois.readObject();
                  if (next instanceof GameState nextState) {
                    gameStates.add(nextState);
                  } else if (next instanceof GameMessage nextMessage) {
                    nextMessage.setSource(socket.getInetAddress().getHostAddress());
                    nextMessage.setSourcePort(socket.getPort());
                    nextMessage.setTarget(socket.getLocalAddress().getHostAddress());
                    nextMessage.setTargetPort(socket.getLocalPort());
                    messages.add(nextMessage);
                  }
                }
              } catch (EOFException e) {
                // 相手からのデータが読めなくなった際はClose扱いにする
                messages.add(new GameMessage(("Closed")));
              } catch (IOException | ClassNotFoundException e) {
                if (socket.isClosed()) {
                  messages.add(new GameMessage(("Closed")));
                } else {
                  throw new RuntimeException(e);
                }
              }
            })
        .start();
  }

  @Override
  public void disconnect() throws IOException {
    // ServerSocketが開いている場合はcloseする
    if (serverSocket != null && !serverSocket.isClosed()) {
      serverSocket.close();
    }
    // Socketが開いている場合はcloseする
    if (socket != null && !socket.isClosed()) {
      socket.close();
    }
    // DatagramSocketが開いている場合はcloseする
    if (receiverDatagramSocket != null && !receiverDatagramSocket.isClosed()) {
      receiverDatagramSocket.close();
    }
    if (serverDatagramSocket != null && !serverDatagramSocket.isClosed()) {
      serverDatagramSocket.close();
    }
  }

  @Override
  public void send(GameState gameState) throws IOException {
    // ソケットがすでに開かれていることを確認
    if (oos == null) {
      throw new IllegalStateException();
    } else {
      // データを送信する
      oos.writeObject(gameState);
      oos.flush();
    }
  }

  @Override
  public void send(GameMessage message) throws IOException {
    // ソケットがすでに開かれていることを確認
    if (oos == null) {
      throw new IllegalStateException();
    } else {
      // データを送信する
      oos.writeObject(message);
      oos.flush();
    }
  }

  @Override
  public void broadcast(GameMessage message) throws IOException {
    if (serverDatagramSocket == null) {
      throw new IllegalStateException();
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    BroadcastWrapper wrapper =
        new BroadcastWrapper(props.targetPort(), this.props.subPort(), message);
    out.writeObject(wrapper);
    DatagramPacket datagramPacket =
        new DatagramPacket(
            bos.toByteArray(),
            bos.size(),
            InetAddress.getByName("255.255.255.255"),
            props.targetPort());
    serverDatagramSocket.send(datagramPacket);
  }

  @Override
  public void reply(GameMessage receivedMessage, GameMessage replyMessage) throws IOException {
    if (receivedMessage == null) {
      throw new IllegalArgumentException();
    }
    if (replyMessage == null) {
      throw new IllegalArgumentException();
    }
    if (serverDatagramSocket == null) {
      throw new IllegalStateException();
    }
    // キャッシュから送信先の情報を取得
    TransportTarget transportTarget = messageCache.get(receivedMessage);
    if (transportTarget == null) {
      throw new IllegalStateException();
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    BroadcastWrapper wrapper =
        new BroadcastWrapper(this.props.targetPort(), this.props.subPort(), replyMessage);
    out.writeObject(wrapper);
    DatagramPacket datagramPacket =
        new DatagramPacket(
            bos.toByteArray(), bos.size(), transportTarget.address(), transportTarget.port());
    serverDatagramSocket.send(datagramPacket);
  }

  @Override
  public GameMessage receive() throws InterruptedException {
    // messagesのQueueから先頭の要素を取得し削除
    return messages.take();
  }

  @Override
  public GameMessage receive(long timeout) throws InterruptedException {
    return messages.poll(timeout, TimeUnit.MILLISECONDS);
  }

  @Override
  public GameState receiveState() throws InterruptedException {
    // gameStatesのQueueから先頭の要素を取得し削除
    return gameStates.take();
  }
}
