import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameEngine engine = new GameEngine();
            GameWindow window = new GameWindow();
            GameController controller = new GameController(engine, window);
            
            window.setController(controller);
            window.setVisible(true);
            
            controller.startGame(); 
        });
    }
}
