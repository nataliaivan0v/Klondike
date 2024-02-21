package cs3500.klondike.model.hw04;

import cs3500.klondike.model.KlondikeImplementation;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.PlayingCard;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * The WhiteheadKlondike class represents a variant of the Klondike Solitaire card game known as
 * Whitehead Klondike. In this variant, all cards in the cascade piles are dealt face-up,
 * builds must be single-colored (red cards on red cards, black cards on black cards), and
 *when moving multiple cards between cascade piles, they must all be of the same suit.
 */
public class WhiteheadKlondike extends KlondikeImplementation {
  public WhiteheadKlondike() {
    // game is already initialized into a state that is ready to start
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException {
    super.startGame(deck, shuffle, numPiles, numDraw);
    List<Card> deckCopy = new ArrayList<>(deck);
    dealCardsWH(deckCopy, numPiles);
    drawPile = deckCopy;
    this.numDraw = numDraw;
  }

  private void dealCardsWH(List<Card> deck, int numPiles) {
    for (int i = 0; i < numPiles; i++) {
      piles.add(new Stack<>());
    }

    for (int i = 0; i < numPiles; i++) {
      for (int j = i; j < numPiles; j++) {
        PlayingCard card = (PlayingCard) deck.remove(0);
        piles.get(j).push(card);
      }
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    if (srcPile >= piles.size() || srcPile == destPile || piles.get(srcPile).size() < numCards) {
      throw new IllegalArgumentException();
    }

    if (srcPile < 0 || destPile < 0 || numCards <= 0
            || srcPile >= getNumPiles() || destPile >= getNumPiles()
            || piles.get(srcPile).size() < numCards) {
      throw new IllegalArgumentException();
    }

    List<Card> run = piles.get(srcPile)
            .subList(piles.get(srcPile).size() - numCards, piles.get(srcPile).size());
    if (numCards > 1) {
      if (!isSingleSuitRun(run)) {
        throw new IllegalStateException();
      }
    }

    PlayingCard stackingCard = (PlayingCard) run.get(0);
    PlayingCard bottomCard;
    if (!piles.get(destPile).isEmpty()) {
      bottomCard = (PlayingCard) piles.get(destPile).peek();
    } else {
      bottomCard = null;
    }

    if (!isMoveAllowableWH(bottomCard, stackingCard)) {
      throw new IllegalStateException();
    }

    movePileHelper(srcPile, numCards, destPile);
  }

  private boolean isMoveAllowableWH(PlayingCard baseCard, PlayingCard stackCard) {
    if (baseCard == null) {
      return true;
    }
    if (!baseCard.color.equals(stackCard.color)) {
      return false;
    }
    return stackCard.getNumericalValue() == baseCard.getNumericalValue() - 1;
  }

  private boolean isSingleSuitRun(List<Card> run) {
    if (run.isEmpty()) {
      return false;
    }

    PlayingCard card1 = (PlayingCard) run.get(0);
    for (int i = 1; i < run.size(); i++) {
      PlayingCard currentCard = (PlayingCard) run.get(i);

      if (!currentCard.suit.equals(card1.suit)
              || currentCard.getNumericalValue() != card1.getNumericalValue() - i) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    moveDrawHelper(destPile);

    PlayingCard drawCard = (PlayingCard) drawPile.remove(0);
    PlayingCard baseCard;

    try {
      baseCard = (PlayingCard) piles.get(destPile).peek();
    } catch (EmptyStackException e) {
      baseCard = null;
    }

    if (!isMoveAllowableWH(baseCard, drawCard)) {
      throw new IllegalStateException();
    }
    piles.get(destPile).push(drawCard);
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) throws IllegalStateException {
    moveToFoundationHelper(srcPile, foundationPile);

    PlayingCard card = (PlayingCard) piles.get(srcPile).peek();
    PlayingCard foundCard;

    if (!foundationPiles.get(foundationPile).isEmpty()) {
      foundCard = (PlayingCard) foundationPiles.get(foundationPile).peek();
    } else {
      foundCard = null;
    }

    if (foundCard == null) {
      if (card.getNumericalValue() == 1) {
        foundationPiles.get(foundationPile).push(card);
        piles.get(srcPile).pop();
      } else {
        throw new IllegalStateException();
      }
      return;
    }

    if (!isMoveAllowableFoundationWH(foundCard, card)) {
      throw new IllegalStateException();
    }

    foundationPiles.get(foundationPile).push(card);
    piles.get(srcPile).pop();
  }

  private boolean isMoveAllowableFoundationWH(PlayingCard card1, PlayingCard card2) {
    PlayingCard baseCard = card1;
    PlayingCard stackCard = card2;
    if (baseCard.suit != stackCard.suit) {
      return false;
    }
    return baseCard.getNumericalValue() == stackCard.getNumericalValue() - 1;
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException {
    moveDrawToFoundationHelper(foundationPile);

    PlayingCard card = (PlayingCard) drawPile.remove(0);

    PlayingCard foundCard;
    if (!foundationPiles.get(foundationPile).isEmpty()) {
      foundCard = (PlayingCard) foundationPiles.get(foundationPile).peek();
    } else {
      foundCard = null;
    }

    if (foundCard == null) {
      if (card.getNumericalValue() == 1) {
        foundationPiles.get(foundationPile).push(card);
      } else {
        throw new IllegalStateException();
      }
      return;
    }

    if (!isMoveAllowableFoundationWH(foundCard, card)) {
      throw new IllegalStateException();
    }

    foundationPiles.get(foundationPile).push(card);
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    try {
      getCardAtHelper(pileNum, card);

      PlayingCard playingCard = (PlayingCard) piles.get(pileNum).get(card);
      return playingCard;
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalStateException {
    isCardVisibleHelper(pileNum, card);

    try {
      PlayingCard playingCard = (PlayingCard) piles.get(pileNum).get(card);
      if (playingCard == null) {
        throw new IllegalArgumentException();
      }
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException();
    }
    return true;
  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    for (int i = 0; i < getNumPiles(); i++) {
      if (getPileHeight(i) != 0) {
        return false;
      }
    }

    return getDrawCards().isEmpty();
  }
}
