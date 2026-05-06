import javax.swing.JPanel; //frame
import javax.swing.ImageIcon;//download image
import javax.swing.Timer;//set time
import java.awt.*;//layout manager
import java.awt.event.MouseAdapter;//chỉ xử lý hành động kick chuột
import java.awt.event.MouseEvent;//trích xuất đc tọa độ x,y
import java.io.File;//dẫn đến các file đc lưu trong mt
import java.util.List; 

public class GamePanel extends JPanel{ //kế thừa JPanel để làm khung vẽ đồ họa
    private Board board;//quản lý mảng kẹo
    private MatchFinder finder=new MatchFinder();// thuật toán quét nổ kẹo
    private GameManager gm=new GameManager(20,700); //quản lý điểm, times:20; score:700

    //xử lý hằng số chết
    private final int CELL_SIZE= 75;//kich thuoc o vuong 
    private final int BOARD_OFFSET_X=14;//lề trái
    private final int BOARD_OFFSET_Y=165;//lề trên
    private final int WINDOW_WIDTH=612;// chiều rộng cửa sổ
    private final int WINDOW_HEIGHT=800;// chiều cao cửa sổ

    private int firstRow=-1;//tọa độ ban đầu của ng chơi khi kick vào
    private int firstCol=-1;
    private Image bgImage;//khung rỗng
    private Timer gameLoop;//đồng hồ khai báo đếm nhịp_animation

    public GamePanel(Board board){
        this.board=board;//khi file main tạo ra gamepanel->giúp gamepanel phân biệt các items candy
        this.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));// set cái kích thước của khung game

        //load ảnh nền
        try{
            File file=new File("resource/bg.png");
            if(!file.exists()) file =new File ("../resource/bg.png");
            if(file.exists()) bgImage= new ImageIcon (file.getAbsolutePath()).getImage();
        } catch(Exception e){}
        //try-catch giúp khi ko tìm thấy ảnh nền nó tự động set về default

        gameLoop= new Timer (16,e ->{//tần số quét giúp duy trì hoạt ảnh
            updateGameLogic();//ksoat tọa độ của các viên kẹo
            repaint();//update tọa độ đã được cập nhật của updateGameLogic
        });
        gameLoop.start();

        //xử lý đk kick chuột(lab5)
        this.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                if (gm.isGameOver() || gm.isGameWon() || isBoardMoving()) return;//ktra trạng thái game
            
                int col= (e.getX()-BOARD_OFFSET_X)/CELL_SIZE;//xác định tọa độ chuột đang kick...
                int row= (e.getY()-BOARD_OFFSET_Y)/CELL_SIZE;

                if (row<0||row>=8||col<0||col>=8) return;
                if (firstRow==-1){
                    firstRow=row; firstCol=col;//ghi nhớ vị trí kick chuột ban đầu
                }else{
                    //ghi nhớ vị trí kick chuột lần 2
                    if(Math.abs(firstRow-row)+Math.abs(firstCol-col)==1){//Math.abs(cột-cột) đảm bảo hai viên kẹo sát vách nhau
                        gm.decreaseMove();//3 viên gần nhau thì nổ và tính điểm + nếu ko đổi thì trả lại ngược vị trí cũ
                        board.swap(firstRow,firstCol,row, col);
                        if(!finder.findMatches(board.getGrid()).isEmpty()){
                            processMatches();
                        }else{
                            board.swap(row,col,firstRow,firstCol);
                        }
                    }
                    firstRow=-1; firstCol=-1;//kết thúc lượt, mt xóa sạch tạo độ và trả lại random mới để bắt đầu cho lượt đổi chỗ kẹo tiếp theo
                        }
                    }
                });
    }
    
    //tọa độ của kẹo
    private void updateGameLogic(){//hoạt ảnh+nổ liên hoàn
        boolean moving =false;
        Candy[][] grid= board.getGrid();
        for (int i=0;i<8;i++){//quét lướt qa cái grid để luon trong trạng thái check kẹo
            for (int j=0; j<8; j++){
                if(grid[i][j]!=null){
                    grid[i][j].update();
                    if (grid[i][j].isMoving())moving=true;
            }
        }
        if (!moving){//giúp cho 3 viên mà gần nhau random thì tự nổ và tính điểm...
            List<Candy> newMatches=finder.findMatches(board.getGrid());//dùng list thay vì aray giúp hệ thống linh hoạt chứa lượng kẹo nổ bất kì mà ko bị giới hạn kích thước cứng
            if (!newMatches.isEmpty()) processMatches();
        }
    }    
    //ngăn ng chơi lướt kẹo khi bảng kẹo chưa ổn định
    private boolean isBoardMoving(){
        for (Candy[] row: board.getGrid()){
            for (Candy c:row){
                if(c!=null && c.isMoving()) return true;
            }
        }
    }
        return false;
    }
    //
    private void processMatches(){
        List<Candy> matches= finder.findMatches(board.getGrid());
        if(!matches.isEmpty()){
            gm.addScore(matches.size());//xem điểm trng ds có bao nhiêu viên thì cộng bấy nhiêu điểm
            for (Candy c:matches) board.getGrid()[c.getRow()][c.getCol()]=null;// lấy tọa độ của từng viên đem đi nổ và xóa mất nó đi
            board.refillBoard();//random thêm kẹo mới, lấp đầy chỗ đã bị nổ
        }
    }
    @Override//lab5 Graphic &JPanel
    protected void paintComponent(Graphics g){
        super.paintComponent(g);//xóa kẹo cũ để chuẩn bị cho kẹo mới rớt xuống thay chỗ
        Graphics2D g2d=(Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//xử lý hình ảnh tải về từ resources

        if (bgImage !=null) g2d.drawImage(bgImage,0,0,WINDOW_WIDTH,WINDOW_HEIGHT,null);//gắn bg

        g2d.setColor(Color.WHITE);//chọn màu cọ
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD,22));//set font chữ và cỡ chữ
        g2d.drawString(gm.getScore()+"/"+gm.getTargetScore(),140,90);//vẽ bảng score
        g2d.drawString(String.valueOf(gm.getMovesLeft()),530,90);//vẽ số lượt đi

        g2d.translate(BOARD_OFFSET_X,BOARD_OFFSET_Y);//Bình thường, gốc tọa độ (0,0) của bản vẽ nằm ở góc trên cùng bên trái của cửa sổ nó giúp dời gốc tọa độ vào đúng góc ...
        Candy[][] grid=board.getGrid();
        for (int i=0;i<8;i++){
            //vẽ viền và vẽ kẹo
            for (int j=0;j<8; j++){

                if(i==firstRow && j==firstCol){
                    g2d.setColor(Color.WHITE);
                    g2d.drawRect(j*CELL_SIZE+2, i*CELL_SIZE+2, CELL_SIZE-4,CELL_SIZE-4);//kẹo đang đc chọn sẽ sẫm màu
                    
                }

                if (grid [i][j]!=null){
                    Candy candy=grid[i][j];
                    g2d.drawImage(candy.getImage(),candy.getX()+5, candy.getY()+5,CELL_SIZE-10, null);
                    //getImage thể hiện tính đa hình,giúp gom kẹo
                }
            }
        }
        g2d.translate(-BOARD_OFFSET_X,-BOARD_OFFSET_Y);

        if (gm.isGameWon()||gm.isGameOver()){
            g2d.setColor(new Color(0,0,0,150));// tạo màu đen với độ trong suốt 150
            g2d.fillRect(-BOARD_OFFSET_X,-BOARD_OFFSET_Y,WINDOW_WIDTH,WINDOW_HEIGHT);
            g2d.setColor(Color.WHITE);
            g2d.drawString(gm.isGameWon()?"Victory!":"GameOver",200,300);
        }
    }
}


