
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
                    // If there are hint tiles on the board, reset them.
                    if (!board.game.getHintTiles().isEmpty()) {
                        ArrayList<Tile> hintTiles = board.game.getHintTiles();
                        for (Tile t : hintTiles) {
                            int[] pos = t.getPosition();
                            board.setHintTileInGrid(pos, false);
                        }
                        board.game.getHintTiles().clear();
                    }

                    // Select a piece and perform a right action depending on the current state.
                    board.selectAPiece(t, board);
                    board.repaint();

                    // If any human piece has been moved
                    if (board.game.getMoved()) {

                        // For multi-leg captures,
                        // if it has captured any opponent piece and it's not a king,
                        if (board.game.getCaptured() && !board.game.getKingCaptured()) {
                            // calculate candidates again from the moved position and see if any possible
                            // forced capture exists.
                            HashMap<Tile, Tile> cands = board.getGame().calculateCandidates(board.getGrid(), t);
                            // If there is a forced capture candidate, update the board to show them.
                            if (board.game.getForced()) {
                                board.game.setCurrentTile(t);
                                board.game.setCandidates(cands);
                                board.repaint();
                                // If there is not, switch the current and the opponent player.
                            } else {
                                board.getGame().setCurrentPlayer(board.getGame().getComputer());
                                board.getGame().setOpponentPlayer(board.getGame().getHuman());
                            }
                        }
                        // If the piece didn't capture anything but only moved, switch the player turn.
                        else {
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

    // A method that uses the minimax algorithm for the human player and shows
    // hints.
    public void enableHints() {
        if (this.game.getCurrentPlayer().getIsHuman()) {
            // Calculate hints only when they were not calculated before.
            if (this.game.getHintTiles().isEmpty()) {
                // Perform the minimax algorithm by giving the current computer pieces as human
                // pieces and vice versa to make the algorithm work for the human player.
                Temp temp = new Temp(this.grid, this.game.getComputer().getPieces(), this.game.getHuman().getPieces());
                temp = this.game.minimax(temp, 2, true, -10000, 10000);
                int ID = temp.firstPiece.getID();
                int index = this.game.getHuman().findPieceIndexByID(ID);
                int[] pos = this.game.getHuman().getPieces().get(index).getPosition();
                int[] lastPos = temp.firstPieceLastPos;
                Tile hintTile = this.grid[pos[0]][pos[1]];
                Tile lastHintTile = this.grid[lastPos[0]][lastPos[1]];
                hintTile.setIsHint(true);
                lastHintTile.setIsHint(true);
                this.game.addHintTile(hintTile);
                this.game.addHintTile(lastHintTile);
                this.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Hints are available when it's your turn.");
        }
    }

    public void computerTurn() {

        if (this.game.getCurrentPlayer().equals(this.game.getComputer())) {
            Temp temp = new Temp(this.grid, this.game.getHuman().getPieces(), this.game.getComputer().getPieces());
            temp = this.game.minimax(temp, 2, true, -10000, 10000);
            int ID = temp.firstPiece.getID();
            Piece movingPiece = this.game.getComputer().getPieces().get(this.game.getComputer().findPieceIndexByID(ID));
            int[] moveTo = temp.firstPieceLastPos;
            int[] moveFrom = movingPiece.getPosition();

            movingPiece.setPosition(moveTo);
            this.grid[moveTo[0]][moveTo[1]].setIsOccupied(true);
            this.grid[moveTo[0]][moveTo[1]].setOccupiedBy(movingPiece);
            this.grid[moveFrom[0]][moveFrom[1]].setIsOccupied(false);
            this.grid[moveFrom[0]][moveFrom[1]].setOccupiedBy(null);
            if (moveTo[0] == 0 || moveTo[0] == 7) {
                movingPiece.setIsKing(true);
            }

            if (!temp.firstTakenPieces.isEmpty()) {
                for (Piece p : temp.firstTakenPieces) {
                    if (p.getIsKing()) {
                        movingPiece.setIsKing(true);
                    }
                    this.game.getHuman().removeAPiece(p.getID());
                    int[] pos = p.getPosition();
                    this.grid[pos[0]][pos[1]].setOccupiedBy(null);
                    this.grid[pos[0]][pos[1]].setIsOccupied(false);
                }
            }

            this.repaint();

            if (this.game.endCheck() != null) {
                JOptionPane.showMessageDialog(this, this.game.endCheck() + " has won!");
            }

            this.game.setCurrentPlayer(this.game.getHuman());
            this.game.setOpponentPlayer(this.game.getComputer());
        } else {
            JOptionPane.showMessageDialog(this, "Please make your move first.");
        }

    }

    public void selectAPiece(Tile t, Board board) {

        // Check if there is a checker on the clicked tile.
        if (t.getIsOccupied() == true) {
            // Enable tiles on which only the pieces of the current player are put.
            if (t.getOccupiedBy().getIsWhite()) {
                // Make sure the tile is not a candidate -> The click was not for selecting the
                // destination of the piece but for choosing the piece to move.
                if (t.getIsCandidate() == false) {
                    // Check if there is already a current tile assigned.
                    if (board.game.getCurrentTile() != null) {
                        board.resetCurrentCandidate();
                        board.game.getCurrentTile().setIsCurrent(false);
                    }
                    // Set the tile as the current tile.
                    board.game.setCurrentTile(t);
                    // Calculate candidates for the current tile.
                    HashMap<Tile, Tile> candidates = board.getGame().calculateCandidates(board.getGrid(), t);
                    // Set the candidates in the board.
                    board.game.setCandidates(candidates);
                    // Change the candidate tiles' property.

                }
            } else {
                if (t.getIsCandidate()) {
                    board.moveAPiece(board.game.getCurrentTile().getOccupiedBy(), t);
                } else {
                    JOptionPane.showMessageDialog(board, "Please select among your pieces.");
                }
            }
        } // If the tile is not occupied and is a candidate, move the piece from the
          // current tile to this
          // tile.
        else {
            if (t.getIsCandidate()) {
                board.moveAPiece(board.game.getCurrentTile().getOccupiedBy(), t);
            }
        }
    }

    public void placeInitialCheckers() {
        for (Piece p : this.game.getHuman().getPieces()) {
            Tile t = this.grid[p.getPosition()[0]][p.getPosition()[1]];
            t.setOccupiedBy(p);
            t.setIsOccupied(true);
        }
        for (Piece p : this.game.getComputer().getPieces()) {
            Tile t = this.grid[p.getPosition()[0]][p.getPosition()[1]];
            t.setOccupiedBy(p);
            t.setIsOccupied(true);
        }

    }

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

        this.resetCurrentCandidate();

        if (t.getIsEnd()) {
            p.setIsKing(true);
        }
        this.game.setMoved(true);
    }

    public void getOpponentPiece(Piece p, Tile t) {
        this.game.setCaptured(true);
        int[] pos = { this.game.getCandidates().get(t).getPosition()[0],
                this.game.getCandidates().get(t).getPosition()[1] };
        Piece taken = this.grid[pos[0]][pos[1]].getOccupiedBy();
        int takenID = taken.getID();
        this.game.getOpponentPlayer().removeAPiece(takenID);
        this.grid[pos[0]][pos[1]].setIsOccupied(false);
        this.grid[pos[0]][pos[1]].setOccupiedBy(null);
        if (taken.getIsKing()) {
            this.game.setKingCaptured(true);
            p.setIsKing(true);
        }
    }

    public void resetCurrentCandidate() {
        this.game.getCurrentTile().setIsCurrent(false);
        for (Tile t : this.game.getCandidates().keySet()) {
            t.setIsCandidate(false);
        }
        this.game.setCurrentTile(null);
        this.game.setCandidates(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Board board = new Board(screenSize);
                JFrame window = new JFrame();
                int tileSize = board.tileSize;
                window.setLayout(new FlowLayout(FlowLayout.CENTER, tileSize / 5, tileSize / 4));
                JPanel ui = new JPanel();
                JButton enableHints = new JButton("HINTS");
                JButton proceed = new JButton("PROCEED");
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
                JLabel text = new JLabel("Click 'PROCEED' after making your moves.");
                text.setFont(new Font("Verdana", Font.BOLD, tileSize / 4));
                ui.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.insets = new Insets(tileSize / 5, tileSize / 5, tileSize / 5, tileSize / 5);
                c.gridx = 0;
                c.gridy = 0;
                ui.add(enableHints, c);
                c.gridy = 1;
                ui.add(proceed, c);
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