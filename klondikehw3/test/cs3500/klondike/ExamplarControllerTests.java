package cs3500.klondike;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

/**
 * This test class is responsible for testing various invalid inputs and scenarios in a Klondike
 * Textual Controller implementation.
 */
public class ExamplarControllerTests {
  BasicKlondike bk = new BasicKlondike();

  @Test
  public void testMovePileFoundationInvalidInput() {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpf 0 1 1 q").apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, bk.getDeck(), false, 7, 1);

    Assert.assertTrue(actualOutput.toString().contains("Invalid move"));
  }

  @Test
  public void testWinGame() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);

    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpf 1 1 mpf 2 2 mpf 2 3 mpf 3 3 mpf 3 1 mpf 3 4 mdf 4 mdf 2")
            .apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, deck, false, 3, 2);

    Assert.assertTrue(actualOutput.toString().contains("Draw: 2♡, 2♢\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♠  ?  ?\n" +
            "    A♢  ?\n" +
            "       2♣\n"));
    Assert.assertTrue(actualOutput.toString().contains("You win!"));
  }

  @Test
  public void testMovePileToPileInvalidInput() {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpp 0 0 0 0 0 3 1 4 q").apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 3);
    Assert.assertTrue(actualOutput.toString().contains("Invalid move"));
  }

  @Test
  public void testWorksAfterInvalidInput() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);
    Collections.reverse(deck);
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("md 0 0 0 0 0 0 0 1 q").apply(fakeUserInput, expectedOutput);
    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, deck, false, 3, 2);
    Assert.assertTrue(actualOutput.toString().contains("Draw: A"));
  }

  @Test
  public void testInputsNotNumbers() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);
    Collections.reverse(deck);

    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mdf h 1 mdf h 2 mpf 3 3 mpf 3 4 mpf 1 4 mpf 2 2 mpf 2 3 mpf 3 1")
            .apply(fakeUserInput, expectedOutput);
    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, deck, false, 3, 2);
    Assert.assertTrue(actualOutput.toString().contains("You win!"));
  }

  @Test
  public void testQuit() {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("q").apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, bk.getDeck(), false, 7, 1);

    Assert.assertTrue(actualOutput.toString().contains("Game quit"));
    Assert.assertTrue(actualOutput.toString().endsWith("Score: 0\n"));
  }

  @Test
  public void testTwoValidMovesInARow() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck());
    Collections.reverse(deck);

    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("dd md 7 md 6 q").apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, deck, false, 7, 3);

    Assert.assertTrue(actualOutput.toString().contains(" 7♡  ?\n" +
            "                6♣ 7♠\n" +
            "                   6♡"));
  }

  @Test
  public void testDiscardDraw() {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpp 1 1 2 mpp 3 1 4 q").apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 7, 3);

    Assert.assertTrue(actualOutput.toString().contains(" X  ?  ?  ?  ?  ?  ?\n" +
            "    2♢ 3♠  ?  ?  ?  ?\n" +
            "    A♠     ?  ?  ?  ?\n" +
            "          5♡  ?  ?  ?\n" +
            "          4♣ 6♡  ?  ?"));
  }

  /**
   * An interaction with the user consists of some input to send the program
   * and some output to expect.  We represent it as an object that takes in two
   * StringBuilders and produces the intended effects on them
   */
  interface Interaction {
    void apply(StringBuilder in, StringBuilder out);
  }

  static Interaction inputs(String in) {
    return (input, output) -> {
      input.append(in);
    };
  }
}

