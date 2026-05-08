public class Move {
    private Position from;
    private Position to;

    public Move(Position from, Position tp) {
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }
    public Position getTo() {
        return to;
    }

    public boolean isAdjacent() {
        int rowDiff = Math.abs(from.getRow() - to.getRow());
        int colDiff = Math.abs(from.getCol() - to.getCol());
    }
}
