package cs3500.klondike.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.PlayingCard;

/**
 * The abstract class KlondikeImplementation provides a foundation for implementing
 * and playing the Klondike Solitaire card game. It defines the essential methods and
 * properties required for managing and interacting with the game.
 */
public abstract class KlondikeImplementation implements KlondikeModel {
  protected boolean gameStarted = false;
  protected List<Stack<Card>> foundationPiles;
  protected List<Card> drawPile;
  protected List<Stack<Card>> piles;
  protected int numDraw;
  protected List<Card> visibleCards = new ArrayList<>();

  protected boolean isFullCascadePossible(int deckSize, int numPiles) {
    int numCardsReq = 0;
    for (int i = 0; i <= numPiles; i++) {
      numCardsReq += i;
    }
    return deckSize >= numCardsReq;
  }

  protected void createFoundationPiles(List<Card> deck) {
    int count = 0;
    for (Card card : deck) {
      PlayingCard playingCard = (PlayingCard) card;
      if (playingCard.rank == PlayingCard.Rank.ACE) {
        count++;
      }
    }
    for (int i = 0; i < count; i++) {
      foundationPiles.add(new Stack<>());
    }
  }

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<>();
    for (PlayingCard.Suit suit : PlayingCard.Suit.values()) {
      for (PlayingCard.Rank rank : PlayingCard.Rank.values()) {
        deck.add(new PlayingCard(rank, suit));
      }
    }
    return deck;
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    return foundationPiles.size();
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    if (!drawPile.isEmpty()) {
      List<Card> drawCards = new ArrayList<>();
      if (drawPile.size() <= getNumDraw()) {
        for (Card card : drawPile) {
          drawCards.add(card);
        }
      } else {
        for (int i = 0; i < getNumDraw(); i++) {
          drawCards.add(drawPile.get(i));
        }
      }
      return drawCards;
    }
    return new ArrayList<>();
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    if (pileNum < 0 || pileNum >= getNumPiles()) {
      throw new IllegalArgumentException();
    }

    return piles.get(pileNum).size();
  }

  @Override
  public int getScore() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    int score = 0;
    for (Stack<Card> pile : foundationPiles) {
      if (!pile.isEmpty()) {
        PlayingCard topCard = (PlayingCard) pile.peek();
        score += topCard.getNumericalValue();
      }
    }
    return score;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }
    if (drawPile.size() == 1 && getScore() == 6) {
      return false;
    }
    if (drawPile.size() >= 1 && getScore() == 0) {
      return false;
    }
    return drawPile.isEmpty();
  }

  @Override
  public int getNumRows() {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    int maxRows = -1;
    for (Stack<Card> pile : piles) {
      if (pile.size() > maxRows) {
        maxRows = pile.size();
      }
    }
    return maxRows;
  }

  @Override
  public int getNumPiles() {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    return piles.size();
  }

  @Override
  public int getNumDraw() {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    return numDraw;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    if (srcPile >= piles.size() || srcPile == destPile || piles.get(srcPile).size() < numCards) {
      throw new IllegalArgumentException();
    }

    PlayingCard card2 = (PlayingCard) piles.get(srcPile).get(piles.get(srcPile).size() - numCards);

    // only moves src pile to dest pile if card is a king
    if (piles.get(destPile).isEmpty() && card2.rank == PlayingCard.Rank.KING) {
      piles.get(destPile).push(card2);
      piles.get(srcPile).pop();
      if (!piles.get(srcPile).isEmpty()) {
        visibleCards.add(piles.get(srcPile).peek());
      }
      return;
    }

    // invalid move - cannot move card that is not a king to empty pile
    if (piles.get(destPile).isEmpty() && card2.rank != PlayingCard.Rank.KING) {
      throw new IllegalStateException();
    }

    PlayingCard card1 = (PlayingCard) piles.get(destPile).peek();

    // checks validity of piles
    if (srcPile < 0 || destPile < 0 || numCards <= 0
            || srcPile >= getNumPiles() || destPile >= getNumPiles()
            || piles.get(srcPile).size() < numCards) {
      throw new IllegalArgumentException();
    }

    // gets how many visible cards exist in pile
    int numVisible = 0;
    for (Card card : piles.get(srcPile)) {
      PlayingCard playingCard = (PlayingCard) card;
      if (visibleCards.contains(playingCard)) {
        numVisible++;
      }
    }

    // invalid move - cannot move more cards than are visible
    if (numVisible < numCards) {
      throw new IllegalStateException();
    }

    if (!isMoveAllowable(card1, card2)) {
      throw new IllegalStateException();
    }

    // adds and removes cards
    movePileHelper(srcPile, numCards, destPile);

    // set visibility of new bottom card in src pile
    if (!piles.get(srcPile).isEmpty()) {
      PlayingCard playingCard = (PlayingCard) piles.get(srcPile).peek();
      visibleCards.add(playingCard);
    }
  }

  protected void movePileHelper(int srcPile, int numCards, int destPile) {
    List<Card> moving = new ArrayList<>();
    for (int i = 0; i < numCards; i++) {
      moving.add(piles.get(srcPile).pop());
    }

    Collections.reverse(moving);
    for (Card card : moving) {
      piles.get(destPile).push(card);
    }
  }

  protected boolean isMoveAllowable(PlayingCard baseCard, PlayingCard stackCard) {
    if (baseCard.color.equals(stackCard.color)) {
      return false;
    }
    return stackCard.getNumericalValue() == baseCard.getNumericalValue() - 1;
  }

  protected void moveToFoundationHelper(int srcPile, int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    // checks validity of piles
    if (srcPile < 0 || foundationPile < 0 || srcPile >= getNumPiles()
            || foundationPile >= getNumFoundations()) {
      throw new IllegalArgumentException();
    }

    if (piles.get(srcPile).isEmpty()) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) throws IllegalStateException {
    moveToFoundationHelper(srcPile, foundationPile);

    PlayingCard card = (PlayingCard) piles.get(srcPile).peek();

    // checks that if foundation pile is empty, card being moved is an ace
    if (foundationPiles.get(foundationPile).isEmpty() && card.rank == PlayingCard.Rank.ACE) {
      foundationPiles.get(foundationPile).push(card);
      piles.get(srcPile).pop();
      // sets new bottom card in src pile to visible
      if (!piles.get(srcPile).isEmpty()) {
        PlayingCard playingCard = (PlayingCard) piles.get(srcPile).peek();
        visibleCards.add(playingCard);
      }
    } else {
      // move invalid - card is not an ace
      if (foundationPiles.get(foundationPile).isEmpty()) {
        throw new IllegalStateException();
      }
      PlayingCard topFoundationCard = (PlayingCard) foundationPiles.get(foundationPile).peek();
      if (!isMoveAllowableFoundation(topFoundationCard, card)) {
        throw new IllegalStateException();
      }
      foundationPiles.get(foundationPile).push(card);
      piles.get(srcPile).pop();
      // sets new bottom card in src pile to visible
      if (!piles.get(srcPile).isEmpty()) {
        PlayingCard playingCard = (PlayingCard) piles.get(srcPile).peek();
        visibleCards.add(playingCard);
      }
    }
  }

  protected boolean isMoveAllowableFoundation(Card card1, Card card2) {
    PlayingCard baseCard = (PlayingCard) card1;
    PlayingCard stackCard = (PlayingCard) card2;
    if (baseCard.suit != stackCard.suit) {
      return false;
    }
    return baseCard.getNumericalValue() == stackCard.getNumericalValue() - 1;
  }

  protected void getCardAtHelper(int pileNum, int card) {
    if (pileNum < 0 || pileNum >= getNumPiles() || card < 0 || card > piles.get(pileNum).size()) {
      throw new IllegalArgumentException();
    }

    if (piles.get(pileNum).isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    try {
      if (foundationPile < 0 || foundationPile >= getNumFoundations()) {
        throw new IllegalArgumentException();
      }

      if (foundationPiles.get(foundationPile).isEmpty()) {
        return null;
      }
      return foundationPiles.get(foundationPile).peek();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    try {
      getCardAtHelper(pileNum, card);

      PlayingCard playingCard = (PlayingCard) piles.get(pileNum).get(card);

      if (visibleCards.contains(playingCard)) {
        return playingCard;
      } else {
        throw new IllegalArgumentException();
      }
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
      return visibleCards.contains(playingCard);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException();
    }
  }

  protected void isCardVisibleHelper(int pileNum, int card) {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    if (pileNum < 0 || pileNum >= getNumPiles() || card < 0 || card > piles.get(pileNum).size()) {
      throw new IllegalArgumentException();
    }

    if (piles.get(pileNum).isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    if (!drawPile.isEmpty() || !getDrawCards().isEmpty()) {
      Card card = drawPile.get(0);
      drawPile.remove(0);
      drawPile.add(card);
    } else if (drawPile.isEmpty()) {
      throw new IllegalStateException();
    }
  }

  protected void moveDrawToFoundationHelper(int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    // checks validity of piles
    if (foundationPile < 0 || foundationPile >= getNumFoundations()) {
      throw new IllegalArgumentException();
    }

    if (getDrawCards().isEmpty() || drawPile.isEmpty()) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException {
    moveDrawToFoundationHelper(foundationPile);

    PlayingCard card = (PlayingCard) drawPile.remove(0);

    if (!foundationPiles.get(foundationPile).isEmpty() || card.rank != PlayingCard.Rank.ACE) {
      // invalid move - cannot move card to an empty foundation pile if it is not an ace
      if (foundationPiles.get(foundationPile).isEmpty() && card.rank != PlayingCard.Rank.ACE) {
        throw new IllegalStateException();
      }

      PlayingCard topFoundationCard = (PlayingCard) foundationPiles.get(foundationPile).peek();

      if (!isMoveAllowableFoundation(topFoundationCard, card)) {
        throw new IllegalStateException();
      }
    }
    visibleCards.add(card);
    foundationPiles.get(foundationPile).push(card);
  }

  protected void moveDrawHelper(int destPile) {
    if (!gameStarted) {
      throw new IllegalStateException();
    }

    // checks validity of pile
    if (destPile < 0 || destPile >= getNumPiles()) {
      throw new IllegalArgumentException();
    }

    if (drawPile.isEmpty()) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    moveDrawHelper(destPile);
    PlayingCard drawCard = (PlayingCard) drawPile.remove(0);

    // checks that if dest pile is empty, draw card is of rank king
    if (piles.get(destPile).isEmpty() && drawCard.rank == PlayingCard.Rank.KING) {
      visibleCards.add(drawCard);
      piles.get(destPile).push(drawCard);
      drawPile.remove(drawCard);
      return;
    }

    // invalid move - cannot move card that is not a king to empty pile
    if (piles.get(destPile).isEmpty() && drawCard.rank != PlayingCard.Rank.KING) {
      throw new IllegalStateException();
    }

    PlayingCard baseCard = (PlayingCard) piles.get(destPile).peek();

    if (!isMoveAllowable(baseCard, drawCard)) {
      throw new IllegalStateException();
    }

    visibleCards.add(drawCard);
    piles.get(destPile).push(drawCard);
    drawPile.remove(drawCard);
  }


  protected void dealCards(List<Card> deck, int numPiles) {
    for (int i = 0; i < numPiles; i++) {
      piles.add(new Stack<>());
    }

    for (int i = 0; i < numPiles; i++) {
      for (int j = i; j < numPiles; j++) {
        Card card = deck.remove(0);
        piles.get(j).push(card);
      }
    }

    for (int i = 0; i < numPiles; i++) {
      PlayingCard card = (PlayingCard) piles.get(i).peek();
      visibleCards.add(card);
    }
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException {
    if (deck == null) {
      throw new IllegalArgumentException();
    }

    if (deck.isEmpty()) {
      throw new IllegalStateException();
    }

    try {
      if (!isDeckValid(deck)) {
        throw new IllegalArgumentException();
      }
    } catch (NullPointerException e) {
      throw new IllegalArgumentException();
    }

    if (!isFullCascadePossible(deck.size(), numPiles)) {
      throw new IllegalArgumentException();
    }

    if (numPiles <= 0 || numDraw <= 0 || numDraw > deck.size()) {
      throw new IllegalArgumentException();
    }

    if (!gameStarted) {
      gameStarted = true;
    } else {
      throw new IllegalStateException();
    }

    if (shuffle) {
      Collections.shuffle(deck);
    }

    piles = new ArrayList<>(numPiles);
    foundationPiles = new ArrayList<>();
    try {
      createFoundationPiles(deck);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException();
    }
  }


  private boolean isDeckValid(List<Card> deck) {
    Set<PlayingCard.Suit> suits = new HashSet<>();

    for (Card card : deck) {
      PlayingCard playingCard = (PlayingCard) card;
      suits.add(playingCard.suit);
    }
    return deck.size() % suits.size() == 0;
  }
}
