package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The `KlondikeTextualController` class implements the `KlondikeController` interface
 * and provides a textual-based controller for playing a game of Klondike solitaire. It interacts
 * with a user through a `Readable` input source and outputs game information to an `Appendable`.
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {
  private Readable r;
  private Appendable a;

  /**
   * Constructs a `KlondikeTextualController` with the specified `Readable` input and
   * `Appendable` output.
   *
   * @param r The `Readable` input source for user interaction.
   * @param a The `Appendable` output for displaying game information.
   * @throws IllegalArgumentException If `r` or `a` is null.
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException();
    }
    this.r = r;
    this.a = a;
  }

  @Override
  public void playGame(
          KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    if (model == null) {
      throw new IllegalArgumentException();
    }

    if (deck.isEmpty()) {
      throw new IllegalStateException();
    }

    Scanner sc = new Scanner(r);
    boolean quit = false;
    boolean gameOver = false;

    model.startGame(deck, shuffle, numPiles, numDraw);

    try {
      KlondikeTextualView kt = new KlondikeTextualView(model, a);
      kt.render();
      a.append("\n");
    } catch (IOException e) {
      throw new IllegalStateException();
    }

    while (!quit && !gameOver) {
      String input;
      try {
        try {
          input = sc.next();
        } catch (IllegalStateException e) {
          break;
        }
      } catch (NoSuchElementException e) {
        throw new IllegalStateException();
      }
      switch (input) {
        case "mpp":
          try {
            int srcPile = getNextValidInt(sc, model) - 1;
            int count = getNextValidInt(sc, model);
            int destPile = getNextValidInt(sc, model) - 1;
            if (srcPile != -12346 && count != -12346 && destPile != -12346) {
              try {
                model.movePile(srcPile, count, destPile);
              } catch (IllegalArgumentException e) {
                a.append("Invalid move. Play again.");
                appendView(model);
                break;
              }
              appendView(model);
              if (model.isGameOver()) {
                gameOver = true;
                gameOver(model);
              }
            }
          } catch (IllegalStateException | IOException e) {
            break;
          }
          break;
        case "md":
          try {
            int destPile2 = getNextValidInt(sc, model) - 1;
            if (destPile2 != -12346) {
              try {
                model.moveDraw(destPile2);
              } catch (IllegalArgumentException e) {
                a.append("Invalid move. Play again.");
                appendView(model);
                break;
              }
              appendView(model);
              if (model.isGameOver()) {
                gameOver = true;
                gameOver(model);
              }
            }
          } catch (IllegalStateException | IOException e) {
            break;
          }
          break;
        case "mpf":
          try {
            int srcPile3 = getNextValidInt(sc, model) - 1;
            int foundPile = getNextValidInt(sc, model) - 1;
            if (srcPile3 != -12346 && foundPile != -12346) {
              try {
                model.moveToFoundation(srcPile3, foundPile);
              } catch (IllegalArgumentException e) {
                a.append("Invalid move. Play again.");
                appendView(model);
                break;
              }
              appendView(model);
              if (model.isGameOver()) {
                gameOver = true;
                gameOver(model);
              }
            }
          } catch (IllegalStateException | IOException e) {
            break;
          }
          break;
        case "mdf":
          try {
            int foundPile2 = getNextValidInt(sc, model) - 1;
            if (foundPile2 != -12346) {
              try {
                model.moveDrawToFoundation(foundPile2);
              } catch (IllegalArgumentException e) {
                a.append("Invalid move. Play again.");
                appendView(model);
                break;
              }
              appendView(model);
              if (model.isGameOver()) {
                gameOver = true;
                gameOver(model);
              }
            }
          } catch (IllegalStateException | IOException e) {
            break;
          }
          break;
        case "dd":
          model.discardDraw();
          appendView(model);
          if (model.isGameOver()) {
            gameOver = true;
            gameOver(model);
          }
          break;
        case "q":
        case "Q":
          quit = true;
          gameQuit(model);
          sc.close();
          break;
        default:
          // no action is intended when no other case applies
      }
    }
  }

  private void gameOver(KlondikeModel model) {
    try {
      KlondikeTextualView kt = new KlondikeTextualView(model, a);
      kt.render();
      a.append("\n");
      a.append("You win!");
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }

  private void appendView(KlondikeModel model) {
    try {
      KlondikeTextualView kt = new KlondikeTextualView(model, a);
      kt.render();
      a.append("\n");
      a.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }

  private void gameQuit(KlondikeModel model) {
    try {
      a.append("Game quit!\n" +
              "State of game when quit:\n");
      appendView(model);
      a.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }

  private int getNextValidInt(Scanner sc, KlondikeModel model) {
    while (true) {
      if (sc.hasNext()) {
        String userInput = sc.next();

        if (userInput.equalsIgnoreCase("q")) {
          gameQuit(model);
          sc.close();
          return -12345;
        }

        try {
          int intValue = Integer.parseInt(userInput);
          if (intValue > 0) {
            return intValue;
          } else {
            try {
              a.append("Invalid move. Play again. Your input must be a natural number.\n");
              appendView(model);
            } catch (IOException e) {
              throw new IllegalStateException();
            }
          }
        } catch (NumberFormatException e) {
          try {
            a.append("Invalid move. Play again. Your input must be an int.\n");
            appendView(model);
          } catch (IOException ex) {
            throw new IllegalStateException();
          }
        }
      }
    }
  }
}
