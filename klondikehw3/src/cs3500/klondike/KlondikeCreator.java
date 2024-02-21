package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * The KlondikeCreator class is responsible for creating instances of different Klondike game types.
 * It offers a factory method to create instances of the KlondikeModel interface based on the
 * specified game type.
 */
public class KlondikeCreator {
  /**
   * Enum representing the different Klondike game types that can be created using this class.
   */
  public enum GameType {
    BASIC, LIMITED, WHITEHEAD
  }

  /**
   * Creates and returns an instance of a KlondikeModel based on the specified game type.
   *
   * @param type The game type to create. Should be one of the values from the GameType enum.
   * @return A new instance of KlondikeModel that corresponds to the specified game type.
   */
  public static KlondikeModel create(GameType type) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        return new LimitedDrawKlondike(2);
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException();
    }
  }
}