package jp.ac.metro_cit.adv_prog_2024.gomoku.services;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Board;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.BoardFactory;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;

/** 試合のファクトリクラス */
public class GameFactory {
  /**
   * 試合を生成する
   *
   * <p>盤は{@link BoardFactory#createBoard()}で生成される
   *
   * @param blackPlayer 黒のプレイヤー
   * @param whitePlayer 白のプレイヤー
   * @return 生成された試合
   */
  public static Game create(Player blackPlayer, Player whitePlayer) {
    return create(blackPlayer, whitePlayer, BoardFactory.create());
  }

  /**
   * 試合を生成する
   *
   * @param blackPlayer 黒のプレイヤー
   * @param whitePlayer 白のプレイヤー
   * @param board 盤インスタンス
   * @return 生成された試合
   */
  public static Game create(Player blackPlayer, Player whitePlayer, Board board) {
    return new Game(blackPlayer, whitePlayer, board);
  }
}
