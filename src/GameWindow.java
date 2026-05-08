// File: GameWindow.java
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Class GameWindow - The main application window extending JFrame.
 * Implements GameView to receive render commands from the Controller.
 * (Cửa sổ chính của ứng dụng - Kế thừa JFrame và triển khai GameView để nhận lệnh vẽ.)
 */
public class GameWindow extends JFrame implements GameView {
    // --- UI COMPONENTS (Các thành phần giao diện) ---
    private BoardPanel boardPanel;
    private HUDPanel hudPanel;
    private GameController controller;
    
    // Stores the text for the endgame overlay (e.g., "VICTORY!")
    // (Lưu trữ dòng chữ cho màn hình kết thúc, ví dụ: "VICTORY!")
    private String overlayMessage = null; 

    // --- CONSTRUCTOR (Hàm khởi tạo) ---
    public GameWindow() {
        setTitle("Candy Crush");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Lock window size (Khóa kích thước cửa sổ)

        // Custom Main Panel to draw the background and overlay
        // (Panel tổng tùy chỉnh để vẽ ảnh nền và màn hình đen kết thúc)
        JPanel mainPanel = new JPanel() {
            private Image bgImage = new ImageIcon("resources/bg.png").getImage();
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    // Draw full background (Vẽ ảnh nền tràn viền)
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }

            // OVERLAY EFFECT: Draw on top of everything else
            // (HIỆU ỨNG LỚP PHỦ: Vẽ đè lên trên tất cả mọi thành phần con khác)
            @Override
            protected void paintChildren(Graphics g) {
                super.paintChildren(g); 
                if (overlayMessage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Dark semi-transparent background (Lớp phủ đen mờ)
                    g2d.setColor(new Color(0, 0, 0, 180));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Giant VICTORY / GAME OVER text (Dòng chữ kết thúc to bự ở giữa)
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(overlayMessage)) / 2;
                    g2d.drawString(overlayMessage, x, 350);
                    
                    // Small instruction text (Dòng chữ hướng dẫn nhỏ bên dưới)
                    g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                    String subText = "Click anywhere to continue";
                    int subX = (getWidth() - g2d.getFontMetrics().stringWidth(subText)) / 2;
                    g2d.drawString(subText, subX, 420);
                }
            }
        };
        
        // HỦY Layout mặc định để dùng tọa độ tuyệt đối (Absolute Layout) từ Figma
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(612, 792)); 

        hudPanel = new HUDPanel();
        boardPanel = new BoardPanel();

        // Map exact coordinates from Figma (Ánh xạ tọa độ chuẩn xác từ thiết kế Figma)
        hudPanel.setBounds(0, 0, 612, 168);
        boardPanel.setBounds(6, 168, 600, 600);

        mainPanel.add(hudPanel);
        mainPanel.add(boardPanel);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center on screen (Căn giữa màn hình)
    }

    public void setController(GameController controller) {
        this.controller = controller;
        boardPanel.setController(controller); // Pass controller to board (Truyền controller xuống bảng kẹo)
    }

    // --- IMPLEMENTING GAMEVIEW (Triển khai các lệnh từ Controller) ---

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
        repaint(); // Trigger paintChildren (Kích hoạt vẽ lớp phủ)
    }
    
    @Override 
    public void hideEndGameOverlay() { 
        this.overlayMessage = null; 
        repaint(); // Remove overlay (Gỡ bỏ lớp phủ)
    }
}
