public class GameSettings {
    private static int numDays = 30; // Default game duration

    public static int getNumDays() {
        return numDays;
    }

    public static void setNumDays(int days) {
        numDays = days;
    }
}
