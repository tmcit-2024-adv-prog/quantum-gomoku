package jp.ac.metro_cit.adv_prog_2024.gomoku.communication;

import java.io.Serializable;

/**
 * データ送受信用のオブジェクト(仮)
 *
 * @author A. Kokubo
 */
public record GameStatus(String data) implements Serializable {

}
