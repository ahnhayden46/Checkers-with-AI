import java.util.ArrayList;

public class Temp {

    public Tile[][] tempGrid = new Tile[8][8];
    public Piece movingPiece;
    public ArrayList<Tile> path;
    public int heuristicScore;
    public boolean forced;

    public Temp(Tile[][] tempGrid, Piece movingPiece) {
        this.tempGrid = tempGrid;
        this.movingPiece = movingPiece;
    }

    public void heuristic1() {

    }
}
