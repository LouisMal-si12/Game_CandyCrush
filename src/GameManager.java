import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class GameManager {
    // ==========================================
    // PHẦN 1: CÁC THUỘC TÍNH QUẢN LÝ LOGIC (Từ Engine cũ)
    // ==========================================
    private Board board;
    private MatchFinder matchFinder;
    private LevelConfig[] levels;
    private int currentLevelIndex;
    private int score;
    private int remainingMoves;
    private List<List<Position>> lastMatches;

    // ==========================================
    // PHẦN 2: CÁC THUỘC TÍNH QUẢN LÝ TƯƠNG TÁC (Từ Controller cũ)
    // ==========================================
    private GameView view; // Ghi chú: Có thể đổi thành GamePanels nếu bạn muốn chuẩn 100% theo UML
    private Position selectedPosition;

    public GameManager(GameView view) {
        this.view = view;
        this.board = new Board();
        this.matchFinder = new MatchFinder();
        this.levels = new LevelConfig[]{
            new LevelConfig(1, 100, 20),
            new LevelConfig(2, 300, 18),
            new LevelConfig(3, 600, 15)
        };
        this.currentLevelIndex = 0;

        // Gắn mouse listener trực tiếp vào view ngay khi khởi tạo
        setupMouseListener();
    }

    private void setupMouseListener() {
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / view.getSquareSize();
                int y = (e.getY() - view.getHudHeight()) / view.getSquareSize();
                handleTileClick(new Position(x, y));
            }
        });
    }

    // ==========================================
    // PHẦN 3: LUỒNG HOẠT ĐỘNG CHÍNH CỦA GAME
    // ==========================================
    
    public void startGame() {
        currentLevelIndex = 0;
        score = 0;
        startLevel(currentLevelIndex);
        syncView();
    }

    public void startLevel(int levelIndex) {
        this.currentLevelIndex = levelIndex;
        this.remainingMoves = levels[levelIndex].getMaxMoves();
        fillRandomCandies();
    }

    public void restartGame() {
        startGame();
    }

    private void syncView() {
        view.setBoard(this.board);
        view.updateHUD(this.score, this.remainingMoves);
        view.refresh();
    }

    // ==========================================
    // PHẦN 4: XỬ LÝ TƯƠNG TÁC NGƯỜI DÙNG (CHUỘT)
    // ==========================================

    public void handleTileClick(Position pos) {
        if (!isInside(pos)) return;

        if (selectedPosition == null) {
            // Lần 1: chọn viên kẹo
            selectedPosition = pos;
            view.highlight(pos);
        } else if (selectedPosition.equals(pos)) {
            // Click lại cùng viên → bỏ chọn
            selectedPosition = null;
            view.clearHighlight();
        } else {
            // Lần 2: thử hoán đổi
            handleSwap(selectedPosition, pos);
            selectedPosition = null;
            view.clearHighlight();
        }

        syncView();
    }

    public void handleSwap(Position from, Position to) {
        boolean adjacent = (Math.abs(from.x - to.x) == 1 && from.y == to.y)
                        || (Math.abs(from.y - to.y) == 1 && from.x == to.x);

        if (!adjacent) {
            // Không kề → chọn viên mới
            selectedPosition = to;
            view.highlight(to);
            return;
        }

        boolean moved = makeMove(from, to);

        if (moved) {
            view.playSwapAnimation();
            view.playMatchAnimation();
            view.playGravityAnimation();

            if (isLevelCompleted()) {
                JOptionPane.showMessageDialog(null,
                    "Level hoàn thành! Điểm: " + this.score, "🎉 Xuất sắc!", JOptionPane.INFORMATION_MESSAGE);
                if (!nextLevel()) {
                    JOptionPane.showMessageDialog(null, "Bạn đã hoàn thành tất cả các màn!", "🏆 Chiến thắng!", JOptionPane.INFORMATION_MESSAGE);
                    restartGame();
                }
            } else if (isGameOver()) {
                JOptionPane.showMessageDialog(null,
                    "Hết lượt! Điểm: " + this.score, "😢 Game Over", JOptionPane.WARNING_MESSAGE);
                restartGame();
            }
        }
    }

    // ==========================================
    // PHẦN 5: XỬ LÝ THUẬT TOÁN LOGIC BÊN DƯỚI
    // ==========================================

    public boolean makeMove(Position from, Position to) {
        if (!isInside(from) || !isInside(to)) return false;

        swap(from, to);
        lastMatches = matchFinder.findMatches(board);

        if (lastMatches.isEmpty()) {
            swap(from, to); // hoán đổi lại nếu không có match
            return false;
        }

        remainingMoves--;
        resolveMatches();
        return true;
    }

    private void resolveMatches() {
        List<Position> flat = matchFinder.getFlatMatches(board);
        if (flat.isEmpty()) return;

        score += flat.size() * 10;
        removeCandy(flat);
        applyGravity();
        resolveMatches(); // cascade (phản ứng dây chuyền)
    }

    public void swap(Position p1, Position p2) {
        int temp = board.getCandy(p1);
        board.setCandy(p1, board.getCandy(p2));
        board.setCandy(p2, temp);
    }

    public void removeCandy(List<Position> positions) {
        for (Position p : positions) {
            board.clearCell(p);
        }
    }

    public boolean isInside(Position pos) {
        return board.isInside(pos);
    }

    public void fillRandomCandies() {
        do {
            board.fillRandom();
        } while (matchFinder.hasMatches(board));
    }

    public void applyGravity() {
        int size = board.getSize();
        for (int x = 0; x < size; x++) {
            int emptyRow = size - 1;
            for (int y = size - 1; y >= 0; y--) {
                if (board.getCandy(x, y) != 0) {
                    board.setCandy(x, emptyRow, board.getCandy(x, y));
                    if (emptyRow != y) board.setCandy(x, y, 0);
                    emptyRow--;
                }
            }
            for (int y = emptyRow; y >= 0; y--) {
                board.setCandy(x, y, board.randomCandy());
            }
        }
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

    // ==========================================
    // GETTERS
    // ==========================================
    public Board getBoard()                        { return board; }
    public int getScore()                          { return score; }
    public int getRemainingMoves()                 { return remainingMoves; }
    public LevelConfig getCurrentLevel()           { return levels[currentLevelIndex]; }
    public List<List<Position>> getLastMatches()   { return lastMatches; }
}