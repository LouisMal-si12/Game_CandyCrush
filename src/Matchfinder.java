import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MatchFinder {
    
    public List<List<Position>> findMatches(Board board) { 
        List<List<Position>> allMatches = new ArrayList<>();
        allMatches.addAll(findHorizontalMatches(board));
        allMatches.addAll(findVerticalMatches(board));
        return allMatches;
    }

    private List<List<Position>> findHorizontalMatches(Board board) {
        List<List<Position>> matches = new ArrayList<>();
        int rows = board.getRows(), cols = board.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols - 2; c++) {
                Candy candy1 = board.getCandy(new Position(r, c));
                if (candy1 == null) continue;

                Candy candy2 = board.getCandy(new Position(r, c + 1));
                Candy candy3 = board.getCandy(new Position(r, c + 2));

              
                if (candy2 != null && candy3 != null &&
                    candy1.getColor() == candy2.getColor() && 
                    candy1.getColor() == candy3.getColor()) {
                    
                    List<Position> match = new ArrayList<>();
                    match.add(new Position(r, c));
                    match.add(new Position(r, c + 1));
                    match.add(new Position(r, c + 2));
                    matches.add(match);
                }
            }
        }
        return matches;
    }

    private List<List<Position>> findVerticalMatches(Board board) {
        List<List<Position>> matches = new ArrayList<>();
        int rows = board.getRows(), cols = board.getCols();
 
        for (int r = 0; r < rows - 2; r++) {
            for (int c = 0; c < cols; c++) {
                Candy candy1 = board.getCandy(new Position(r, c));
                if (candy1 == null) continue;

                Candy candy2 = board.getCandy(new Position(r + 1, c));
                Candy candy3 = board.getCandy(new Position(r + 2, c));

                if (candy2 != null && candy3 != null &&
                    candy1.getColor() == candy2.getColor() && 
                    candy1.getColor() == candy3.getColor()) {
                    
                    List<Position> match = new ArrayList<>();
                    match.add(new Position(r, c));
                    match.add(new Position(r + 1, c));
                    match.add(new Position(r + 2, c));
                    matches.add(match);
                }
            }
        }
        return matches;
    }

 
    public List<Position> getFlatMatches(Board board){
        Set<Position> seen = new HashSet<>();
        List<Position> flat = new ArrayList<>();
        
        for (List<Position> group : findMatches(board)) {
            for (Position p : group) {
                if (seen.add(p)) { 
                    flat.add(p);
                }
            }
        }
        return flat;
    }
}
