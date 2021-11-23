/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */
public class Human extends Player {

    public Human() {
        this.setIsHuman(true);
        this.addInitialPieces();
    }

    public void addInitialPieces() {

        int current = 0;
        for (int i = 1; i <= 4; i++) {
            int[] position = { 5, current };
            Piece p = new Piece(position, true, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }

        current = 1;
        for (int i = 5; i <= 8; i++) {
            int[] position = { 6, current };
            Piece p = new Piece(position, true, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }

        current = 0;
        for (int i = 9; i <= 12; i++) {
            int[] position = { 7, current };
            Piece p = new Piece(position, true, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }
    }

}
