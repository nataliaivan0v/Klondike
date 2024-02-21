package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This test class is responsible for testing LimitedDrawKlondike and WhiteheadKlondike.
 */
public class ExamplarExtendedModelTests {
  BasicKlondike bk = new BasicKlondike();
  WhiteheadKlondike wh = new WhiteheadKlondike();
  LimitedDrawKlondike ld0 = new LimitedDrawKlondike(0);
  LimitedDrawKlondike ld1 = new LimitedDrawKlondike(1);

  @Test
  public void testLimitedDrawRemovesCards() {
    ld1.startGame(bk.getDeck().subList(0, 8), false, 3, 2);
    // A B
    ld1.discardDraw();
    // B
    Assert.assertEquals(1, ld1.getDrawCards().size());
    ld1.discardDraw();
    Assert.assertEquals(0, ld1.getDrawCards().size());
  }

  @Test
  public void testLimitedDrawZero() {
    ld0.startGame(bk.getDeck().subList(0, 8), false, 3, 2);
    ld0.discardDraw();
    Assert.assertEquals(1, ld0.getDrawCards().size());
    ld0.discardDraw();
    Assert.assertEquals(0, ld0.getDrawCards().size());
  }

  @Test
  public void testWhiteheadCanMoveNotKingToEmptyPile() {
    List<Card> deck = sortCards(bk.getDeck()).subList(0, 8);
    wh.startGame(deck, false, 3, 2);
    wh.moveToFoundation(0, 0);
    wh.movePile(2, 1, 0);
    Assert.assertEquals(1, wh.getPileHeight(0));
  }

  @Test
  public void testWhiteheadSingleColorMove() {
    List<Card> deck = sortCards(bk.getDeck()).subList(0, 8);
    wh.startGame(deck, false, 3, 2);
    wh.movePile(0, 1, 2);
    Assert.assertEquals(4, wh.getPileHeight(2));
  }

  @Test
  public void testWhiteheadSingleSuitRunOn() {
    List<Card> deck = sortCards(bk.getDeck()).subList(0, 8);
    wh.startGame(deck, false, 3, 2);
    wh.moveToFoundation(1, 0);
    wh.movePile(1, 1, 2);
    wh.movePile(2, 2, 1);
    Assert.assertEquals(2, wh.getPileHeight(1));
  }

  @Test
  public void testWhiteheadInvalidMove() {
    List<Card> deck = sortCards(bk.getDeck()).subList(0, 8);
    wh.startGame(deck, false, 3, 2);
    Assert.assertThrows(IllegalStateException.class, () -> wh.movePile(1, 1, 2));
  }

  @Test
  public void testWhiteheadInvalidMove2() {
    List<Card> deck = sortCards(bk.getDeck()).subList(0, 8);
    wh.startGame(deck, false, 3, 2);
    wh.movePile(0, 1, 2);
    Assert.assertThrows(IllegalStateException.class, () -> wh.movePile(2, 2, 0));
  }

  protected static List<Card> sortCards(List<Card> deck) {
    Comparator<Card> cardComparator = (card1, card2) -> {
      String values = "A234567891JQK";
      String suits = "♠♣♡♢";

      int valueIndex1 = values.indexOf(card1.toString().charAt(0));
      int suitIndex1 = suits.indexOf(card1.toString().charAt(1));
      if (card1.toString().length() > 2) {
        suitIndex1 = suits.indexOf(card1.toString().charAt(2));
      }

      int valueIndex2 = values.indexOf(card2.toString().charAt(0));
      int suitIndex2 = suits.indexOf(card2.toString().charAt(1));
      if (card2.toString().length() > 2) {
        suitIndex2 = suits.indexOf(card2.toString().charAt(2));
      }

      if (valueIndex1 != valueIndex2) {
        return Integer.compare(valueIndex1, valueIndex2);
      } else {
        return Integer.compare(suitIndex1, suitIndex2);
      }
    };
    Collections.sort(deck, cardComparator);
    return deck;
  }
}
