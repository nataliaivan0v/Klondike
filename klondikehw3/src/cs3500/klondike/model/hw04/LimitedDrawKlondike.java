package cs3500.klondike.model.hw04;

import cs3500.klondike.model.KlondikeImplementation;
import cs3500.klondike.model.hw02.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * The LimitedKlondike class represents a limited draw variant of the Klondike Solitaire card game.
 * In this variant, players are allowed a limited number of redraws during gameplay.
 */
public class LimitedDrawKlondike extends KlondikeImplementation {
  final int numRedraw;
  int maxRedrawsAllowed;
  private int redrawCount = 0;

  /**
   * Constructs a new instance of the LimitedDrawKlondike game, a concrete implementation of
   * the KlondikeModel interface. This class allows limited redraws during gameplay.
   *
   * @param numTimesRedrawAllowed The maximum number of times a player is allowed to redraw
   *                              cards during the game.
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException();
    }
    this.numRedraw = numTimesRedrawAllowed;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException {
    super.startGame(deck, shuffle, numPiles, numDraw);
    List<Card> deckCopy = new ArrayList<>(deck);
    dealCards(deckCopy, numPiles);
    drawPile = deckCopy;
    this.numDraw = numDraw;
    this.maxRedrawsAllowed = drawPile.size() * (numRedraw + 1);
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    super.moveDraw(destPile);
    maxRedrawsAllowed--;
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException {
    super.moveDrawToFoundation(foundationPile);
    maxRedrawsAllowed--;
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    if (maxRedrawsAllowed == 0 && !drawPile.isEmpty()) {
      drawPile.remove(drawPile.get(0));
      return;
    }

    Card card = null;
    if (!drawPile.isEmpty() || !getDrawCards().isEmpty()) {
      card = drawPile.get(0);
      drawPile.remove(0);
      drawPile.add(card);
    } else if (drawPile.isEmpty()) {
      throw new IllegalStateException();
    }

    if (redrawCount <= maxRedrawsAllowed
            && redrawCount >= maxRedrawsAllowed - getDrawCards().size()) {
      drawPile.remove(card);
    }
    redrawCount++;
  }
}
