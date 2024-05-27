import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main {
    private static Game game;
    private static Statistics stats = new Statistics();
    private static WordDatabase db = new WordDatabase();
    private static JLabel wordLabel;
    private static JLabel attemptsLabel;
    private static JPanel lettersPanel;
    private static int currentDifficulty;

    public static void main(String[] args) {
        addWordsToDatabase();

        JFrame frame = new JFrame("Hangman Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);

        startNewGame(1);
    }

    private static void addWordsToDatabase() {
        // Poziom łatwy
        db.addWord("apple", 1);
        db.addWord("banana", 1);
        db.addWord("grape", 1);
        db.addWord("orange", 1);
        db.addWord("peach", 1);
        db.addWord("pear", 1);
        db.addWord("plum", 1);
        db.addWord("kiwi", 1);
        db.addWord("melon", 1);
        db.addWord("berry", 1);

        // Poziom średni
        db.addWord("elephant", 2);
        db.addWord("giraffe", 2);
        db.addWord("dolphin", 2);
        db.addWord("kangaroo", 2);
        db.addWord("penguin", 2);
        db.addWord("ostrich", 2);
        db.addWord("hippopotamus", 2);
        db.addWord("alligator", 2);
        db.addWord("crocodile", 2);
        db.addWord("chimpanzee", 2);

        // Poziom trudny
        db.addWord("substitution", 3);
        db.addWord("encyclopedia", 3);
        db.addWord("communication", 3);
        db.addWord("transformation", 3);
        db.addWord("unbelievable", 3);
        db.addWord("extraordinary", 3);
        db.addWord("misunderstanding", 3);
        db.addWord("international", 3);
        db.addWord("characteristic", 3);
    }

    private static void placeComponents(JPanel panel) {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        panel.add(topPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Hangman Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(titleLabel);

        wordLabel = new JLabel();
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(wordLabel);

        attemptsLabel = new JLabel();
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(attemptsLabel);

        lettersPanel = new JPanel();
        lettersPanel.setLayout(new GridLayout(4, 7, 5, 5));
        panel.add(lettersPanel, BorderLayout.CENTER);

        for (char c = 'A'; c <= 'Z'; c++) {
            JButton letterButton = new JButton(String.valueOf(c));
            letterButton.setFont(new Font("Arial", Font.PLAIN, 18));
            letterButton.setPreferredSize(new Dimension(40, 40));
            letterButton.addActionListener(new LetterButtonListener());
            lettersPanel.add(letterButton);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel editWordPanel = new JPanel();
        editWordPanel.setLayout(new FlowLayout());

        JButton editWordButton = new JButton("Manage Words");
        editWordPanel.add(editWordButton);

        editWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showManageWordsDialog();
            }
        });

        bottomPanel.add(editWordPanel);

        JPanel newGamePanel = new JPanel();
        newGamePanel.setLayout(new FlowLayout());

        JButton newGameButton = new JButton("Change difficulty");
        newGamePanel.add(newGameButton);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Easy", "Medium", "Hard"};
                int difficulty = JOptionPane.showOptionDialog(null, "Select difficulty level:",
                        "Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                startNewGame(difficulty + 1);  // Easy=1, Medium=2, Hard=3
            }
        });

        bottomPanel.add(newGamePanel);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout());

        JButton statsButton = new JButton("Show Statistics");
        statsPanel.add(statsButton);

        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStatistics();
            }
        });

        bottomPanel.add(statsPanel);
    }

    private static void startNewGame(int difficulty) {
        currentDifficulty = difficulty;
        String word = db.getRandomWord(difficulty);
        game = new Game(word, difficulty);
        updateGameState();
        
        for (Component component : lettersPanel.getComponents()) {
            component.setEnabled(true);
        }
    }

    private static void updateGameState() {
        wordLabel.setText(game.getMaskedWord());
        attemptsLabel.setText("Attempts: " + game.getAttempts() + " / " + game.getMaxAttempts());
        if (game.isGameOver()) {
            if (game.isWon()) {
                stats.recordWin();
                db.addResult("win", game.getAttempts(), currentDifficulty);
                JOptionPane.showMessageDialog(null, "Congratulations! You've won!");
            } else {
                stats.recordLoss();
                db.addResult("loss", game.getAttempts(), currentDifficulty);
                JOptionPane.showMessageDialog(null, "Sorry, you've lost. The word was: " + game.getWord());
            }
            startNewGame(currentDifficulty);
        }
    }

    private static void showStatistics() {
        WordDatabase.SummaryStats summary = db.getSummaryStats();
        List<String> stats = db.getStatistics();
        StringBuilder statsText = new StringBuilder();

        statsText.append("Total Wins: ").append(summary.getWins()).append("\n");
        statsText.append("Total Losses: ").append(summary.getLosses()).append("\n");
        statsText.append("Total Attempts: ").append(summary.getTotalAttempts()).append("\n\n");

        for (String stat : stats) {
            statsText.append(stat).append("\n");
        }

        JTextArea textArea = new JTextArea(statsText.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(null, scrollPane, "Game Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showManageWordsDialog() {
        JDialog dialog = new JDialog((Frame) null, "Manage Words", true);
        dialog.setSize(800, 400);

        List<WordDatabase.WordEntry> words = db.getAllWords();
        String[] wordList = new String[words.size()];
        for (int i = 0; i < words.size(); i++) {
            wordList[i] = words.get(i).toString();
        }

        JPanel panel = new JPanel(new GridLayout(1, 3));
        
        JPanel addWordPanel = new JPanel();
        addWordPanel.setLayout(new GridLayout(4, 2));
        addWordPanel.setBorder(BorderFactory.createTitledBorder("Add New Word"));

        addWordPanel.add(new JLabel("Word:"));
        JTextField addWordField = new JTextField(10);
        addWordPanel.add(addWordField);

        addWordPanel.add(new JLabel("Difficulty:"));
        JComboBox<String> addDifficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        addWordPanel.add(addDifficultyComboBox);

        JButton addWordButton = new JButton("Add Word");
        addWordPanel.add(addWordButton);
        addWordPanel.add(new JLabel(""));
        
        JPanel editWordPanel = new JPanel();
        editWordPanel.setLayout(new GridLayout(5, 2));
        editWordPanel.setBorder(BorderFactory.createTitledBorder("Edit Existing Word"));

        editWordPanel.add(new JLabel("Select Word:"));
        JComboBox<String> wordComboBox = new JComboBox<>(wordList);
        editWordPanel.add(wordComboBox);

        editWordPanel.add(new JLabel("New Word:"));
        JTextField newWordField = new JTextField(10);
        editWordPanel.add(newWordField);

        editWordPanel.add(new JLabel("New Difficulty:"));
        JComboBox<String> difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        editWordPanel.add(difficultyComboBox);

        JButton updateWordButton = new JButton("Update Word");
        editWordPanel.add(updateWordButton);
        editWordPanel.add(new JLabel(""));
        
        JPanel deleteWordPanel = new JPanel();
        deleteWordPanel.setLayout(new GridLayout(3, 1));
        deleteWordPanel.setBorder(BorderFactory.createTitledBorder("Delete Word"));

        JComboBox<String> deleteWordComboBox = new JComboBox<>(wordList);
        deleteWordPanel.add(new JLabel("Select Word:"));
        deleteWordPanel.add(deleteWordComboBox);

        JButton deleteWordButton = new JButton("Delete Word");
        deleteWordPanel.add(deleteWordButton);
        
        panel.add(addWordPanel);
        panel.add(editWordPanel);
        panel.add(deleteWordPanel);
        
        addWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newWord = addWordField.getText().toUpperCase();
                int difficulty = addDifficultyComboBox.getSelectedIndex() + 1;
                if (!newWord.isEmpty()) {
                    db.addWord(newWord, difficulty);
                    JOptionPane.showMessageDialog(dialog, "Word added successfully!");
                    addWordField.setText("");
                    refreshManageWordsDialog(dialog);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter a word.");
                }
            }
        });

        updateWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = wordComboBox.getSelectedIndex();
                if (selectedIndex >= 0) {
                    WordDatabase.WordEntry selectedWord = words.get(selectedIndex);
                    String newWord = newWordField.getText().toUpperCase();
                    int newDifficulty = difficultyComboBox.getSelectedIndex() + 1;
                    if (!newWord.isEmpty()) {
                        db.updateWord(selectedWord.getId(), newWord, newDifficulty);
                        JOptionPane.showMessageDialog(dialog, "Word updated successfully!");
                        refreshManageWordsDialog(dialog);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Please enter a new word.");
                    }
                }
            }
        });

        deleteWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = deleteWordComboBox.getSelectedIndex();
                if (selectedIndex >= 0) {
                    WordDatabase.WordEntry selectedWord = words.get(selectedIndex);
                    db.deleteWord(selectedWord.getId());
                    JOptionPane.showMessageDialog(dialog, "Word deleted successfully!");
                    refreshManageWordsDialog(dialog);
                }
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static void refreshManageWordsDialog(JDialog dialog) {
        dialog.dispose();
        showManageWordsDialog();
    }


    static class LetterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String letter = button.getText().toLowerCase();
            button.setEnabled(false);
            game.guess(letter.charAt(0));
            updateGameState();
        }
    }
}
