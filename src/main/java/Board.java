
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.FlowLayout;
import java.awt.Font;

// A class representing the game board.
public class Board extends JPanel {

    private Tile[][] grid = new Tile[8][8]; // Consists of 8*8 tiles.
    private Dimension screenSize; // The screensize of the user to visualise the board.
    private int tileSize; // The size of the tiles, shares the same value with the tile objects.
    private Game game; // A game object that contains information needed for gameplay.
    private GridBagConstraints c = new GridBagConstraints(); // Layout component for neat visualisation.

    public Board(Dimension screenSize) {
        this.screenSize = screenSize;
        this.tileSize = (int) (Math.min(screenSize.getHeight(), screenSize.getWidth()) / 16);
        this.game = new Game();
        this.setGrid();
        this.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        this.arrangeTiles();
        this.placeInitialCheckers();
    }

    // Sets a grid by creating tile objects
    public void setGrid() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int[] position = { i, j };
                Tile t = new Tile(position, this.tileSize);
                initMouseAdapter(t, this);
                this.grid[i][j] = t;
            }
        }
    }

    // Place tiles on the board using the grid bag layout.
    public void arrangeTiles() {
        int x = 0;
        int y = 0;
        for (Tile[] row : this.grid) {
            for (Tile tile : row) {
                c.gridx = x;
                c.gridy = y;
                this.add(tile, c);
                x++;
            }
            x = 0;
            y++;
        }
    }

    // Places the checkers of the human and computer on the grid, i.e., game board.
    public void placeInitialCheckers() {
        // Place the human player's pieces
        for (Piece p : this.game.getHuman().getPieces()) {
            Tile t = this.grid[p.getPosition()[0]][p.getPosition()[1]];
            t.setOccupiedBy(p);
            t.setIsOccupied(true);
        }
        // Place the computer's pieces
        for (Piece p : this.game.getComputer().getPieces()) {
            Tile t = this.grid[p.getPosition()[0]][p.getPosition()[1]];
            t.setOccupiedBy(p);
            t.setIsOccupied(true);
        }
    }

    // A method for setting a hint tile outside this class.
    public void setHintTileInGrid(int[] pos, boolean isHint) {
        this.grid[pos[0]][pos[1]].setIsHint(isHint);
    }

    // Adds a mouse adapter for each tile to enable interactions between the game
    // and the player.
    public void initMouseAdapter(Tile t, Board board) {
        t.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                // If it's the human player's turn
                if (board.getGame().getCurrentPlayer().getIsHuman()) {
                    int[] pos = t.getPosition();
                    // If there are hint tiles on the board, reset them.
                    if (!board.game.getHintTiles().isEmpty()) {
                        ArrayList<Tile> hintTiles = board.game.getHintTiles();
                        for (Tile t : hintTiles) {
                            int[] position = t.getPosition();
                            board.setHintTileInGrid(position, false);
                        }
                        board.game.getHintTiles().clear();
                    }

                    // Select a piece and perform a right action depending on the current state.
                    board.selectAPiece(t, board);
                    board.repaint();

                    // If any human piece has been moved
                    if (board.game.getMoved()) {

                        // For multi-leg captures
                        // if it has captured any opponent piece and it's not a king,
                        if (board.game.getCaptured() && !board.game.getKingCaptured()) {
                            // calculate candidates again from the moved position and see if any possible
                            // forced capture exists.
                            HashMap<Tile, Tile> cands = board.getGame().calculateCandidates(board.getGrid(), t);
                            // If there is a forced capture candidate, update the board to show them.
                            if (board.game.getForced()) {
                                for (Tile t : board.game.getForcedCandidates()) {
                                    t.setIsForcedCandidate(false);
                                }
                                board.game.getForcedCandidates().clear();
                                board.game.addForcedCandidates(t);
                                t.setIsForcedCandidate(true);
                                board.game.setCurrentTile(t);
                                board.game.setCandidates(cands);
                                board.repaint();
                                // If there is not, reset the forced candidate variable and switch the current
                                // and the opponent player.
                            } else {
                                for (Tile t : board.game.getForcedCandidates()) {
                                    t.setIsForcedCandidate(false);
                                }
                                board.game.getForcedCandidates().clear();
                                board.getGame().setCurrentPlayer(board.getGame().getComputer());
                                board.getGame().setOpponentPlayer(board.getGame().getHuman());
                            }
                        }
                        // If the piece didn't capture anything but only moved, switch the player turn.
                        else {
                            for (Tile t : board.game.getForcedCandidates()) {
                                t.setIsForcedCandidate(false);
                            }
                            board.game.getForcedCandidates().clear();
                            board.getGame().setCurrentPlayer(board.getGame().getComputer());
                            board.getGame().setOpponentPlayer(board.getGame().getHuman());
                        }

                        // Reset values of the game.
                        board.game.setMoved(false);
                        board.game.setKingCaptured(false);
                        board.game.setCaptured(false);

                        // Checks if the game has ended, i.e., if either of the players doesn't have any
                        // piece left.
                        if (board.game.endCheck() != null) {
                            JOptionPane.showMessageDialog(board, board.game.endCheck() + " has won!");
                        }
                    }
                }
            }
        });
    }

    // Checks if there should be a forced capture.
    public void forcedCapturingAvailable() {
        ArrayList<Tile> forcedCandidates = new ArrayList<>();
        // For every piece of the current player on the board
        for (Piece p : this.game.getCurrentPlayer().getPieces()) {
            // Create a temp object to calculate all the candidates
            Temp temp = new Temp(this.grid);
            int[] pos = p.getPosition();
            Tile tile = temp.tempGrid[pos[0]][pos[1]];
            temp.calculateCandidates(tile);
            // If there is an available forced capture, add it to the forced candidate
            // variable.
            if (temp.forced) {
                forcedCandidates.add(tile);
                this.game.setForcedCandidates(forcedCandidates);
                tile.setIsForcedCandidate(true);
            }
        }
    }

    // Shows the rules when clicked
    public void showRules() {
        JOptionPane.showMessageDialog(this,
                "Each checker can make a diagonal move forward from one square to an adjacent sqaure. \nWhen it reaches at the end of the row, it becomes a king and can move backward too. \nCapturing moves occur when a player jumps an opposing piece and the square behind is unoccupied by another piece. \nWhen the requirement is met, the capturing must be made and several jumps can be made in one turn.");
    }

    // A method that uses the minimax algorithm for the human player and shows
    // hints.
    public void enableHints() {
        if (this.game.getCurrentPlayer().getIsHuman()) {
            // Calculate hints only when they were not calculated before.
            if (this.game.getHintTiles().isEmpty()) {
                // Perform the minimax algorithm for the human player by giving the current
                // computer pieces as human
                // pieces and vice versa.
                Temp temp = new Temp(this.grid, this.game.getComputer().getPieces(), this.game.getHuman().getPieces());
                temp = this.game.minimax(temp, 2, true, -10000, 10000);
                // Retrieve the information of the node (a temp object) that has the highest
                // heuristic score calculated by the minimax.
                int ID = temp.firstPiece.getID();
                int index = this.game.getHuman().findPieceIndexByID(ID);
                int[] pos = this.game.getHuman().getPieces().get(index).getPosition();
                int[] lastPos = temp.firstPieceLastPos;
                Tile hintTile = this.grid[pos[0]][pos[1]];
                Tile lastHintTile = this.grid[lastPos[0]][lastPos[1]];
                // Update the hint tiles
                hintTile.setIsHint(true);
                lastHintTile.setIsHint(true);
                this.game.addHintTile(hintTile);
                this.game.addHintTile(lastHintTile);
                this.repaint();
            }
            // Triggered when the user tries to enable hints when it's a computer's turn.
        } else {
            JOptionPane.showMessageDialog(this, "Hints are available when it's your turn.");
        }
    }

    // A method that gets called when the computer player takes turn.
    // Performs the minimax and then move a piece and update the current grid
    // depending on the result.
    public void computerTurn() {
        // Only triggered when it's computer's turn.
        if (this.game.getCurrentPlayer().equals(this.game.getComputer())) {
            // Perform minimax and get the result.
            Temp temp = new Temp(this.grid, this.game.getHuman().getPieces(), this.game.getComputer().getPieces());
            // Calculate the best move using the minimax
            temp = this.game.minimax(temp, 2, true, -10000, 10000);
            // If the temp object doesn't have the firstPiece variable, it means there's no
            // possible move.
            if (temp.firstPiece != null) {
                // Update the best move on the real grid
                int ID = temp.firstPiece.getID();
                Piece movingPiece = this.game.getComputer().getPieces()
                        .get(this.game.getComputer().findPieceIndexByID(ID));
                int[] moveTo = temp.firstPieceLastPos;
                int[] moveFrom = movingPiece.getPosition();
                movingPiece.setPosition(moveTo);
                this.grid[moveTo[0]][moveTo[1]].setIsOccupied(true);
                this.grid[moveTo[0]][moveTo[1]].setOccupiedBy(movingPiece);
                this.grid[moveFrom[0]][moveFrom[1]].setIsOccupied(false);
                this.grid[moveFrom[0]][moveFrom[1]].setOccupiedBy(null);
                movingPiece.setIsKing(temp.firstPiece.getIsKing());

                // If any piece should be taken due to the move
                if (!temp.firstTakenPieces.isEmpty()) {
                    // For each piece taken,
                    for (Piece p : temp.firstTakenPieces) {
                        // Remove the piece from the human player's pieces list and the grid.
                        this.game.getHuman().removeAPiece(p.getID());
                        int[] pos = p.getPosition();
                        this.grid[pos[0]][pos[1]].setOccupiedBy(null);
                        this.grid[pos[0]][pos[1]].setIsOccupied(false);
                    }
                }
                this.repaint();

                // Check if the game has been over
                if (this.game.endCheck() != null) {
                    JOptionPane.showMessageDialog(this, this.game.endCheck() + " has won!");
                }

                // Switch the current player
                this.game.setCurrentPlayer(this.game.getHuman());
                this.game.setOpponentPlayer(this.game.getComputer());
                this.forcedCapturingAvailable();

                // If the method gets called when it's not the computer's turn, show an error
                // message.
            }
            else {
                JOptionPane.showMessageDialog(this, "There is no available move for the Computer, you won!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please make your move first.");
        }
    }

    // A method that selects a piece when it's a player's turn.
    // Decides which action to perform (e.g., choose the tile as a current tile)
    // depending on the current state
    public void selectAPiece(Tile t, Board board) {

        // Check if there is a checker on the clicked tile.
        if (t.getIsOccupied() == true) {
            // Enable tiles where the pieces of the current player are only.
            if (t.getOccupiedBy().getIsWhite()) {
                // If there are hint tiles, reset them.
                if (!board.getGame().getHintTiles().isEmpty()) {
                    for (Tile tile : board.getGame().getHintTiles()) {
                        tile.setIsHint(false);
                    }
                    board.getGame().getHintTiles().clear();
                }
                // If there is any forced capture available, only allow the related tiles to get
                // selected.
                if (!board.game.getForcedCandidates().isEmpty()) {
                    if (t.getIsForcedCandidate()) {
                        if (board.game.getCurrentTile() != null) {
                            board.game.resetTiles();
                        }
                        // Set the tile as the current tile.
                        board.game.setCurrentTile(t);
                        // Calculate candidates for the current tile.
                        HashMap<Tile, Tile> candidates = board.getGame().calculateCandidates(board.getGrid(), t);
                        // Set the candidates in the board.
                        board.game.setCandidates(candidates);
                        // When other pieces get selected while a forced capture is possible, show the
                        // error dialog.
                    } else {
                        JOptionPane.showMessageDialog(board,
                                "Forced Capturing available, go ahead and take the enemy down!");
                    }
                }
                // Make sure the tile is not a candidate -> The click was not for selecting the
                // destination of the piece but for choosing the piece to move.
                else if (t.getIsCandidate() == false) {
                    // Check if there is already a current tile assigned.
                    if (board.game.getCurrentTile() != null) {
                        board.game.resetTiles();
                    }
                    // Set the tile as the current tile.
                    board.game.setCurrentTile(t);
                    // Calculate candidates for the current tile.
                    HashMap<Tile, Tile> candidates = board.getGame().calculateCandidates(board.getGrid(), t);
                    // Set the candidates in the board.
                    board.game.setCandidates(candidates);
                    // Change the candidate tiles' property.

                }

                // If the selected tile is a candidate, move the current tile's piece to it.
            } else {
                if (t.getIsCandidate()) {
                    board.moveAPiece(board.game.getCurrentTile().getOccupiedBy(), t);

                    // When the user tries to click on a piece that doesn't belong to them, show the
                    // error message.
                } else {
                    JOptionPane.showMessageDialog(board, "Please select among your pieces.");
                }
            }
        } // If the tile is not occupied and is a candidate, move the piece from the
          // current tile to this tile.
        else {
            if (t.getIsCandidate()) {
                board.moveAPiece(board.game.getCurrentTile().getOccupiedBy(), t);
            }
        }
    }

    // Moves the given piece to the given tile
    public void moveAPiece(Piece p, Tile t) {
        // Vacate the tile from which the piece moved
        int[] currentPos = p.getPosition();
        Tile currentTile = this.grid[currentPos[0]][currentPos[1]];
        currentTile.setOccupiedBy(null);
        currentTile.setIsOccupied(false);
        currentTile.setIsCurrent(false);
        // Move position of the piece
        p.setPosition(t.getPosition());
        // Occupy the tile to which the piece moved
        t.setOccupiedBy(p);
        t.setIsOccupied(true);
        t.setIsCandidate(false);

        // Check if the piece gets an opponent piece.
        if (this.game.getCandidates().get(t) != null) {
            this.getOpponentPiece(p, t);
        }

        // Reset the current, candidate, hint and forced tiles
        this.game.resetTiles();

        if (t.getIsEnd()) {
            p.setIsKing(true);
        }
        this.game.setMoved(true);
    }

    // The given piece gets rid of the opponent piece on the given tile.
    public void getOpponentPiece(Piece p, Tile t) {
        // Change the state of the game
        this.game.setCaptured(true);
        int[] pos = { this.game.getCandidates().get(t).getPosition()[0],
                this.game.getCandidates().get(t).getPosition()[1] };
        Piece taken = this.grid[pos[0]][pos[1]].getOccupiedBy();
        int takenID = taken.getID();
        // Remove the piece from the grid and update the grid
        this.game.getOpponentPlayer().removeAPiece(takenID);
        this.grid[pos[0]][pos[1]].setIsOccupied(false);
        this.grid[pos[0]][pos[1]].setOccupiedBy(null);
        // If the taken piece is a king, make the taking piece a king (regicide)
        if (taken.getIsKing()) {
            this.game.setKingCaptured(true);
            p.setIsKing(true);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Set up a JFrame to contain all the elements
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Board board = new Board(screenSize);
                JFrame window = new JFrame();
                int tileSize = board.tileSize;
                window.setLayout(new FlowLayout(FlowLayout.CENTER, tileSize / 5, tileSize / 4));
                // UI elements
                JPanel ui = new JPanel();
                JButton enableHints = new JButton("HINTS");
                JButton proceed = new JButton("PROCEED");
                JButton rules = new JButton("RULES");
                enableHints.setBounds(tileSize, tileSize, tileSize, tileSize);
                enableHints.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.enableHints();
                    }
                });
                proceed.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.computerTurn();
                    }
                });
                rules.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.showRules();
                    }
                });
                JLabel text = new JLabel("Click 'PROCEED' after making your moves.");
                text.setFont(new Font("Verdana", Font.BOLD, tileSize / 4));
                ui.setLayout(new GridBagLayout());

                // Layout elements
                GridBagConstraints c = new GridBagConstraints();
                c.insets = new Insets(tileSize / 5, tileSize / 5, tileSize / 5, tileSize / 5);
                c.gridx = 0;
                c.gridy = 0;
                ui.add(enableHints, c);
                c.gridy = 1;
                ui.add(proceed, c);
                c.gridy = 2;
                ui.add(rules, c);

                // Add them all and show
                window.setSize(tileSize * 11, tileSize * 10);
                window.add(board);
                window.add(ui);
                window.add(text);
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setVisible(true);
            }
        });
    }

    // Getters and setters from here.
    public GridBagConstraints getC() {
        return c;
    }

    public void setC(GridBagConstraints c) {
        this.c = c;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
    }

    public double getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public Tile[][] getGrid() {
        return grid;
    }

}

// i agree that u did quite a work for the last bit but before most of the time
// u weren't rly there for the seminar meetings due to ur visa problem or
// whatever that was,
//