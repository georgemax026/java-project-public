package main.java.myApp.model;

public enum Genre {
    ACTION("ACTION"),
    ADULT("ADULT"),
    ADVENTURE("ADVENTURE"),
    ANIMATION("ANIMATION"),
    BIOGRAPHY("BIOGRAPHY"),
    COMEDY("COMEDY"),
    CRIME("CRIME"),
    DOCUMENTARY("DOCUMENTARY"),
    DRAMA("DRAMA"),
    FAMILY("FAMILY"),
    FANTASY("FANTASY"),
    FILM_NOIR("FILM_NOIR"),
    GAME_SHOW("GAME_SHOW"),
    HISTORY("HISTORY"),
    HORROR("HORROR"),
    MUSICAL("MUSICAL"),
    MUSIC("MUSIC"),
    MYSTERY("MYSTERY"),
    ROMANCE("ROMANCE"),
    SCI_FI("SCI_FI"),
    SPORT("SPORT"),
    THRILLER("THRILLER"),
    WAR("WAR"),
    WESTERN("WESTERN");

    private final String dbValue;

    Genre(String dbValue) {
        this.dbValue = dbValue;
    }
    public String getDbValue() {
        return dbValue;
    }

    /**
     * Converts a database string value to a Genre enum.
     * @param dbString The string from the database.
     * @return The corresponding Genre enum.
     * @throws IllegalArgumentException if the dbString does not match any known Genre.
     */
    public static Genre fromDbString(String dbString) {
        for (Genre genre : Genre.values()) {
            if (genre.dbValue.equalsIgnoreCase(dbString)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre from DB: " + dbString);
    }
}
