import javax.swing.*;
import java.awt.*;

public class GameLauncher {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setSize(500, 600);
        frame.setLayout(new GridLayout(3, 1, 10, 10)); 

        JButton btn1 = new JButton("Tic-tac-joe");
        JButton btn2 = new JButton("AdamAsmaca");
        JButton btn3 = new JButton("Flappy Bird");

        // Привязываем запуск игр к кнопкам
        btn1.addActionListener(e -> ste.main(null));
        btn2.addActionListener(e -> AdamAsmacaGUI.main(null));
        btn3.addActionListener(e -> FlappyBird.main(null));

        frame.add(btn1);
        frame.add(btn2);
        frame.add(btn3);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

        
