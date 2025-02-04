package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;

/**
 * コネクションの確率時など、ゲームの状態を持たない際のデータ
 *
 * @author A. Kokubo
 */
public class GameMessage implements Serializable {
  private final Serializable data;
  private String source;
  private int sourcePort;
  private String target;
  private int targetPort;

  public GameMessage(Serializable data) {
    this.data = data;
  }

  public GameMessage(Serializable data, String source, int sourcePort, String target, int targetPort) {
    this.data = data;
    this.source = source;
    this.sourcePort = sourcePort;
    this.target = target;
  }

  public int sourcePort() {
    return sourcePort;
  }

  public int targetPort() {
    return targetPort;
  }

  public Serializable data() {
    return data;
  }

  public String source() {
    return source;
  }

  public String target() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setSourcePort(int sourcePort) {
    this.sourcePort = sourcePort;
  }

  public void setTargetPort(int targetPort) {
    this.targetPort = targetPort;
  }
}
