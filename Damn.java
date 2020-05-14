package damn;

import java.util.*; 

public class Damn {
    
    public static void main(String[] args) {
        
        Board board = new Board();
        board.initialize();
        Game game = new Game(board, 1);
        
        game.start();
    }
    
}
