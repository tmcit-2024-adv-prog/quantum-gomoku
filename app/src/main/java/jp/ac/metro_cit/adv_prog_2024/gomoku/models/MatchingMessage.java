package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;
import javax.annotation.Nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public record MatchingMessage(MatchingMessageType type, @Nullable Player player)
    implements Serializable {
  /**
   * コンストラクタ
   *
   * @param type メッセージの種類
   * @param player プレイヤー情報
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
  public MatchingMessage(MatchingMessageType type, Player player) {
    this.type = type;
    this.player = player;
  }

  /**
   * コンストラクタ
   *
   * @param type メッセージの種類
   */
  public MatchingMessage(MatchingMessageType type) {
    this(type, null);
  }
}
