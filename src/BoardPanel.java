import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class BoardPanel extends JPanel {
    private GameController controller;
    private Board board;
    private Position highlightedPos; 
    private Timer gameLoop;          
   
    private boolean wasAnimating = false; 

    private final int CELL_SIZE = 75; 

    public BoardPanel() {
        this.setOpaque(false); 
        
        gameLoop = new Timer(16, e -> {
            if (board != null) {
                updateAnimations(); 
                repaint();          

                boolean currentlyAnimating = isAnimating();
                if (wasAnimating && !currentlyAnimating) {
               
                    if (controller != null) {
                        controller.checkCascades();
                    }
                }
                wasAnimating = currentlyAnimating; 
            }
        });
        gameLoop.start();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               
                if (controller == null || isAnimating()) return; 

                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;
                Position clickedPos = new Position(row, col);
                
             
                if (highlightedPos == null) {
                    highlightTile(clickedPos); 
                } else if (highlightedPos.equals(clickedPos)) {
                    clearHighlight(); 
                } else {
                    clearHighlight(); 
                }
                
                controller.handleTileClick(clickedPos);
            }
        });
    }

    public void setController(GameController controller) { 
        this.controller = controller;
    }
    public void renderBoard(Board board) {
        this.board = board; repaint();
    }
    public void highlightTile(Position pos) {
        this.highlightedPos = pos; repaint(); 
    }
    public void clearHighlight() { 
        this.highlightedPos = null; repaint(); 
    }

    private void updateAnimations() {
        if (board == null) return;
        int rows = board.getRows(), cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Candy candy = board.getCandy(new Position(r, c));
                if (candy != null) {
                    candy.update();
                }
            }
        }
    }

   
    private boolean isAnimating() {
        if (board == null) return false;
        int rows = board.getRows(), cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Candy candy = board.getCandy(new Position(r, c));
                if (candy != null && candy.isMoving()) return true;
            }
        }
        return false;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (highlightedPos != null) {
            int x = highlightedPos.getCol() * CELL_SIZE;
            int y = highlightedPos.getRow() * CELL_SIZE;
            
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, 20, 20);
            
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(4)); 
            g2d.drawRoundRect(x + 4, y + 4, CELL_SIZE - 8, CELL_SIZE - 8, 20, 20);
            
            g2d.setStroke(new BasicStroke(1)); 
        }

        int rows = board.getRows(), cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Candy candy = board.getCandy(new Position(r, c));
                if (candy != null && candy.getImage() != null) {
                    g2d.drawImage(candy.getImage(), candy.getX() + 5, candy.getY() + 5, CELL_SIZE - 10, CELL_SIZE - 10, null);
                }
            }
        }
    }
}
