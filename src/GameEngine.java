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
        this.board.fillRandomCandies(); // Spawn initial candies (Thả kẹo lấp đầy bàn cờ)
    }

    /**
     * Validates and executes a player's move.
     * (Kiểm tra tính hợp lệ và thực hiện nước đi của người chơi.)
     */
    public boolean makeMove(Move move) {
        // Must be adjacent tiles (Luật 1: Phải là 2 ô sát cạnh nhau ngang hoặc dọc)
        if (!move.isAdjacent()) return false;

        // 1. Attempt the swap (Thử hoán đổi vị trí trước)
        board.swap(move.getFrom(), move.getTo());
        lastMatches = matchFinder.findMatches(board);

        // 2. Reject if no matches found (Luật 2: Hủy bỏ nếu hoán đổi không tạo ra chuỗi 3)
        if (lastMatches.isEmpty()) {
            board.swap(move.getFrom(), move.getTo()); // Revert (Đổi ngược lại)
            return false;
        }

        // 3. Move is valid, deduct a turn and process explosions 
        // (Hợp lệ: Trừ đi 1 lượt và tiến hành nổ kẹo)
        remainingMoves--;
        resolveMatches();
        return true;
    }

    /**
     * Resolves all matches on the board, applies gravity, and triggers refills.
     * Returns true if matches were found (useful for cascades).
     * (Xóa kẹo trùng, áp dụng trọng lực và thả kẹo mới. Trả về true nếu có nổ.)
     */
    public boolean resolveMatches() {
        // Find duplicate coordinates and flatten the list (Gộp các tọa độ trùng lặp lại)
        List<Position> flatMatches = matchFinder.getFlatMatches(board);
        
        if (flatMatches.isEmpty()) return false; // Break chain (Dừng dây chuyền nổ)

        // Add score: 10 points per candy (Cộng điểm: Mỗi viên kẹo 10 điểm)
        score += flatMatches.size() * 10;

        // Remove matched candies (Xóa kẹo bị nổ khỏi mảng)
        for (Position p : flatMatches) {
            board.removeCandy(p);
        }

        // Apply physics and spawn new candies (Áp dụng trọng lực và thả kẹo mới từ trên cao)
        board.applyGravity();
        board.fillRandomCandies();

        // Save last match data for potential particle effects 
        // (Lưu lại thông tin nổ cho mục đích vẽ hiệu ứng sau này)
        lastMatches = matchFinder.findMatches(board);
        
        return true; 
    }

    // --- LEVEL MANAGEMENT & GETTERS (Quản lý màn chơi & Các hàm lấy dữ liệu) ---

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
