import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class BoardPanel - The main visual component of the game board.
 * Handles rendering candies, animations, and capturing mouse input.
 * (Sân khấu chính - Phụ trách vẽ kẹo, chạy hoạt ảnh và bắt sự kiện chuột.)
 */
public class BoardPanel extends JPanel {
    // --- DEPENDENCIES & STATE (Thành phần liên kết & Trạng thái) ---
    private GameController controller;
    private Board board;
    private Position highlightedPos; // Candy with glowing border (Viên kẹo đang được viền sáng)
    private Timer gameLoop;          // 60 FPS Engine (Đồng hồ đếm nhịp 60 khung hình/giây)
    
    // Flag to detect when candies stop moving to trigger cascades
    // (Cờ theo dõi khoảnh khắc kẹo vừa dừng rơi để kích hoạt nổ dây chuyền)
    private boolean wasAnimating = false; 

    // Constant size for grid calculations (Kích thước chuẩn cho 1 ô kẹo)
    private final int CELL_SIZE = 75; 

    // --- CONSTRUCTOR (Hàm khởi tạo) ---
    public BoardPanel() {
        this.setOpaque(false); // Transparent background (Nền trong suốt)
        
        // --- ANIMATION LOOP (Vòng lặp hoạt ảnh) ---
        gameLoop = new Timer(16, e -> {
            if (board != null) {
                updateAnimations(); // Move candies slightly (Di chuyển kẹo từng chút một)
                repaint();          // Redraw screen (Vẽ lại màn hình)

                // CASCADE DETECTION LOGIC (Logic bắt nhịp nổ dây chuyền)
                boolean currentlyAnimating = isAnimating();
                if (wasAnimating && !currentlyAnimating) {
                    // Candies just stopped moving -> Request a match check
                    // (Kẹo vừa đứng im -> Yêu cầu Controller quét bàn cờ tìm chuỗi 3)
                    if (controller != null) {
                        controller.checkCascades();
                    }
                }
                wasAnimating = currentlyAnimating; 
            }
        });
        gameLoop.start();

        // --- MOUSE LISTENER (Lắng nghe sự kiện chuột) ---
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Lock input if candies are still moving
                // (Khóa chuột không cho bấm khi kẹo trên sân đang bay lơ lửng)
                if (controller == null || isAnimating()) return; 

                // Coordinate translation: Pixel -> Grid Index
                // (Ánh xạ tọa độ: Điểm ảnh trên màn hình -> Vị trí mảng hàng/cột)
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;
                Position clickedPos = new Position(row, col);
                
                // Toggle highlight based on user interaction
                // (Tự động bật/tắt viền sáng dựa trên thao tác người dùng)
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

    // --- SETTERS & UPDATERS (Hàm thiết lập & Cập nhật) ---
    public void setController(GameController controller) { this.controller = controller; }
    public void renderBoard(Board board) { this.board = board; repaint(); }
    public void highlightTile(Position pos) { this.highlightedPos = pos; repaint(); }
    public void clearHighlight() { this.highlightedPos = null; repaint(); }

    /**
     * Triggers the internal calculation for moving candies.
     * (Kích hoạt bộ tính toán tọa độ di chuyển cho tất cả viên kẹo.)
     */
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

    /**
     * Checks if any candy on the board is currently in motion.
     * (Kiểm tra xem có bất kỳ viên kẹo nào đang trượt/rớt hay không.)
     */
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
    
    // --- RENDERING METHOD (Hàm vẽ đồ họa chính) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) return;

        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smooth graphics (Bật khử răng cưa cho nét vẽ mượt)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. DRAW HIGHLIGHT GLOW (Vẽ hiệu ứng viền sáng nổi bật)
        if (highlightedPos != null) {
            int x = highlightedPos.getCol() * CELL_SIZE;
            int y = highlightedPos.getRow() * CELL_SIZE;
            
            // Soft white background (Lớp nền sáng trắng mờ)
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, 20, 20);
            
            // Bright thick border (Khung viền sáng nét dày)
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(4)); 
            g2d.drawRoundRect(x + 4, y + 4, CELL_SIZE - 8, CELL_SIZE - 8, 20, 20);
            
            g2d.setStroke(new BasicStroke(1)); // Reset stroke (Trả lại nét mảnh mặc định)
        }

        // 2. DRAW CANDIES (Vẽ các viên kẹo)
        int rows = board.getRows(), cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Candy candy = board.getCandy(new Position(r, c));
                if (candy != null && candy.getImage() != null) {
                    // Draw using real-time pixel coordinates X and Y
                    // (Vẽ kẹo theo tọa độ Pixel ảo X và Y đang chạy liên tục)
                    g2d.drawImage(candy.getImage(), candy.getX() + 5, candy.getY() + 5, CELL_SIZE - 10, CELL_SIZE - 10, null);
                }
            }
        }
    }
}
