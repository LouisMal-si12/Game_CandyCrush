import java.awt.Image;
import javax.swing.ImageIcon;
import java.io.File;

// Lớp trừu tượng làm bản vẽ gốc cho mọi loại kẹo
public abstract class Candy {
    protected int row;
    protected int col;
    protected Candy color;
    protected Image image;

    // Các biến phục vụ cho Animation 
    protected int x, y;               // Tọa độ vẽ pixel thực tế hiện tại
    protected int targetX, targetY;   // Tọa độ pixel đích đến muốn bay tới
    protected boolean isMoving;       // Trạng thái: đang bay hay đang đứng im
    protected final int CELL_SIZE = 75; // Khớp với kích thước ô bên GamePanel

    //  Hàm khởi tạo
    public Candy(Candy color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
        
        // Ban đầu khởi tạo vị trí pixel và đích đến trùng nhau (đứng im)
        this.x = col * CELL_SIZE;
        this.y = row * CELL_SIZE;
        this.targetX = x;
        this.targetY = y;
        this.isMoving = false;
        
        loadImage(); 
    }

    // Hàm load ảnh kẹo
    protected void loadImage() {
        try {
            String fileName = color.toString() + ".png"; 
            File file = new File("resource/" + fileName); // Trỏ đúng vào thư mục resource
            if (!file.exists()) file = new File("../resource/" + fileName);
            
            if (file.exists()) {
                this.image = new ImageIcon(file.getAbsolutePath()).getImage();
            } else {
                System.out.println("CẢNH BÁO: Không tìm thấy ảnh " + fileName + "!");
            }
        } catch (Exception e) {}
    }

    // Tính Trừu tượng_ Buộc các loại kẹo con (Kẹo Đỏ, Kẹo Xanh, Kẹo Sọc...) tự định nghĩa cách nổ
    public abstract void crush();

    // HÀM QUAN TRỌNG Động cơ Animation trượt mượt mà
    public void update() {
        if (x != targetX || y != targetY) {
            isMoving = true;
            
            // Tính toán tốc độ trượt Khoảng cách càng xa trượt càng nhanh
            int speedX = (targetX - x) / 4; 
            int speedY = (targetY - y) / 4;

            // Nếu khoảng cách còn quá nhỏ (< 4px), ép nó về đích luôn cho khỏi giật
            if (Math.abs(targetX - x) < 4) x = targetX;
            else x += speedX;

            if (Math.abs(targetY - y) < 4) y = targetY;
            else y += speedY;
        } else {
            isMoving = false; // Đã tới đích, dừng lại
        }
    }

    //  Getters 
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
    public Candy getColor() { 
        return color; 

    }
    public Image getImage() { 
        return image; 
    }

    // Setters 
    
    // Cập nhật vị trí logic và tự động set tọa độ đích để kẹo bắt đầu trượt
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
        this.targetX = col * CELL_SIZE;
        this.targetY = row * CELL_SIZE;
    }

    // Dùng khi sinh kẹo mới_đặt kẹo tàng hình ở trên cao rồi rớt xuống
    public void dropFromTop(int row, int col) {
        this.row = row;
        this.col = col;
        this.targetX = col * CELL_SIZE;
        this.targetY = row * CELL_SIZE;
        this.x = targetX;
        this.y = targetY - 400; // Xuất phát từ vị trí lùi lên 400px so với đích để tạo hiệu ứng rớt
    }
}