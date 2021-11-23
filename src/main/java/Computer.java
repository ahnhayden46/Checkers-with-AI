/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */
public class Computer extends Player {

    public Computer() {
        this.setIsHuman(false);
        this.addInitialPieces();
    }

    public void addInitialPieces() {

        int current = 1;
        for (int i = 1; i <= 4; i++) {
            int[] position = { 0, current };
            Piece p = new Piece(position, false, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }

        current = 0;
        for (int i = 5; i <= 8; i++) {
            int[] position = { 1, current };
            Piece p = new Piece(position, false, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }

        current = 1;
        for (int i = 9; i <= 12; i++) {
            int[] position = { 2, current };
            Piece p = new Piece(position, false, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }
    }
}
