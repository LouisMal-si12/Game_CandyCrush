public class Move {
    private Position from;
    private Position to;

    public Move(Position from, Position to) {
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
        return (Math.abs(from.getRow() - to.getRow()) == 1 && from.getCol() == to.getCol()) ||
               (Math.abs(from.getCol() - to.getCol()) == 1 && from.getRow() == to.getRow());
    }
}
