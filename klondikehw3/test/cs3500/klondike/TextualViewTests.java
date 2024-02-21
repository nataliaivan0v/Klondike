package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.view.KlondikeTextualView;

/**
 * This test class contains tests that check that the KlondikeTextualView produces the
 * correct output.
 */
public class TextualViewTests {
  private final BasicKlondike bk = new BasicKlondike();
  private final KlondikeTextualView kt = new KlondikeTextualView(bk);

  @Test
  public void testTextualViewAtStart() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    Assert.assertEquals("Draw: 3♡, 4♡, 5♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♣  ?  ?  ?  ?  ?  ?\n" +
            "    8♣  ?  ?  ?  ?  ?\n" +
            "       A♠  ?  ?  ?  ?\n" +
            "          6♠  ?  ?  ?\n" +
            "            10♠  ?  ?\n" +
            "                K♠  ?\n" +
            "                   2♡", kt.toString());
  }

  @Test
  public void testViewEmptyPileAndFoundation() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    bk.moveToFoundation(0, 0);
    Assert.assertEquals("Draw: 3♡, 4♡, 5♡\n" +
            "Foundation: A♣, <none>, <none>, <none>\n" +
            "  X  ?  ?  ?  ?  ?  ?\n" +
            "    8♣  ?  ?  ?  ?  ?\n" +
            "       A♠  ?  ?  ?  ?\n" +
            "          6♠  ?  ?  ?\n" +
            "            10♠  ?  ?\n" +
            "                K♠  ?\n" +
            "                   2♡", kt.toString());
    bk.movePile(5, 1, 0);
  }

  @Test
  public void testViewMoveToEmptyPile() {
    bk.startGame(bk.getDeck(), false, 7, 3);
    bk.moveToFoundation(0, 0);
    bk.movePile(5, 1, 0);
    Assert.assertEquals("Draw: 3♡, 4♡, 5♡\n" +
            "Foundation: A♣, <none>, <none>, <none>\n" +
            " K♠  ?  ?  ?  ?  ?  ?\n" +
            "    8♣  ?  ?  ?  ?  ?\n" +
            "       A♠  ?  ?  ?  ?\n" +
            "          6♠  ?  ?  ?\n" +
            "            10♠ J♠  ?\n" +
            "                    ?\n" +
            "                   2♡", kt.toString());
  }
}

