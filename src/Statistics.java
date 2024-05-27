public class Statistics {
    private int wins;
    private int losses;

    public Statistics() {
        this.wins = 0;
        this.losses = 0;
    }

    public void recordWin() {
        wins++;
    }

    public void recordLoss() {
        losses++;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
}
