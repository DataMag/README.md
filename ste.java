import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ste {
    private static char[][] oyunAlani = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
    private static char aktifplayer = 'X';
    private static JFrame oyunPenceresi;
    private static JButton[][] alanButonlari = new JButton[3][3];
    private static JLabel siraLabel;
    private static JLabel analizLabel; // Новая надпись для шансов

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ste::oyunPenceresiOlustur);
    }

    private static void oyunPenceresiOlustur() {
        oyunPenceresi = new JFrame("Tic-Tac-Toe с аналитикой");
        oyunPenceresi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        oyunPenceresi.setLayout(new BorderLayout());

        JPanel alanPaneli = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                alanButonlari[i][j] = new JButton(" ");
                alanButonlari[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                alanButonlari[i][j].setFocusable(false);
                alanButonlari[i][j].addActionListener(new ButonTiklamaIsleyici(i, j));
                alanPaneli.add(alanButonlari[i][j]);
            }
        }

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        siraLabel = new JLabel("Очередь: X", JLabel.CENTER);
        analizLabel = new JLabel("Рекомендация: любой центр или угол", JLabel.CENTER);
        siraLabel.setFont(new Font("Arial", Font.BOLD, 16));
        analizLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        analizLabel.setForeground(Color.BLUE);

        infoPanel.add(siraLabel);
        infoPanel.add(analizLabel);

        oyunPenceresi.add(alanPaneli, BorderLayout.CENTER);
        oyunPenceresi.add(infoPanel, BorderLayout.SOUTH);

        oyunPenceresi.setSize(450, 500);
        oyunPenceresi.setLocationRelativeTo(null);
        oyunPenceresi.setVisible(true);
    }

    // --- ЛОГИКА АНАЛИЗА ---

    private static void обновитьАналитику() {
        if (kazananKontrol() || alanDoluMu()) {
            analizLabel.setText("Игра окончена");
            return;
        }

        int winX = 0, winO = 0, draws = 0;
        int bestVal = -1000;
        String recommendation = "";

        // Просчитываем каждый возможный ход для текущего игрока
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (oyunAlani[i][j] == ' ') {
                    oyunAlani[i][j] = aktifplayer;
                    int moveVal = minimax(oyunAlani, 0, false);

                    // Статистика для шансов (упрощенно)
                    if (moveVal > 0) winX++;
                    else if (moveVal < 0) winO++;
                    else draws++;

                    if (moveVal > bestVal) {
                        bestVal = moveVal;
                        recommendation = "Лучший ход: ряд " + (i + 1) + ", кол " + (j + 1);
                    }
                    oyunAlani[i][j] = ' ';
                }
            }
        }

        analizLabel.setText(recommendation);
    }

    private static int minimax(char[][] board, int depth, boolean isMax) {
        if (checkWin(board, 'X')) return 10 - depth;
        if (checkWin(board, 'O')) return depth - 10;
        if (isBoardFull(board)) return 0;

        if (isMax) {
            int best = -1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        } else {
            int best = 1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        }
    }

    // Вспомогательные методы для анализа (без статики основного поля)
    private static boolean checkWin(char[][] b, char p) {
        for (int i = 0; i < 3; i++) {
            if (b[i][0] == p && b[i][1] == p && b[i][2] == p) return true;
            if (b[0][i] == p && b[1][i] == p && b[2][i] == p) return true;
        }
        return (b[0][0] == p && b[1][1] == p && b[2][2] == p) || (b[0][2] == p && b[1][1] == p && b[2][0] == p);
    }

    private static boolean isBoardFull(char[][] b) {
        for (char[] r : b) for (char c : r) if (c == ' ') return false;
        return true;
    }

    // --- СТАНДАРТНЫЕ МЕТОДЫ ИГРЫ ---

    private static boolean kazananKontrol() {
        return checkWin(oyunAlani, aktifplayer);
    }

    private static boolean alanDoluMu() {
        return isBoardFull(oyunAlani);
    }

    private static void butonlariKapat() {
        for (JButton[] row : alanButonlari) for (JButton b : row) b.setEnabled(false);
    }

    private static class ButonTiklamaIsleyici implements ActionListener {
        private int r, c;

        public ButonTiklamaIsleyici(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (oyunAlani[r][c] == ' ') {
                // 1. Твой ход (Крестик)
                сделатьЛогикуХода(r, c);

                // 2. Если игра не закончилась и сейчас очередь Бота (O)
                if (aktifplayer == 'O') {
                    ходБота();
                }
            }
        }

        // Вынес логику в отдельный метод, чтобы и ты, и бот могли её использовать
        private void сделатьЛогикуХода(int row, int col) {
            oyunAlani[row][col] = aktifplayer;
            alanButonlari[row][col].setText(String.valueOf(aktifplayer));
            alanButonlari[row][col].setBackground(aktifplayer == 'X' ? Color.CYAN : Color.PINK);

            if (kazananKontrol()) {
                siraLabel.setText("Oyuncu " + aktifplayer + " kazandı");
                analizLabel.setText("Конец игры");
                butonlariKapat();
            } else if (alanDoluMu()) {
                siraLabel.setText("Ничья!");
                analizLabel.setText("Мест нет");
            } else {
                aktifplayer = (aktifplayer == 'X' ? 'O' : 'X');
                siraLabel.setText("Очередь: " + aktifplayer);
                обновитьАналитику();
            }
        }

        private void ходБота() { // Убрал static, чтобы бот видел кнопки
            int bestScore = -1000;
            int moveRow = -1;
            int moveCol = -1;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (oyunAlani[i][j] == ' ') {
                        oyunAlani[i][j] = 'O';
                        int score = minimax(oyunAlani, 0, false);
                        oyunAlani[i][j] = ' ';
                        if (score > bestScore) {
                            bestScore = score;
                            moveRow = i;
                            moveCol = j;
                        }
                    }
                }
            }

            if (moveRow != -1) {
                сделатьЛогикуХода(moveRow, moveCol); // Бот реально делает ход
            }
        }
    }
}
        