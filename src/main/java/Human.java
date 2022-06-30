/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hayden
 */

// A class representing the human player of the game.
// Extends the Player class.
public class Human extends Player {

    public Human() {
        this.setIsHuman(true);
        this.addInitialPieces();
    }

    // The method that allocates pieces to the initial positions and distributes
    // unique IDs to each of them for the human.
    public void addInitialPieces() {

        // For the pieces on the 6th row
        // Pieces should be placed starting from the first tile.
        int current = 0;
        for (int i = 1; i <= 4; i++) {
            int[] position = { 5, current };
            // The integer i becomes the id of the piece.
            Piece p = new Piece(position, true, false, (Player) this, i);
            // Add the piece to the player object.
            this.addPieces(p);
            current += 2;
        }

        // For the pieces on the 7th row
        // Pieces should be placed starting from the second tile.
        current = 1;
        for (int i = 5; i <= 8; i++) {
            int[] position = { 6, current };
            Piece p = new Piece(position, true, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }

        // For the pieces on the 8th row
        // Pieces should be placed starting from the first tile.
        current = 0;
        for (int i = 9; i <= 12; i++) {
            int[] position = { 7, current };
            Piece p = new Piece(position, true, false, (Player) this, i);
            this.addPieces(p);
            current += 2;
        }
    }

}
