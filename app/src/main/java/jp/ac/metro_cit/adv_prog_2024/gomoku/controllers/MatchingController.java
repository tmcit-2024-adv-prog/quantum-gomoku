package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.IOException;
import java.time.Duration;
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

  private final Sender sender;
  private final Receiver receiver;
  private final Player localPlayer;
  private final Duration retryInterval;
  private final int retryLimit;
  private final Duration timeout;

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
  protected MatchingController(
      Player localPlayer,
      Sender sender,
      Receiver receiver,
      Duration retryInterval,
      int retryLimit,
      Duration timeout) {
    this.localPlayer = localPlayer;
    this.sender = sender;
    this.receiver = receiver;
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
    // マッチングメッセージを受信して相手プレイヤーを取得
    CompletableFuture<Player> receiveFuture =
        CompletableFuture.supplyAsync(
            () -> {
              Player receivedPlayer = null;
              MatchingMessageType phase = MatchingMessageType.DISCOVER;
              int retryCount = 0;
              while (retryCount < this.retryLimit) {
                GameMessage receivedMsg;
                try {
                  receivedMsg = receiver.receive();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                  retryCount += 1;
                  continue;
                }

                // メッセージがMatchingMessageでない場合は無視
                MatchingMessage receivedMatchingMsg;
                if (receivedMsg.data() instanceof MatchingMessage) {
                  receivedMatchingMsg = (MatchingMessage) receivedMsg.data();
                } else {
                  continue;
                }

                // メッセージの種類に応じて処理を分岐
                MatchingMessage sendMsg;
                switch (receivedMatchingMsg.type()) {
                  case DISCOVER:
                    // Discoverメッセージを受信した場合はOfferメッセージを返す
                    if (phase == MatchingMessageType.DISCOVER) {
                      sendMsg = new MatchingMessage(MatchingMessageType.OFFER, this.localPlayer);
                      break;
                    }
                    continue;

                  case OFFER:
                    // Offerメッセージを受信した場合はRequestメッセージを返す
                    // 相手プレイヤーがnull、または自分自身の場合は無視
                    if (phase == MatchingMessageType.DISCOVER
                        && receivedMatchingMsg.player() != null
                        && receivedMatchingMsg.player() != this.localPlayer) {
                      sendMsg = new MatchingMessage(MatchingMessageType.REQUEST, this.localPlayer);
                      break;
                    }
                    continue;

                  case REQUEST:
                    // Requestメッセージを受信した場合はAckメッセージを返す
                    // 相手プレイヤーがnull、または自分自身の場合は無視
                    if (phase == MatchingMessageType.OFFER
                        && receivedMatchingMsg.player() != null
                        && receivedMatchingMsg.player() != this.localPlayer) {
                      sendMsg = new MatchingMessage(MatchingMessageType.ACK);
                      break;
                    }
                    continue;

                  case ACK:
                    // Ackメッセージを受信した場合は相手プレイヤーを返す
                    if (phase == MatchingMessageType.REQUEST) {
                      return receivedPlayer;
                    }
                    continue;

                  default:
                    // ここに到達することはない
                    throw new RuntimeException(new MatchingFailedException());
                }

                // メッセージを送信
                try {
                  this.sender.reply(receivedMsg, new GameMessage(sendMsg));
                } catch (IOException e) {
                  e.printStackTrace();
                  retryCount += 1;
                  continue;
                }
                switch (sendMsg.type()) {
                  case OFFER:
                    // Offerメッセージを送信した場合はOFFERフェーズに移行
                    phase = MatchingMessageType.OFFER;
                    break;

                  case REQUEST:
                    // Requestメッセージを送信した場合はREQUESTフェーズに移行
                    phase = MatchingMessageType.REQUEST;
                    receivedPlayer = receivedMatchingMsg.player();
                    break;

                  case ACK:
                    // Ackメッセージを送信した場合はRequestメッセージを受信した相手プレイヤーを返す
                    return receivedMatchingMsg.player();

                  default:
                    // ここに到達することはない
                    throw new RuntimeException(new MatchingFailedException());
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
                  // Discoverメッセージを送信
                  this.sender.broadcast(
                      new GameMessage(new MatchingMessage(MatchingMessageType.DISCOVER)));
                } catch (IOException e) {
                  e.printStackTrace();
                }
                try {
                  // retryIntervalで設定された時間待機
                  Thread.sleep(this.retryInterval.toMillis());
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                retryCount++;
              }

              throw new RuntimeException(new MatchingTimeoutException());
            });

    // タイムアウト処理
    try {
      CompletableFuture.anyOf(receiveFuture, sendFuture)
          .orTimeout(this.timeout.toMillis(), TimeUnit.MILLISECONDS)
          .join();
    } catch (CancellationException e) {
      throw new MatchingFailedException();
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof RuntimeException && cause.getCause() != null) {
        cause = cause.getCause();
      }
      if (cause instanceof TimeoutException) {
        throw new MatchingTimeoutException();
      } else if (cause instanceof MatchingTimeoutException) {
        throw (MatchingTimeoutException) cause;
      } else {
        e.printStackTrace();
        throw new MatchingFailedException();
      }
    }
    try {
      Player result = receiveFuture.get();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new MatchingFailedException();
    }
  }
}
