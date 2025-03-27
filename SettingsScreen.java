import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingsScreen extends JFrame {
    private JTextField daysField;
    private JButton confirmButton;

    public SettingsScreen() {
        setTitle("Settings");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter number of days:");
        daysField = new JTextField(10);
        daysField.setText(String.valueOf(GameSettings.getNumDays())); // Pre-fill with current value

        confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e -> {
            try {
                int days = Integer.parseInt(daysField.getText());
                GameSettings.setNumDays(days);
                dispose(); // Close settings screen
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            }
        });

        add(label);
        add(daysField);
        add(confirmButton);
        setVisible(true);
    }
}
