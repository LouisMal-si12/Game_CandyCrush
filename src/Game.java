import java.awt.*; // hàm này tạo ra một GUI tạo ra các giao diện cửa sổ bla bla..
import java.awt.event.*;
import java.io.File; // hàm này tạo ra một GUI tạo ra các giao diện cửa sổ bla bla..
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*; // hàm này để tạo ra được các thành phần của giao diện người dùng như JFrame, JPanel, v.v.

public class Game {
    final int SIZE = 8; // tạo một biến SIZE để xác định kích thước của bảng chơi
    final int HUD_HEIGHT = 80; // tạo một biến HUD_HEIGHT để xác định chiều cao của khu vực hiển thị điểm số
    final int PIXEL_SIZE = 800; // tạo một biến PIXEL_SIZE để xác định kích thước của mỗi ô trên bảng chơi
    final int SQUARE_SIZE = PIXEL_SIZE / SIZE; // tạo một biến SQUARE_SIZE để xác định kích thước của mỗi ô trên bảng chơi
    int score = 0; // tạo một biến score để lưu trữ điểm số của người chơi
    Image BG;
    Image SELECTOR; 
    Image [] CANDY = new Image[6];
    Random random = new Random(); 
    Point selectedPoint = null; // tạo một biến selected để lưu trữ vị trí của viên kẹo được chọn
    // ban đầu ta cho là null sẽ thay đổi khi thông qua click hay mouselistener để chọn một viên kẹo nào đó trả về tọa độ 
    int [][] board = new int[SIZE][SIZE]; // tạo một biến board để lưu trữ trạng thái của bảng chơi
     // đây là một mảng 2 chiều để lưu trữ trạng thái của bảng chơi

    JFrame frame = new JFrame("Candy Crush"); // tạo một biến frame để tạo ra một cửa sổ
    JPanel panel; // tạo một biến panel để tạo ra một bảng điều khiển

    public void run() throws  IOException { // tạo một hàm phương thức để chạy trò chơi
        // throws IOException để báo lỗi nếu có lỗi xảy ra khi đọc hình ảnh từ các tệp
        BG = ImageIO.read(new File("resources/b.png")); // đọc hình ảnh nền từ một tệp
        SELECTOR = ImageIO.read(new File("resources/s.png")); // đọc hình ảnh selector từ một tệp
        for (int i = 0; i < CANDY.length; i++) {
            CANDY[i] = ImageIO.read(new File("resources/" + (i + 1) + ".png")); // đọc hình ảnh của các viên kẹo từ các tệp
        }
    initBoard(); // gọi phương thức initBoard để khởi tạo bảng chơi
        panel = new JPanel(){ // tạo một đối tượng panel mới bằng cách mở rộng bằng {...} để điều chỉnh như một subclass của JPanel, để có thể 
        // override phương thức paintComponent để vẽ các thành phần của trò chơi trên bảng điều khiển
            @Override
            protected void paintComponent(Graphics g) { // tạo một phương thức để vẽ các thành phần trên bảng điều khiển
                super.paintComponent(g);
    // Đây là một phương thức đặc biệt của Swing. Khi màn hình cần hiển thị (ví dụ: khi bạn mở cửa sổ, hoặc khi một viên kẹo nổ và
    //  biến mất),
    //  hệ thống Java sẽ tự động gọi hàm này để "vẽ" lại giao diện, tức là nó sẽ luôn tự động vẽ lại hình ảnh sau một cái gì đấy xảy ra.
                // vẽ các thành phần của trò chơi ở đây
                // vì ta đã có một phương thức paintComponent nên ta có thể vẽ các thành phần của trò chơi ở đây, ví dụ: vẽ bảng chơi, 
                // vẽ các viên kẹo, vẽ điểm số, v.v.
                render(g); // gọi phương thức render để vẽ các thành phần của trò chơi trên bảng điều khiển
        }
    };
    frame.add(panel);
    panel.addMouseListener(new MouseAdapter() { // thêm một MouseListener vào panel để xử lý các sự kiện chuột
        @Override
        public void mouseClicked(MouseEvent e) { // tạo một phương thức để xử lý sự kiện click chuột
           SolveCLick(e);
        }
    });
    panel.setPreferredSize(new Dimension(PIXEL_SIZE, PIXEL_SIZE + HUD_HEIGHT)); // đặt kích thước cho bảng điều khiển
    frame.pack(); // điều chỉnh kích thước của cửa sổ nếu java nhận thấy kích thước đưa ra quá nhỏ hoặc quá lớn so với kích thước của bảng 
        //điều khiển

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // đặt hành động khi đóng cửa sổ
    frame.setVisible(true); // hiển thị cửa sổ
    }
 private void render(Graphics g) {
    // Vẽ background toàn màn hình
    g.drawImage(BG, 0, HUD_HEIGHT, PIXEL_SIZE, PIXEL_SIZE, frame);
    g.setColor(new Color(0, 0, 0, 80));
    g.fillRect(0, HUD_HEIGHT, PIXEL_SIZE, PIXEL_SIZE);

    // Vẽ kẹo - thêm HUD_HEIGHT vào tọa độ y
    for (int y = 0; y < SIZE; y++) {
        for (int x = 0; x < SIZE; x++) {
            g.drawRect(
                x * SQUARE_SIZE,
                y * SQUARE_SIZE + HUD_HEIGHT,  // ← Cộng HUD_HEIGHT
                SQUARE_SIZE, SQUARE_SIZE
            );
            g.drawImage(CANDY[board[x][y] - 1],
                x * SQUARE_SIZE,
                y * SQUARE_SIZE + HUD_HEIGHT,  // ← Cộng HUD_HEIGHT
                SQUARE_SIZE, SQUARE_SIZE, frame
            );
        }
    }

    // Vẽ selector - thêm HUD_HEIGHT vào tọa độ y
    if (selectedPoint != null) {
        g.drawImage(SELECTOR,
            selectedPoint.x * SQUARE_SIZE,
            selectedPoint.y * SQUARE_SIZE + HUD_HEIGHT,  // ← Cộng HUD_HEIGHT
            SQUARE_SIZE, SQUARE_SIZE, frame
        );
    }

    drawScoreBox(g);  // Vẽ HUD ở trên cùng
}
    private void SolveCLick(MouseEvent e) {
    int x = e.getX() / SQUARE_SIZE; // tính toán tọa độ x của ô được click bằng cách lấy tọa độ x của sự kiện chuột 
    // và chia cho kích thước của mỗi ô trên bảng chơi
    int y = (e.getY() - HUD_HEIGHT) / SQUARE_SIZE; // tính toán tọa độ y của ô được click bằng cách lấy tọa độ y của sự kiện chuột, 
    // trừ đi chiều cao của khu vực hiển thị điểm số và chia cho kích thước của mỗi ô trên bảng chơi

    // Kiểm tra tọa độ hợp lệ
    if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return; // nếu click ra ngoài bảng chơi thì không làm gì cả

    if (selectedPoint == null) {
        // Lần click 1: chưa chọn viên nào → chọn viên này
        selectedPoint = new Point(x, y);

    } else if (selectedPoint.x == x && selectedPoint.y == y) {
        // Click lại đúng viên đó → bỏ chọn
        selectedPoint = null; // nếu click lại đúng viên kẹo đã chọn thì bỏ chọn nó đi bằng cách gán selectedPoint về null

    }  else {
    // Kiểm tra có kề nhau không (chỉ ngang hoặc dọc)
    boolean isAdjacent = (Math.abs(selectedPoint.x - x) == 1 && selectedPoint.y == y) || // Kề nhau → hoán đổi
                         (Math.abs(selectedPoint.y - y) == 1 && selectedPoint.x == x); 

    if (isAdjacent) { // nếu hai viên kẹo được chọn kề nhau (chỉ ngang hoặc dọc) thì hoán đổi vị trí của chúng
        // Kề nhau → hoán đổi
        replacecandy(selectedPoint, new Point(x, y));
        selectedPoint = null; 
        processMatches(); // sau khi hoán đổi, gọi phương thức processMatches để kiểm tra và 
        // xử lý các viên kẹo trùng nhau trên bảng chơi
    } else {
        // Không kề nhau → chọn viên mới
        selectedPoint = new Point(x, y);    
    }
}
if (board[x][y] >= 0 ) { // nếu ô được click không phải là ô trống thì tìm kiếm các viên kẹo trùng nhau trên bảng chơi
    ArrayList<Point> matched = findmatches(); // gọi phương thức findmatches để tìm kiếm các viên kẹo trùng nhau trên bảng chơi và lưu trữ kết quả vào một danh sách
    if (!matched.isEmpty()) { // nếu có các viên kẹo trùng nhau được tìm thấy thì loại bỏ chúng khỏi bảng chơi
        removematches(matched); // gọi phương thức removematches để loại bỏ các viên kẹo trùng nhau khỏi bảng chơi
        score += matched.size(); // tăng điểm số của người chơi lên bằng số lượng viên kẹo trùng nhau đã bị loại bỏ
    }
}
    panel.repaint();
}

private void processMatches() {
    ArrayList<Point> matched = findmatches();
    if (!matched.isEmpty()) {
        removematches(matched);          // xóa kẹo trùng
        score += matched.size() * 10;   // cộng điểm
        gravity();                       // kẹo rơi xuống
        processMatches();                // ← Tự gọi lại (cascade)
    }
}

 private ArrayList<Point> findmatches(){
    ArrayList <Point> matched = new ArrayList<>(); // tạo một phương thức để tìm kiếm các viên kẹo trùng nhau trên
    //  bảng chơi và trả về một danh sách các điểm tương ứng với vị trí của các viên kẹo đó
    // tạo một danh sách rỗng để lưu trữ các điểm tương ứng với vị  trí của các viên kẹo trùng nhau
    for (int y = 0; y < SIZE; y++) {
        for (int x = 0; x < SIZE; x++) {
            int type = board[x][y]; // lấy loại viên kẹo tại vị trí (x, y)
            if (type == 0) continue; // nếu ô trống thì bỏ qua
            // Kiểm tra theo chiều ngang
            if (x < SIZE - 2 && board[x + 1][y] == type && board[x + 2][y] == type) { // nếu có ít nhất 3 viên kẹo cùng loại nằm ngang
            //  với nhau thì thêm các điểm tương ứng vào danh sách matched
                matched.add(new Point(x, y));
                matched.add(new Point(x + 1, y));
                matched.add(new Point(x + 2, y));
            }
            // Kiểm tra theo chiều dọc
            if (y < SIZE - 2 && board[x][y + 1] == type && board[x][y + 2] == type) { // nếu có ít nhất 3 viên kẹo cùng loại nằm dọc
            //  với nhau thì thêm các điểm tương ứng vào danh sách matched
                matched.add(new Point(x, y));
                matched.add(new Point(x, y + 1));
                matched.add(new Point(x, y + 2));
            }
        }
    }
    return matched;
 }

 

 private void removematches(ArrayList<Point> matched) { // tạo một phương thức để loại bỏ các viên kẹo trùng nhau trên bảng chơi
    for (Point p : matched) {
        board[p.x][p.y] = 0; // thay thế giá trị của các viên kẹo trùng nhau bằng 0 để biểu thị rằng chúng đã bị loại bỏ
    }
    gravity(); // gọi phương thức gravity để dồn các viên kẹo xuống dưới sau khi đã loại bỏ các viên kẹo trùng nhau
    refillboard(); // gọi phương thức refillboard để làm đầy lại bảng chơi sau khi đã loại bỏ các viên kẹo trùng nhau
}

private void refillboard() { // tạo một phương thức để làm đầy lại bảng chơi sau khi đã loại bỏ các viên kẹo
//  trùng nhau bằng cách dồn các viên kẹo xuống dưới và tạo ra các viên kẹo mới ở trên cùng
    for (int x = 0; x < SIZE; x++) {
        // Dồn kẹo xuống dưới
        int emptyRow = SIZE - 1;
        for (int y = SIZE - 1; y >= 0; y--) {
            if (board[x][y] != 0) {
                board[x][emptyRow] = board[x][y];
                if (emptyRow != y) board[x][y] = 0;
                emptyRow--;
            }
        }
        // Tạo kẹo mới ở trên
        for (int y = emptyRow; y >= 0; y--) {
            board[x][y] = random.nextInt(6) + 1;
        }
    }
}

 private void initBoard() {
    do {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                board[x][y] = random.nextInt(6) + 1;
            }
        }
    } while (!findmatches().isEmpty()); // ← Random lại nếu có match
}

private void gravity() { // tạo một phương thức để dồn các viên kẹo xuống dưới sau khi đã loại 
// bỏ các viên kẹo trùng nhau bằng cách di chuyển các giá trị của các viên kẹo xuống dưới và gán giá trị 0 cho các ô trống
    for (int x = 0; x < SIZE; x++) {
        int emptyRow = SIZE - 1;
        for (int y = SIZE - 1; y >= 0; y--) {
            if (board[x][y] != 0) {
                board[x][emptyRow] = board[x][y];
                if (emptyRow != y) board[x][y] = 0;
                emptyRow--;
            }
        }
        for (int y = emptyRow; y >= 0; y--) {
            board[x][y] = random.nextInt(6) + 1;
        }
    }

    // ← Sau khi refill, kiểm tra match mới tự động
    processMatches();
}


private void drawScoreBox(Graphics g) {
    // Nền HUD toàn bộ phía trên
    g.setColor(new Color(0, 0, 0, 180));
    g.fillRect(0, 0, PIXEL_SIZE, HUD_HEIGHT);

    // Viền dưới HUD
    g.setColor(new Color(255, 215, 0));
    g.drawLine(0, HUD_HEIGHT, PIXEL_SIZE, HUD_HEIGHT);

    // Ô Score
    g.setColor(new Color(255, 255, 255, 30));
    g.fillRoundRect(10, 10, 200, 60, 15, 15);
    g.setColor(new Color(255, 215, 0));
    g.drawRoundRect(10, 10, 200, 60, 15, 15);

    // Chữ SCORE
    g.setColor(new Color(255, 215, 0));
    g.setFont(new Font("Arial", Font.BOLD, 16));
    g.drawString("SCORE", 20, 32);

    // Số điểm
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 28));
    g.drawString(String.valueOf(score), 20, 62);
}
    private void replacecandy(Point p1, Point p2) { // tạo một phương thức để thay thế vị trí của hai viên kẹo
        int temp = board[p1.x][p1.y]; // lưu trữ giá trị
        board[p1.x][p1.y] = board[p2.x][p2.y]; // thay thế giá trị của viên kẹo tại vị trí p1 bằng giá trị của viên kẹo tại vị trí p2
        board[p2.x][p2.y] = temp; // thay thế giá trị của viên kẹo tại vị trí p2 bằng giá trị đã lưu trữ
            }
        
}
//----------------------------------------------------------------------------------------//
//                               Click hoán đổi
//                                      ↓
//                                replacecandy()     → Hoán đổi 2 viên
//                                      ↓
//                                 findmatches()     → Tìm viên trùng
//                                      ↓
//                                removematches()    → Xóa viên trùng, board[x][y] = 0
//                                      ↓
//                                   gravity()       → Kẹo rơi xuống, sinh kẹo mới trên cùng
//                                      ↓
//                                 findmatches()     → Kiểm tra có match mới không (cascade)
//                                      ↓
//                                panel.repaint()    → Vẽ lại màn hình
//----------------------------------------------------------------------------------------//