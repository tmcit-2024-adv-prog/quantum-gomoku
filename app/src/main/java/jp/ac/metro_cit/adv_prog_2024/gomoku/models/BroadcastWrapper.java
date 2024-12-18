package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;

/**
 * ブロードキャスト時にデータの返送先を送信するためのラッパー
 *
 * @param replyPort 返送先のポート
 * @param message 送受信するデータ
 */
public record BroadcastWrapper(int replyPort, GameMessage message) implements Serializable {}
