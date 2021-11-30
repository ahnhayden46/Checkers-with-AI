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

// A class containing information for gameplay.
public class Game {
    /**
     * @param args the command line arguments
     */

    private Player currentPlayer;
    private Player opponentPlayer;
    private Player human;
    private Player computer;
    private int difficulty = 0; // Difficulty of the game -> set when the object is initialised.

    private Tile currentTile = null; // The tile selected as current by the selecting a piece method in the Board
                                     // class.
    private ArrayList<Tile> hintTiles = new ArrayList<>(); // An arraylist of the hint tiles calculated by the enabling
                                                           // hints method in the Board class.
    private HashMap<Tile, Tile> candidates = null; // A hashmap that contains tiles to which the current moving piece
                                                   // can go as keys and tiles that it should jump over as values.
    private boolean forced = false; // Indicates that the current piece performed a forced capture.
    private boolean moved = false; // Indicates that any piece has moved to another tile.
    private boolean kingCaptured = false; // Indicates that a king has been captured.
    private boolean captured = false; // Indicates that any piece has been captured.
    private ArrayList<Tile> forcedCandidates = new ArrayList<>();

    public Game() {
        this.human = new Human();
        this.computer = new Computer();
        this.currentPlayer = human;
        this.opponentPlayer = computer;
        // Sets the difficulty for the game by asking the player
        this.setDifficulty();
    }

    // Sets the difficulty by showing an option dialog.
    // Easy, normal, and hard modes are available.
    public void setDifficulty() {
        String[] options = { "Easy", "Normal", "Hard" };
        int x = JOptionPane.showOptionDialog(null, "Please choose the difficulty.", "Difficulty Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        this.difficulty = x;
    }

    // Checks if the game has ended, i.e., if either of the players doesn't have any
    // piece left and if it has, returns which player has won.
    public String endCheck() {
        if (this.human.getPieces().isEmpty()) {
            return "Computer";
        } else if (this.computer.getPieces().isEmpty()) {
            return "Human";
        }

        return null;
    }

    // Checks if the tiles given are occupied by different players.
    public static boolean isOccupiedByOpponent(Tile currentTile, Tile targetTile) {
        return (currentTile.getOccupiedBy().getIsWhite() != targetTile.getOccupiedBy().getIsWhite());
    }

    // Successor functions.
    // Calculate the candidate tiles to which the piece on the tile given can go
    // after checking the validity.
    // Returns a hashmap containing keys which are the possible destinations of the
    // moving piece and values which are the tiles that the piece should jump over
    // if it has to.
    public HashMap<Tile, Tile> calculateCandidates(Tile[][] grid, Tile current) {

        HashMap<Tile, Tile> candidates = new HashMap<Tile, Tile>(); // For normal moves
        HashMap<Tile, Tile> forcedCandidates = new HashMap<Tile, Tile>(); // For forced capturing moves

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
                    Tile t = grid[pos[0]][pos[1]];
                    // Check if the tile is occupied.
                    if (t.getIsOccupied() == true) {
                        // If it is, check if it's occupied by an opponent piece.
                        if (Game.isOccupiedByOpponent(current, t)) {
                            int[] oppositePos = positions.get(pos);
                            // Make sure the tile position is not pointing outside the grid.
                            if (oppositePos[0] >= 0 && oppositePos[0] < 8 && oppositePos[1] >= 0
                                    && oppositePos[1] < 8) {
                                Tile oppositeTile = grid[oppositePos[0]][oppositePos[1]];
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

    // Decides which heuristic to use depending on the difficulty of the game.
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

    // The hueristic for easy mode,
    // only calculates how many pieces the player can take at this turn.
    public int heuristic1(Temp temp) {
        return temp.firstTakenPieces.size();
    }

    // The hueristic for normal mode,
    // reflects how many more pieces the computer will have than the human after
    // exploring the minimax algorithm and the number of king pieces of the
    // computer.
    public int heuristic2(Temp temp) {
        return (temp.computerPieces.size() - temp.humanPieces.size());
    }

    // The heuristic for hard mode,
    // reflects the second heuristic plus the number of king pieces of the human
    // player.
    public int heuristic3(Temp temp) {
        return (temp.computerPieces.size() - temp.humanPieces.size()) + temp.numberOfComputerKings() * 2
                - temp.numberOfHumanKings();
    }

    // The minimax algorithm for the computer and showing hints to the human player.
    // Returns a temp object that contains not only the heuristic score but also the
    // information of moves.
    // A recursive method
    public Temp minimax(Temp temp, int depth, boolean max_player, int alpha, int beta) {
        // Returns the temp object with a heuristic score when it reaches the set depth.
        if (depth == 0) {
            temp.heuristicScore = heuristic(temp);
            return temp;
        }

        Temp result = new Temp(temp);
        // An arraylist of temp objects that will store possible states (nodes).
        ArrayList<Temp> temps = new ArrayList<>();

        // When it should maximise the score.
        if (max_player) {
            // For each piece, calculate possible future states.
            for (Piece p : temp.computerPieces) {
                temp.movingPiece = temp.computerPieces.get(temp.findPieceIndexByID(p.getID(), temp.computerPieces));
                temps = getPieceAllMoves(temp, p, temps, depth);
                // For every state calculated,
                for (Temp temp2 : temps) {
                    // Expand nodes by calling the method recursively.
                    // Change the 'max_player' value to false so it can maximise the state score on
                    // the next depth.
                    temp2 = minimax(temp2, depth - 1, false, alpha, beta);

                    // Implement alpha-beta pruning
                    // If the current state's heuristic score is higher than alpha, make the state
                    // alpha and update the alpha value.
                    if (alpha <= temp2.heuristicScore) {
                        alpha = temp2.heuristicScore;
                        result = temp2;
                        // If alpha is bigger than beta, break the loop and stop exploring further.
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
                // Since I divided getting all the possible states into two steps, break it once
                // more outside the nested for loop.
                if (beta <= alpha) {
                    break;
                }
            }
            return result;

            // When it should minimise the score.
        } else {
            for (Piece p : temp.humanPieces) {
                temp.movingPiece = temp.humanPieces.get(temp.findPieceIndexByID(p.getID(), temp.humanPieces));
                temps = getPieceAllMoves(temp, p, temps, depth);
                for (Temp temp2 : temps) {
                    // Change the 'max_player' value to true so it can maximise the state score on
                    // the next depth.
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

    // A method to calculate all possible moves of the piece given.
    public ArrayList<Temp> getPieceAllMoves(Temp temp, Piece p, ArrayList<Temp> temps, int depth) {

        // Get the position of the given piece and calculate candidate tiles from it.
        int[] movingPiecePos = temp.movingPiece.getPosition();
        HashMap<Tile, Tile> cands = temp.calculateCandidates(temp.tempGrid[movingPiecePos[0]][movingPiecePos[1]]);

        // If there is any candidate tile available from the current position
        if (!cands.isEmpty()) {
            // Create a new temp object and copy the given temp to expand a new node.
            Temp newTemp = new Temp(temp);
            // If it's the first recursion, assign the first moving piece as the given
            // piece.
            if (depth == 2) {
                newTemp.firstPiece = new Piece(p);
            }
            // If there's no force capture available
            if (!newTemp.forced) {
                // For each candidate tile,
                for (Tile t : cands.keySet()) {
                    // Create another temp object to save each candidate tile.
                    newTemp = new Temp(newTemp);
                    // Get the position of the candidate tile and move the piece to it and update
                    // the grid.
                    int[] candPos = t.getPosition();
                    newTemp.movingPiece.setPosition(candPos);
                    newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setOccupiedBy(null);
                    newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setIsOccupied(false);
                    newTemp.tempGrid[candPos[0]][candPos[1]].setOccupiedBy(newTemp.movingPiece);
                    newTemp.tempGrid[candPos[0]][candPos[1]].setIsOccupied(true);
                    // Check if it has reached the king's row.
                    if (candPos[0] == 0 || candPos[0] == 7) {
                        newTemp.movingPiece.setIsKing(true);
                    }
                    // Calculate the heuristic score of the current state.
                    newTemp.heuristicScore = heuristic(newTemp);
                    // If it's the first move of the first recursion, update the first piece's
                    // information in the temp object.
                    if (depth == 2 && newTemp.firstPiece.getID() == newTemp.movingPiece.getID()) {
                        newTemp.firstPieceLastPos = candPos;
                        newTemp.firstPiece.setIsKing(newTemp.movingPiece.getIsKing());
                    }
                    temps.add(newTemp);
                }
                return temps;

                // If the candidate tiles have any forced capture, perform the method to
                // expand nodes further.
            } else {
                temps = getPieceForcedMove(newTemp, cands, temps, depth);
            }
        }
        return temps;
    }

    // Returns an arraylist with possible states with forced capturing moves.
    // A recursive method that stops when there's not any forced capture
    // possibleanymore.
    public ArrayList<Temp> getPieceForcedMove(Temp temp, HashMap<Tile, Tile> cands, ArrayList<Temp> temps, int depth) {
        // If there's not forced capture possible,
        if (!temp.forced) {
            // Evaluate the state
            temp.heuristicScore = heuristic(temp);
            // When it's calculating the first move of the firstly selected piece,
            if (depth == 2 && temp.firstPiece.getID() == temp.movingPiece.getID()) {
                // Update the information of the first piece.
                temp.firstPieceLastPos = temp.movingPiece.getPosition();
                temp.firstPiece.setIsKing(temp.movingPiece.getIsKing());
            }
            temps.add(temp);
            return temps;
        }

        // For each candidate tiles,
        for (Tile t : cands.keySet()) {
            // Create a new temp object and retrieve the current information.
            Temp newTemp = new Temp(temp);
            // Get the positions of the current piece, its candidate, and the tile it should
            // jump over.
            int[] movingPiecePos = newTemp.movingPiece.getPosition();
            int[] candPos = t.getPosition();
            int[] takenPos = cands.get(t).getPosition();
            // Update the position of the current piece and the grid.
            newTemp.movingPiece.setPosition(candPos);
            newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setOccupiedBy(null);
            newTemp.tempGrid[movingPiecePos[0]][movingPiecePos[1]].setIsOccupied(false);
            newTemp.tempGrid[candPos[0]][candPos[1]].setOccupiedBy(newTemp.movingPiece);
            newTemp.tempGrid[candPos[0]][candPos[1]].setIsOccupied(true);

            // Check if it has reached the king's row
            if (candPos[0] == 0 || candPos[0] == 7) {
                // If it has, make it a king.
                newTemp.movingPiece.setIsKing(true);
            }

            // Update the taken piece's information and the tile on the grid which it was
            // on.
            Piece takenPiece = newTemp.tempGrid[takenPos[0]][takenPos[1]].getOccupiedBy();

            // Remove the piece from the temp object's pieces list.
            // Use try to avoid complicated if statements (it still works since the human
            // and computer have pieces with unique IDs).
            try {
                newTemp.humanPieces.remove(newTemp.findPieceIndexByID(takenPiece.getID(), newTemp.humanPieces));
            } catch (Exception e) {
            }
            try {
                newTemp.computerPieces.remove(newTemp.findPieceIndexByID(takenPiece.getID(), newTemp.humanPieces));
            } catch (Exception e) {
            }

            // If it's the first move of the first piece, remember the taken piece to update
            // on the real grid.
            if (depth == 2 && newTemp.firstPiece.getID() == newTemp.movingPiece.getID()) {
                newTemp.firstTakenPieces.add(new Piece(takenPiece));
            }

            // Update the grid in the current temp object.
            newTemp.tempGrid[takenPos[0]][takenPos[1]].setOccupiedBy(null);
            newTemp.tempGrid[takenPos[0]][takenPos[1]].setIsOccupied(false);

            // If the taken piece is a king, perform regicide and cease further moving.
            if (takenPiece.getIsKing()) {
                newTemp.movingPiece.setIsKing(true);
                newTemp.heuristicScore = heuristic(temp);

                // If it's the first move of the first piece, update the first taken piece's
                // information.
                if (depth == 2 && newTemp.firstPiece.getID() == newTemp.movingPiece.getID()) {
                    newTemp.firstPieceLastPos = newTemp.movingPiece.getPosition();
                    newTemp.firstPiece.setIsKing(newTemp.movingPiece.getIsKing());
                }
                temps.add(newTemp);
                continue;
            }

            // Recursion call to calculate candiates until it has no forced capture
            // available.
            HashMap<Tile, Tile> newCands = newTemp.calculateCandidates(newTemp.tempGrid[candPos[0]][candPos[1]]);
            temps = getPieceForcedMove(newTemp, newCands, temps, depth);
        }
        return temps;
    }

    // Resets the current and candidate tile instances in the object.
    public void resetTiles() {
        this.currentTile.setIsCurrent(false);
        for (Tile t : this.candidates.keySet()) {
            t.setIsCandidate(false);
        }
        this.currentTile = null;
        this.candidates = null;
    }

    // Getters and setters from here.
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

    public ArrayList<Tile> getHintTiles() {
        return hintTiles;
    }

    public void addHintTile(Tile hintTile) {
        this.hintTiles.add(hintTile);
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

    public ArrayList<Tile> getForcedCandidates() {
        return forcedCandidates;
    }

    public void setForcedCandidates(ArrayList<Tile> forcedCandidates) {
        this.forcedCandidates = forcedCandidates;
    }

    public void addForcedCandidates(Tile t) {
        this.forcedCandidates.add(t);
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
}
