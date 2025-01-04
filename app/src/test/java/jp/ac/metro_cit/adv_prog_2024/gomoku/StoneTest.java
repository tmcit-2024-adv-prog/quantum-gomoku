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
  @DisplayName("[正常系] stoneオブジェクトの初期状態が正しく設定される")
  void testConstructedStoneInitialValue() {
    // Stoneに関する構成情報の作成
    StoneColor testcolor = StoneColor.BLACK;
    Vector2D testposition = new Vector2D(3, 4);
    Stone teststone = new Stone(testcolor, testposition);

    // 初期状態でcolorとpositionが正しく設定されているか確認
    assertEquals(testcolor, stone.getColor(), "teststoneの色が正しく設定されていません");
    assertEquals(testposition, stone.getPosition(), "teststoneの位置が正しく設定されていません");
  }

  @Test
  @DisplayName("[正常系] 同じ構成情報を持つ2つのstoneオブジェクトを比較したとき、equalsでtrueを返す")
  void testStoneEquals() {
    // 同じ構成情報を持つstoneオブジェクトを作成
    StoneColor testcolor = StoneColor.BLACK;
    Vector2D testposition = new Vector2D(1, 1);
    // 比較対象1
    Stone teststone1 = new Stone(testcolor, testposition);
    // 比較対象2
    Stone teststone2 = new Stone(testcolor, testposition);

    // 同じ色と位置の構成情報を持つため、teststone1とteststone2の比較においてequalsでtrueとなるべき
    assertTrue(teststone1.equals(teststone2), "同じ色と位置を持つstoneオブジェクトがequalsでtrueとならない");
  }

  @Test
  @DisplayName("[異常系] 異なる色の構成情報を持つ2つのStoneオブジェクトを比較したとき、equalsでfalseを返す")
  void testStoneEqualsDifferentColor() {
    // 色が異なる構成情報を持つstoneオブジェクトを作成
    StoneColor testcolor1 = StoneColor.BLACK;
    StoneColor testcolor2 = StoneColor.WHITE;
    Vector2D testposition = new Vector2D(2, 2);
    // 比較対象1
    Stone teststone1 = new Stone(testcolor1, testposition);
    // 比較対象2
    Stone teststone2 = new Stone(testcolor2, testposition);

    // 異なる色の構成情報を持つため、teststone1とteststone2の比較においてequalsでfalseとなるべき
    assertFalse(teststone1.equals(teststone2), "異なる色を持つstoneオブジェクトがequalsでfalseとならない");
  }

  @Test
  @DisplayName("[異常系] 異なる位置の構成情報を持つ2つのStoneオブジェクトを比較したとき、equalsでfalseを返す")
  void testStoneEqualsDifferentPosition() {
    // 位置が異なる構成情報を持つstoneオブジェクトを作成
    StoneColor testcolor = StoneColor.BLACK;
    Vector2D testposition1 = new Vector2D(1, 1);
    Vector2D testposition2 = new Vector2D(1, 2);
    // 比較対象1
    Stone teststone1 = new Stone(testcolor, testposition1);
    // 比較対象2
    Stone teststone2 = new Stone(testcolor, testposition2);

    // 異なる位置の構成情報を持つため、teststone1とteststone2の比較においてequalsでfalseとなるべき
    assertFalse(teststone1.equals(teststone2), "異なる位置を持つstoneオブジェクトがequalsでfalseとならない");
  }

  @Test
  @DisplayName("[異常系] stoneオブジェクトが異なる型のオブジェクトと比較される場合、equalsでfalseを返す")
  void testStoneEqualsDifferentObjectType() {
    // 異なる型とStoneオブジェクトを比較
    StoneColor testcolor = StoneColor.BLACK;
    Vector2D testposition = new Vector2D(1, 1);
    Stone teststone = new Stone(testcolor, testposition);

    // 比較対象のStringオブジェクト
    String notStoneObject = "Not a Stone";

    // StoneオブジェクトとStringオブジェクトの比較においてequalsでfalseとなるべき
    assertFalse(teststone.equals(notStoneObject), "Stoneオブジェクトと異なる型(String)がequalsでfalseとならない");
  }

  @Test
  @DisplayName("[正常系] StoneオブジェクトがhashCodeで正しくハッシュを計算する")
  void testStoneHashCode() {
    // 同じ構成情報を持つstoneオブジェクトを作成
    StoneColor testcolor = StoneColor.BLACK;
    Vector2D testposition = new Vector2D(1, 1);
    // 比較対象1
    Stone teststone1 = new Stone(testcolor, testposition);
    // 比較対象2
    Stone teststone2 = new Stone(testcolor, testposition);

    // 2つのstoneオブジェクトにおいて同じ構成情報を持つ場合、hashCodeも一致するべき
    assertEquals(
        teststone1.hashCode(),
        teststone2.hashCode(),
        "同じ構成情報を持つ2つのStoneオブジェクトのハッシュコードが一致しないため、正しくハッシュの計算ができていない");
  }

  @Test
  @DisplayName("[正常系] 異なる構成情報を持つ2つのstoneオブジェクトのhashCodeが異なる")
  void testStoneHashCodeDifferentObjects() {
    // 異なる構成情報を持つstoneオブジェクトを作成
    StoneColor testcolor1 = StoneColor.BLACK;
    StoneColor testcolor2 = StoneColor.WHITE;
    Vector2D testposition = new Vector2D(1, 1);
    Stone teststone1 = new Stone(testcolor1, testposition);
    Stone teststone2 = new Stone(testcolor2, testposition);

    // 2つのstoneオブジェクトにおいて構成情報が一致しない(今回は色)場合、hashCodeも一致しないべき
    assertNotEquals(
        teststone1.hashCode(),
        teststone2.hashCode(),
        "2つのstoneオブジェクトについて、それぞれの構成情報が異なるのに対してハッシュコードが一致するため、正しくハッシュの計算ができていない");
  }
}
