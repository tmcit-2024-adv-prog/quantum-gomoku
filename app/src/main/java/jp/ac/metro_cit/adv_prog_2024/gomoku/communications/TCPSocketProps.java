package jp.ac.metro_cit.adv_prog_2024.gomoku.communications;

import javax.annotation.Nullable;

import jp.ac.metro_cit.adv_prog_2024.gomoku.interfaces.SocketProps;

/**
 * {@link TCPSocket}用の{@link SocketProps} {@link TCPSocket}を作成する際に必要な引数を定義する
 *
 * @param address ソケットに紐づけるIPアドレス
 * @param port ソケットに紐付けるポート番号
 * @author A. Kokubo
 */
public record TCPSocketProps(@Nullable String address, int port) implements SocketProps {}
