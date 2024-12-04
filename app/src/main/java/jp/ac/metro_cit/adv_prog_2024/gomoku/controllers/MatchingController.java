package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.lang.model.type.NullType;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingFailedException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingTimeoutException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessageType;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;

public class MatchingController {
  private Sender<MatchingMessage> sender;
  private Receiver receiver;
  private Player localPlayer;
  private Duration retryInterval = Duration.ofSeconds(1);
  private int retryLimit = 10;
  private Duration timeout = Duration.ofSeconds(10);

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   */
  public MatchingController(Player localPlayer, Sender<MatchingMessage> sender, Receiver receiver) {
    this.sender = sender;
    this.receiver = receiver;
    this.localPlayer = localPlayer;
  }

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   * @param retryLimit Discoverメッセージの再送回数
   */
  public MatchingController(
      Player localPlayer, Sender<MatchingMessage> sender, Receiver receiver, int retryLimit) {
    this(localPlayer, sender, receiver);
    this.retryLimit = retryLimit;
  }

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   * @param retryInterval Discoverメッセージの再送間隔
   * @param retryLimit Discoverメッセージの再送回数
   */
  public MatchingController(
      Player localPlayer,
      Sender<MatchingMessage> sender,
      Receiver receiver,
      @Nullable Duration retryInterval,
      @Nullable Integer retryLimit) {
    this(localPlayer, sender, receiver);
    this.retryInterval = retryInterval;
    if (retryLimit != null) {
      this.retryLimit = retryLimit.intValue();
    }
  }

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   * @param retryInterval Discoverメッセージの再送間隔
   * @param timeout マッチングのタイムアウト時間
   */
  public MatchingController(
      Player localPlayer,
      Sender<MatchingMessage> sender,
      Receiver receiver,
      Duration retryInterval,
      Duration timeout) {
    this(localPlayer, sender, receiver);
    this.retryInterval = retryInterval;
    this.timeout = timeout;
  }

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   * @param retryLimit Discoverメッセージの再送回数
   * @param timeout マッチングのタイムアウト時間
   */
  public MatchingController(
      Player localPlayer,
      Sender<MatchingMessage> sender,
      Receiver receiver,
      int retryLimit,
      Duration timeout) {
    this(localPlayer, sender, receiver);
    this.retryLimit = retryLimit;
    this.timeout = timeout;
  }

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   * @param retryInterval Discoverメッセージの再送間隔
   * @param retryLimit Discoverメッセージの再送回数
   * @param timeout マッチングのタイムアウト時間
   */
  public MatchingController(
      Player localPlayer,
      Sender<MatchingMessage> sender,
      Receiver receiver,
      Duration retryInterval,
      int retryLimit,
      Duration timeout) {
    this(localPlayer, sender, receiver);
    this.retryInterval = retryInterval;
    this.retryLimit = retryLimit;
    this.timeout = timeout;
  }

  /**
   * マッチングする
   *
   * @return マッチングに成功した場合は相手プレイヤーの情報を返す
   */
  public Player match() throws MatchingTimeoutException, MatchingFailedException {
    // Discoverメッセージを送信して相手プレイヤーを探す
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Player> offerFeature = executor.submit(this::sendDiscover);
    Player remotePlayer;
    try {
      remotePlayer = offerFeature.get();
    } catch (Exception e) {
      executor.shutdown();
      throw new MatchingFailedException();
    }

    // Requestメッセージを送信してマッチングを完了する
    Future<NullType> ackFeature =
        executor.submit(
            () -> {
              this.sendRequest(this.localPlayer);

              return null;
            });
    try {
      ackFeature.get(this.timeout.toMillis(), TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      throw new MatchingFailedException();
    } finally {
      executor.shutdown();
    }

    return remotePlayer;
  }

  /**
   * Discoverメッセージを送信する
   *
   * @return 返ってきたOfferメッセージに含まれる相手プレイヤーの情報
   */
  private Player sendDiscover() throws MatchingTimeoutException, InterruptedException {
    GameMessage<MatchingMessage> message =
        new GameMessage<MatchingMessage>(new MatchingMessage(MatchingMessageType.DISCOVER, null));

    int retryCount = 0;
    while (retryCount < this.retryLimit) {
      try {
        this.sender.send(message);
      } catch (Exception ignored) {
        // 送信に失敗した場合はリトライ
        retryCount += 1;
        Thread.sleep(this.retryInterval);
        continue;
      }

      // Offerメッセージを受信する
      Player remotePlayer;
      while (true) {
        GameMessage<Serializable> msg;
        msg = this.receiver.receive();
        if (msg == null) {
          continue;
        }
        if (!msg.data().getClass().isAssignableFrom(MatchingMessage.class)) {
          continue;
        }
        MatchingMessage matchingMessage = (MatchingMessage) msg.data();
        if (matchingMessage.type() != MatchingMessageType.OFFER
            || matchingMessage.player() == null) {
          continue;
        }

        remotePlayer = matchingMessage.player();

        return remotePlayer;
      }
    }

    throw new MatchingTimeoutException();
  }

  /**
   * Requestメッセージを送信する
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   */
  private void sendRequest(Player localPlayer)
      throws MatchingTimeoutException, InterruptedException {
    GameMessage<MatchingMessage> message =
        new GameMessage<MatchingMessage>(
            new MatchingMessage(MatchingMessageType.REQUEST, localPlayer));

    int retryCount = 0;
    while (retryCount < this.retryLimit) {
      try {
        this.sender.send(message);
      } catch (Exception ignored) {
        // 送信に失敗した場合はリトライ
        retryCount += 1;
        Thread.sleep(this.retryInterval);
        continue;
      }

      // Ackメッセージを受信する
      while (true) {
        GameMessage<Serializable> msg;
        msg = this.receiver.receive();
        if (msg == null) {
          continue;
        }
        if (!msg.data().getClass().isAssignableFrom(MatchingMessage.class)) {
          continue;
        }
        MatchingMessage matchingMessage = (MatchingMessage) msg.data();
        if (matchingMessage.type() != MatchingMessageType.ACK) {
          continue;
        }

        return;
      }
    }

    throw new MatchingTimeoutException();
  }
}
