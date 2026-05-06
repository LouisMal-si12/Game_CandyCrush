
import java.util.list;
   
    public class GameEngine {
        private Board board;
        private MatchFinder matchFinder;
        private LevelConfig[] levels;
        private int currentLevelIndex;
        private int score;
        private int remainingMoves;
        private List<List<Position>> lastMatches;
    
        public GameEngine() {
            this.board = new Board();
            this.matchFinder = new MatchFinder();
            this.levels = new LevelConfig[]{
                new LevelConfig(1, 100, 20),
                new LevelConfig(2, 300, 18),
                new LevelConfig(3, 600, 15)
            };
            this.currentLevelIndex = 0;
        }
    
        public void startGame() {
            currentLevelIndex = 0;
            score = 0;
            startLevel(currentLevelIndex);
        }
    
        public void startLevel(int levelIndex) {
            this.currentLevelIndex = levelIndex;
            this.remainingMoves    = levels[levelIndex].getMaxMoves();
            fillRandomCandies();
        }
        
        public boolean isInsde( Position pos){
            return board.isInside(pos);
        }

        public boolean makeMove(Position from, Position to) {
            if (!isInside(from) || !isInside(to)) return false;
    
            swap(from, to);
            lastMatches = matchFinder.findMatches(board);
            if (lastMatches.isEmpty()) {
                swap(from, to); 
                return false;
        }

        remainingMoves--;
        resolveMatches();
        return true;
        }

        public void resolveMatches() {
            List<Position> flat = matchFinder.getFlatMatches(board);
            if (flat.isEmpty()) return;

            score += flat.size() * 10;
            removeCandy(flat);
            applyGravity();
            resolveMatches();
        }

        public void swap(Position p1, Position p2) {
            int temp = board.getCandy(p1);
            board.setCandy(p1, board.getCandy(p2));
            board.setCandy(p2, temp);
        }
        
        public void removeCandy(List<Position> Positions) { 
            for (Position p : Positions) {
                board.clearCell(p);
            }
        }
}
