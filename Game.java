import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;
import java.net.URL;
import java.io.File;
import java.io.IOException;

public class Game {
    // Necessary private instance variables
    private JPanel buttonPanel;
    private JFrame gameFrame;
    private GamePanel gamePanel;
    private double stockPrice;
    private double previousStockPrice;
    private double highScore;
    private int day;
    private String option1, option2;
    private Option currentEvent;
    private Conflict currentConflict; 
    private String investigationResult; 
    private boolean waitingForInput;
    private boolean beingInvestigated;
    private boolean waitingForInvestigation;
    private boolean gameEnded;
    private ArrayList<Double> stockPriceHistory;  // List to store stock prices for line graph
    private int businessEfficiency;
    private int ethicalPoints;
    private Random rand;
    private Timer gameTimer;  // Timer to handle day progression
    private int yesCount;
    private int noCount;
    private int stickyXGlobal = 800, stickyYGlobal = 350;
    private int barXBegin = 200, barXEnd = 600;
    private boolean first = true;

    public Game() {
        stockPrice = 100.0;  // Starting stock price
        previousStockPrice = 100.0;
        highScore = 100.0; 
        day = 1;           
        option1 = "YES";
        option2 = "NO";
        currentEvent = new Option();
        currentEvent.randomize(); 
        currentConflict = new Conflict(); 
        investigationResult = "null"; 
        waitingForInput = false;  // Handles input
        waitingForInvestigation = false; 
        beingInvestigated = false; 
        gameEnded = false; 
        stockPriceHistory = new ArrayList<>();
        stockPriceHistory.add(stockPrice);  // Add the initial stock price to history
        rand = new Random();
    }
    
    public Font newFont(String f, float size) {
        // Creates specified font with specified size
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(f + ".ttf"));
            return font.deriveFont(size);
        } catch (IOException | FontFormatException e) {
            // If error, return null
            System.err.println("Failed to load or derive the font: " + e.getMessage());
            return null;
        }
    }
    
    public void registerFont() {
        // Registers font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("PermanentMarker-Regular.ttf")));
        } catch (IOException | FontFormatException e) {
            System.err.println("Failed to register the font: " + e.getMessage());
        }
    }
    
    public void reset() {
        stockPrice = 100.0;  
        previousStockPrice = 100.0;
        highScore = 100.0; 
        businessEfficiency = 0; 
        ethicalPoints = 0; 
        day = 1;           
        option1 = "YES";
        option2 = "NO";
        currentEvent = new Option();
        currentEvent.randomize(); 
        currentConflict = new Conflict(); 
        investigationResult = "null"; 
        waitingForInput = false;  
        waitingForInvestigation = false; 
        beingInvestigated = false; 
        gameEnded = false; 
        stockPriceHistory = new ArrayList<>();
        stockPriceHistory.add(stockPrice);  
        rand = new Random();
    }
    
    public void endGame() {
        // Ends the game, with play again or quit options
        gameEnded = true; 
        option1 = "PLAY AGAIN"; 
        option2 = "RETURN TO TITLEPAGE"; 
        updateOptions(); 
        buttonPanel.setVisible(true); 
        gameTimer.start(); 
    }

    public void start() {
        // Initialize and set up the game window (JFrame)
        gameFrame = new JFrame("Game Window");
        gamePanel = new GamePanel();

        gameFrame.setSize(1200, 700);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setFocusable(true);  // Ensure it can capture key events
        gameFrame.add(gamePanel);
        
        // Handle arrow keys
        gameFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (waitingForInput) {
                    handleKeyPress(e);
                }
            }
        });
        
        // Bind escape key to quit
        JRootPane rootPane = gameFrame.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        actionMap.put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Escape pressed. Exiting game...");
                System.exit(0); // Exit the application
            }
        });

        // Set up buttons for options
        buttonPanel = new JPanel();
        JButton optionButton1 = new JButton(option1);
        optionButton1.addActionListener(e -> handleOption(1));
        JButton optionButton2 = new JButton(option2);
        optionButton2.addActionListener(e -> handleOption(2));

        buttonPanel.add(optionButton1);
        buttonPanel.add(optionButton2);
        buttonPanel.setVisible(false);
        gameFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        gameFrame.setVisible(true);
       
        // Start the game loop (repaint every 100ms to simulate frames)
        gameTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!waitingForInput && !beingInvestigated && !waitingForInvestigation&!gameEnded) {
                    updateGame();  // Continue to simulate game
                }
                gamePanel.repaint();  // Repaint the panel to update game interface
            }
        });
        gameTimer.start();
    }
    
    private void updateOptions() {
        // Set up buttons for options
        buttonPanel.setVisible(false); 
        buttonPanel = new JPanel();
        JButton optionButton1 = new JButton(option1);
        optionButton1.addActionListener(e -> handleOption(1));
        JButton optionButton2 = new JButton(option2);
        optionButton2.addActionListener(e -> handleOption(2));
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> handleOption(3));

        buttonPanel.add(optionButton1);
        buttonPanel.add(optionButton2);
        if (waitingForInput) buttonPanel.add(confirmButton);
        buttonPanel.setVisible(false);
        gameFrame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            handleOption(1);  // Select Option 1 with the Left Arrow
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            handleOption(2);  // Select Option 2 with the Right Arrow
        }
    }

    private void handleOption(int option) {
        if (beingInvestigated) {
            handleInvestigation(option); 
            return; 
        }
        if (waitingForInvestigation) {
            handleInvResponse(option); 
            return; 
        }
        if (gameEnded) {
            handleEnd(option);
            return; 
        }
        if (option == 1) {
            // Yes
            ethicalPoints += currentEvent.ethicalChange();
            businessEfficiency += currentEvent.efficiencyChange();
            first = true;
            yesCount++;
        } else if (option == 2) {
            // No
            ethicalPoints -= currentEvent.ethicalChange();
            businessEfficiency -= currentEvent.efficiencyChange();
            first = true;
            noCount++;
        } else if (option == 3) {
            // Confirm
            if (stickyXGlobal + 125 <= (barXBegin + barXEnd) / 2) {
                yesCount++;
                ethicalPoints += currentEvent.ethicalChange();
                businessEfficiency += currentEvent.efficiencyChange();
            }
            else {
                noCount++;
                ethicalPoints -= currentEvent.ethicalChange();
                businessEfficiency -= currentEvent.efficiencyChange();
            }
            System.out.println(yesCount);
            System.out.println(noCount);
            first = true;
        }

        // Update and continue necessary items
        updateStockPrice();
        day++;
        waitingForInput = false;
        gameTimer.start();
    }
    
    private void handleInvestigation(int option) {
        // Results of investigation
        option1 = "I UNDERSTAND"; 
        option2 = "I UNDERSTAND"; 
        updateOptions(); 
        buttonPanel.setVisible(true);
        int res = currentConflict.getRes(option, ethicalPoints); 
        investigationResult = currentConflict.getStr(); 
        businessEfficiency += currentConflict.efficiencyChange(); 
        ethicalPoints += currentConflict.ethicalChange();
        beingInvestigated = false; 
        waitingForInvestigation = true; 
        gamePanel.repaint(); 
    }
    
    private void handleInvResponse(int option) {
        // Handles response to investigation result
        waitingForInvestigation = false; 
        if (currentConflict.efficiencyChange() == -100) {
            endGame(); 
            return; 
        }
        updateStockPrice(); 
        day++;
        gameTimer.start(); 
    }
    
    private void handleEnd(int option) {
        // Handles end of game
        System.out.println("endclick"); 
        if (option == 1) {
            reset(); 
            gameTimer.start(); 
        }
        if (option == 2) {
            new TitlePage(); 
            gameFrame.dispose();
        }
    }

    private void updateGame() {
        // Automatically updates game every day
        updateStockPrice();  
        day++;

        // Ends game
        if (day == GameSettings.getNumDays()) {
            endGame(); 
            return; 
        }
        // Pause game to wait for user input
        if (day % 30 == 0) {
            gameTimer.stop();  
            waitingForInput = true;
            option1 = "YES";
            option2 = "NO";
            updateOptions(); 
            buttonPanel.setVisible(true);
            currentEvent.reroll(); 
        }
        else if (day % 30 == 15) {
            // Ethic checks
            if (rand.nextDouble() * 40 < -ethicalPoints*GameSettings.getDifficulty()) {
                // Start investigation
                beingInvestigated = true; 
                gameTimer.stop(); 
                currentConflict.setConflict();
                option1 = currentConflict.getOp(0); 
                option2 = currentConflict.getOp(1); 
                updateOptions(); 
                buttonPanel.setVisible(true); 
            }
        }
        else {
            buttonPanel.setVisible(false);
        }
    }

    private void updateStockPrice() {
        // Update the stock price
        double changePercent = (rand.nextDouble() * (10 + businessEfficiency/10.0)) - 5;  // Generate a random number between -5 and +5
        double changeAmount = stockPrice * (changePercent / 100);
        stockPrice += changeAmount;
        
        // Ensure stock price doesn't go negative
        if (stockPrice < 0) stockPrice = 0;

        // Add new stock price to the history
        stockPriceHistory.add(stockPrice);

        // Limit history to the last 100 days
        if (stockPriceHistory.size() > 100) {
            stockPriceHistory.remove(0);  // Remove oldest data (day 1)
        }
        highScore = Math.max(highScore, stockPrice); 
    }

    // GamePanel class for custom drawing
    private class GamePanel extends JPanel {
        private int stickyX = 800, stickyY = 350, stickyWidth = 250;
        private boolean stickyNoteDragging = false;
        private Image img;
        private Image bg; 
        private Image[] conImg = {img,bg};

        public GamePanel() {
            // Load necessary images
            try {
                URL imageUrl = new URL("https://i.imgur.com/CY2ydnP.png");
                img = new ImageIcon(imageUrl).getImage();
            } catch (Exception e) {
                System.out.println("Failed to load image");
            }
            try {
                URL imageUrl = new URL("https://i.imgur.com/v7VDzrC.png");
                bg = new ImageIcon(imageUrl).getImage();
            } catch (Exception e) {
                System.out.println("Failed to load image"); 
            }
            try {
                URL imageUrl = new URL("https://i.imgur.com/hBgsb29.png");
                conImg[0] = new ImageIcon(imageUrl).getImage();
            } catch (Exception e) {
                System.out.println("Failed to load image"); 
            }
            try {
                URL imageUrl = new URL("https://i.imgur.com/hXi2BeN.png");
                conImg[1] = new ImageIcon(imageUrl).getImage();
            } catch (Exception e) {
                System.out.println("Failed to load image"); 
            }
            
            // Handle sticky note dragging
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getX() >= stickyX && e.getX() <= stickyX + stickyWidth && e.getY() >= stickyY && e.getY() <= stickyY + stickyWidth) {
                        stickyNoteDragging = true;
                    }
                    first = false;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    stickyNoteDragging = false;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (stickyNoteDragging) {
                        stickyX = e.getX() - stickyWidth / 2;
                        stickyY = e.getY() - stickyWidth / 2;
                        stickyXGlobal = stickyX;
                        stickyYGlobal = stickyY;
                        repaint();
                    }
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Set up font and color for text
            Font pm = newFont("PermanentMarker-Regular", 20f);
            g.setFont(pm);
            g.setColor(Color.BLACK);
            
            // Draw background
            g.drawImage(bg, 0, 0, 1200, 625, this); 
            
            // Display game end information
            if (gameEnded) {
                g.drawString("The game has ended!", 200, 100); 
                if (day == GameSettings.getNumDays()) {
                    g.drawString("Congratulations on staying in business until the end!",200,200);
                    if (stockPrice > 100) g.drawString("You ended with a stock price of $"+String.format("%.2f",stockPrice)+", making a profit and winning!",200,250);
                    else g.drawString("You ended with a stock price of $"+String.format("%.2f",stockPrice)+", try to make a profit next time!",200,250); 
                }
                else g.drawString("You lost everything... better luck next time!",200,200); 
                g.drawString("Your stock peaked at a price of $"+String.format("%.2f",highScore),200,300); 
                if (day == 930) {
                    g.drawString("Final Business Efficiency: " + businessEfficiency,200,400); 
                    g.drawString("Final Ethical Points: " + ethicalPoints,200,350); 
                }
                return;
            }

            // Draw stock price graph
            if (stockPrice - previousStockPrice > 0) {
                g.setColor(Color.GREEN);
                g.drawString("Stock Price: $" + String.format("%.2f (+%.2f)", stockPrice, stockPrice - previousStockPrice), 50, 50);
            }
            else {
                g.setColor(Color.RED);
                g.drawString("Stock Price: $" + String.format("%.2f (%.2f)", stockPrice, stockPrice - previousStockPrice), 50, 50);
            }
            g.setColor(Color.BLACK);
            if (day % 30 == 0) previousStockPrice = stockPrice;
            g.drawString("Day: " + day, 50, 80);
            g.drawString("Ethical Points: " + ethicalPoints, 50, 110); 
            g.drawString("Business Efficiency: " + businessEfficiency, 50, 140); 
            g.drawString("Highest Recorded Price: $" + String.format("%.2f", highScore), 400, 50);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(10));
            
            // Draw T-chart and sticky notes
            g2.drawLine(barXBegin, 300, barXEnd, 300);
            g2.drawLine((barXBegin + barXEnd) / 2, 250, (barXBegin + barXEnd) / 2, 550);
            
            g.setColor(Color.YELLOW);
            int row = 0, count = 0;
            for (int i = 1; i <= yesCount; i++) {
                g.fillRect(barXBegin + count * 30, 310 + row * 30, 20, 20);
                count++;
                if (i % 6 == 0) {
                    row++;
                    count = 0;
                }
            }
            
            row = 0; count = 0;
            for (int i = 1; i <= noCount; i++) {
                g.fillRect((barXBegin + barXEnd) / 2 + 20 + count * 30, 310 + row * 30, 20, 20);
                count++;
                if (i % 6 == 0) {
                    row++;
                    count = 0;
                }
            }
            g.setColor(Color.GREEN);
            g.drawString("YES", 275, 280);
            g.setColor(Color.RED);
            g.drawString("NO", 475, 280);
            
            // Reset style
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.BLACK);
            
            // Draw sticky note
            if (day % 30 == 0) {
                if (first) {
                    stickyX = 800;
                    stickyY = 350;
                }
                g.drawImage(img, stickyX, stickyY, stickyWidth, stickyWidth, this);
                g.setColor(Color.BLACK);
                g.setFont(newFont("Caveat-VariableFont_wght", 24f));
                drawWrappedText(g, currentEvent.toString(), stickyX + 30, stickyY + 75, stickyWidth - 70);
                String EP = "" + currentEvent.ethicalChange();
                if (currentEvent.ethicalChange() > 0) EP = "+" + EP;
                String BE = "" + currentEvent.efficiencyChange();
                if (currentEvent.efficiencyChange() > 0) BE = "+" + BE;
                g.drawString(EP + " EP, " + BE + " BE", stickyX + 60, stickyY + 30);
            }
            
            
            if (beingInvestigated) {
                g.drawImage(conImg[currentConflict.getIdx()],700,300,400,300,this);
            }
            if (waitingForInvestigation) g.drawString(investigationResult,100,575); 
            
            // Draw instructions for key presses or clicking buttons
            Font pm2 = newFont("PermanentMarker-Regular", 14f);
            g.setFont(pm2);
            g.drawString("Press Left/Right Arrow or click a button or drag sticky note to option and click confirm to choose an option", 50, 220);

            // Draw line graph for stock prices
            drawStockPriceGraph(g);
        }

        private void drawStockPriceGraph(Graphics g) {
            // Set dimensions
            int width = 300;
            int height = 150;
            int marginRight = 30;
            int marginTop = 50;
            
            g.setColor(Color.WHITE); 
            g.fillRect(getWidth()-marginRight-width,marginTop,width,height); 
            
            // Find min and max stock prices to scale graph
            double maxPrice = stockPriceHistory.stream().max(Double::compare).orElse(stockPrice);
            double minPrice = stockPriceHistory.stream().min(Double::compare).orElse(stockPrice);
            
            // Scales graph
            double priceRange = maxPrice - minPrice;
            if (priceRange == 0) priceRange = 1; // Avoid division by zero
            
            // Draws graph
            for (int i = 1; i < stockPriceHistory.size(); i++) {
                int x1 = getWidth() - marginRight - width + (i - 1) * (width / (stockPriceHistory.size() - 1));
                int y1 = marginTop + height - (int) ((stockPriceHistory.get(i - 1) - minPrice) / priceRange * height);
                int x2 = getWidth() - marginRight - width + i * (width / (stockPriceHistory.size() - 1));
                int y2 = marginTop + height - (int) ((stockPriceHistory.get(i) - minPrice) / priceRange * height);
            
                g.setColor(Color.BLUE);
                g.drawLine(x1, y1, x2, y2);
            }
            
            // Draw axes
            g.setColor(Color.BLACK);
            int upperNumWidth = g.getFontMetrics().stringWidth("" + maxPrice);
            int lowerNumWidth = g.getFontMetrics().stringWidth("" + minPrice);
            g.drawLine(getWidth() - marginRight - width, marginTop + height, getWidth() - marginRight, marginTop + height);  // X-axis
            g.drawLine(getWidth() - marginRight - width, marginTop, getWidth() - marginRight - width, marginTop + height); // Y-axis
            
            String maxPriceLabel = "$" + String.format("%.2f", maxPrice);
            String minPriceLabel = "$" + String.format("%.2f", minPrice);
            
            // Calculate maximum width of labels
            int maxLabelWidth = Math.max(g.getFontMetrics().stringWidth(maxPriceLabel), g.getFontMetrics().stringWidth(minPriceLabel));
            int labelWidth1 = g.getFontMetrics().stringWidth(maxPriceLabel);
            int labelWidth2 = g.getFontMetrics().stringWidth(minPriceLabel);
            
            // Display values with proper spacing
            int labelX1 = getWidth() - marginRight - width - labelWidth1 - 10; // Add a margin of 10 pixels
            int labelX2 = getWidth() - marginRight - width - labelWidth2 - 10;
            g.drawString(maxPriceLabel, labelX1, marginTop + 10);
            g.drawString(minPriceLabel, labelX2, marginTop + height);
            
            // Draw label for X-axis (days)
            g.drawString("Last 100 Days", getWidth() - marginRight - width + width / 2 - 40, marginTop + height + 20);
        }
    }
    
    private void drawWrappedText(Graphics g, String text, int x, int y, int width) {
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getHeight(); // Height of each line
        int curY = y;

        // Break text into words
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            // Check if adding word exceeds line width
            if (fm.stringWidth(line + word) > width) {
                // Draw the current line
                g.drawString(line.toString(), x, curY);
                curY += lineHeight;

                // Reset line
                line = new StringBuilder(word).append(" ");
            } else {
                // Add word to current line
                line.append(word).append(" ");
            }
        }

        // Draw last line
        if (line.length() > 0) {
            g.drawString(line.toString(), x, curY);
        }
    }
}
