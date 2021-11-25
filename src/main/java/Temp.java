import java.util.ArrayList;

public class Temp {

    public Tile[][] tempGrid = new Tile[8][8];
    public Piece movingPiece;
    public int[] finalPostiion;
    public ArrayList<int[]> takenPoses = new ArrayList<>();
    public int heuristicScore = -10000;
    public boolean forced;
    public ArrayList<Piece> humanPieces = new ArrayList<>();
    public ArrayList<Piece> computerPieces = new ArrayList<>();

    public Temp() {

    }

    public Temp(Temp temp) {
        this.tempGrid = temp.tempGrid;
        this.movingPiece = temp.movingPiece;
        this.finalPostiion = temp.finalPostiion;
        this.takenPoses = temp.takenPoses;
        this.heuristicScore = temp.heuristicScore;
        this.forced = temp.forced;
        this.humanPieces = temp.humanPieces;
        this.computerPieces = temp.computerPieces;
    }

    public Temp(Tile[][] tempGrid, Piece movingPiece) {
        this.tempGrid = tempGrid;
        this.movingPiece = movingPiece;
    }

    public Temp(Tile[][] tempGrid, ArrayList<Piece> humanPieces, ArrayList<Piece> computerPieces) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                System.out.println(tempGrid[i][j].getIsOccupied());
                Tile newTile = new Tile(tempGrid[i][j]);
                this.tempGrid[i][j] = newTile;
            }
        }
        if (!humanPieces.isEmpty()) {
            for (Piece p : humanPieces) {
                Piece newPiece = new Piece(p);
                this.humanPieces.add(newPiece);
            }
        }
        if (!computerPieces.isEmpty()) {
            for (Piece p : computerPieces) {
                Piece newPiece = new Piece(p);
                this.computerPieces.add(newPiece);
            }
        }
    }

    public void printAll() {
        System.out.println(this.movingPiece + " " + this.finalPostiion + " " + this.heuristicScore);
    }
}
