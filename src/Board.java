import java.util.Random;

public class Board {
    private int rows;
    private int cols;
    private Candy[][] grid; 
    private Random random;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Candy[rows][cols];
        this.random = new Random();
        
        initBoard(); 
    }

    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }

   
    public boolean isInside(Position pos) {
        return pos.getRow() >= 0 && pos.getRow() < rows &&
        pos.getCol() >= 0 && pos.getCol() < cols;
    }

    public Candy getCandy(Position pos) {
        if (!isInside(pos)) return null;
        return grid[pos.getRow()][pos.getCol()];
    }

    public void setCandy(Position pos, Candy candy) {
        if (isInside(pos)) {
            grid[pos.getRow()][pos.getCol()] = candy;
            // ANIMATION TRIGGER (Ngòi nổ hoạt ảnh)
            if (candy != null) {
                candy.setPosition(pos.getRow(), pos.getCol()); 
            }
        }
    }

  
    public void swap(Position p1, Position p2) {
        Candy temp = getCandy(p1);
        setCandy(p1, getCandy(p2));
        setCandy(p2, temp);
    }

  
    public void removeCandy(Position pos) {
        if (isInside(pos)) {
            Candy c = grid[pos.getRow()][pos.getCol()];
            if (c != null) {
                c.crush(); 
            }
            grid[pos.getRow()][pos.getCol()] = null;
        }
    }

   
    public void applyGravity() {
        for (int c = 0; c < cols; c++) {
            int emptyRow = rows - 1; 
            for (int r = rows - 1; r >= 0; r--) {
                if (grid[r][c] != null) {
                    Candy candy = grid[r][c];
                    grid[r][c] = null; 
                    grid[emptyRow][c] = candy; 
                    
                    candy.setPosition(emptyRow, c); 
                    emptyRow--;
                }
            }
        }
    }


    public void fillRandomCandies() {
        CandyColor[] colors = CandyColor.values();
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                if (grid[r][c] == null) {
                    CandyColor randomColor = colors[random.nextInt(colors.length)];
                    NormalCandy newCandy = new NormalCandy(randomColor, r, c);
                    
                    newCandy.dropFromTop(r, c); 
                    grid[r][c] = newCandy;
                }
            }
        }
    }

    
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
                grid[r][c] = candy; 
            }
        }
    }
}
