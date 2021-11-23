/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */

import java.util.ArrayList;

public class Player {

    private boolean isHuman;// 1 if white, -1 if black
    private ArrayList<Piece> pieces = new ArrayList<>();

    public Player() {
    }

    public Player(boolean isHuman, ArrayList<Piece> pieces) {
        this.isHuman = isHuman;
        this.pieces = pieces;
    }

    public boolean getIsHuman() {
        return isHuman;
    }

    public void setIsHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void addPieces(Piece p) {
        this.pieces.add(p);
    }

    public void removeAPiece(int ID) {
        int i = findPieceIndexByID(ID);
        this.pieces.remove(i);
    }

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

}
