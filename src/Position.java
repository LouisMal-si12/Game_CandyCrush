public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o) return true;
        if(!(o instanceof Position)) return false;
        Position other = (Position) o;
        return this.row == other.row && this.col == other.col;
    }
    @Override
        public int hashCode() {
            return 31 * row + col;
        }
}
