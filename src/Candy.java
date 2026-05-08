// File: Candy.java
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * Abstract Class Candy - The blueprint for all candy types.
 * Handles logical coordinates, pixel coordinates, and smooth sliding animations.
 * (Bản vẽ gốc của mọi loại kẹo - Xử lý tọa độ hàng/cột, tọa độ pixel vẽ và hoạt ảnh trượt.)
 */
public abstract class Candy {
    // Logic position (Vị trí logic)
    protected int row;
    protected int col;
    protected CandyColor color;
    protected Image image;

    // Animation variables (Các biến phục vụ hoạt ảnh)
    protected int x, y;               // Current pixel coordinates (Tọa độ điểm ảnh thực tế)
    protected int targetX, targetY;   // Destination pixel coordinates (Tọa độ đích đến)
    protected boolean isMoving;       // Motion state (Trạng thái đang di chuyển)
    protected final int CELL_SIZE = 75; 

    public Candy(CandyColor color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
        
        // Start statically at the destination (Ban đầu sinh ra đứng im tại chỗ)
        this.x = col * CELL_SIZE;
        this.y = row * CELL_SIZE;
        this.targetX = x;
        this.targetY = y;
        this.isMoving = false;
        
        loadImage();
    }

    /**
     * Loads the specific candy image based on its color.
     * (Tải ảnh kẹo tương ứng dựa vào tên màu.)
     */
    protected void loadImage() {
        try {
            
            String path = "/resources/" + color.name().toLowerCase() + ".png";
            
            java.net.URL imgURL = getClass().getResource(path);
            
            if (imgURL != null) {
                this.image = new ImageIcon(imgURL).getImage();
            } else {
                System.out.println("WARNING: Image not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading image for color: " + color);
            e.printStackTrace();
        }
    }
    public abstract String getTypeName(); 
    public abstract void crush();

    /**
     * Animation Engine: Gradually moves the candy towards its target coordinate.
     * (Động cơ Hoạt ảnh: Di chuyển kẹo dần dần về tọa độ đích với vận tốc giảm dần.)
     */
    public void update() {
        if (x != targetX || y != targetY) {
            isMoving = true;
            // Calculate velocity (Tính toán tốc độ trượt - Khoảng cách chia 4)
            int speedX = (targetX - x) / 4; 
            int speedY = (targetY - y) / 4;

            // Snap to grid if very close to prevent jitter
            // (Ép dính vào lưới nếu khoảng cách quá nhỏ để tránh giật hình)
            if (Math.abs(targetX - x) < 4) x = targetX; else x += speedX;
            if (Math.abs(targetY - y) < 4) y = targetY; else y += speedY;
        } else {
            isMoving = false;
        }
    }

    // Getters (Các hàm lấy giá trị)
    public int getRow() { 
        return row; 
    }
    public int getCol() {
        return col; 
    }
    public int getX() {
        return x;
    }
    public int getY() { 
        return y; 
    }
    public boolean isMoving() { 
        return isMoving;
    }
    public CandyColor getColor() {
        return color;
    }
    public Image getImage() {
        return image;
    }

    /**
     * Updates logic position and sets the new target pixel for sliding.
     * (Cập nhật vị trí logic và châm ngòi tọa độ đích để kẹo trượt tới.)
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
        this.targetX = col * CELL_SIZE;
        this.targetY = row * CELL_SIZE;
    }

    /**
     * Spawns the candy high above the board to create a falling effect.
     * (Sinh kẹo ở trên trần nhà để tạo hiệu ứng rơi tự do.)
     */
    public void dropFromTop(int row, int col) {
        this.row = row;
        this.col = col;
        this.targetX = col * CELL_SIZE;
        this.targetY = row * CELL_SIZE;
        this.x = targetX;
        this.y = targetY - 400; // Start 400px above (Bắt đầu từ tọa độ Y lùi lên 400px)
    }
}
