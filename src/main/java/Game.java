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
    private int difficulty;

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

    public boolean endCheck() {
        return (this.currentPlayer.getPieces().isEmpty());
    }

    public void switchCurrentPlayer() {
        Player current = this.currentPlayer;
        this.currentPlayer = this.opponentPlayer;
        this.opponentPlayer = current;
    }

    public boolean isOccupiedByOpponent(Tile currentTile, Tile targetTile) {
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

    public int heuristic1() {
        return this.currentPlayer.getPieces().size() - this.opponentPlayer.getPieces().size();
    }

    public void minimax(Temp temp, int depth, boolean max_player, int alpha, int beta) {
        if (depth == 0) {
            int score = heuristic1();

        }

        if (max_player) {
            int max_eval = -10000;
            for (Piece p : this.currentPlayer.getPieces()) {
            }
        }
    }

    // public Temp calculateMoves(Temp temp) {
    // int[] pos = temp.getMovingPiece().getPosition();
    // Tile[][] tempGrid = temp.getTempGrid();
    // Tile tempTile = tempGrid[pos[0]][pos[1]];
    // HashMap<Tile, Tile> cands = calculateCandidates(tempGrid, tempTile);

    // }
}
