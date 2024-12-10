package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
  private Duration timeout = this.retryInterval.multipliedBy(this.retryLimit);

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   * @param retryInterval Discoverメッセージの再送間隔 デフォルトは1秒
   * @param retryLimit Discoverメッセージの再送回数 デフォルトは10回
   * @param timeout マッチングのタイムアウト時間 デフォルトはretryInterval * retryLimit
   */
  public MatchingController(
      Player localPlayer,
      Sender<MatchingMessage> sender,
      Receiver receiver,
      Optional<Duration> retryInterval,
      Optional<Integer> retryLimit,
      Optional<Duration> timeout) {
    this.localPlayer = localPlayer;
    this.sender = sender;
    this.receiver = receiver;
    retryInterval.ifPresent(value -> this.retryInterval = value);
    retryLimit.ifPresent(value -> this.retryLimit = value);
    timeout.ifPresent(value -> this.timeout = value);
  }

  /**
   * マッチングする
   *
   * @return マッチングに成功した場合は相手プレイヤーの情報を返す
   */
  public Player match() throws MatchingTimeoutException, MatchingFailedException {
    // マッチングメッセージを受信して相手プレイヤーを取得
    CompletableFuture<Player> receiveFuture =
        CompletableFuture.supplyAsync(
            () -> {
              Optional<Player> receivedPlayer = Optional.empty();
              MatchingMessageType phase = MatchingMessageType.DISCOVER;
              int retryCount = 0;
              while (retryCount < this.retryLimit) {
                GameMessage<Serializable> receivedMsg;
                try {
                  receivedMsg = receiver.receive();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                  retryCount += 1;
                  continue;
                }
                MatchingMessage receivedMatchingMsg;
                if (receivedMsg.data() instanceof MatchingMessage) {
                  receivedMatchingMsg = (MatchingMessage) receivedMsg.data();
                } else {
                  continue;
                }

                switch (receivedMatchingMsg.type()) {
                  case DISCOVER:
                    if (phase == MatchingMessageType.DISCOVER) {
                      try {
                        this.sender.send(
                            new GameMessage<>(
                                new MatchingMessage(MatchingMessageType.OFFER, this.localPlayer)));
                      } catch (IOException e) {
                        e.printStackTrace();
                        retryCount += 1;
                        continue;
                      }

                      phase = MatchingMessageType.OFFER;
                    }
                    break;

                  case OFFER:
                    if (phase == MatchingMessageType.DISCOVER
                        && receivedMatchingMsg.player() != null
                        && receivedMatchingMsg.player() != this.localPlayer) {
                      try {
                        this.sender.send(
                            new GameMessage<>(
                                new MatchingMessage(
                                    MatchingMessageType.REQUEST, this.localPlayer)));
                      } catch (IOException e) {
                        e.printStackTrace();
                        retryCount += 1;
                        continue;
                      }
                      receivedPlayer = Optional.of(receivedMatchingMsg.player());

                      phase = MatchingMessageType.REQUEST;
                    }
                    break;

                  case REQUEST:
                    if (phase == MatchingMessageType.OFFER
                        && receivedMatchingMsg.player() != null
                        && receivedMatchingMsg.player() != this.localPlayer) {
                      try {
                        this.sender.send(
                            new GameMessage<>(new MatchingMessage(MatchingMessageType.ACK)));
                      } catch (IOException e) {
                        e.printStackTrace();
                        retryCount += 1;
                        continue;
                      }

                      return receivedMatchingMsg.player();
                    }
                    break;

                  case ACK:
                    if (phase == MatchingMessageType.REQUEST && receivedPlayer.isPresent()) {
                      return receivedPlayer.get();
                    }
                    break;

                  default:
                    break;
                }
              }

              throw new RuntimeException(new MatchingFailedException());
            });
    CompletableFuture<NullType> sendFuture =
        CompletableFuture.supplyAsync(
            () -> {
              int retryCount = 0;
              while (retryCount < this.retryLimit) {
                try {
                  this.sender.send(
                      new GameMessage<>(new MatchingMessage(MatchingMessageType.DISCOVER)));
                } catch (IOException e) {
                  e.printStackTrace();
                }
                try {
                  Thread.sleep(this.retryInterval.toMillis());
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                retryCount++;
              }

              throw new RuntimeException(new MatchingTimeoutException());
            });
    try {
      CompletableFuture.anyOf(receiveFuture, sendFuture)
          .orTimeout(this.timeout.toMillis(), TimeUnit.MILLISECONDS)
          .join();
    } catch (CancellationException e) {
      throw new MatchingFailedException();
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof TimeoutException) {
        throw new MatchingTimeoutException();
      } else {
        e.printStackTrace();
        throw new MatchingFailedException();
      }
    }
    // タイムアウト処理
    try {
      Object result = receiveFuture.get();
      if (result instanceof Player) {
        return (Player) result;
      } else {
        throw new MatchingFailedException();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new MatchingFailedException();
    }
  }
}
