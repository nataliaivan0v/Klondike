package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.KlondikeImplementation;

/**
 * The BasicKlondike class represents a basic version of the Solitaire card game.
 * This class provides the core functionality for creating and playing the game.
 */
public class BasicKlondike extends KlondikeImplementation {
  public BasicKlondike() {
    // game is already initialized into a state that is ready to start
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    super.startGame(deck, shuffle, numPiles, numDraw);
    List<Card> deckCopy = new ArrayList<>(deck);
    dealCards(deckCopy, numPiles);
    drawPile = deckCopy;
    this.numDraw = numDraw;
  }
}