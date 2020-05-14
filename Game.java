package damn;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

public class Game {
    
    private Board board;
    private int currentColor;
    private boolean ok;
    
    public Game(Board board, int startingTurnVal){
        this.board = board;
        this.currentColor = startingTurnVal;
    }
    
    public void start(){
        System.out.println("~~~ Have fun (╯°□°）╯︵ ┻━┻) ~~~ \n");
        
        while(true){
            System.out.println("Current board: \n" + board);
            
            
            System.out.println("---- Captured tiles ----\nWhite captured " + 
                    board.getCapturedBlacks() + " tiles\nBlack captured " + 
                    board.getCapturedWhites() + " tiles\n------------------------ ");
             
            if(currentColor == 0)
                System.out.println("Black, its your turn");
            else
                System.out.println("White, its your turn");
            

            board.iterateCapture(currentColor);

            if(board.canTilesCapture()){
                System.out.println("Give the tile and capture sequence you want to execute (row col, index of sequence; 6 1 0)");
                Scanner scan = new Scanner(System.in);
                int xFrom = scan.nextInt();
                int yFrom = scan.nextInt();            
                int index = scan.nextInt();
                
                Tile capturingTile = board.getTileOnPos(xFrom, yFrom);
                ok = board.capture(capturingTile,index);
                System.out.println("Whoa... " + capturingTile + " captured all these tiles: " + capturingTile.getCapturedTiles() + "\npretty nice...");
            } else {
                //From coords
                System.out.println("Which tile do you want to move (from row col and to row col; ex 6 1 5 0)");
                Scanner scan = new Scanner(System.in);
                int xFrom = scan.nextInt();
                int yFrom = scan.nextInt();            
                int xTo = scan.nextInt();
                int yTo = scan.nextInt();
                
                ok = board.makeMove(xFrom, yFrom, xTo, yTo, currentColor);
            }
            
            if(ok)
                currentColor = ((currentColor + 1) % 2);
            else
                System.out.println("!!!!!!!!!!!!!!!\nInvalid move\n!!!!!!!!!!!!!!!\n");
            
        }
    }
    
    public Board getBoard(){
        return board;
    }
    

}
