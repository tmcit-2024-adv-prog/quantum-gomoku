package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;

/**
 * コネクションの確率時など、ゲームの状態を持たない際のデータ
 *
 * @author A. Kokubo
 */
public record GameMessage<T extends Serializable>(T data) implements Serializable {}
