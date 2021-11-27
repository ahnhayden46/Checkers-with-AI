import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */

public class Game {
    /**
     * @param args the command line arguments
     */

    private Player currentPlayer;
    private Player opponentPlayer;
    private Player human;
    private Player computer;
    private int difficulty = 0;

    private Tile currentTile = null;
    private HashMap<Tile, Tile> candidates = null;
    private boolean forced = false;
    private boolean moved = false;
    private boolean kingCaptured = false;
    private boolean captured = false;

    public Game() {
        this.human = new Human();
        this.computer = new Computer();
        this.currentPlayer = human;
        this.opponentPlayer = computer;
        this.setDifficulty();
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
        if (currentTile != null) {
            currentTile.setIsCurrent(true);
        }
    }

    public boolean getForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public HashMap<Tile, Tile> getCandidates() {
        return candidates;
    }

    public void setCandidates(HashMap<Tile, Tile> candidates) {
        this.candidates = candidates;
        if (candidates != null) {
            for (Tile tile : candidates.keySet()) {

                tile.setIsCandidate(true);
            }
        }

    }

    public boolean getMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean getCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public boolean getKingCaptured() {
        return kingCaptured;
    }

    public void setKingCaptured(boolean kingCaptured) {
        this.kingCaptured = kingCaptured;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty() {
        String[] options = { "Easy", "Normal", "Hard" };
        int x = JOptionPane.showOptionDialog(null, "Please choose the difficulty.", "Difficulty Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        this.difficulty = x;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public void setOpponentPlayer(Player opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public Player getHuman() {
        return human;
    }

    public void setHuman(Player human) {
        this.human = human;
    }

    public Player getComputer() {
        return computer;
    }

    public void setComputer(Player computer) {
        this.computer = computer;
    }

    public String endCheck() {
        if (this.human.getPieces().isEmpty()) {
            return "Computer";
        } else if (this.computer.getPieces().isEmpty()) {
            return "Human";
        }

        return null;
    }

    public void switchCurrentPlayer() {
        Player current = this.currentPlayer;
        this.currentPlayer = this.opponentPlayer;
        this.opponentPlayer = current;
    }

    public static boolean isOccupiedByOpponent(Tile currentTile, Tile targetTile) {
        return (currentTile.getOccupiedBy().getIsWhite() != targetTile.getOccupiedBy().getIsWhite());
    }

    public HashMap<Tile, Tile> calculateCandidates(Tile[][] grid, Tile current) {

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
                Tile t = grid[position[0] + forward][position[1] + 1];
                if (t.getIsOccupied() == true) {
                    if (isOccupiedByOpponent(current, t)) {
                        int[] oppositePos = { t.getPosition()[0] + forward, t.getPosition()[1] + 1 };
                        if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0 && oppositePos[1] < 8) {
                            Tile oppositeTile = grid[oppositePos[0]][oppositePos[1]];
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
                Tile t = grid[position[0] + forward][position[1] - 1];
                if (t.getIsOccupied() == true) {
                    if (isOccupiedByOpponent(current, t)) {
                        int[] oppositePos = { t.getPosition()[0] + forward, t.getPosition()[1] - 1 };
                        if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0 && oppositePos[1] < 8) {
                            Tile oppositeTile = grid[oppositePos[0]][oppositePos[1]];
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
                    Tile t = grid[position[0] + backward][position[1] + 1];
                    if (t.getIsOccupied() == true) {
                        if (isOccupiedByOpponent(current, t)) {
                            int[] oppositePos = { t.getPosition()[0] + backward, t.getPosition()[1] + 1 };
                            if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0
                                    && oppositePos[1] < 8) {
                                Tile oppositeTile = grid[oppositePos[0]][oppositePos[1]];
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
                    Tile t = grid[position[0] + backward][position[1] - 1];
                    if (t.getIsOccupied() == true) {
                        if (isOccupiedByOpponent(current, t)) {
                            int[] oppositePos = { t.getPosition()[0] + backward, t.getPosition()[1] - 1 };
                            if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0
                                    && oppositePos[1] < 8) {
                                Tile oppositeTile = grid[oppositePos[0]][oppositePos[1]];
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

    // Count how many pieces do the player have more than the opponent (Could be
    // minus)

    public int heuristic(Temp temp) {
        int score;
        switch (this.difficulty) {
            case 0:
                score = heuristic1(temp);
                break;

            case 1:
                score = heuristic2(temp);
                break;

            case 2:
                score = heuristic3(temp);
                break;

            default:
                score = heuristic1(temp);
        }
        return score;
    }

    public int heuristic1(Temp temp) {
        return temp.firstTakenPieces.size();
    }

    public int heuristic2(Temp temp) {
        return temp.firstTakenPieces.size() + temp.numberOfComputerKings();
    }

    public int heuristic3(Temp temp) {
        return temp.firstTakenPieces.size() + temp.numberOfComputerKings() - temp.numberOfHumanKings();
    }

    public Temp minimax(Temp temp, int depth, boolean max_player, int alpha, int beta) {
        if (depth == 0) {
            temp.heuristicScore = heuristic(temp);
            return temp;
        }

        Temp result = new Temp(temp);
        ArrayList<Temp> temps = new ArrayList<>();
        if (max_player) {
            for (Piece p : temp.computerPieces) {
                temp.movingPiece = temp.computerPieces.get(temp.findPieceIndexByID(p.getID(), temp.computerPieces));
                temps = getPieceAllMoves(temp, p, temps, depth);
                for (Temp temp2 : temps) {
                    temp2 = minimax(temp2, depth - 1, false, alpha, beta);
                    if (alpha <= temp2.heuristicScore) {
                        alpha = temp2.heuristicScore;
                        result = temp2;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return result;
        } else {
            for (Piece p : temp.humanPieces) {
                temp.movingPiece = temp.humanPieces.get(temp.findPieceIndexByID(p.getID(), temp.humanPieces));
                temps = getPieceAllMoves(temp, p, temps, depth);
                for (Temp temp2 : temps) {
                    temp2 = minimax(temp2, depth - 1, true, alpha, beta);
                    if (beta >= temp2.heuristicScore) {
                        beta = temp2.heuristicScore;
                        result = temp2;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return result;
        }
    }

    public ArrayList<Temp> getPieceForcedMove(Temp temp, HashMap<Tile, Tile> cands, ArrayList<Temp> temps, int depth) {
        if (!temp.forced) {
            temp.heuristicScore = heuristic(temp);
            if (depth == 2 && temp.firstPiece.getID() == temp.movingPiece.getID()) {
                temp.firstPieceLastPos = temp.movingPiece.getPosition();
                temp.firstPiece.setIsKing(temp.movingPiece.getIsKing());
            }
            temps.add(temp);
            return temps;
        }

        for (Tile t : cands.keySet()) {
            Temp newTemp = new Temp(temp);
            int[] movingPiecePos = newTemp.movingPiece.getPosition();
            int[] candPos = t.getPosition();
            int[] takenPos = cands.get(t).getPosition();
            System.out.println(candPos[0] + " " + candPos[1] + "    " + takenPos[0] + " " + takenPos[1]);
            newTemp.movingPiece.setPosition(candPos);
            newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setOccupiedBy(null);
            newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setIsOccupied(false);
            newTemp.tempGrid[candPos[0]][candPos[1]].setOccupiedBy(temp.movingPiece);
            newTemp.tempGrid[candPos[0]][candPos[1]].setIsOccupied(true);

            if (candPos[0] == 0 || candPos[0] == 7) {
                newTemp.movingPiece.setIsKing(true);
            }

            Piece takenPiece = newTemp.tempGrid[takenPos[0]][takenPos[1]].getOccupiedBy();

            if (takenPiece.getIsWhite()) {
                newTemp.humanPieces.remove(newTemp.findPieceIndexByID(takenPiece.getID(), newTemp.humanPieces));
            } else {
                newTemp.computerPieces.remove(newTemp.findPieceIndexByID(takenPiece.getID(), newTemp.computerPieces));
            }

            if (depth == 2 && newTemp.firstPiece.getID() == newTemp.movingPiece.getID()) {
                newTemp.firstTakenPieces.add(new Piece(takenPiece));
            }

            newTemp.tempGrid[takenPos[0]][takenPos[1]].setOccupiedBy(null);
            newTemp.tempGrid[takenPos[0]][takenPos[1]].setIsOccupied(false);

            if (takenPiece.getIsKing()) {
                newTemp.movingPiece.setIsKing(true);
                newTemp.heuristicScore = heuristic(temp);
                if (depth == 2 && newTemp.firstPiece.getID() == newTemp.movingPiece.getID()) {
                    newTemp.firstPieceLastPos = newTemp.movingPiece.getPosition();
                    newTemp.firstPiece.setIsKing(newTemp.movingPiece.getIsKing());
                }
                temps.add(newTemp);
                continue;
            }

            HashMap<Tile, Tile> newCands = newTemp.calculateCandidates(newTemp.tempGrid[candPos[0]][candPos[1]]);

            temps = getPieceForcedMove(newTemp, newCands, temps, depth);
        }
        return temps;
    }

    public ArrayList<Temp> getPieceAllMoves(Temp temp, Piece p, ArrayList<Temp> temps, int depth) {
        int[] movingPiecePos = temp.movingPiece.getPosition();
        System.out.println(movingPiecePos[0]);
        HashMap<Tile, Tile> cands = temp.calculateCandidates(temp.tempGrid[movingPiecePos[0]][movingPiecePos[1]]);

        if (!cands.isEmpty()) {
            Temp newTemp = new Temp(temp);
            if (depth == 2) {
                newTemp.firstPiece = new Piece(p);
            }
            if (!temp.forced) {
                for (Tile t : cands.keySet()) {
                    int[] candPos = t.getPosition();
                    newTemp.movingPiece.setPosition(candPos);
                    newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setOccupiedBy(null);
                    newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setIsOccupied(false);
                    newTemp.tempGrid[candPos[0]][candPos[1]].setOccupiedBy(temp.movingPiece);
                    newTemp.tempGrid[candPos[0]][candPos[1]].setIsOccupied(true);
                    if (candPos[0] == 0 || candPos[0] == 7) {
                        newTemp.movingPiece.setIsKing(true);
                    }
                    newTemp.heuristicScore = heuristic(newTemp);
                    if (depth == 2 && newTemp.firstPiece.getID() == newTemp.movingPiece.getID()) {
                        newTemp.firstPieceLastPos = candPos;
                        newTemp.firstPiece.setIsKing(newTemp.movingPiece.getIsKing());
                    }
                    temps.add(newTemp);
                }
                return temps;

            } else {
                temps = getPieceForcedMove(newTemp, cands, temps, depth);
            }
        }
        return temps;
    }

    // public ArrayList<Temp> getAllMoves(Temp temp, boolean isHuman,
    // ArrayList<Temp> temps, int depth) {
    // ArrayList<Piece> pieces = new ArrayList<>();
    // if (isHuman) {
    // pieces = temp.humanPieces;
    // } else {
    // pieces = temp.computerPieces;
    // }
    // if (!pieces.isEmpty()) {
    // for (Piece p : pieces) {
    // temp.movingPiece = p;
    // if (depth == 3) {
    // temp.firstMovingPiece = p;
    // }
    // temps.addAll(getPieceAllMoves(temp, temps, depth));
    // }
    // } else {
    // System.out.println("Pieces are empty.");
    // }

    // return temps;
    // }

    // public Temp minimax2(Temp temp, int depth, boolean max_player, int alpha, int
    // beta) {
    // // If search is done with set depth, return the heuristic score with the
    // other
    // // info.
    // if (depth == 0) {
    // return temp;
    // }

    // // If max_player is true (when calculating computer player's move)
    // if (max_player) {
    // // For every piece the computer has
    // for (Piece p : temp.computerPieces) {
    // int[] pos = p.getPosition();
    // HashMap<Tile, Tile> cands = calculateCandidates(temp.tempGrid,
    // temp.tempGrid[pos[0]][pos[1]],
    // temp.forced);
    // if (!temp.forced) {
    // for (Tile t : cands.keySet()) {
    // Temp newTemp = new Temp(temp);
    // int[] tilePos = t.getPosition();
    // // Move the piece to the candidate tile
    // newTemp.tempGrid[tilePos[0]][tilePos[1]].setOccupiedBy(null);
    // newTemp.tempGrid[tilePos[0]][tilePos[1]].setIsOccupied(false);
    // newTemp.tempGrid[tilePos[0]][tilePos[1]].setOccupiedBy(newTemp.movingPiece);
    // newTemp.tempGrid[tilePos[0]][tilePos[1]].setIsOccupied(true);

    // if (tilePos[0] == 0 || tilePos[0] == 7) {
    // newTemp.movingPiece.setIsKing(true);
    // }

    // newTemp.heuristicScore = heuristic1(newTemp);
    // if(alpha <= newTemp.heuristicScore){
    // alpha = newTemp.heuristicScore;
    // }

    // if(beta <= alpha){
    // break;
    // }
    // }
    // }
    // else{
    // Temp newTemp = calculateMoves(temp, max_player, alpha, beta);
    // }
    // }
    // }

    // else{
    // for (Piece p : temp.)
    // }
    // }

    // public Temp calculateMoves2(Temp temp, boolean max_player, int alpha, int
    // beta) {
    // // Current tile where the moving piece is on is the last tile in the path
    // // arraylist.
    // Tile currentTile = temp.path.get(temp.path.size() - 1);
    // // Calculate candidate tiles for the current tile.
    // HashMap<Tile, Tile> cands = calculateCandidates(temp.tempGrid, currentTile,
    // temp.forced);
    // // If the candidates are not forced ones, halt by returning the parameter
    // temp.
    // if (!temp.forced) {
    // temp.heuristicScore = heuristic1(temp);
    // return temp;
    // }
    // Temp newTemp = new Temp();
    // // If there's any forced candidates, for every candidate do
    // for (Tile t : cands.keySet()) {
    // // Add the candidate to the path arraylist.
    // temp.path.add(t);
    // int[] pos = t.getPosition();
    // // Move the piece to the candidate tile
    // temp.tempGrid[pos[0]][pos[1]].setOccupiedBy(null);
    // temp.tempGrid[pos[0]][pos[1]].setIsOccupied(false);
    // temp.tempGrid[pos[0]][pos[1]].setOccupiedBy(temp.movingPiece);
    // temp.tempGrid[pos[0]][pos[1]].setIsOccupied(true);

    // if (pos[0] == 0 || pos[0] == 7) {
    // temp.movingPiece.setIsKing(true);
    // }

    // // Get the position of the tile that the candidate was jumped over to
    // through.
    // int[] takenPos = cands.get(t).getPosition();
    // // Remove the piece on that tile
    // temp.tempGrid[takenPos[0]][takenPos[1]].setOccupiedBy(null);
    // temp.tempGrid[takenPos[0]][takenPos[1]].setIsOccupied(false);
    // newTemp = calculateMoves(temp, max_player, alpha, beta);
    // if (max_player) {
    // if (alpha <= newTemp.heuristicScore) {
    // alpha = newTemp.heuristicScore;
    // return newTemp;
    // }
    // if (beta <= alpha) {
    // break;
    // }
    // }

    // }
    // return newTemp;
    // }

}
