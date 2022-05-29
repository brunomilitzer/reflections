package section4.data;

import java.util.Arrays;

public class GameConfig {
    private int releaseYear = 2000;
    private String gameName;
    private double price;
    private String[] characterNames;

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getGameName() {
        return gameName;
    }

    public double getPrice() {
        return price;
    }

    public String[] getCharacterNames() {
        return characterNames;
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "releaseYear=" + releaseYear +
                ", gameName='" + gameName + '\'' +
                ", price=" + price +
                ", characterNames=" + Arrays.toString(characterNames) +
                '}';
    }
}
