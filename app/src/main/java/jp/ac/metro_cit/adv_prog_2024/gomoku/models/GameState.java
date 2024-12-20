package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.Nullable;

/**
 * 試合の状態を表すクラス.
 *
 * @param phase 現在のゲームのフェーズ
 * @param blackPlayer 黒の碁石を置くプレイヤー
 * @param whitePlayer 白の碁石を置くプレイヤー
 * @param winner 勝利したプレイヤー
 * @param board 盤面
 */
public record GameState(
    GamePhase phase,
    Player blackPlayer,
    Player whitePlayer,
    @Nullable Player winner,
    HashMap<Vector2D, Stone> board)
    implements Serializable {}
