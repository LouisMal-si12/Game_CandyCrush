import java.util.*;

public class Matchfinder {
    public List<List<Position>> findMatches(Board board) { 
        List<List<Position>> allMatches = new Arraylist<>();
        allmatches.addAll(findHorizontalMatches(board));
        allmatcges.addAll(findVerticalMatches(board));
        return allMatches;
    }

}

private list<list<position>> findHorizontalMatches(Board board) {
    list<list<position>> matches = new Arraylist<>();
    int size = board.getSize();

    for (int y = 0; y < size; y++) {
            for (int x = 0; x < size - 2; x++) {
                int type = board.getCandy(x, y);
                if (type == 0) continue;
                if (board.getCandy(x + 1, y) == type && board.getCandy(x + 2, y) == type) {
                    List<Position> match = new ArrayList<>();
                    match.add(new Position(x, y));
                    match.add(new Position(x + 1, y));
                    match.add(new Position(x + 2, y));
                    matches.add(match);
                }
            }
        }
        return matches;
    }

private List<List<Position>> findVerticalMatches(Board board) {
    List<List<Position>> matches = new ArrayList<>();
    int size = board.getSize();
 
    for (int y = 0; y < size - 2; y++) {
        for (int x = 0; x < size; x++) {
            int type = board.getCandy(x, y);
            if (type == 0) continue;
            if (board.getCandy(x, y + 1) == type && board.getCandy(x, y + 2) == type) {
                List<Position> match = new ArrayList<>();
                match.add(new Position(x, y));
                match.add(new Position(x, y + 1));
                match.add(new Position(x, y + 2));
                matches.add(match);
            }
        }
    }
    return matches;
}

public List<position> FindDuplicateMatches( Board board){
    Set<String> seen = new HashSet<>();
        List<Position> flat = new ArrayList<>();
        for (List<Position> group : findMatches(board)) {
            for (Position p : group) {
                String key = p.x + "," + p.y;
                if (seen.add(key)) {
                    flat.add(p);
                }
            }
        }
        return flat;
    }
