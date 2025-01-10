package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BoardTest {
    @Test
    @DisplayName("[正常系]盤が生成したとき、それを取得できる")
    void initBoardClass() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        HashMap<Vector2D, Stone> getBoard = board.getBoard();
        assertNotNull(board, "Create Board");
        assertNotNull(getBoard, "Get Board");
    }

    @Test
    @DisplayName("[正常系]白い石が置かれたとき、それを確認できる")
    void putStoneWhite() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(0, 0), new Stone(StoneColor.WHITE, new Vector2D(0, 0)));
        Stone stone = board.getStone(new Vector2D(0, 0));
        assertTrue(stone.getColor() == StoneColor.WHITE);
    }

    @Test
    @DisplayName("[正常系]黒い石が置かれたとき、それを確認できる")
    void putStoneBlack() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(0, 0), new Stone(StoneColor.BLACK, new Vector2D(0, 0)));
        Stone stone = board.getStone(new Vector2D(0, 0));
        assertTrue(stone.getColor() == StoneColor.BLACK);
    }

    @Test
    @DisplayName("[正常系]指定した座標の石がnullで同じ石が5個並んでいるか確認するとき、まだ同じ石が5個並んでいないことを確認できる")
    void checkWinnerNull() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(12, 0), new Stone(StoneColor.WHITE, new Vector2D(12, 0)));
        board.putStone(new Vector2D(12, 1), new Stone(StoneColor.WHITE, new Vector2D(12, 1)));
        // (12, 2)には石を置かない
        board.putStone(new Vector2D(12, 3), new Stone(StoneColor.WHITE, new Vector2D(12, 3)));
        board.putStone(new Vector2D(12, 4), new Stone(StoneColor.WHITE, new Vector2D(12, 4)));
        board.putStone(new Vector2D(12, 5), new Stone(StoneColor.WHITE, new Vector2D(12, 5)));
        
        assertFalse(board.checkWinner(new Vector2D(12, 2)));

    }

    @Test
    @DisplayName("[正常系]白が5個縦に並んで置かれたとき、同じ石が5個並んだことを確認できる")
    void checkWinnerWhiteTate5() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(0, 0), new Stone(StoneColor.WHITE, new Vector2D(0, 0)));
        board.putStone(new Vector2D(0, 1), new Stone(StoneColor.WHITE, new Vector2D(0, 1)));
        board.putStone(new Vector2D(0, 2), new Stone(StoneColor.WHITE, new Vector2D(0, 2)));
        board.putStone(new Vector2D(0, 3), new Stone(StoneColor.WHITE, new Vector2D(0, 3)));
        board.putStone(new Vector2D(0, 4), new Stone(StoneColor.WHITE, new Vector2D(0, 4)));
        
        assertTrue(board.checkWinner(new Vector2D(0, 4)));

    }

    @Test
    @DisplayName("[正常系]黒が4個縦に並んで置かれたとき、まだ同じ石が5個並んでいないことを確認できる")
    void checkWinnerBlackTate4() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(11, 0), new Stone(StoneColor.BLACK, new Vector2D(11, 0)));
        board.putStone(new Vector2D(11, 1), new Stone(StoneColor.BLACK, new Vector2D(11, 1)));
        board.putStone(new Vector2D(11, 2), new Stone(StoneColor.BLACK, new Vector2D(11, 2)));
        board.putStone(new Vector2D(11, 3), new Stone(StoneColor.BLACK, new Vector2D(11, 3)));
        
        assertFalse(board.checkWinner(new Vector2D(11, 3)));

    }

    @Test
    @DisplayName("[正常系]白と黒が5個混じって縦に並んで置かれたとき、まだ同じ石が5個並んでいないことを確認できる")
    void checkWinnerMix5() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(11, 10), new Stone(StoneColor.BLACK, new Vector2D(11, 10)));
        board.putStone(new Vector2D(11, 11), new Stone(StoneColor.WHITE, new Vector2D(11, 11)));
        board.putStone(new Vector2D(11, 12), new Stone(StoneColor.BLACK, new Vector2D(11, 12)));
        board.putStone(new Vector2D(11, 13), new Stone(StoneColor.BLACK, new Vector2D(11, 13)));
        board.putStone(new Vector2D(11, 14), new Stone(StoneColor.WHITE, new Vector2D(11, 14)));
        
        assertFalse(board.checkWinner(new Vector2D(11, 12)));

    }

    @Test
    @DisplayName("[正常系]黒が5個横に並んで置かれたとき、同じ石が5個並んだことを確認できる")
    void checkWinnerBlackYoko5() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(1, 0), new Stone(StoneColor.BLACK, new Vector2D(1, 0)));
        board.putStone(new Vector2D(2, 0), new Stone(StoneColor.BLACK, new Vector2D(2, 0)));
        board.putStone(new Vector2D(3, 0), new Stone(StoneColor.BLACK, new Vector2D(3, 0)));
        board.putStone(new Vector2D(4, 0), new Stone(StoneColor.BLACK, new Vector2D(4, 0)));
        board.putStone(new Vector2D(5, 0), new Stone(StoneColor.BLACK, new Vector2D(5, 0)));
        
        assertTrue(board.checkWinner(new Vector2D(3, 0)));

    }

    @Test
    @DisplayName("[正常系]白が4個横に並んで置かれたとき、まだ同じ石が5個並んでいないことを確認できる")
    void checkWinnerWhiteYoko4() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(15, 18), new Stone(StoneColor.WHITE, new Vector2D(15, 18)));
        board.putStone(new Vector2D(16, 18), new Stone(StoneColor.WHITE, new Vector2D(16, 18)));
        board.putStone(new Vector2D(17, 18), new Stone(StoneColor.WHITE, new Vector2D(17, 18)));
        board.putStone(new Vector2D(18, 18), new Stone(StoneColor.WHITE, new Vector2D(18, 18)));
        
        assertFalse(board.checkWinner(new Vector2D(16, 18)));

    }

    @Test
    @DisplayName("[正常系]白が5個右斜め下に並んで置かれたとき、同じ石が5個並んだことを確認できる")
    void checkWinnerWhiteNaname5() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(14, 18), new Stone(StoneColor.WHITE, new Vector2D(14, 18)));
        board.putStone(new Vector2D(15, 17), new Stone(StoneColor.WHITE, new Vector2D(15, 17)));
        board.putStone(new Vector2D(16, 16), new Stone(StoneColor.WHITE, new Vector2D(16, 16)));
        board.putStone(new Vector2D(17, 15), new Stone(StoneColor.WHITE, new Vector2D(17, 15)));
        board.putStone(new Vector2D(18, 14), new Stone(StoneColor.WHITE, new Vector2D(18, 14)));
        
        assertTrue(board.checkWinner(new Vector2D(17, 15)));

    }

    @Test
    @DisplayName("[正常系]黒が4個右斜め下に並んで置かれたとき、まだ同じ石が5個並んでいないことを確認できる")
    void checkWinnerBlackNaname4() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(6, 10), new Stone(StoneColor.BLACK, new Vector2D(6, 10)));
        board.putStone(new Vector2D(7, 9), new Stone(StoneColor.BLACK, new Vector2D(7, 9)));
        board.putStone(new Vector2D(8, 8), new Stone(StoneColor.BLACK, new Vector2D(8, 8)));
        board.putStone(new Vector2D(9, 7), new Stone(StoneColor.BLACK, new Vector2D(9, 7)));
        
        assertFalse(board.checkWinner(new Vector2D(6, 10)));

    }

    @Test
    @DisplayName("[正常系]黒が5個右斜め上に並んで置かれたとき、同じ石が5個並んだことを確認できる")
    void checkWinnerBlackNaname5() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(1, 1), new Stone(StoneColor.BLACK, new Vector2D(1, 1)));
        board.putStone(new Vector2D(2, 2), new Stone(StoneColor.BLACK, new Vector2D(2, 2)));
        board.putStone(new Vector2D(3, 3), new Stone(StoneColor.BLACK, new Vector2D(3, 3)));
        board.putStone(new Vector2D(4, 4), new Stone(StoneColor.BLACK, new Vector2D(4, 4)));
        board.putStone(new Vector2D(5, 5), new Stone(StoneColor.BLACK, new Vector2D(5, 5)));
        
        assertTrue(board.checkWinner(new Vector2D(2, 2)));

    }

    @Test
    @DisplayName("[正常系]白が4個右斜め上に並んで置かれたとき、まだ同じ石が5個並んでいないことを確認できる")
    void checkWinnerWhiteNaname4() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(11, 12), new Stone(StoneColor.WHITE, new Vector2D(11, 12)));
        board.putStone(new Vector2D(12, 13), new Stone(StoneColor.WHITE, new Vector2D(12, 13)));
        board.putStone(new Vector2D(13, 14), new Stone(StoneColor.WHITE, new Vector2D(13, 14)));
        board.putStone(new Vector2D(14, 15), new Stone(StoneColor.WHITE, new Vector2D(14, 15)));
        
        assertFalse(board.checkWinner(new Vector2D(14, 15)));

    }

    @Test
    @DisplayName("[異常系]nullの座標に石が置かれたとき、IllegalArgumentExceptionが発生する")
    void putStonePositionNull() {
        Board board = new Board(new Vector2D(19, 19));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            board.putStone(null, new Stone(StoneColor.WHITE, new Vector2D(18, 18)));
        });

        assertEquals("Position and stone cannot be null",exception.getMessage());         
    }

    @Test
    @DisplayName("[異常系]nullの石が置かれたとき、IllegalArgumentExceptionが発生する")
    void putStoneStoneNull() {
        Board board = new Board(new Vector2D(19, 19));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            board.putStone(new Vector2D(10, 15), null);
        });

        assertEquals("Position and stone cannot be null",exception.getMessage());         
    }

    @Test
    @DisplayName("[異常系]不正な座標(19, 0)に石が置かれたとき、IllegalArgumentExceptionが発生する")
    void putStonePositionErrorX19Y0() {
        Board board = new Board(new Vector2D(19, 19));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            board.putStone(new Vector2D(19, 0), new Stone(StoneColor.WHITE, new Vector2D(19, 0)));
        });

        assertEquals("Position is out of bounds",exception.getMessage());         
    }

    @Test
    @DisplayName("[異常系]不正な座標(0, 19)に石が置かれたとき、IllegalArgumentExceptionが発生する")
    void putStonePositionErrorX0Y19() {
        Board board = new Board(new Vector2D(19, 19));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            board.putStone(new Vector2D(0, 19), new Stone(StoneColor.WHITE, new Vector2D(0, 19)));
        });

        assertEquals("Position is out of bounds",exception.getMessage());         
    }

    @Test
    @DisplayName("[異常系]不正な座標(-1, 0)に石が置かれたとき、IllegalArgumentExceptionが発生する")
    void putStonePositionErrorXminus1Y0() {
        Board board = new Board(new Vector2D(19, 19));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            board.putStone(new Vector2D(-1, 0), new Stone(StoneColor.WHITE, new Vector2D(-1, 0)));
        });

        assertEquals("Position is out of bounds",exception.getMessage());         
    }

    @Test
    @DisplayName("[異常系]不正な座標(0, -1)に石が置かれたとき、IllegalArgumentExceptionが発生する")
    void putStonePositionErrorX0Yminus1() {
        Board board = new Board(new Vector2D(19, 19));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            board.putStone(new Vector2D(0, -1), new Stone(StoneColor.WHITE, new Vector2D(0, -1)));
        });

        assertEquals("Position is out of bounds",exception.getMessage());         
    }

    @Test
    @DisplayName("[異常系]すでに石が置かれている座標に石を置かれたとき、IllegalStateExceptionが発生する")
    void putStoneStoneError() throws Exception{
        Board board = new Board(new Vector2D(19, 19));
        board.putStone(new Vector2D(10, 0), new Stone(StoneColor.WHITE, new Vector2D(10, 0)));
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            board.putStone(new Vector2D(10, 0), new Stone(StoneColor.BLACK, new Vector2D(10, 0)));
        });

        assertEquals("Position is already occupied",exception.getMessage());         
    }

    @Test
    @DisplayName("[異常系]不正な座標(0, 19)で同じ石が5個並んでいるか確認するとき、IllegalArgumentExceptionが発生する")
    void checkWinnerError() {
        Board board = new Board(new Vector2D(19, 19));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            board.checkWinner(new Vector2D(0, 19));
        });

        assertEquals("Position is out of bounds",exception.getMessage());         
    }
}
