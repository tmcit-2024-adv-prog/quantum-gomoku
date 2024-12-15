package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import java.time.Duration;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;

public class MatchingControllerBuilder {

  private final Player localPlayer;
  private final Sender sender;
  private final Receiver receiver;
  private Duration retryInterval = Duration.ofSeconds(1);
  private int retryCount = 10;
  @Nullable private Duration timeout = null;

  public MatchingControllerBuilder(Player localPlayer, Sender sender, Receiver receiver) {
    this.localPlayer = localPlayer;
    this.sender = sender;
    this.receiver = receiver;
  }

  public MatchingControllerBuilder setRetryInterval(Duration retryInterval) {
    this.retryInterval = retryInterval;

    return this;
  }

  public MatchingControllerBuilder setRetryCount(int retryCount) {
    this.retryCount = retryCount;

    return this;
  }

  public MatchingControllerBuilder setTimeout(@Nonnull Duration timeout) {
    this.timeout = timeout;

    return this;
  }

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
