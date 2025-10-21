package main.java.myApp.model;

public enum Race {
    CAUCASOID("CAUCASOID"),
    NEGROID("NEGROID"),
    MONGOLOID("MONGOLOID"),
    ASIAN("ASIAN"),
    PACIFIC_ISLAND_AND_AUSTRALIAN("PACIFIC_ISLAND_AND_AUSTRALIAN"),
    AMERINDIANS_AND_ESKIMOS("AMERINDIANS_AND_ESKIMOS");

    private final String dbValue;

    Race(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
    /**
     * @param dbString The string from the database.
     * @return The corresponding Race enum.
     * @throws IllegalArgumentException if the dbString does not match any known Race.
     */
    public static Race fromDbString(String dbString) {
        for (Race race : Race.values()) {
            if (race.dbValue.equalsIgnoreCase(dbString)) {
                return race;
            }
        }
        throw new IllegalArgumentException("Unknown Race string from DB: " + dbString);
    }
}