/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */

// A class representing a checker piece on the board.
public class Piece {

    private int[] position;
    private boolean isWhite; // If true, it's a human player's piece.
    private boolean isKing;
    private int ID;

    public Piece(int[] position, boolean isWhite, boolean isKing, Player ownedBy, int ID) {
        this.position = position;
        this.isWhite = isWhite;
        this.isKing = isKing;
        this.ID = ID;
    }

    public Piece(Piece p) {
        this.position = p.position;
        this.isWhite = p.isWhite; // If true, it's a human player's piece.
        this.isKing = p.isKing;
        this.ID = p.ID;
    }

    // Getters and setters from here.
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
