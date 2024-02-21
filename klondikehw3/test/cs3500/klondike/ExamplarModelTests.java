package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * This test class is responsible for testing various invalid moves and scenarios in a Klondike
 * card game implementation.
 */
public class ExamplarModelTests {

  private BasicKlondike bk = new BasicKlondike();

  @Test
  public void testInvalidMoves() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 1);
    bk.moveToFoundation(0, 1);
    bk.movePile(2, 1, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(3, 2, 4));
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(4, 1, 6));
    bk.movePile(1, 1, 2);
    Assert.assertThrows(IllegalStateException.class, () -> bk.startGame(bk.getDeck(), false, 7, 1));
  }

  @Test
  public void testMoveDrawInvalidMove() {
    bk.startGame(bk.getDeck(), false, 7, 1);
    Assert.assertThrows(IllegalStateException.class, () -> bk.startGame(bk.getDeck(), false, 7, 1));
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDraw(5));
  }

  @Test
  public void testMoveDrawInvalidMove2() {
    bk.startGame(bk.getDeck(), false, 7, 1);
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDraw(0));
  }

  @Test
  public void testMoveDrawEmpty() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck());
    bk.startGame(deck, false, 9, 1);
    deck.remove(0);
    deck.remove(1);
    deck.remove(2);
    deck.remove(3);
    deck.remove(4);
    deck.remove(5);
    deck.remove(6);
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDraw(1));
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDrawToFoundation(1));
    Assert.assertThrows(IllegalStateException.class, () -> bk.startGame(bk.getDeck(), false, 7, 1));
  }

  @Test
  public void testMovePileInvalidMoves() {
    bk.startGame(bk.getDeck(), false, 4, 1);
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(2, 1, 0));
  }

  @Test
  public void testMovePileNotEnoughCardsToMove() {
    bk.startGame(bk.getDeck(), false, 7, 1);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.movePile(0, 2, 2));
  }

  @Test
  public void testMovePileSamePile() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertThrows(IllegalArgumentException.class, () -> bk.movePile(3, 2, 3));
  }

  @Test
  public void testMovePileInvalidMoves2() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(4, 1, 2));
  }

  @Test
  public void testMoveToFoundationInvalidMove() {
    bk.startGame(bk.getDeck(), false, 7, 1);
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveToFoundation(5, 0));
  }

  @Test
  public void testMoveDrawToFoundationInvalidMove() {
    bk.startGame(bk.getDeck(), false, 7, 1);
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveDrawToFoundation(0));
  }

  @Test
  public void testMovePileSortedMoveInvalid() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 1);
    bk.movePile(2, 1, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(5, 1, 6));
  }

  @Test
  public void testMovePileSortedMoveInvalid2() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 1);
    bk.movePile(2, 1, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(5, 2, 6));
  }

  @Test
  public void testMoveToFoundationSortedInvalidMove() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 1);
    bk.moveToFoundation(0, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveToFoundation(0, 2));
  }

  @Test
  public void testMovePileEmptySpaceNotKing() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 1);
    bk.moveToFoundation(0, 1);
    Assert.assertThrows(IllegalStateException.class, () -> bk.movePile(3, 1, 0));
  }

  @Test
  public void testMoveInvalidCardToFoundationWithAce() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 1);
    bk.moveToFoundation(0, 1);
    Assert.assertThrows(IllegalStateException.class, () -> bk.moveToFoundation(1, 1));
  }

  @Test
  public void testGetDrawCardsSame() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 3);
    Assert.assertEquals(3, bk.getDrawCards().size());
    bk.discardDraw();
    Assert.assertEquals(3, bk.getDrawCards().size());
  }

  @Test
  public void testSimulation() {
    bk.startGame(ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 9, 1);
    Assert.assertFalse(bk.isGameOver());
    bk.moveToFoundation(0, 0);
    Assert.assertEquals(1, bk.getScore());
    bk.movePile(3, 1, 4);
    bk.movePile(7, 1, 8);
    bk.movePile(6, 1, 7);
    bk.movePile(5, 1, 6);
    bk.discardDraw();
    bk.discardDraw();
    bk.discardDraw();
    bk.moveDraw(0);
    bk.discardDraw();
    bk.discardDraw();
    bk.discardDraw();
    bk.discardDraw();
    bk.moveDraw(0);
    bk.movePile(7, 2, 0);
    bk.movePile(7, 1, 8);
    bk.movePile(5, 1, 7);
    bk.movePile(7, 2, 0);
    bk.movePile(5, 1, 7);
    bk.movePile(3, 1, 5);
    bk.movePile(5, 2, 7);
    bk.movePile(5, 1, 7);
    bk.movePile(3, 1, 7);
    bk.movePile(5, 1, 7);
    bk.moveToFoundation(3, 3);
    bk.discardDraw();
    bk.moveDraw(3);
    bk.moveDraw(5);
    bk.movePile(8, 3, 5);
    bk.movePile(6, 2, 8);
    bk.movePile(7, 7, 8);
    bk.movePile(2, 1, 7);
    bk.movePile(7, 2, 4);
    bk.movePile(4, 4, 6);
    bk.movePile(7, 1, 6);
    bk.moveToFoundation(7, 3);
    bk.moveDraw(7);
    bk.moveDraw(7);
    bk.movePile(8, 10, 7);
    bk.moveDraw(3);
    bk.movePile(8, 1, 3);
    bk.movePile(8, 1, 3);
    Assert.assertThrows(IllegalStateException.class, () -> bk.startGame(bk.getDeck(), false, 7, 1));
  }
}
