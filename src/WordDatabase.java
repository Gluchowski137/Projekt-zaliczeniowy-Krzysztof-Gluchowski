import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordDatabase {
    private Connection conn;

    public WordDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:words.db");
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS words (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "word TEXT NOT NULL," +
                    "difficulty INTEGER NOT NULL)";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS results (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "result TEXT NOT NULL," +
                    "attempts INTEGER NOT NULL," +
                    "difficulty INTEGER NOT NULL)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addWord(String word, int difficulty) {
        String sql = "INSERT INTO words(word, difficulty) VALUES(?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word);
            pstmt.setInt(2, difficulty);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWord(int id, String newWord, int newDifficulty) {
        String sql = "UPDATE words SET word = ?, difficulty = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newWord);
            pstmt.setInt(2, newDifficulty);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWord(int id) {
        String sql = "DELETE FROM words WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getWords(int difficulty) {
        List<String> words = new ArrayList<>();
        String sql = "SELECT word FROM words WHERE difficulty = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, difficulty);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                words.add(rs.getString("word"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public List<WordEntry> getAllWords() {
        List<WordEntry> words = new ArrayList<>();
        String sql = "SELECT id, word, difficulty FROM words";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String word = rs.getString("word");
                int difficulty = rs.getInt("difficulty");
                words.add(new WordEntry(id, word, difficulty));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public String getRandomWord(int difficulty) {
        List<String> words = getWords(difficulty);
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }

    public void addResult(String result, int attempts, int difficulty) {
        String sql = "INSERT INTO results(result, attempts, difficulty) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, result);
            pstmt.setInt(2, attempts);
            pstmt.setInt(3, difficulty);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getStatistics() {
        List<String> stats = new ArrayList<>();
        String sql = "SELECT result, attempts, difficulty FROM results";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String result = rs.getString("result");
                int attempts = rs.getInt("attempts");
                int difficulty = rs.getInt("difficulty");
                String difficultyStr = (difficulty == 1) ? "Easy" : (difficulty == 2) ? "Medium" : "Hard";
                stats.add(String.format("Result: %s, Attempts: %d, Difficulty: %s", result, attempts, difficultyStr));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public SummaryStats getSummaryStats() {
        int wins = 0;
        int losses = 0;
        int totalAttempts = 0;
        String sql = "SELECT result, attempts FROM results";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String result = rs.getString("result");
                int attempts = rs.getInt("attempts");
                totalAttempts += attempts;
                if ("win".equals(result)) {
                    wins++;
                } else if ("loss".equals(result)) {
                    losses++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new SummaryStats(wins, losses, totalAttempts);
    }

    public static class WordEntry {
        private int id;
        private String word;
        private int difficulty;

        public WordEntry(int id, String word, int difficulty) {
            this.id = id;
            this.word = word;
            this.difficulty = difficulty;
        }

        public int getId() {
            return id;
        }

        public String getWord() {
            return word;
        }

        public int getDifficulty() {
            return difficulty;
        }

        @Override
        public String toString() {
            return String.format("ID: %d, Word: %s, Difficulty: %d", id, word, difficulty);
        }
    }

    public static class SummaryStats {
        private int wins;
        private int losses;
        private int totalAttempts;

        public SummaryStats(int wins, int losses, int totalAttempts) {
            this.wins = wins;
            this.losses = losses;
            this.totalAttempts = totalAttempts;
        }

        public int getWins() {
            return wins;
        }

        public int getLosses() {
            return losses;
        }

        public int getTotalAttempts() {
            return totalAttempts;
        }
    }
}
