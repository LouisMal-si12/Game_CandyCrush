public class GameController {
    private GameEngine engine;  
    private GameView view;      

    
    private Position selectedPosition; 
    private boolean isGameEnded = false; 
    public GameController(GameEngine engine, GameView view) {
        this.engine = engine;
        this.view = view;
    }
    public void startGame() {
        engine.startGame();
        isGameEnded = false;
        view.hideEndGameOverlay();
        updateView();
    }

  
    public void handleTileClick(Position pos) {
       
        if (isGameEnded) {
            isGameEnded = false;
            view.hideEndGameOverlay();
            
            if (engine.isLevelCompleted() && engine.nextLevel()) {
            } else {
                engine.startGame(); 
            }
            updateView();
            return;
        }

       
        if (!engine.getBoard().isInside(pos)) return;

        if (selectedPosition == null) {
            selectedPosition = pos;
            view.renderBoard(engine.getBoard()); 
        } else if (selectedPosition.equals(pos)) {
            selectedPosition = null;
            view.renderBoard(engine.getBoard()); 
        } else {
            handleSwap(selectedPosition, pos);
            selectedPosition = null;
        }
    }

    public void handleSwap(Position from, Position to) {
        Move move = new Move(from, to);
        boolean isValidMove = engine.makeMove(move); 

        if (isValidMove) {
            view.playSwapAnimation(from, to);
            view.playMatchAnimation(engine.getLastMatches());
            
            if (engine.isLevelCompleted()) {
                isGameEnded = true;
                view.showEndGameOverlay("VICTORY!");
            } else if (engine.isGameOver()) {
                isGameEnded = true;
                view.showEndGameOverlay("GAME OVER");
            }
        } else {
            view.playInvalidSwapAnimation(from, to);
        }
        updateView();
    }
    public void checkCascades() {
        if (isGameEnded) return;

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

   
    private void updateView() {
        view.renderBoard(engine.getBoard());
        view.renderHUD(engine.getScore(), engine.getRemainingMoves(), engine.getCurrentLevel());
    }
}
