package cs3500.klondike.model.hw02;

/**
 * The PlayingCard class represents a playing card with a specific rank and suit.
 */
public class PlayingCard implements Card {
  public final Rank rank;
  public final Suit suit;
  public final Color color;

  /**
   * The Suit enum represents the suits of playing cards: Spades, Clubs, Hearts, and Diamonds.
   */
  public enum Suit {
    Spades("♠"), Clubs("♣"), Hearts("♡"), Diamonds("♢");

    final String stringValue;

    Suit(String s) {
      this.stringValue = s;
    }
  }

  /**
   * The Rank enum represents the ranks of playing cards: ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN,
   * EIGHT, NINE, TEN, JACK, QUEEN, KING.
   */
  public enum Rank {
    ACE(1, "A"), TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"), SEVEN(7, "7"),
    EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"), JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K");

    final int numericalValue;
    final String stringValue;

    Rank(int value, String stringValue) {
      this.numericalValue = value;
      this.stringValue = stringValue;
    }
  }

  /**
   * The Color enum represents the colors of playing cards: BLACK and RED.
   */
  public enum Color {
    BLACK, RED
  }

  /**
   * The PlayingCard class represents a playing card with a specific rank and suit.
   */
  public PlayingCard(Rank value, Suit suit) {
    this.rank = value;
    this.suit = suit;

    switch (suit) {
      case Hearts:
      case Diamonds:
        this.color = Color.RED;
        break;
      case Clubs:
      case Spades:
        this.color = Color.BLACK;
        break;
      default:
        this.color = null;
    }
  }

  /**
   * Returns the numerical value associated with the rank of this playing card.
   */
  public int getNumericalValue() {
    return rank.numericalValue;
  }

  @Override
  public String toString() {
    return rank.stringValue + suit.stringValue;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    PlayingCard otherCard = (PlayingCard) obj;

    return suit == otherCard.suit && rank == otherCard.rank;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result * rank.hashCode();
    result = 31 * result * suit.hashCode();
    return result;
  }
}