/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

// A class representing each tile of the board.
// The board consists of 8*8 tile objects.
public class Tile extends JPanel {

    private int[] position;
    private boolean isLight = false; // If it's colored light or dark to make a checkerboard.
    private boolean isOccupied = false; // If it's occupied by any piece.
    private Piece occupiedBy = null; // Which piece is occupying. -> Null if there's no piece on it.
    private boolean isEnd = false; // If it's on the end row (the king's row)
    private int tileSize; // The size of the tile needed to visualise on the board.
    private boolean isCurrent = false; // If the tile is selected.
    private boolean isCandidate = false; // If it is set as a candidate by the calculating candidates algorithm in the
                                         // Game class.
    private boolean isForcedCandidate = false;
    private boolean isHint = false; // If it is set as a hint by the enabling hints algorithm in the Board class.

    // A constructor that copies another tile object.
    public Tile(Tile t) {
        this.position = t.position;
        this.isLight = t.isLight;
        this.isOccupied = t.isOccupied;
        if (t.occupiedBy != null) {
            this.occupiedBy = new Piece(t.occupiedBy);
        } else {
            this.occupiedBy = null;
        }
        this.isEnd = t.isEnd;
        this.tileSize = t.tileSize;
        this.isCurrent = t.isCurrent;
        this.isCandidate = t.isCandidate;
        this.isHint = t.isHint;
    }

    public Tile(int[] position, int tileSize) {
        this.position = position;
        this.setIsLight();
        this.setIsEnd();
        this.tileSize = tileSize;
        this.setPreferredSize(new Dimension((int) tileSize, (int) tileSize));
    }

    // A method overriding the paintComponent of JPanel class.
    // Decides how the tile should be visualised on the board.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Painted light gray or black depending on its 'isLight' value.
        this.setBackground(this.isLight ? Color.LIGHT_GRAY : Color.BLACK);

        // If it is a forced capturing tile, paint it yellow.
        if (this.isForcedCandidate) {
            this.setBackground(Color.YELLOW);
        }

        // If it is selected a current tile or a candidate or a hint.
        if (this.isCurrent) {
            // Background color is magenta when it's a current tile.
            this.setBackground(Color.MAGENTA);
        } else if (this.isCandidate) {
            // Background color is magenta when it's a candidate.
            this.setBackground(Color.GREEN);
            // Background color is cyan when it's a hint.
        } else if (this.isHint) {
            this.setBackground(Color.CYAN);
        }

        // If it is occupied by a checker piece
        if (this.isOccupied) {
            // If the occupying piece is white, i.e., the human player's piece
            if (this.occupiedBy.getIsWhite()) {
                Graphics2D g2d = (Graphics2D) g;
                // Draw and paint a white circle
                g.setColor(Color.WHITE);
                g2d.fillOval(this.tileSize / 4, this.tileSize / 4, this.tileSize / 2, this.tileSize / 2);
                if (occupiedBy.getIsKing()) {
                    g.setColor(Color.BLACK);
                    g.drawOval(this.tileSize / 3, this.tileSize / 3, this.tileSize / 3, this.tileSize / 3);
                }
                // If the occupying piece is white, i.e., the human player's piece
            } else {
                Graphics2D g2d = (Graphics2D) g;
                // Draw and paint a white circle
                g.setColor(Color.RED);
                g2d.fillOval(this.tileSize / 4, this.tileSize / 4, this.tileSize / 2, this.tileSize / 2);
                if (occupiedBy.getIsKing()) {
                    g.setColor(Color.BLACK);
                    g.drawOval(this.tileSize / 3, this.tileSize / 3, this.tileSize / 3, this.tileSize / 3);
                }
            }
        }

    }

    // Getters and setters from here.

    public boolean getIsForcedCandidate() {
        return isForcedCandidate;
    }

    public void setIsForcedCandidate(boolean isForcedCandidate) {
        this.isForcedCandidate = isForcedCandidate;
    }

    public boolean getIsHint() {
        return isHint;
    }

    public void setIsHint(boolean isHint) {
        this.isHint = isHint;
    }

    public boolean getIsCurrent() {
        return this.isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public boolean getIsLight() {
        return isLight;
    }

    public boolean getIsCandidate() {
        return isCandidate;
    }

    public void setIsCandidate(boolean isCandidate) {
        this.isCandidate = isCandidate;
    }

    public Piece getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(Piece occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    public void setIsLight() {
        if ((this.position[0] % 2 == 0) && (this.position[1] % 2 == 0)) {
            this.isLight = true;
        } else if ((this.position[0] % 2 == 1) && (this.position[1] % 2 == 1)) {
            this.isLight = true;
        }
    }

    public int[] getPosition() {
        return this.position;
    }

    public boolean getIsEnd() {
        return this.isEnd;
    }

    public void setIsEnd() {
        if (this.position[0] == 0 || this.position[0] == 7) {
            this.isEnd = true;
        }
    }

}
