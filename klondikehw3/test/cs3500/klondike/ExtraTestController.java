package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * This test class contains extra tests that test various moves and scenarios in a Klondike
 * Textual Controller implementation.
 */
public class ExtraTestController {
  BasicKlondike bk = new BasicKlondike();
  KlondikeController kc = new KlondikeTextualController(new StringReader(""), new StringBuilder());

  @Test
  public void testPlayGameNullModel() {
    Assert.assertThrows(IllegalArgumentException.class, () -> kc.playGame(null, bk.getDeck(),
            false, 7, 3));
  }

  @Test
  public void testPlayGameCannotBeStarted() {
    Assert.assertThrows(IllegalStateException.class, () -> kc.playGame(bk,
            new ArrayList<Card>(), false, 7, 3));
    Assert.assertThrows(IllegalStateException.class, () -> kc.playGame(bk, bk.getDeck(),
            false, -1, 3));
    Assert.assertThrows(IllegalStateException.class, () -> kc.playGame(bk, bk.getDeck(),
            false, 7, 0));
  }

  @Test
  public void testPlayGameCannotInteract() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new KlondikeTextualController(null, new StringBuilder()));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new KlondikeTextualController(new StringReader(""), null));
  }

  @Test
  public void test() {
    KlondikeTextualController controller =
            new KlondikeTextualController(new StringReader("mpp 4 2 5"), new StringBuilder());
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(bk, bk.getDeck(), false, 7, 3));
  }

  @Test
  public void testGameCannotBeStarted() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);

    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpf 1 1 mpf 2 2 mpf 2 3 mpf 3 3 mpf 3 1 mpf 3 4 mdf 4 mdf 2")
            .apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(bk, deck, false, 5, 3));
  }

  @Test
  public void testSomething3() {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("").apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(bk, bk.getDeck(), false, 7, 1));
  }

  @Test
  public void testThrows() {
    KlondikeController controller =
            new KlondikeTextualController(new StringReader(""), new StringBuilder());
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(bk, bk.getDeck(), false, 7, 3));

    KlondikeController kc =
            new KlondikeTextualController(new StringReader("q"), new StringBuilder());
    Assert.assertThrows(IllegalArgumentException.class, () ->
            kc.playGame(null, bk.getDeck(), false, 7, 2));
    Assert.assertThrows(IllegalStateException.class, () ->
            kc.playGame(bk, bk.getDeck(), false, 20, 2));
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
    Assert.assertTrue(actualOutput.toString()
            .contains("Foundation: <none>, <none>, <none>, <none>"));
  }

  @Test
  public void testSomething2() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);

    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpf 1 1 mpf 2 2 mpf 2 3 mpf 3 3 mpf 3 1 mpf 3 4 mdf 4 q")
            .apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, deck, false, 3, 2);

    Assert.assertTrue(actualOutput.toString().contains("Draw: 2♢\n"
            + "Foundation: 2♠, A♢, 2♣, 2♡"));
    Assert.assertTrue(actualOutput.toString().contains("Score: 7"));
  }

  @Test
  public void testWin() {
    List<Card> deck = ExamplarExtendedModelTests.sortCards(bk.getDeck()).subList(0, 8);

    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpf 1 1 mpf 2 2 mpf 2 3 mpf 3 3 mpf 3 1 mpf 3 4 mdf 4 mdf 2")
            .apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, deck, false, 3, 2);

    Assert.assertTrue(actualOutput.toString().contains("You win!"));

  }

  @Test
  public void testMovePileFoundationWorksInvalidInput() {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    inputs("mpf 1 1 0 0 q").apply(fakeUserInput, expectedOutput);

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(bk, ExamplarExtendedModelTests.sortCards(bk.getDeck()), false, 3, 3);
    Assert.assertTrue(actualOutput.toString().contains("Score: 1"));
  }

  interface Interaction {
    void apply(StringBuilder in, StringBuilder out);
  }

  static ExamplarControllerTests.Interaction prints(String... lines) {
    return (input, output) -> {
      for (String line : lines) {
        output.append(line).append('\n');
      }
    };
  }

  static ExamplarControllerTests.Interaction inputs(String in) {
    return (input, output) -> {
      input.append(in);
    };
  }

  void testRun(KlondikeModel model, ExamplarControllerTests.Interaction... interactions) {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    for (ExamplarControllerTests.Interaction interaction : interactions) {
      interaction.apply(fakeUserInput, expectedOutput);
    }

    StringReader input = new StringReader(fakeUserInput.toString());
    StringBuilder actualOutput = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, actualOutput);
    controller.playGame(model, ExamplarExtendedModelTests.sortCards(model.getDeck()), false, 7, 3);

    Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
  }
}

