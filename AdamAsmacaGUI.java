import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AdamAsmacaGUI {
    private static final String[] KELIMELER = new String[]{"java", "programlama", "bilgisayar", "yazilim", "teknoloji", "yapayzeka"};
    private String secilenKelime;
    private char[] gizliKelime;
    private int can;
    private String tahminler;
    private JFrame frame;
    private JLabel gizliKelimeLabel;
    private JLabel tahminlerLabel;
    private JLabel canLabel;
    private JTextField tahminInput;
    private JButton tahminButton;

    public AdamAsmacaGUI() {
        this.secilenKelime = KELIMELER[(new Random()).nextInt(KELIMELER.length)];
        this.gizliKelime = new char[this.secilenKelime.length()];

        for(int i = 0; i < this.gizliKelime.length; ++i) {
            this.gizliKelime[i] = '_';
        }

        this.can = 6;
        this.tahminler = "";
        this.initializeGUI();
    }

    private void initializeGUI() {
        this.frame = new JFrame("Adam Asmaca");
        this.frame.setDefaultCloseOperation(2);
        this.frame.setSize(400, 300);
        this.frame.setLayout(new GridLayout(5, 1));
        this.gizliKelimeLabel = new JLabel("Kelime: " + String.valueOf(this.gizliKelime));
        this.gizliKelimeLabel.setHorizontalAlignment(0);
        this.tahminlerLabel = new JLabel("Tahminler: " + this.tahminler);
        this.tahminlerLabel.setHorizontalAlignment(0);
        this.canLabel = new JLabel("Kalan Can: " + this.can);
        this.canLabel.setHorizontalAlignment(0);
        this.tahminInput = new JTextField();
        this.tahminButton = new JButton("Tahmin Et");
        this.tahminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = AdamAsmacaGUI.this.tahminInput.getText().toLowerCase();
                if (input.length() != 1) {
                    JOptionPane.showMessageDialog(AdamAsmacaGUI.this.frame, "Lütfen sadece bir harf girin!", "Hata", 0);
                } else {
                    char tahmin = input.charAt(0);
                    AdamAsmacaGUI.this.tahminInput.setText("");
                    AdamAsmacaGUI.this.handleTahmin(tahmin);
                }
            }
        });
        this.frame.add(this.gizliKelimeLabel);
        this.frame.add(this.tahminlerLabel);
        this.frame.add(this.canLabel);
        this.frame.add(this.tahminInput);
        this.frame.add(this.tahminButton);
        this.frame.setVisible(true);
    }

    private void handleTahmin(char tahmin) {
        if (this.tahminler.indexOf(tahmin) != -1) {
            JOptionPane.showMessageDialog(this.frame, "Bu harfi zaten tahmin ettiniz!", "Uyarı", 2);
        } else {
            String var10001 = String.valueOf(this.tahminler);
            this.tahminler = var10001 + tahmin + " ";
            this.tahminlerLabel.setText("Tahminler: " + this.tahminler);
            if (this.secilenKelime.indexOf(tahmin) != -1) {
                for(int i = 0; i < this.secilenKelime.length(); ++i) {
                    if (this.secilenKelime.charAt(i) == tahmin) {
                        this.gizliKelime[i] = tahmin;
                    }
                }

                this.gizliKelimeLabel.setText("Kelime: " + String.valueOf(this.gizliKelime));
                if (this.kelimeTahminEdildi()) {
                    JOptionPane.showMessageDialog(this.frame, "Tebrikler! Kelimeyi doğru bildiniz: " + this.secilenKelime, "Kazandınız", 1);
                    this.frame.dispose();
                }
            } else {
                --this.can;
                this.canLabel.setText("Kalan Can: " + this.can);
                if (this.can == 0) {
                    JOptionPane.showMessageDialog(this.frame, "Üzgünüz! Doğru kelime: " + this.secilenKelime, "Kaybettiniz", 0);
                    this.frame.dispose();
                }
            }

        }
    }

    private boolean kelimeTahminEdildi() {
        char[] var4;
        int var3 = (var4 = this.gizliKelime).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            char c = var4[var2];
            if (c == '_') {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdamAsmacaGUI::new);
    }
}


        