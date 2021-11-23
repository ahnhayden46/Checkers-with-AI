import java.util.ArrayList;

public class Temp {

    Tile[][] tempGrid = new Tile[8][8];
    Piece movingPiece;
    ArrayList<Tile> path;

    public Temp(Tile[][] tempGrid, Piece movingPiece, ArrayList<Tile> path) {
        this.tempGrid = tempGrid;
        this.movingPiece = movingPiece;
        this.path = path;
    }

    public Tile[][] getTempGrid() {
        return tempGrid;
    }

    public void setTempGrid(Tile[][] tempGrid) {
        this.tempGrid = tempGrid;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    public ArrayList<Tile> getPath() {
        return path;
    }

    public void setPath(ArrayList<Tile> path) {
        this.path = path;
    }
}
