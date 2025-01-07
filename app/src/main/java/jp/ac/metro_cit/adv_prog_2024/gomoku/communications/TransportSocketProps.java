package jp.ac.metro_cit.adv_prog_2024.gomoku.communications;

import javax.annotation.Nullable;

import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.SocketProps;

/**
 * {@link TransportSocket}用の{@link SocketProps} {@link TransportSocket}を作成する際に必要な引数を定義する
 *
 * @param address ソケットに紐づけるIPアドレス
 * @param port ソケットに紐付けるポート番号
 * @param subPort broadcastを受信するためのポート
 * @param targetPort broadcastの送信先のポート
 * @author A. Kokubo
 */
public record TransportSocketProps(@Nullable String address, int port, int subPort, int targetPort)
    implements SocketProps {}
