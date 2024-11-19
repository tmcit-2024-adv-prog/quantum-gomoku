package jp.ac.metro_cit.adv_prog_2024.gomoku.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCPで通信を行うためのクラス
 *
 * @author A Kokubo
 */
public class TCPSocket implements Sender, Receiver {

  private final TCPSocketProps props;
  private ServerSocket serverSocket = null;
  private Socket socket = null;
  private ObjectOutputStream oos = null;
  private ObjectInputStream ois= null;

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

    // Socketが開いている間は通信の待受をし続ける
    while (!socket.isClosed()) {
      Socket socket = this.serverSocket.accept();
      // Objectの受け渡しを行うため、ObjectInput/OutputStreamに変換してグローバル変数に代入
      ois = new ObjectInputStream(socket.getInputStream());
      oos = new ObjectOutputStream(socket.getOutputStream());
    }
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
      this.socket = new Socket(address, port);
      // Objectの受け渡しを行うため、ObjectInput/OutputStreamに変換してグローバル変数に代入
      ois = new ObjectInputStream(this.socket.getInputStream());
      oos = new ObjectOutputStream(this.socket.getOutputStream());
    }
  }

  @Override
  public void startReceive() {
    if (ois == null) {
      // 通信が確立していないタイミングではエラーを出す
      throw new IllegalStateException();
    }
    // スレッドを作成し、別プロセスとしてデータを待ち受ける
    new Thread(() -> {
      try {
        // データを受け取ったら処理を引き渡す
        GameStatus nextStatus = (GameStatus) ois.readObject();
        onReceive(nextStatus);
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }).start();
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
  public void send(GameStatus gameStatus) throws IOException {
    // ソケットがすでに開かれていることを確認
    if (oos == null) {
      throw new IllegalStateException();
    } else {
      // データを送信する
      oos.writeObject(gameStatus);
      oos.flush();
    }
  }

  @Override
  public void onReceive(GameStatus gameStatus) {
    // TODO: 受け取ったデータをいい感じにするアレに投げる
  }
}
