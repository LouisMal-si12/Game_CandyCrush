// File: Board.java
import java.util.Random;

/**
 * Class Board - The Data Structure for the game board.
 * Stores a 2D array of Candy objects and manipulates their positions.
 * (Cấu trúc dữ liệu của bàn cờ - Chứa mảng 2 chiều các đối tượng Kẹo và quản lý vị trí.)
 */
public class Board {
    // --- STATE VARIABLES (Trạng thái bộ nhớ) ---
    private int rows;
    private int cols;
    private Candy[][] grid; // 2D Array of Candy objects (Mảng 2 chiều chứa các đối tượng kẹo)
    private Random random;

    // --- CONSTRUCTOR (Hàm khởi tạo) ---
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Candy[rows][cols];
        this.random = new Random();
        
        initBoard(); // Setup initial safe board (Tạo bàn cờ an toàn ban đầu)
    }

    // --- BASIC UTILITIES (Các hàm tiện ích cơ bản) ---
    public int getRows() { 
        return rows;
    }
    public int getCols() { 
        return cols; 
    }

    /**
     * Checks if a coordinate is within the board boundaries.
     * (Kiểm tra xem tọa độ truyền vào có nằm trong phạm vi bàn cờ không.)
     */
    public boolean isInside(Position pos) {
        return pos.getRow() >= 0 && pos.getRow() < rows &&
               pos.getCol() >= 0 && pos.getCol() < cols;
    }

    public Candy getCandy(Position pos) {
        if (!isInside(pos)) return null;
        return grid[pos.getRow()][pos.getCol()];
    }

    /**
     * Places a candy at a specific position and triggers its slide animation.
     * (Đặt kẹo vào 1 vị trí và đồng thời châm ngòi hoạt ảnh trượt tới vị trí đó.)
     */
    public void setCandy(Position pos, Candy candy) {
        if (isInside(pos)) {
            grid[pos.getRow()][pos.getCol()] = candy;
            // ANIMATION TRIGGER (Ngòi nổ hoạt ảnh)
            if (candy != null) {
                candy.setPosition(pos.getRow(), pos.getCol()); 
            }
        }
    }

    // --- BOARD MANIPULATIONS (Các thao tác thay đổi bàn cờ) ---

    /**
     * Swaps two candies safely.
     * (Hoán đổi 2 viên kẹo an toàn.)
     */
    public void swap(Position p1, Position p2) {
        Candy temp = getCandy(p1);
        setCandy(p1, getCandy(p2));
        setCandy(p2, temp);
    }

    /**
     * Removes a candy and triggers its crush logic.
     * (Xóa kẹo và kích hoạt logic nổ của nó.)
     */
    public void removeCandy(Position pos) {
        if (isInside(pos)) {
            Candy c = grid[pos.getRow()][pos.getCol()];
            if (c != null) {
                c.crush(); // Trigger particle effect logic (Kích hoạt logic hiệu ứng hạt)
            }
            grid[pos.getRow()][pos.getCol()] = null;
        }
    }

    /**
     * Applies gravity by making candies fall into empty spaces below.
     * (Áp dụng trọng lực: Quét từ dưới lên và hút các viên kẹo rơi vào chỗ trống.)
     */
    public void applyGravity() {
        for (int c = 0; c < cols; c++) {
            int emptyRow = rows - 1; // Start scanning from the bottom (Bắt đầu từ đáy quét lên)
            for (int r = rows - 1; r >= 0; r--) {
                if (grid[r][c] != null) {
                    Candy candy = grid[r][c];
                    grid[r][c] = null; // Pull candy from old spot (Rút kẹo khỏi vị trí cũ)
                    grid[emptyRow][c] = candy; // Place into empty spot (Gán vào vị trí rỗng bên dưới)
                    
                    // ANIMATION TRIGGER: Smooth falling (Kích hoạt hoạt ảnh rớt trượt mượt mà)
                    candy.setPosition(emptyRow, c); 
                    emptyRow--;
                }
            }
        }
    }

    /**
     * Fills empty spaces at the top with newly generated random candies.
     * (Bổ sung kẹo ngẫu nhiên mới vào các ô trống ở trên cùng.)
     */
    public void fillRandomCandies() {
        CandyColor[] colors = CandyColor.values();
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                if (grid[r][c] == null) {
                    CandyColor randomColor = colors[random.nextInt(colors.length)];
                    NormalCandy newCandy = new NormalCandy(randomColor, r, c);
                    
                    // ANIMATION TRIGGER: Drop from ceiling (Ép kẹo xuất hiện từ trên trần nhà và rơi xuống)
                    newCandy.dropFromTop(r, c); 
                    grid[r][c] = newCandy;
                }
            }
        }
    }

    /**
     * Initializes a board without any pre-existing matches.
     * (Hàm hỗ trợ: Khởi tạo bàn cờ an toàn, đảm bảo lúc mới vào game không có viên kẹo nào nổ sẵn.)
     */
    private void initBoard() {
        CandyColor[] colors = CandyColor.values();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CandyColor safeColor;
                do {
                    safeColor = colors[random.nextInt(colors.length)];
                } while (
                    (c >= 2 && grid[r][c-1] != null && grid[r][c-1].getColor() == safeColor && grid[r][c-2] != null && grid[r][c-2].getColor() == safeColor) ||
                    (r >= 2 && grid[r-1][c] != null && grid[r-1][c].getColor() == safeColor && grid[r-2][c] != null && grid[r-2][c].getColor() == safeColor)
                );
                
                NormalCandy candy = new NormalCandy(safeColor, r, c);
                grid[r][c] = candy; // Static spawn, no drop animation (Mới vào game thì đứng im)
            }
        }
    }
}
