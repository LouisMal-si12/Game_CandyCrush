/**
 * Class NormalCandy - Standard candy implementation.
 * (Kẹo bình thường - Có thể mở rộng kẹo sọc, kẹo bọc sau này.)
 */
public class NormalCandy extends Candy {
    public NormalCandy(CandyColor color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public String getTypeName() { return "Normal"; }

    @Override
    public void crush() {
        // Output for debugging (In ra console để test)
        System.out.println("Crushed candy at [" + row + "][" + col + "]");
    }
}
