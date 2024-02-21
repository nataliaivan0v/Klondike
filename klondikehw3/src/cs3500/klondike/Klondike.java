package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * The Klondike class provides a command-line entry point for playing different variants
 * of the Klondike Solitaire card game.
 */
public final class Klondike {
  /**
   * The main method serves as the entry point for the Klondike Solitaire game. It accepts
   * command-line arguments to specify the game type and parameters for starting the game.
   *
   * @param args An array of command-line arguments, where the first argument specifies the game
   *             type and other arguments are used to configure the game.
   */
  public static void main(String[] args) {
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException();
    }

    String gameType = args[0].toLowerCase();

    KlondikeModel model;
    switch (gameType) {
      case "basic":
        model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
        break;
      case "limited":
        model = KlondikeCreator.create(KlondikeCreator.GameType.LIMITED);
        break;
      case "whitehead":
        model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
        break;
      default:
        throw new IllegalArgumentException();
    }

    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;
    KlondikeTextualController controller = new KlondikeTextualController(rd, ap);

    try {
      if (args.length == 4) {
        model = new LimitedDrawKlondike(Integer.parseInt(args[1]));
        try {
          controller.playGame(model, model.getDeck(), true,
                  Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } catch (IllegalArgumentException e) {
          // do nothing
        }
      } else if (args.length == 3) {
        try {
          controller.playGame(model, model.getDeck(), true,
                  Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } catch (IllegalArgumentException e) {
          // do nothing
        }
      } else if (args.length == 2) {
        try {
          controller.playGame(model, model.getDeck(), true, Integer.parseInt(args[1]), 3);
        } catch (IllegalArgumentException e) {
          if (model instanceof LimitedDrawKlondike) {
            throw new IllegalArgumentException();
          }
        }
      } else {
        controller.playGame(model, model.getDeck(), true, 7, 3);
      }
    } catch (NullPointerException e) {
      // do nothing
    }
  }
}
