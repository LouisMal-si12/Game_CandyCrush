// File: GameView.java
import java.util.List;

/**
 * Interface GameView - Contract for rendering the UI.
 * (Giao kèo dành cho phần vẽ Giao diện.)
 */
public interface GameView {
    void renderBoard(Board board);
    void renderHUD(int score, int moves, LevelConfig level);
    void showMessage(String message);
    void playSwapAnimation(Position from, Position to);
    void playInvalidSwapAnimation(Position from, Position to);
    void playMatchAnimation(List<List<Position>> matches);
    void showEndGameOverlay(String message);
    void hideEndGameOverlay();
}
