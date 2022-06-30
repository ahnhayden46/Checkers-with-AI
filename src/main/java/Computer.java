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
public class Computer extends Player {

    public Computer() {
        this.setIsHuman(false);
        this.addInitialPieces();
    }

    // The method that allocates pieces to the initial positions and distributes
    // unique IDs to each of them for the computer.
    public void addInitialPieces() {

        // For the pieces on the 1st row
        // Pieces should be placed starting from the second tile.
        int current = 1;
        for (int i = 1; i <= 4; i++) {
            int[] position = { 0, current };
            // The integer i becomes the id of the piece.
            Piece p = new Piece(position, false, false, (Player) this, i * -1);
            // Add the piece to the player object.
            this.addPieces(p);
            current += 2;
        }

        // For the pieces on the 2nd row
        // Pieces should be placed starting from the first tile.
        current = 0;
        for (int i = 5; i <= 8; i++) {
            int[] position = { 1, current };
            Piece p = new Piece(position, false, false, (Player) this, i * -1);
            this.addPieces(p);
            current += 2;
        }

        // For the pieces on the 3rd row
        // Pieces should be placed starting from the second tile.
        current = 1;
        for (int i = 9; i <= 12; i++) {
            int[] position = { 2, current };
            Piece p = new Piece(position, false, false, (Player) this, i * -1);
            this.addPieces(p);
            current += 2;
        }
    }
}
