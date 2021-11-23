/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */
public class Piece {

    private int[] position;
    private boolean isWhite; // If true, it's a human player's piece.
    private boolean isKing;
    private Player ownedBy;
    private int ID;

    public Piece(int[] position, boolean isWhite, boolean isKing, Player ownedBy, int ID) {
        this.position = position;
        this.isWhite = isWhite;
        this.isKing = isKing;
        this.ownedBy = ownedBy;
        this.ID = ID;
    }

    public Player getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Player ownedBy) {
        this.ownedBy = ownedBy;
    }

    public int getID() {
        return ID;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void setIsWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public void setIsKing(boolean isKing) {
        this.isKing = isKing;
    }

    public boolean getIsWhite() {
        return isWhite;
    }

    public boolean getIsKing() {
        return isKing;
    }

}
