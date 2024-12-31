package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.time.Duration;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;

/** MatchingControllerのビルダークラス */
public class MatchingControllerBuilder {
  private final Sender sender;
  private final Receiver receiver;
  private final Supplier<Integer> getRandomInt;
  private Duration retryInterval = Duration.ofSeconds(1);
  private int retryCount = 10;
  @Nullable private Duration timeout = null;

  /**
   * コンストラクタ
   *
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public MatchingControllerBuilder(
      Sender sender, Receiver receiver, Supplier<Integer> getRandomInt) {
    this.sender = sender;
    this.receiver = receiver;
    this.getRandomInt = getRandomInt;
  }

  /**
   * マッチングメッセージの再送間隔を設定する
   *
   * @param retryInterval マッチングメッセージの再送間隔
   * @return 設定後のビルダー
   */
  public MatchingControllerBuilder setRetryInterval(Duration retryInterval) {
    this.retryInterval = retryInterval;

    return this;
  }

  /**
   * マッチングメッセージの再送回数を設定する
   *
   * @param retryCount マッチングメッセージの再送回数
   * @return 設定後のビルダー
   */
  public MatchingControllerBuilder setRetryCount(int retryCount) {
    this.retryCount = retryCount;

    return this;
  }

  /**
   * マッチングのタイムアウト時間を設定する
   *
   * @param timeout マッチングのタイムアウト時間
   * @return 設定後のビルダー
   */
  public MatchingControllerBuilder setTimeout(@Nonnull Duration timeout) {
    this.timeout = timeout;

    return this;
  }

  /**
   * ビルダーを元にMatchingControllerを構築する
   *
   * @return 構築したMatchingController
   */
  public MatchingController build() {
    return new MatchingController(
        this.sender,
        this.receiver,
        this.getRandomInt,
        this.retryInterval,
        this.retryCount,
        this.timeout == null ? this.retryInterval.multipliedBy(this.retryCount) : this.timeout);
  }
}
