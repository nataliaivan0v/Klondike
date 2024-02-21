package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test class contains extra tests that test various invalid moves and scenarios in a Klondike
 * card game implementation.
 */
public class ExtraTestModel {

  private BasicKlondike bk = new BasicKlondike();

  @Test
  public void testIsGameOver() {
    bk.startGame(bk.getDeck(), true, 7, 3);
    Assert.assertFalse(bk.isGameOver());
  }

  @Test
  public void testGetScore() {
    bk.startGame(bk.getDeck(), true, 7, 3);
    Assert.assertEquals(0, bk.getScore());
  }

  @Test
  public void testGetPileHeight() {
    bk.startGame(bk.getDeck(), true, 7, 3);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.getPileHeight(30));
    Assert.assertEquals(1, bk.getPileHeight(0));
    Assert.assertEquals(2, bk.getPileHeight(1));
    Assert.assertEquals(3, bk.getPileHeight(2));
    Assert.assertEquals(4, bk.getPileHeight(3));
    Assert.assertEquals(5, bk.getPileHeight(4));
    Assert.assertEquals(6, bk.getPileHeight(5));
    Assert.assertEquals(7, bk.getPileHeight(6));
  }

  @Test
  public void testIsCardVisible() {
    bk.startGame(bk.getDeck(), true, 7, 3);
    Assert.assertTrue(bk.isCardVisible(0, 0));
    Assert.assertFalse(bk.isCardVisible(1, 0));
    Assert.assertTrue(bk.isCardVisible(2, 2));
  }

  @Test
  public void testThrows() {
    bk.startGame(bk.getDeck(), true, 7, 3);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.isCardVisible(-1, -1));
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.getCardAt(-1, -1));
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.getCardAt(5));
  }

  @Test
  public void testGetNumFoundations() {
    bk.startGame(bk.getDeck(), true, 7, 3);
    Assert.assertEquals(4, bk.getNumFoundations());
  }

  @Test
  public void testGetNumDraw() {
    bk.startGame(bk.getDeck(), true, 7, 1);
    Assert.assertEquals(1, bk.getNumDraw());
  }

  @Test
  public void testGetNumRows() {
    bk.startGame(bk.getDeck(), true, 7, 1);
    Assert.assertEquals(bk.getNumPiles(), bk.getNumRows());
    Assert.assertEquals(7, bk.getNumRows());
  }

  @Test
  public void testDiscardDraw() {
    Assert.assertThrows(IllegalStateException.class, () -> bk.discardDraw());
  }

  @Test
  public void testMoveDraw3() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDraw(1));
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDraw(2));
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDraw(6));
  }

  @Test
  public void testNullDeck() {
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.startGame(null, false, 7, 1));
  }

  @Test
  public void testMovePileInvalidPileNums() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(1, 2, 4));
  }

  @Test
  public void testMoveDrawInvalidPileNum() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.moveDraw(-1));
  }

  @Test
  public void testMoveToFoundationInvalidPileNum() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.moveToFoundation(-1, 9));
  }

  @Test
  public void testMoveDrawToFoundationInvalidPileNum() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.moveDrawToFoundation(22));
  }

  @Test
  public void testIsCardVisiblePileEmpty() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    bk.moveToFoundation(0, 0);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.isCardVisible(0, 0));
  }

  @Test
  public void testGetCardAtPileEmpty() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    bk.moveToFoundation(0, 0);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.getCardAt(0, 0));
    Assert.assertNull(bk.getCardAt(1));
  }

  @Test
  public void testGetCardAtCorrectCard() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertEquals("A♣", bk.getCardAt(0, 0).toString());
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.getCardAt(1, 0));
    bk.moveToFoundation(0, 0);
    Assert.assertEquals("A♣", bk.getCardAt(0).toString());
  }

  @Test
  public void testGameAlreadyStarted() {
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(1, 1, 2));
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDraw(2));
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveToFoundation(2, 3));
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDrawToFoundation(3));
    Assert.assertThrows(IllegalStateException.class, () -> bk.discardDraw());
    Assert.assertThrows(IllegalStateException.class, () -> bk.getNumRows());
    Assert.assertThrows(IllegalStateException.class, () -> bk.getNumPiles());
    Assert.assertThrows(IllegalStateException.class, () -> bk.getNumDraw());
    Assert.assertThrows(IllegalStateException.class, () -> bk.isGameOver());
    Assert.assertThrows(IllegalStateException.class, () -> bk.getScore());
    Assert.assertThrows(IllegalStateException.class, () -> bk.getPileHeight(1));
    Assert.assertThrows(IllegalStateException.class, () -> bk.isCardVisible(2, 1));
    Assert.assertThrows(IllegalStateException.class, () -> bk.getCardAt(4, 1));
    Assert.assertThrows(IllegalStateException.class, () -> bk.getCardAt(2));
    Assert.assertThrows(IllegalStateException.class, () -> bk.getDrawCards());
    Assert.assertThrows(IllegalStateException.class, () -> bk.getNumFoundations());
  }
}

