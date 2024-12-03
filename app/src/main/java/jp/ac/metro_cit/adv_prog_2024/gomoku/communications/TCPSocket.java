package jp.ac.metro_cit.adv_prog_2024.gomoku.communications;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameState;

/**
 * TCPで通信を行うためのクラス
 *
 * <p>本クラスを使用する際にはセンダー側(通信を待ち受ける側)およびレシーバー側(接続する側)にそれぞれ以下のように記載します。
 *
 * <p>センダー側
 *
 * <pre>{@code
 *  public void someSenderSideMethod() {
 *     // 接続を最初に受け待つ側の処理
 *     TCPSocket tcpSocket = new TCPSocket(new TCPSocketProps(null, 5050));
 *     // ソケットの初期化
 *     tcpSocket.initSender();
 *     // 接続相手からの通信を待ち受ける
 *     tcpSocket.startReceive();
 * }
 * }</pre>
 *
 * レシーバー側
 *
 * <pre>{@code
 * public void someReceiverSideMethod() {
 *     // 接続を最初に受け待つ側の処理
 *     TCPSocket tcpSocket = new TCPSocket(new TCPSocketProps("192.168.100.1", 5050));
 *     // ソケットの初期化
 *     tcpSocket.initReceiver();
 *     // 接続相手からの通信を待ち受ける
 *     tcpSocket.startReceive();
 * }
 * }</pre>
 *
 * @author A Kokubo
 */
public class TCPSocket implements Sender, Receiver {

  private final TCPSocketProps props;
  private ServerSocket serverSocket = null;
  private Socket socket = null;
  private ObjectOutputStream oos = null;
  private ObjectInputStream ois = null;
  private GameState latestStatus = null;

  // スレッドセーフなQueueで送られてきたデータを保持する
  private final ConcurrentLinkedQueue<GameState> gameStates = new ConcurrentLinkedQueue<>();
  private final ConcurrentLinkedQueue<GameMessage> messages = new ConcurrentLinkedQueue<>();

  public TCPSocket(TCPSocketProps props) {
    // 渡された引数がTCPSocket用のものであるかを検証
    this.props = props;
  }

  @Override
  public void initSender() throws IOException {
    // 引数から紐付けるアドレスとポートを取得
    String address = props.address();
    int port = props.port();
    if (address == null) {
      // アドレスがnullの場合は全てのIPアドレスで通信を待ち受ける
      this.serverSocket = new ServerSocket(port);
    } else {
      // その他の場合は指定されたアドレスで通信を待ち受ける
      this.serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
    }

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
    String address = props.address();
    int port = props.port();
    // レシーバーの場合は宛先のアドレスが必要なため、nullの場合にエラーを出す
    if (address == null) {
      throw new IllegalArgumentException();
    } else {
      try {
        this.socket = new Socket(address, port);
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
                    latestStatus = nextState;
                    gameStates.add(nextState);
                  } else if (next instanceof GameMessage nextMessage) {
                    messages.add(nextMessage);
                  }
                }
              } catch (EOFException e) {
                // 相手からのデータが読めなくなった際はClose扱いにする
                messages.add(new GameMessage(("Closed")));
              } catch (IOException | ClassNotFoundException e) {
                if ((socket != null && socket.isClosed())) {
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
  public GameMessage receive() {
    // messagesのQueueから先頭の要素を取得し削除
    return messages.poll();
  }

  @Override
  public GameState receiveState() {
    // gameStatesのQueueから先頭の要素を取得し削除
    return gameStates.poll();
  }
}
