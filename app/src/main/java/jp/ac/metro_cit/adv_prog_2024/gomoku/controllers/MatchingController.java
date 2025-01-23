package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import javax.lang.model.type.NullType;

import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingFailedException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.exceptions.MatchingTimeoutException;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.GameMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessage;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.MatchingMessageType;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.utils.Pair;

public class MatchingController {
  private final Sender sender;
  private final Receiver receiver;
  private final Supplier<Integer> getRandomInt;
  private final Duration retryInterval;
  private final int retryLimit;
  private final Duration timeout;

  /**
   * コンストラクタ
   *
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   * @param retryInterval Discoverメッセージの再送間隔 デフォルトは1秒
   * @param retryLimit Discoverメッセージの再送回数 デフォルトは10回
   * @param timeout マッチングのタイムアウト時間 デフォルトはretryInterval * retryLimit
   */
  protected MatchingController(
      Sender sender,
      Receiver receiver,
      Supplier<Integer> getRandomInt,
      Duration retryInterval,
      int retryLimit,
      Duration timeout) {
    this.sender = sender;
    this.receiver = receiver;
    this.getRandomInt = getRandomInt;
    this.retryInterval = retryInterval;
    this.retryLimit = retryLimit;
    this.timeout = timeout;
  }

  /**
   * マッチングする
   *
   * @param localPlayerName ローカルプレイヤーの名前
   * @return マッチングに成功した場合は自身と相手プレイヤーを返す <localPlayer, remotePlayer>
   * @throws MatchingTimeoutException マッチングがタイムアウトした場合
   * @throws MatchingFailedException マッチングに失敗した場合
   */
  public Pair<Player, Player> match(String localPlayerName)
      throws MatchingTimeoutException, MatchingFailedException {
    Player localPlayer = new Player(localPlayerName);
    // マッチングメッセージを受信して相手プレイヤーを取得
    CompletableFuture<Player> receiveFuture =
        CompletableFuture.supplyAsync(
            () -> {
              Player remotePlayer = null;
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
                Serializable receivedData = receivedMatchingMsg.data();
                System.out.println(receivedMatchingMsg);
                switch (receivedMatchingMsg.type()) {
                  case DISCOVER:
                    // Discoverメッセージを受信した場合はOfferメッセージを返す
                    // データがnull、またはIntegerでない場合は無視
                    if (phase == MatchingMessageType.DISCOVER
                        && receivedData != null
                        && receivedData instanceof Integer) {
                      // 相手から受信した乱数値と自身の乱数値を比較して色を決定
                      localPlayer.setColor(
                          this.getRandomInt.get() > (Integer) receivedData
                              ? StoneColor.BLACK
                              : StoneColor.WHITE);
                      sendMsg = new MatchingMessage(MatchingMessageType.OFFER, localPlayer);
                      break;
                    }
                    continue;

                  case OFFER:
                    // Offerメッセージを受信した場合はRequestメッセージを返す
                    // 相手プレイヤーがnull、または自分自身の場合は無視
                    if (phase == MatchingMessageType.DISCOVER
                        && receivedData != null
                        && receivedData instanceof Player
                        && !receivedData.equals(localPlayer)) {
                      Player receivedPlayer = (Player) receivedData;
                      System.out.println(receivedPlayer);
                      if (receivedPlayer.getColor() == null) {
                        continue;
                      }
                      remotePlayer = receivedPlayer;
                      localPlayer.setColor(receivedPlayer.getColor().opposite());
                      sendMsg = new MatchingMessage(MatchingMessageType.REQUEST, localPlayer);
                      break;
                    }
                    continue;

                  case REQUEST:
                    // Requestメッセージを受信した場合はAckメッセージを返す
                    // 相手プレイヤーがnull、または自分自身の場合は無視
                    if (phase == MatchingMessageType.OFFER
                        && receivedData != null
                        && receivedData instanceof Player
                        && !receivedData.equals(localPlayer)) {
                      Player receivedPlayer = (Player) receivedData;
                      if (receivedPlayer.getColor() == null
                          || receivedPlayer.getColor() == localPlayer.getColor()) {
                        continue;
                      }
                      remotePlayer = receivedPlayer;
                      sendMsg = new MatchingMessage(MatchingMessageType.ACK);
                      break;
                    }
                    continue;

                  case ACK:
                    // Ackメッセージを受信した場合は相手プレイヤーを返す
                    if (phase == MatchingMessageType.REQUEST) {
                      return remotePlayer;
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
                    break;

                  case ACK:
                    // Ackメッセージを送信した場合はRequestメッセージを受信した相手プレイヤーを返す
                    return remotePlayer;

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
                      new GameMessage(
                          new MatchingMessage(
                              MatchingMessageType.DISCOVER, this.getRandomInt.get())));
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
      Player remotePlayer = receiveFuture.get();
      return Pair.of(localPlayer, remotePlayer);
    } catch (Exception e) {
      e.printStackTrace();
      throw new MatchingFailedException();
    }
  }
}
