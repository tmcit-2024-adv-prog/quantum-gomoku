package jp.ac.metro_cit.adv_prog_2024.gomoku.controllers;

import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.IGameCommunicationController;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Receiver;
import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.Sender;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;

/** IGameCommunicationControllerのビルダ */
public class GameCommunicationControllerBuilder {
  private final Sender sender;
  private final Receiver receiver;

  /**
   * コンストラクタ
   *
   * @param sender Senderを実装したクラス
   * @param receiver Receiverを実装したクラス
   */
  public GameCommunicationControllerBuilder(Sender sender, Receiver receiver) {
    this.sender = sender;
    this.receiver = receiver;
  }

  /**
   * GameCommunicationControllerを生成する
   *
   * @param localPlayer ローカルプレイヤー
   * @param remotePlayer リモートプレイヤー
   * @return 生成したIGameCommunicationController
   */
  public IGameCommunicationController build(Player localPlayer, Player remotePlayer) {
    return GameCommunicationControllerFactory.create(localPlayer, remotePlayer, sender, receiver);
  }
}
