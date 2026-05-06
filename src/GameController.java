// File: GameController.java

/**
 * Class GameController - The "Controller" in the MVC pattern.
 * Coordinates between the Model (GameEngine) and the View (GameWindow/BoardPanel).
 */
public class GameController {
    // --- DEPENDENCIES (Thành phần liên kết) ---
    private GameEngine engine;  // The Logic Model (Kết nối với Bộ não xử lý luật)
    private GameView view;      // The Visual View (Kết nối với Sân khấu để ra lệnh vẽ)

    // --- STATE VARIABLES (Trạng thái bộ nhớ) ---
    // Remembers the grid coordinates of the currently selected candy
    // (Ghi nhớ tọa độ của viên kẹo đang được chọn, mặc định null là chưa chọn)
    private Position selectedPosition;
    
    // Flag to lock user input when the game is over
    // (Cờ khóa thao tác chuột khi trò chơi đã kết thúc)
    private boolean isGameEnded = false; 

    //  CONSTRUCTOR 
    public GameController(GameEngine engine, GameView view) {
        this.engine = engine;
        this.view = view;
    }

    // --- MAIN LOGIC FLOW (Luồng xử lý chính) ---

    /**
     * Initializes and starts a new game session.
     * (Khởi tạo và bắt đầu một ván game mới.)
     */
    public void startGame() {
        engine.startGame();
        isGameEnded = false;
        view.hideEndGameOverlay(); // Turn off dark overlay (Tắt màn hình đen kết thúc)
        updateView();
    }

    /**
     * Triggered whenever the user clicks a valid tile on the board.
     * (Hàm này được gọi mỗi khi người chơi bấm vào một ô hợp lệ trên bàn cờ.)
     */
    public void handleTileClick(Position pos) {
        // Handle clicks on the endgame overlay to proceed to the next level or restart
        // (Xử lý thao tác nhấp chuột khi đang ở màn hình kết thúc để qua màn hoặc chơi lại)
        if (isGameEnded) {
            isGameEnded = false;
            view.hideEndGameOverlay();
            
            if (engine.isLevelCompleted() && engine.nextLevel()) {
                // Automatically proceed to the next level (Tự động sang level tiếp theo)
            } else {
                engine.startGame(); // Restart from level 1 (Chơi lại từ đầu)
            }
            updateView();
            return;
        }

        // Ignore clicks outside the valid grid boundaries
        // (Bỏ qua các thao tác nhấp chuột ra ngoài phạm vi bàn cờ)
        if (!engine.getBoard().isInside(pos)) return;

        if (selectedPosition == null) {
            // First click: Select the candy (Bấm lần 1: Chọn viên kẹo)
            selectedPosition = pos;
            view.renderBoard(engine.getBoard()); 
        } else if (selectedPosition.equals(pos)) {
            // Click the same candy: Deselect it (Bấm lại chính viên đó: Hủy chọn)
            selectedPosition = null;
            view.renderBoard(engine.getBoard()); 
        } else {
            // Second click: Attempt to swap (Bấm lần 2: Thử hoán đổi 2 viên kẹo)
            handleSwap(selectedPosition, pos);
            selectedPosition = null;
        }
    }

    /**
     * Processes the logic for swapping two candies.
     * (Xử lý logic khi hoán đổi vị trí hai viên kẹo.)
     */
    public void handleSwap(Position from, Position to) {
        Move move = new Move(from, to);
        boolean isValidMove = engine.makeMove(move); // Check rules (Kiểm tra luật)

        if (isValidMove) {
            // Valid swap: Play animations (Đi đúng luật: Chạy hoạt ảnh trượt và nổ)
            view.playSwapAnimation(from, to);
            view.playMatchAnimation(engine.getLastMatches());
            
            // Check win/loss conditions (Kiểm tra điều kiện thắng/thua)
            if (engine.isLevelCompleted()) {
                isGameEnded = true;
                view.showEndGameOverlay("VICTORY!");
            } else if (engine.isGameOver()) {
                isGameEnded = true;
                view.showEndGameOverlay("GAME OVER");
            }
        } else {
            // Invalid swap: Play shake animation (Đi sai luật: Chạy hoạt ảnh rung lắc từ chối)
            view.playInvalidSwapAnimation(from, to);
        }
        updateView();
    }

    /**
     * Checks for chain reactions after candies have finished falling.
     * (Kiểm tra nổ dây chuyền sau khi kẹo đã rớt xuống xong.)
     */
    public void checkCascades() {
        if (isGameEnded) return;

        // Resolve matches automatically returns true if a chain reaction occurs
        // (Hàm này trả về true nếu vô tình tạo ra chuỗi nổ mới)
        boolean hasChainReaction = engine.resolveMatches();

        if (hasChainReaction) {
            updateView(); 
            
            if (engine.isLevelCompleted()) {
                isGameEnded = true;
                view.showEndGameOverlay("VICTORY!");
            } else if (engine.isGameOver()) {
                isGameEnded = true;
                view.showEndGameOverlay("GAME OVER");
            }
        }
    }

    /**
     * Syncs the Model data with the View components.
     * (Đồng bộ hóa dữ liệu từ Bộ não xuống các thành phần Giao diện.)
     */
    private void updateView() {
        view.renderBoard(engine.getBoard());
        view.renderHUD(engine.getScore(), engine.getRemainingMoves(), engine.getCurrentLevel());
    }
}
