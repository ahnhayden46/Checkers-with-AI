import java.util.ArrayList;

public class State {

    public Piece movingPiece;
    public ArrayList<Tile> path;

    public State(Piece movingPiece, ArrayList<Tile> path) {
        this.movingPiece = movingPiece;
        this.path = path;
    }

}
