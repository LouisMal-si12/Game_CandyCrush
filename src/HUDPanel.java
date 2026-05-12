import javax.swing.*;
import java.awt.*;
public class HUDPanel extends JPanel {
    private int currentScore = 0;
    private int remainingMoves = 0;
    private LevelConfig currentLevel;

    public HUDPanel() {
        this.setOpaque(false); 
    }

    public void updateScore(int score) { 
        this.currentScore = score; 
        repaint();
    }
    public void updateMoves(int moves) { 
        this.remainingMoves = moves; 
        repaint(); 
    }
    public void updateLevel(LevelConfig level) { 
        this.currentLevel = level; 
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        
        if (currentLevel != null) {
            g2d.drawString(currentScore + " / " + currentLevel.getTargetScore(), 140, 90);
                        g2d.drawString(String.valueOf(remainingMoves), 530, 90);
        }
    }
}
