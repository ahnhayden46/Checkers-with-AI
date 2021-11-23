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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile extends JPanel {

    private int[] position;
    private boolean isLight = false;
    private boolean isOccupied = false;
    private Piece occupiedBy = null;
    private boolean isEnd = false;
    private int tileSize;
    private boolean isCurrent = false;
    private boolean isCandidate = false;

    public Tile(int[] position, double tileSize) {
        this.position = position;
        this.setIsLight();
        this.setIsEnd();
        this.tileSize = (int) tileSize;
        this.setPreferredSize(new Dimension((int) tileSize, (int) tileSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(this.isLight ? Color.LIGHT_GRAY : Color.BLACK);

        if (this.isCurrent) {
            this.setBackground(Color.MAGENTA);
        } else if (this.isCandidate) {
            this.setBackground(Color.GREEN);
        }

        if (this.isOccupied) {
            if (this.occupiedBy.getIsWhite()) {
                Graphics2D g2d = (Graphics2D) g;
                g.setColor(Color.WHITE);
                g2d.fillOval(this.tileSize / 4, this.tileSize / 4, this.tileSize / 2, this.tileSize / 2);
                if (occupiedBy.getIsKing()) {
                    g.setColor(Color.BLACK);
                    g.drawOval(this.tileSize / 3, this.tileSize / 3, this.tileSize / 3, this.tileSize / 3);
                }
            } else {
                Graphics2D g2d = (Graphics2D) g;
                g.setColor(Color.RED);
                g2d.fillOval(this.tileSize / 4, this.tileSize / 4, this.tileSize / 2, this.tileSize / 2);
                if (occupiedBy.getIsKing()) {
                    g.setColor(Color.BLACK);
                    g.drawOval(this.tileSize / 3, this.tileSize / 3, this.tileSize / 3, this.tileSize / 3);
                }
            }
        }
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
