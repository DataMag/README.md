import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GROUND_HEIGHT = 120;
    private static final int PIPE_WIDTH = 100;
    private static final int PIPE_GAP = 250; // Üst ve alt boru arasındaki mesafe
    private static final int TICK_SPEED = 20;

    private JFrame frame;
    private Timer timer;
    private Random random;

    private Rectangle bird;
    private ArrayList<Rectangle> pipes;

    private int yMotion;
    private int score;
    private boolean gameOver;
    private boolean started;

    public FlappyBird() {
        frame = new JFrame("Flappy Bird");
        timer = new Timer(TICK_SPEED, this);
        random = new Random();

        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        pipes = new ArrayList<>();

        
        addPipe(true);
        addPipe(true);

        frame.add(this); // Paneli çerçeveye (frame) ekle
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);
        frame.setVisible(true);

        timer.start();
    }

    private void addPipe(boolean isInitial) {
        int height = 50 + random.nextInt(300);
        int xPos = isInitial ? WIDTH + PIPE_WIDTH + pipes.size() * 300 : pipes.get(pipes.size() - 1).x + 600;

        // Alt boru çizimi
        pipes.add(new Rectangle(xPos, HEIGHT - height - GROUND_HEIGHT, PIPE_WIDTH, height));
        // Üst boru çizimi
        pipes.add(new Rectangle(xPos, 0, PIPE_WIDTH, HEIGHT - height - PIPE_GAP));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gökyüzü çizimi
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Zemin çizimi
        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT - GROUND_HEIGHT, WIDTH, GROUND_HEIGHT);
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - GROUND_HEIGHT, WIDTH, 20);

        // Kuşun çizimi
        g.setColor(Color.RED);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        // Boruların çizimi ve hareketi
        g.setColor(Color.GREEN.darker());
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));

        if (!started) {
            drawCenteredString(g, "Press SPACE to Start!", HEIGHT / 2);
        } else if (gameOver) {
            drawCenteredString(g, "Game Over!", HEIGHT / 2);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            drawCenteredString(g, "Score: " + score, HEIGHT / 2 + 50);
        } else {
            g.drawString("Score: " + score, 20, 50);
        }
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    private void jump() {
        if (gameOver) {
            resetGame();
            return;
        }

        if (!started) {
            started = true;
        } else {
            yMotion = (yMotion > 0) ? 0 : yMotion; // Yukarı sıçramadan önce düşüşü sıfırla
            yMotion -= 10;
        }
    }

    private void resetGame() {
        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        pipes.clear();
        yMotion = 0;
        score = 0;
        gameOver = false;
        addPipe(true);
        addPipe(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (started && !gameOver) {
            int speed = 10;

            // Движение труб
            for (int i = 0; i < pipes.size(); i++) {
                pipes.get(i).x -= speed;
            }

            // Yerçekimi etkisi
            yMotion += 1;
            bird.y += yMotion;

            // Eski boruları temizle ve yenilerini ekle
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle p = pipes.get(i);

                if (p.x + p.width < 0) {
                    pipes.remove(p);
                    if (p.y == 0) addPipe(false); 
                }
            }

            // Çarpışma ve puan kontrolü
            for (Rectangle p : pipes) {
                // Kuş boru ortasını geçtiğinde puan ekle
                if (p.y == 0 && bird.x + bird.width / 2 == p.x + p.width / 2) {
                    score++;
                }

                if (p.intersects(bird)) {
                    gameOver = true;
                }
            }

            // Sınır çarpışma kontrolü
            if (bird.y > HEIGHT - GROUND_HEIGHT || bird.y < 0) {
                gameOver = true;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new FlappyBird();
    }
}
        
