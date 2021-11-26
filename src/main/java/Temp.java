import java.util.ArrayList;
import java.util.HashMap;

public class Temp {

    public Tile[][] tempGrid = new Tile[8][8];
    public int[] firstPieceLastPos;
    public Piece movingPiece;
    public Piece firstPiece;
    public ArrayList<Piece> firstTakenPieces = new ArrayList<>();
    public ArrayList<Piece> takenPieces = new ArrayList<>();
    public int heuristicScore = 0;
    public boolean forced;
    public ArrayList<Piece> humanPieces = new ArrayList<>();
    public ArrayList<Piece> computerPieces = new ArrayList<>();

    public Temp() {

    }

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
        for (Piece p : temp.takenPieces) {
            this.takenPieces.add(p);
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

    public HashMap<Tile, Tile> calculateCandidates(Tile current) {

        HashMap<Tile, Tile> candidates = new HashMap<Tile, Tile>();
        HashMap<Tile, Tile> forcedCandidates = new HashMap<Tile, Tile>();

        // Check if the given tile is occupied by any piece.
        if (current.getIsOccupied() == true) {
            int forward = 0;
            // int backward = 0;
            int[] position = current.getPosition();
            // If it is occupied, check whether it is a human player's or a computer
            // player's.
            if (current.getOccupiedBy().getIsWhite()) {
                // Set the right forward direction accordingly.
                forward = -1;
            } else {
                forward = 1;
            }

            // ArrayList<int[][]> targetAndOppositePos = new ArrayList<>();
            // // Nested int array consists of the {x, y} coordinates of the target tile and
            // // one over it.
            // int[][] pos1 = { { position[0] + forward, position[1] + 1 },
            // { position[0] + (2 * forward), position[1] + 2 } };
            // targetAndOppositePos.add(pos1);
            // int[][] pos2 = { { position[0] + forward, position[1] - 1 },
            // { position[0] + (2 * forward), position[1] - 2 } };
            // targetAndOppositePos.add(pos2);

            // // If the occupying piece is a king, calculate backward direction too.
            // if (current.getOccupiedBy().getIsKing()) {
            // if (current.getOccupiedBy().getIsWhite()) {
            // backward = 1;
            // } else {
            // backward = -1;
            // }
            // int[][] pos3 = { { position[0] + backward, position[1] + 1 },
            // { position[0] + (2 * backward), position[1] + 2 } };
            // targetAndOppositePos.add(pos3);
            // int[][] pos4 = { { position[0] + backward, position[1] - 1 },
            // { position[0] + (2 * backward), position[1] - 2 } };
            // targetAndOppositePos.add(pos4);
            // }

            // for (int[][] pos : targetAndOppositePos) {
            // if(pos)
            // if (t.getIsOccupied()) {
            // if (this.isOccupiedByOpponent(current, t)) {

            // }
            // }
            // }
            // Append to the candidates array the forward diagonal tiles first.
            // In case it is facing the end of the board, use try{}.
            try {
                Tile t = this.tempGrid[position[0] + forward][position[1] + 1];
                if (t.getIsOccupied() == true) {
                    if (Game.isOccupiedByOpponent(current, t)) {
                        int[] oppositePos = { t.getPosition()[0] + forward, t.getPosition()[1] + 1 };
                        if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0 && oppositePos[1] < 8) {
                            Tile oppositeTile = this.tempGrid[oppositePos[0]][oppositePos[1]];
                            if (!oppositeTile.getIsOccupied()) {
                                forcedCandidates.put(oppositeTile, t);
                            }
                        }
                    }
                } else {
                    candidates.put(t, null);
                }
            } catch (Exception e) {
            }
            try {
                Tile t = this.tempGrid[position[0] + forward][position[1] - 1];
                if (t.getIsOccupied() == true) {
                    if (Game.isOccupiedByOpponent(current, t)) {
                        int[] oppositePos = { t.getPosition()[0] + forward, t.getPosition()[1] - 1 };
                        if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0 && oppositePos[1] < 8) {
                            Tile oppositeTile = this.tempGrid[oppositePos[0]][oppositePos[1]];
                            if (!oppositeTile.getIsOccupied()) {
                                forcedCandidates.put(oppositeTile, t);
                            }
                        }
                    }
                } else {
                    candidates.put(t, null);
                }
            } catch (Exception e) {
            }

            // If the piece is a king, calculate backward candidates too.
            if (current.getOccupiedBy().getIsKing()) {
                int backward;
                if (current.getOccupiedBy().getIsWhite()) {
                    // Set the right forward direction accordingly.
                    backward = 1;
                } else {
                    backward = -1;
                }

                try {
                    Tile t = this.tempGrid[position[0] + backward][position[1] + 1];
                    if (t.getIsOccupied() == true) {
                        if (Game.isOccupiedByOpponent(current, t)) {
                            int[] oppositePos = { t.getPosition()[0] + backward, t.getPosition()[1] + 1 };
                            if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0
                                    && oppositePos[1] < 8) {
                                Tile oppositeTile = this.tempGrid[oppositePos[0]][oppositePos[1]];
                                if (!oppositeTile.getIsOccupied()) {
                                    forcedCandidates.put(oppositeTile, t);
                                }
                            }
                        }
                    } else {
                        candidates.put(t, null);
                    }
                } catch (Exception e) {
                }
                try {
                    Tile t = this.tempGrid[position[0] + backward][position[1] - 1];
                    if (t.getIsOccupied() == true) {
                        if (Game.isOccupiedByOpponent(current, t)) {
                            int[] oppositePos = { t.getPosition()[0] + backward, t.getPosition()[1] - 1 };
                            if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0
                                    && oppositePos[1] < 8) {
                                Tile oppositeTile = this.tempGrid[oppositePos[0]][oppositePos[1]];
                                if (!oppositeTile.getIsOccupied()) {
                                    forcedCandidates.put(oppositeTile, t);
                                }
                            }
                        }
                    } else {
                        candidates.put(t, null);
                    }
                } catch (Exception e) {
                }
            }
        }

        if (!forcedCandidates.isEmpty()) {
            this.forced = true;
            return forcedCandidates;
        }
        this.forced = false;
        return candidates;
    }
}
