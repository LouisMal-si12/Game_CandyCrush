import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;


public abstract class Candy {
    protected int row;
    protected int col;
    protected CandyColor color;
    protected Image image;

    protected int x, y;              
    protected int targetX, targetY;   
    protected boolean isMoving;       
    protected final int CELL_SIZE = 75; 

    public Candy(CandyColor color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
        
        this.x = col * CELL_SIZE;
        this.y = row * CELL_SIZE;
        this.targetX = x;
        this.targetY = y;
        this.isMoving = false;
        
        loadImage();
    }


    protected void loadImage() {
        try {
            
            String path = "/resources/" + color.name().toLowerCase() + ".png";
            
            java.net.URL imgURL = getClass().getResource(path);
            
            if (imgURL != null) {
                this.image = new ImageIcon(imgURL).getImage();
            } else {
                System.out.println("WARNING: Image not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading image for color: " + color);
            e.printStackTrace();
        }
    }
    public abstract String getTypeName(); 
    public abstract void crush();


    public void update() {
        if (x != targetX || y != targetY) {
            isMoving = true;
            int speedX = (targetX - x) / 4;
            int speedY = (targetY - y) / 4;

        
            if (Math.abs(targetX - x) < 4) x = targetX; else x += speedX;
            if (Math.abs(targetY - y) < 4) y = targetY; else y += speedY;
        } else {
            isMoving = false;
        }
    }
    public int getRow() { 
        return row; 
    }
    public int getCol() {
        return col; 
    }
    public int getX() {
        return x;
    }
    public int getY() { 
        return y; 
    }
    public boolean isMoving() { 
        return isMoving;
    }
    public CandyColor getColor() {
        return color;
    }
    public Image getImage() {
        return image;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
        this.targetX = col * CELL_SIZE;
        this.targetY = row * CELL_SIZE;
    }

 
    public void dropFromTop(int row, int col) {
        this.row = row;
        this.col = col;
        this.targetX = col * CELL_SIZE;
        this.targetY = row * CELL_SIZE;
        this.x = targetX;
        this.y = targetY - 400; 
}
}
