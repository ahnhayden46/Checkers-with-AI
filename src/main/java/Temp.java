import java.util.ArrayList;
import java.util.HashMap;

// A class containing information of a state for the minimax algorithm.
public class Temp {

    public Tile[][] tempGrid = new Tile[8][8];
    public Piece movingPiece; // Currently moving piece
    public Piece firstPiece; // The firstly selected piece (since the minimax will explore several depths of
                             // states, it should remember what was the piece that moved first)
    public int[] firstPieceLastPos; // The last position of the first piece, i.e., the position to which the piece
                                    // on the real board should move.
    public ArrayList<Piece> firstTakenPieces = new ArrayList<>(); // An arraylist of pieces that were taken by the first
                                                                  // piece on the first move.
    public int heuristicScore = 0; // Evaluation of the state
    public boolean forced; // If the current piece has performed forced capture.
    public ArrayList<Piece> humanPieces = new ArrayList<>();
    public ArrayList<Piece> computerPieces = new ArrayList<>();

    public Temp() {
    }

    // A constructor that copies another temp object.
    public Temp(Temp temp) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Tile tile = new Tile(temp.tempGrid[i][j]);
                this.tempGrid[i][j] = tile;
            }
        }
        if (temp.firstPiece != null) {
            this.firstPiece = new Piece(temp.firstPiece);
        }
        for (Piece p : temp.firstTakenPieces) {
            this.firstTakenPieces.add(p);
        }
        this.firstPieceLastPos = temp.firstPieceLastPos;
        for (Piece p : temp.humanPieces) {
            this.humanPieces.add(p);
        }
        for (Piece p : temp.computerPieces) {
            this.computerPieces.add(p);
        }
        if (temp.movingPiece != null) {
            this.movingPiece = new Piece(temp.movingPiece);

        }
        this.heuristicScore = temp.heuristicScore;
        this.forced = temp.forced;
    }

    public Temp(Tile[][] tempGrid) {
        this.tempGrid = tempGrid;
    }

    public Temp(Tile[][] tempGrid, ArrayList<Piece> humanPieces, ArrayList<Piece> computerPieces) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
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

    // Returns the number of the kings in the computer pieces.
    public int numberOfComputerKings() {
        int i = 0;
        for (Piece p : this.computerPieces) {
            if (p.getIsKing()) {
                i++;
            }
        }
        return i;
    }

    // Returns the number of the kings in the computer pieces.
    public int numberOfHumanKings() {
        int i = 0;
        for (Piece p : this.humanPieces) {
            if (p.getIsKing()) {
                i++;
            }
        }
        return i;
    }

    // Finds the index of a specific piece in the given arraylist by its ID.
    public int findPieceIndexByID(int ID, ArrayList<Piece> pieces) {
        boolean found = false;
        int i = 0;
        while (!found) {
            if (pieces.get(i).getID() == ID) {
                found = true;
            } else {
                i++;
            }
        }
        return i;
    }

    // The same code as in the Game class but added here for coding convenience.
    public HashMap<Tile, Tile> calculateCandidates(Tile current) {

        HashMap<Tile, Tile> candidates = new HashMap<Tile, Tile>();
        HashMap<Tile, Tile> forcedCandidates = new HashMap<Tile, Tile>();

        // Check if the given tile is occupied by any piece.
        if (current.getIsOccupied() == true) {
            HashMap<int[], int[]> positions = new HashMap<>(); // <The position of an adjacent tile, the position of the
                                                               // tile beyond the adjacent one>
            int forward = 0;
            int backward;
            int[] position = current.getPosition();

            // Check whether it is a human player's or a computer player's.
            if (current.getOccupiedBy().getIsWhite()) {
                // Set the right forward direction accordingly.
                forward = -1;
            } else {
                forward = 1;
            }

            int[] candidatePos1 = { position[0] + forward, position[1] + 1 };
            int[] oppositePos1 = { candidatePos1[0] + forward, candidatePos1[1] + 1 };
            positions.put(candidatePos1, oppositePos1);
            int[] candidatePos2 = { position[0] + forward, position[1] - 1 };
            int[] oppositePos2 = { candidatePos2[0] + forward, candidatePos2[1] - 1 };
            positions.put(candidatePos2, oppositePos2);

            // If the piece is a king, calculate backward diagonal candidates too.
            if (current.getOccupiedBy().getIsKing()) {
                if (current.getOccupiedBy().getIsWhite()) {
                    // Set the right forward direction accordingly.
                    backward = 1;
                } else {
                    backward = -1;
                }
                int[] candidatePos3 = { position[0] + backward, position[1] + 1 };
                int[] oppositePos3 = { candidatePos3[0] + backward, candidatePos3[1] + 1 };
                positions.put(candidatePos3, oppositePos3);
                int[] candidatePos4 = { position[0] + backward, position[1] - 1 };
                int[] oppositePos4 = { candidatePos4[0] + backward, candidatePos4[1] - 1 };
                positions.put(candidatePos4, oppositePos4);
            }

            for (int[] pos : positions.keySet()) {
                try {
                    Tile t = this.tempGrid[pos[0]][pos[1]];
                    // Check if the tile is occupied.
                    if (t.getIsOccupied() == true) {
                        // If it is, check if it's occupied by an opponent piece.
                        if (Game.isOccupiedByOpponent(current, t)) {
                            int[] oppositePos = positions.get(pos);
                            // Make sure the tile position is not pointing outside the grid.
                            if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0
                                    && oppositePos[1] < 8) {
                                Tile oppositeTile = this.tempGrid[oppositePos[0]][oppositePos[1]];
                                // If it is, check if the tile beyond the occupied tile is empty so the moving
                                // piece can proceed to it.
                                if (!oppositeTile.getIsOccupied()) {
                                    // Add the tile to the forced candidates hashmap which contains the tile that
                                    // the piece should be placed on and the tile that it jumped over.
                                    forcedCandidates.put(oppositeTile, t);
                                }
                            }
                        }
                        // If the tile is empty, just add it as a key to the candidate hashmap with a
                        // 'null' value.
                    } else {
                        candidates.put(t, null);
                    }
                } catch (Exception e) {
                }
            }
        }

        // If any forced candidate tile was given, return the forced candidates hashmap
        // and set the current temp object's 'forced' value to true so further
        // algorithms can decide appropriate actions.
        if (!forcedCandidates.isEmpty()) {
            this.forced = true;
            return forcedCandidates;
        }

        // Otherwise, return the normal candidates hashmap.
        this.forced = false;
        return candidates;
    }
}
