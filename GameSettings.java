public class GameSettings {
    private static int numDays = 30; // Default game duration
    private static int difficulty = 1; // 1 for easy, 2 for medium, 3 for hard

    public static int getNumDays() {
        return numDays;
    }

    public static void setNumDays(int days) {
        numDays = days;
    }
    
    public static int getDifficulty() {
        return difficulty;
    }
    
    public static void setDifficulty(int dif) {
        difficulty = dif;
    }
}
