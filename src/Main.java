import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run on Event Dispatch Thread for smooth UI
        // (Chạy trên luồng sự kiện của Swing để đồ họa mượt)
        SwingUtilities.invokeLater(() -> {
            GameEngine engine = new GameEngine();
            GameWindow window = new GameWindow();
            GameController controller = new GameController(engine, window);
            
            window.setController(controller);
            window.setVisible(true);
            
            controller.startGame(); // Let's play! (Bắt đầu chơi!)
        });
    }
}
