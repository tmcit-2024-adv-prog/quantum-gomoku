package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.time.Duration;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;

/** MatchingControllerのビルダークラス */
public class MatchingControllerBuilder {

  private final Player localPlayer;
  private final Sender sender;
  private final Receiver receiver;
  private Duration retryInterval = Duration.ofSeconds(1);
  private int retryCount = 10;
  @Nullable private Duration timeout = null;

  /**
   * コンストラクタ
   *
   * @param localPlayer このプログラムが実行される端末のプレイヤー
   * @param sender 送信インターフェースを実装したクラス
   * @param receiver 受信インターフェースを実装したクラス
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public MatchingControllerBuilder(Player localPlayer, Sender sender, Receiver receiver) {
    this.localPlayer = localPlayer;
    this.sender = sender;
    this.receiver = receiver;
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
        this.localPlayer,
        this.sender,
        this.receiver,
        this.retryInterval,
        this.retryCount,
        this.timeout == null ? this.retryInterval.multipliedBy(this.retryCount) : this.timeout);
  }
}
