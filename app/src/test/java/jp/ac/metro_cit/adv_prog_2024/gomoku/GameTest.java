package jp.ac.metro_cit.adv_prog_2024.gomoku;

import java.awt.Point;

public class GameTest {
  public String GameTest(int dir, Point initPos) {
    // 初期化
    Game game = new Game();
    game.StartGame();
    Point pos = initPos;
    int count = 0;

    // ゲーム開始
    while (count < 20) {
      System.out.println("player: " + game.currentPlayer.stoneColor);
      Stone stone = new Stone(game.currentPlayer.stoneColor);
      stone.pos = pos;
      Boolean bool = game.SetStone(pos, stone);
      if (!bool) {
        return "Error: SetStone";
      }
      if (game.CheckWinner(pos)) {
        return "Winner: " + game.currentPlayer.stoneColor;
      }

      // 設置場所の変更
      if (game.phase == GamePhase.WHITE_TURN) {
        switch (dir) {
          case 0:
            pos.x++;
            pos.y = 0;
            break;
          case 1:
            pos.y++;
            pos.x = 0;
            break;
          case 2:
            pos.x++;
            break;
          case 3:
            pos.x--;
            break;

          default:
            break;
        }
        pos.x--;
      } else {
        switch (dir) {
          case 0:
            pos.y = 1;
            break;
          case 1:
            pos.x = 1;
            break;
          case 2:
            pos.y++;
            break;
          case 3:
            pos.y--;
            break;
        }
      }
      game.NextPhase();
      count++;
    }
    return null;
  }

  public static void main(String[] args) {
    for (int i = 0; i < 4; i++) {
      System.out.println(new App().GameTest(i, new Point(5, 5)));
    }
  }
}
