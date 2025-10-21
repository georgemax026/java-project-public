package main.java.myApp.model;

public enum Gender {
    MALE("M"),
    FEMALE("F");

    private final String dbValue;

    Gender(String dbValue) {
        this.dbValue = dbValue;
    }
    public String getDbValue() {
        return dbValue;
    }
    /**
     * Converts a database string value to a Gender enum.
     * @param dbString The string from the database.
     * @return The corresponding Gender enum.
     * @throws IllegalArgumentException if the dbString does not match any known Gender.
     */
    public static Gender fromDbString(String dbString) {
        for (Gender gender : Gender.values()) {
            if (gender.dbValue.equalsIgnoreCase(dbString)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown Gender string from DB: " + dbString);
    }
}
