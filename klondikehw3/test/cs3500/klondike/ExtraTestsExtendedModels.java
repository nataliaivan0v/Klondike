package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * This test class contains extra tests that test LimitedDrawKlondike and WhiteheadKlondike.
 */
public class ExtraTestsExtendedModels {
  BasicKlondike bk = new BasicKlondike();

  @Test
  public void testLimitedDrawCorrectSize() {
    LimitedDrawKlondike ld = new LimitedDrawKlondike(2);
    ld.startGame(bk.getDeck().subList(0, 8), false, 3, 2);
    // A B
    ld.discardDraw();
    // B A
    ld.discardDraw();
    // A B
    ld.discardDraw();
    // B
    ld.discardDraw();
    Assert.assertEquals(0, ld.getDrawCards().size());
  }

  @Test
  public void testLimitedDrawInvalidMove() {
    LimitedDrawKlondike ld = new LimitedDrawKlondike(0);
    ld.startGame(bk.getDeck().subList(0, 8), false, 3, 2);
    ld.discardDraw();
    ld.discardDraw();
    Assert.assertThrows(IllegalStateException.class, () -> ld.movePile(1, 2, 2));
  }

  @Test
  public void testLimitedDrawPlayGame() {
    LimitedDrawKlondike ld = new LimitedDrawKlondike(0);
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);
    ld.startGame(deck, false, 3, 2);
    ld.moveToFoundation(0, 0);
    ld.moveToFoundation(1, 1);
    ld.discardDraw();
    ld.moveToFoundation(1, 2);
    ld.moveToFoundation(2, 2);
    ld.discardDraw();
    ld.moveToFoundation(2, 0);
    ld.moveToFoundation(2, 3);
    Assert.assertTrue(ld.isGameOver());
    Assert.assertEquals(6, ld.getScore());
  }

  @Test
  public void testWhiteheadPlayGame() {
    WhiteheadKlondike wh = new WhiteheadKlondike();
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);
    wh.startGame(deck, false, 3, 2);
    wh.moveToFoundation(0, 0);
    wh.moveToFoundation(1, 1);
    wh.moveToFoundation(1, 2);
    wh.moveToFoundation(2, 2);
    wh.moveToFoundation(2, 0);
    wh.moveToFoundation(2, 3);
    wh.moveDraw(0);
    wh.moveDraw(1);
    wh.moveToFoundation(0, 3);
    wh.moveToFoundation(1, 1);
    Assert.assertTrue(wh.isGameOver());
    Assert.assertEquals(8, wh.getScore());
  }

  @Test
  public void testWhiteheadCardsAreVisible() {
    WhiteheadKlondike wh = new WhiteheadKlondike();
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);
    wh.startGame(deck, false, 3, 2);
    Assert.assertTrue(wh.isCardVisible(1, 0));
    Assert.assertTrue(wh.isCardVisible(2, 1));
  }

  @Test
  public void testWhiteheadMovePile() {
    WhiteheadKlondike wh = new WhiteheadKlondike();
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 12);
    wh.startGame(deck, false, 4, 2);
    wh.movePile(0, 1, 1);
    wh.movePile(1, 2, 3);
    Assert.assertEquals(6, wh.getPileHeight(3));
  }
}
