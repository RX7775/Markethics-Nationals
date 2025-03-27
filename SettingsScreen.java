import javax.swing.*;
import java.awt.*;

public class SettingsScreen extends JFrame {
    private JTextField daysField, difField;
    private JButton confirmButton;

    public SettingsScreen() {
        setTitle("Settings");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1)); // Use GridLayout to stack components vertically

        JLabel label = new JLabel("Enter Number of Months:");
        daysField = new JTextField(10);
        daysField.setText(String.valueOf(GameSettings.getNumDays())); // Pre-fill with current value
        
        JLabel label2 = new JLabel("Enter difficulty (1=easy, 2=medium, 3=hard)");
        difField = new JTextField(10);
        difField.setText(String.valueOf(GameSettings.getDifficulty()));

        confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e -> {
            try {
                int days = Integer.parseInt(daysField.getText());
                GameSettings.setNumDays(days);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for months.");
            }

            try {
                int dif = Integer.parseInt(difField.getText());
                GameSettings.setDifficulty(dif);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for difficulty.");
            }

            dispose(); // Close settings screen after handling input
        });

        add(label);
        add(daysField);
        add(label2);
        add(difField);
        add(confirmButton);

        setVisible(true);
    }
}
