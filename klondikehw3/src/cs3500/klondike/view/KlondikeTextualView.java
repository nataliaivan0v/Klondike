package cs3500.klondike.view;

import java.io.IOException;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.PlayingCard;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private final Appendable output;

  public KlondikeTextualView(KlondikeModel model) {
    this(model, null);
  }

  public KlondikeTextualView(KlondikeModel model, Appendable output) {
    this.model = model;
    this.output = output;
  }

  @Override
  public void render() throws IOException {
    output.append(this.toString());
  }

  /**
   * Retrieves a String representation of the current board, suitable for display.
   *
   * @return A String containing the current state of the board, formatted for display.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(drawLine()).append("\n");
    sb.append(foundationLine()).append("\n");
    for (int i = 0; i < model.getNumRows(); i++) {
      sb.append(pileLines(i));
      if (i != model.getNumRows() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  private String pileLines(int currRow) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < model.getNumPiles(); i++) {
      if (model.getPileHeight(i) == 0) {
        if (currRow == 0) {
          sb.append("  X");
        } else {
          sb.append("   ");
        }
        continue;
      }

      PlayingCard card;
      try {
        card = (PlayingCard) model.getCardAt(i, currRow);
      } catch (IllegalArgumentException e) {
        if (model.getPileHeight(i) > currRow) {
          sb.append("  ?");
        } else {
          sb.append("   ");
        }
        continue;
      }

      try {
        if (model.isCardVisible(i, currRow)) {
          String cardString = card.toString();
          if (cardString.length() == 2) {
            sb.append(" ").append(cardString);
          } else {
            sb.append(cardString);
          }
        } else {
          sb.append("  ?");
        }
      } catch (NullPointerException e) {
        if (model.getPileHeight(i) < currRow - 1) {
          sb.append("  ?");
        } else {
          sb.append("   ");
        }
      }
    }
    return sb.toString();
  }

  private String foundationLine() {
    StringBuilder sb = new StringBuilder();
    sb.append("Foundation: ");
    for (int i = 0; i < model.getNumFoundations(); i++) {
      if (model.getCardAt(i) == null) {
        sb.append("<none>");
      } else {
        PlayingCard card = (PlayingCard) model.getCardAt(i);
        sb.append(card.toString());
      }
      if (i != model.getNumFoundations() - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  private String drawLine() {
    StringBuilder sb = new StringBuilder();
    sb.append("Draw: ");
    if (model.getDrawCards().isEmpty()) {
      return sb.toString();
    }
    for (int i = 0; i < model.getDrawCards().size(); i++) {
      PlayingCard card = (PlayingCard) model.getDrawCards().get(i);
      sb.append(card.toString());
      if (i != model.getDrawCards().size() - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }
}


