import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class TitlePage {
    // Global Variables
    private GamePanel gamePanel;
    private boolean instructionsOn; 
    private JPanel buttonPanel;
    private JPanel otherPanel; 
    private Timer gameTimer;
    private JFrame frame; 
    public TitlePage() {
        // Default values 
        instructionsOn = false; 
        
        // Create the JFrame for the title page
        gamePanel = new GamePanel();
        frame = new JFrame("Game Title Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout());
        frame.setFocusable(true);
        frame.add(gamePanel,BorderLayout.CENTER);
        
        // Instructions Panel
        otherPanel = new JPanel();
        JButton responseButton = new JButton("GOT IT");
        responseButton.addActionListener(e -> hideInstructions());
        otherPanel.add(responseButton);
        otherPanel.setVisible(false);
        frame.add(otherPanel, BorderLayout.SOUTH);
        
        // Homescreen Buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // Play button
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Marker Felt", Font.PLAIN, 24));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the title page
                startGame();    // Start the game
            }
        });
        buttonPanel.add(playButton);
        
        // Settings button
        JButton settingsButton = new JButton("Settings");
        settingsButton.setFont(new Font("Marker Felt", Font.PLAIN, 24));
        settingsButton.addActionListener(e -> new SettingsScreen());
        buttonPanel.add(settingsButton);
        
        
        // Instructions button
        JButton instructionsButton = new JButton("How to Play");
        instructionsButton.setFont(new Font("Marker Felt", Font.PLAIN, 24));
        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInstructions(); 
            }
        });
        buttonPanel.add(instructionsButton);
        

        // Quit button
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Marker Felt", Font.PLAIN, 24));
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the game
            }
        });
        buttonPanel.add(quitButton);

        // Escape key 
        JRootPane rootPane = frame.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        // Map the Escape key to exit game 
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        actionMap.put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Escape pressed. Exiting game...");
                System.exit(0); // Exit the application
            }
        });
        buttonPanel.setVisible(true); 
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame to be visible
        frame.setVisible(true);
    }
    
    private void showInstructions() {
        // Turning on instructions 
        SwingUtilities.invokeLater(() -> {
            instructionsOn = true; 
            buttonPanel.setVisible(false); 
            frame.add(otherPanel, BorderLayout.SOUTH);
            otherPanel.setVisible(true); 
            gamePanel.repaint();
        });
    }
    
    private void hideInstructions() {
        // Hiding instructions 
        SwingUtilities.invokeLater(() -> {
            instructionsOn = false; 
            otherPanel.setVisible(false); 
            frame.add(buttonPanel, BorderLayout.SOUTH);
            buttonPanel.setVisible(true); 
            gamePanel.repaint();
        });
    }

    private void startGame() {
        // Starting the game 
        System.out.println("Game started!");
        Game game = new Game(); 
        game.start();          
    }
    
    private class GamePanel extends JPanel {
        // Image files
        private Image instructionsImage;
        private Image logoImage;
        private Image bg; 
    
        public GamePanel() {
            try {
                // Image links 
                URL instructionsUrl = new URL("https://i.imgur.com/4K4nHkT.png"); // Instructions
                instructionsImage = new ImageIcon(instructionsUrl).getImage();
                
                URL logoUrl = new URL("https://i.imgur.com/bBsxoVs.png"); // Logo
                logoImage = new ImageIcon(logoUrl).getImage();
                
                URL noteUrl = new URL("https://i.imgur.com/CY2ydnP.png"); // Sticky note
                
                URL imageUrl = new URL("https://i.imgur.com/v7VDzrC.png"); // Background
                // bg = new ImageIcon(noteUrl).getImage();
                bg = new ImageIcon(imageUrl).getImage();
            } catch (Exception e) {
                System.out.println("Failed to load image.");
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Font
            g.setFont(new Font("Marker Felt", Font.BOLD, 20));
            g.setColor(Color.BLACK);
            
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            // System.out.println("panelWidth: " + panelWidth);
            // System.out.println("panelHeight: " + panelHeight);
            // Display images
            if (instructionsOn) {
                if (instructionsImage != null) g.drawImage(instructionsImage, 0, 0, panelWidth, panelHeight, this);
            } else if (logoImage != null) {
                g.drawImage(bg, 0, 0, panelWidth, panelHeight, this); 
                // g.drawImage(logoImage, 300, 100, 600, 400, this);
                int imageWidth = 600;
                int imageHeight = 400;
                int imageX = (panelWidth - imageWidth) / 2;
                int imageY = (panelHeight - imageHeight) / 2;
                g.drawImage(logoImage, imageX, imageY, imageWidth, imageHeight, this);
            } else {
                g.drawString("Failed to load image", 600, 350);
            }
        }
    }

    public static void main(String[] args) {
        // Launch the title page
        new TitlePage();
    }
}
