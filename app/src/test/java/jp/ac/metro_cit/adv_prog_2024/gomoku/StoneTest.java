package jp.ac.metro_cit.adv_prog_2024.gomoku;

import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Stone;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.StoneColor;
import jp.ac.metro_cit.adv_prog_2024.gomoku.models.Vector2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoneTest {
  @Test
  @DisplayName("[正常系] stoneオブジェクトの初期状態が与えられた情報の通りに設定される")
  void testConstructedStoneInitialValue() {
    // Stoneに関する構成情報の作成
    StoneColor testColor = StoneColor.BLACK;
    Vector2D testPosition = new Vector2D(3, 4);
    Stone testStone = new Stone(testColor, testPosition);

    // 初期状態でcolorとpositionが正しく設定されているか確認
    assertEquals(testColor, testStone.getColor(), "石の色が与えられた通りに設定されていません");
    assertEquals(testPosition, testStone.getPos(), "石の位置が与えられた通りに設定されていません");
  }

  @Test
  @DisplayName("[正常系] 同じ構成情報を持つ2つのstoneオブジェクトを比較したとき、equalsでtrueを返す")
  void testStoneEquals() {
    // 同じ構成情報を持つstoneオブジェクトを作成
    StoneColor testColor = StoneColor.BLACK;
    Vector2D testPosition = new Vector2D(1, 1);
    // 比較対象1
    Stone testStone1 = new Stone(testColor, testPosition);
    // 比較対象2
    Stone testStone2 = new Stone(testColor, testPosition);

    // 同じ色と位置の構成情報を持つため、testStone1とtestStone2の比較においてequalsでtrueとなるべき
    assertTrue(testStone1.equals(testStone2), "同じ色と位置を持つstoneオブジェクトがequalsでtrueとならない");
  }

  @Test
  @DisplayName("[異常系] 異なる色の構成情報を持つ2つのStoneオブジェクトを比較したとき、equalsでfalseを返す")
  void testStoneEqualsDifferentColor() {
    // 色が異なる構成情報を持つstoneオブジェクトを作成
    StoneColor testColor1 = StoneColor.BLACK;
    StoneColor testColor2 = StoneColor.WHITE;
    Vector2D testPosition = new Vector2D(2, 2);
    // 比較対象1
    Stone testStone1 = new Stone(testColor1, testPosition);
    // 比較対象2
    Stone testStone2 = new Stone(testColor2, testPosition);

    // 異なる色の構成情報を持つため、testStone1とtestStone2の比較においてequalsでfalseとなるべき
    assertFalse(testStone1.equals(testStone2), "異なる色を持つstoneオブジェクトがequalsでfalseとならない");
  }

  @Test
  @DisplayName("[異常系] 異なる位置の構成情報を持つ2つのstoneオブジェクトを比較したとき、equalsでfalseを返す")
  void testStoneEqualsDifferentPosition() {
    // 位置が異なる構成情報を持つstoneオブジェクトを作成
    StoneColor testColor = StoneColor.BLACK;
    Vector2D testPosition1 = new Vector2D(1, 1);
    Vector2D testPosition2 = new Vector2D(1, 2);
    // 比較対象1
    Stone testStone1 = new Stone(testColor, testPosition1);
    // 比較対象2
    Stone testStone2 = new Stone(testColor, testPosition2);

    // 異なる位置の構成情報を持つため、testStone1とtestStone2の比較においてequalsでfalseとなるべき
    assertFalse(testStone1.equals(testStone2), "異なる位置を持つstoneオブジェクトがequalsでfalseとならない");
  }

  @Test
  @DisplayName("[異常系] stoneオブジェクトが異なる型のオブジェクトと比較される場合、equalsでfalseを返す")
  void testStoneEqualsDifferentObjectType() {
    // 異なる型とStoneオブジェクトを比較
    StoneColor testColor = StoneColor.BLACK;
    Vector2D testPosition = new Vector2D(1, 1);
    Stone testStone = new Stone(testColor, testPosition);

    // 比較対象のStringオブジェクト
    String notStoneObject = "Not a Stone";

    // StoneオブジェクトとStringオブジェクトの比較においてequalsでfalseとなるべき
    assertNotEquals(testStone, notStoneObject, "stoneオブジェクトと異なる型(String)がequalsでfalseとならない");
  }

  @Test
  @DisplayName("[正常系] stoneオブジェクトがhashCodeでハッシュを計算する")
  void testStoneHashCode() {
    // 同じ構成情報を持つstoneオブジェクトを作成
    StoneColor testColor = StoneColor.BLACK;
    Vector2D testPosition = new Vector2D(1, 1);
    // 比較対象1
    Stone testStone1 = new Stone(testColor, testPosition);
    // 比較対象2
    Stone testStone2 = new Stone(testColor, testPosition);

    // 2つのstoneオブジェクトにおいて同じ構成情報を持つ場合、hashCodeも一致するべき
    assertEquals(
        testStone1.hashCode(),
        testStone2.hashCode(),
        "同じ構成情報を持つ2つのstoneオブジェクトのハッシュコードが一致しないため、ハッシュの計算ができていない");
  }

  @Test
  @DisplayName("[正常系] 異なる構成情報を持つ2つのstoneオブジェクトのhashCodeが異なる")
  void testStoneHashCodeDifferentObjects() {
    // 異なる構成情報を持つstoneオブジェクトを作成
    StoneColor testColor1 = StoneColor.BLACK;
    StoneColor testColor2 = StoneColor.WHITE;
    Vector2D testPosition = new Vector2D(1, 1);
    Stone testStone1 = new Stone(testColor1, testPosition);
    Stone testStone2 = new Stone(testColor2, testPosition);

    // 2つのstoneオブジェクトにおいて構成情報が一致しない(今回は色)場合、hashCodeも一致しないべき
    assertNotEquals(
        testStone1.hashCode(),
        testStone2.hashCode(),
        "2つのstoneオブジェクトについて、それぞれの構成情報が異なるのに対してハッシュコードが一致するため、ハッシュの計算ができていない");
  }
}
