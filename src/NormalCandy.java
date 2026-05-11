public class NormalCandy extends Candy {
    public NormalCandy(CandyColor color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public String getTypeName() { return "Normal"; }

    @Override
    public void crush() {
        System.out.println("Crushed candy at [" + row + "][" + col + "]");
    }
}
