import javax.swing.JFrame;
public class Main {
    public static void main(String[] args){
        JFrame frame=new JFrame ("Candy Crush");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);//cố định kích thước frame

        Board board = new Board(8,8);//đối tượng qly data, logic cua bang chinh
        GamePanel panel= new GamePanel(board);
        frame.add (panel);

        frame.pack();
        frame.setLocationRelativeTo(null);//hiện khung ra chính giữa
        frame.setVisible(true);//hiển thị cửa sổ
    }
    
}
