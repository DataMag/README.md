import javax.swing.*;
import java.awt.*;

public class GameLauncher {
    public static void main(String[] args) {
        JFrame frame = new JFrame("мой игровой центр");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Тут выход разрешен, это главное меню
        frame.setSize(500, 600);
        frame.setLayout(new GridLayout(3, 1, 10, 10)); // 3 кнопки друг под другом

        JButton btn1 = new JButton("Крестики-нолики");
        JButton btn2 = new JButton("Виселица");
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
        