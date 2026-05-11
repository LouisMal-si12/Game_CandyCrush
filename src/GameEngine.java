import java.util.List;

public class GameEngine {
    private Board board;
    private MatchFinder matchFinder;
    private LevelConfig[] levels;
    private int currentLevelIndex;
    private int score;
    private int remainingMoves;
    private List<List<Position>> lastMatches;

    public GameEngine() {
        this.board = new Board(8, 8);
        this.matchFinder = new MatchFinder();
        
        this.levels = new LevelConfig[]{
            new LevelConfig(1, 1000, 20),
            new LevelConfig(2, 2000, 18),
            new LevelConfig(3, 3000, 15)
        };
        this.currentLevelIndex = 0;
    }


    public void startGame() {
        currentLevelIndex = 0;
        startLevel(currentLevelIndex);
    }

    public void startLevel(int levelIndex) {
        this.currentLevelIndex = levelIndex;
        this.score = 0;
        this.remainingMoves = levels[levelIndex].getMaxMoves();
        this.board.fillRandomCandies();
    }

    public boolean makeMove(Move move) {
        if (!move.isAdjacent()) return false;

        board.swap(move.getFrom(), move.getTo());
        lastMatches = matchFinder.findMatches(board);

        if (lastMatches.isEmpty()) {
            board.swap(move.getFrom(), move.getTo()); 
            return false;
        }
        remainingMoves--;
        resolveMatches();
        return true;
    }

 
    public boolean resolveMatches() {
        List<Position> flatMatches = matchFinder.getFlatMatches(board);
        
        if (flatMatches.isEmpty()) return false; 
        score += flatMatches.size() * 10;

        for (Position p : flatMatches) {
            board.removeCandy(p);
        }

        board.applyGravity();
        board.fillRandomCandies();

        lastMatches = matchFinder.findMatches(board);
        
        return true;
    }


    public boolean nextLevel() {
        if (currentLevelIndex + 1 < levels.length) {
            startLevel(++currentLevelIndex);
            return true;
        }
        return false;
    }

    public boolean isLevelCompleted() {
        return score >= levels[currentLevelIndex].getTargetScore();
    }

    public boolean isGameOver() {
        return remainingMoves <= 0 && !isLevelCompleted();
    }

    public Board getBoard() { return board; }
    public int getScore() { return score; }
    public int getRemainingMoves() { return remainingMoves; }
    public LevelConfig getCurrentLevel() { return levels[currentLevelIndex]; }
    public List<List<Position>> getLastMatches() { return lastMatches; }
}
