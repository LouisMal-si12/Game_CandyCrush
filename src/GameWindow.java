import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameWindow extends JFrame implements GameView {
    private BoardPanel boardPanel;
    private HUDPanel hudPanel;
    private GameController controller;
    private String overlayMessage = null; 
    public GameWindow() {
        setTitle("Candy Crush");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); 
        JPanel mainPanel = new JPanel() {
            private Image bgImage = new ImageIcon(getClass().getResource("/resources/bg.png")).getImage();
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
            @Override
            protected void paintChildren(Graphics g) {
                super.paintChildren(g); 
                if (overlayMessage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(new Color(0, 0, 0, 180));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(overlayMessage)) / 2;
                    g2d.drawString(overlayMessage, x, 350);
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                    String subText = "Click anywhere to continue";
                    int subX = (getWidth() - g2d.getFontMetrics().stringWidth(subText)) / 2;
                    g2d.drawString(subText, subX, 420);
                }
            }
        };
        
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(612, 792)); 

        hudPanel = new HUDPanel();
        boardPanel = new BoardPanel();

        hudPanel.setBounds(0, 0, 612, 168);
        boardPanel.setBounds(6, 168, 600, 600);

        mainPanel.add(hudPanel);
        mainPanel.add(boardPanel);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); 
    }

    public void setController(GameController controller) {
        this.controller = controller;
        boardPanel.setController(controller); 
    }


    @Override public void renderBoard(Board board) { boardPanel.renderBoard(board); }
    
    @Override public void renderHUD(int score, int moves, LevelConfig level) {
        hudPanel.updateScore(score);
        hudPanel.updateMoves(moves);
        hudPanel.updateLevel(level);
    }
    
    @Override public void showMessage(String message) {}
    @Override public void playSwapAnimation(Position from, Position to) {}
    @Override public void playInvalidSwapAnimation(Position from, Position to) {}
    @Override public void playMatchAnimation(List<List<Position>> matches) {}

    @Override 
    public void showEndGameOverlay(String message) { 
        this.overlayMessage = message; 
        repaint();
    }
    
    @Override 
    public void hideEndGameOverlay() { 
        this.overlayMessage = null; 
        repaint(); 
    }
}
