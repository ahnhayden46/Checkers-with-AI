/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */

import java.util.ArrayList;

// A class representing a player of the game.
// Extended by the Computer and Human classes.
public class Player {

    private boolean isHuman; // If it's a human player or not.
    private ArrayList<Piece> pieces = new ArrayList<>(); // An arraylist of pieces that the player has.

    public Player() {
    }

    // A constructor that copies another player object.
    public Player(Player p) {
        this.isHuman = p.isHuman;// 1 if white, -1 if black
        this.pieces = new ArrayList<>();
        if (!p.pieces.isEmpty()) {
            for (Piece piece : p.pieces) {
                Piece newPiece = new Piece(piece);
                pieces.add(newPiece);
            }
        }

    }

    public Player(boolean isHuman, ArrayList<Piece> pieces) {
        this.isHuman = isHuman;
        this.pieces = pieces;
    }

    // Adds a given piece to the player's pieces list.
    public void addPieces(Piece p) {
        this.pieces.add(p);
    }

    // Removes the piece with the given ID from the player's pieces list.
    public void removeAPiece(int ID) {
        int i = findPieceIndexByID(ID);
        this.pieces.remove(i);
    }

    // Finds a specific piece in the pieces arraylist by its ID
    public int findPieceIndexByID(int ID) {
        boolean found = false;
        int i = 0;
        while (!found) {
            if (this.pieces.get(i).getID() == ID) {
                found = true;
            } else {
                i++;
            }
        }
        return i;
    }

    // Getters and setters from here.
    public boolean getIsHuman() {
        return isHuman;
    }

    public void setIsHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

}
