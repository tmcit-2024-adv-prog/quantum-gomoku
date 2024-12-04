package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;
import javax.annotation.Nullable;

public record MatchingMessage(MatchingMessageType type, @Nullable Player player)
    implements Serializable {
  /**
   * コンストラクタ
   *
   * @param type メッセージの種類
   * @param player プレイヤー情報
   */
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
