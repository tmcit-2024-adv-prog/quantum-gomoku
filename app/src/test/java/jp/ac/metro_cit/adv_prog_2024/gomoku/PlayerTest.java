package jp.ac.metro_cit.adv_prog_2024.gomoku;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Player;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTest {
  @Test
  @DisplayName("[正常系] 初期状態のcolorがnullであり、setColorでBLACKを正しく設定できる")
  void testSetColorIsBlack() {
    Player testplayer = new Player("playername");

    // 初期状態でcolorはnullであることを確認
    assertNull(testplayer.getColor(), "初期状態のcolorはnullである");

    // BLACKを設定
    testplayer.setColor(StoneColor.BLACK);

    // 設定後、getColorがBLACKを返すか確認
    assertEquals(StoneColor.BLACK, testplayer.getColor(), "setColorで設定したBLACKが正しく取得されません");
  }

  @Test
  @DisplayName("[正常系] 初期状態のcolorがnullであり、setColorでWHITEを正しく設定できる")
  void testSetColorIsWhite() {
    // WHITEでも同様にテスト
    Player testplayer2 = new Player("player2name");

    // 初期状態でcolorはnullであることを確認
    assertNull(testplayer2.getColor(), "初期状態のcolorはnullである");

    // WHITEを設定
    testplayer2.setColor(StoneColor.WHITE);

    // 設定後、getColorがWHITEを返すか確認
    assertEquals(StoneColor.WHITE, testplayer2.getColor(), "setColorで設定したWHITEが正しく取得されません");
  }

  @Test
  @DisplayName("[異常系] 既に設定済みのcolorを上書きしようとするとIllegalStateExceptionが発生する")
  void testSetColorIsException() {
    Player testplayer = new Player("playername");

    // まずBLACKを設定
    testplayer.setColor(StoneColor.BLACK);

    // colorがBLACKであることを確認
    assertEquals(StoneColor.BLACK, testplayer.getColor(), "初期設定のcolorがBLACKである");

    // 再度setColorでWHITEを設定しようとする -> IllegalStateExceptionが発生することを確認
    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> testplayer.setColor(StoneColor.WHITE),
            "既に色が設定されている状態でsetColorを呼び出すと、IllegalStateExceptionが発生する");

    // 例外のメッセージが正しいか確認
    assertEquals("既に色が与えられているため、上書きできません。", exception.getMessage(), "例外メッセージが期待値と異なります");
  }
}
