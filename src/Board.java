import javax.swing.text.Position;

public class Board {
    private int rows;
    private int cols;
    private Candy[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Candy[rows][cols];
    }

    public boolean isInside(Position pos) {
        return pos.getRow() >= 0 && pos.getRow() < rows
            && pos.getCol >= 0 && pos.getCol < cols;
    }

    public Candy getCandy(Position pos) {
        return grid[pos.getRow()][pos.getCol()];
    }

    public void setCandy(Position pos, Candy candy) {
        grid[pos.getRow()][pos.getCol()] = candy;
    }

    public void swap(Position p1, Position p2) {
        Candy temp = getCandy(p1);
        setCandy(p1, getCandy(p2));
        setCandy(p2, temp);
    }

    public void removeCandy(Position pos) {
        grid[pos.getRow()][pos.getCol()] = null;
    }

    public void fillRamdomCandies() {
        // duyệt grid, trống thì tạo ngẫu nhiên
    }

    public void applyGravity(){} //kẹo rơi xuống lấp chỗ trống
    
    
}
