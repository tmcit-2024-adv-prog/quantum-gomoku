package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.Nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * 試合の状態を表すクラス.
 *
 * @param phase 現在のゲームのフェーズ
 * @param localPlayer 自分側プレイヤー
 * @param remotePlayer 相手側プレイヤー
 * @param winner 勝利したプレイヤー
 * @param board 盤面
 */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public record GameState(
    GamePhase phase,
    Player localPlayer,
    Player remotePlayer,
    @Nullable Player winner,
    HashMap<Vector2D, Stone> board)
    implements Serializable {}
