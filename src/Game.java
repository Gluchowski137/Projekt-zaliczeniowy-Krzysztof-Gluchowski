import java.util.HashSet;
import java.util.Set;

public class Game {
    private String word;
    private Set<Character> guessedLetters;
    private int attempts;
    private int difficulty;
    private int maxAttempts;

    public Game(String word, int difficulty) {
        this.word = word;
        this.difficulty = difficulty;
        this.guessedLetters = new HashSet<>();
        this.attempts = 0;
        setMaxAttempts();
    }

    private void setMaxAttempts() {
        switch (difficulty) {
            case 1:
                this.maxAttempts = 23;
                break;
            case 2:
                this.maxAttempts = 13;
                break;
            case 3:
                this.maxAttempts = 8; 
                break;
        }
    }

    public boolean guess(char letter) {
        guessedLetters.add(letter);
        if (!word.contains(String.valueOf(letter))) {
            attempts++;
        }
        return word.contains(String.valueOf(letter));
    }

    public String getMaskedWord() {
        StringBuilder masked = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (guessedLetters.contains(c)) {
                masked.append(c);
            } else {
                masked.append("_ ");
            }
        }
        return masked.toString();
    }

    public boolean isGameOver() {
        return getMaskedWord().equals(word) || attempts >= maxAttempts;
    }

    public boolean isWon() {
        return getMaskedWord().equals(word);
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getWord() {
        return word;
    }
}
