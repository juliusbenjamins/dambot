package damn;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Board {

    private Tile board[][];
    private int BLACK, WHITE, EMPTY, capturedWhites, capturedBlacks;
    Tile garbageTile;

    public Board() {
        this.board = new Tile[10][10];
        this.BLACK = 0;
        this.WHITE = 1;
        this.EMPTY = 2;
        this.capturedBlacks = 0;
        this.capturedWhites = 0;
        this.garbageTile = new Tile(EMPTY, -1, -1);
    }

    //Function that initializes the board, so just normal starting configuration
    public void initialize() {
        //Black rows
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                if (row % 2 == 0) {
                    if (col % 2 == 0) {
                        Tile tile = new Tile(EMPTY, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    } else {
                        Tile tile = new Tile(BLACK, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    }
                } else {
                    if (col % 2 == 1) {
                        Tile tile = new Tile(EMPTY, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    } else {
                        Tile tile = new Tile(BLACK, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    }
                }
            }
        }

        //Empty rows
        for (int row = 4; row < 6; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col] = new Tile(EMPTY, row, col);
            }
        }

        //White rows
        for (int row = 6; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (row % 2 == 0) {
                    if (col % 2 == 0) {
                        Tile tile = new Tile(EMPTY, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    } else {
                        Tile tile = new Tile(WHITE, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    }

                } else {
                    if (col % 2 == 1) {
                        Tile tile = new Tile(EMPTY, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    } else {
                        Tile tile = new Tile(WHITE, row, col);
                        tile.setPos(row, col);
                        board[row][col] = tile;
                    }
                }
            }
        }
    }

    //Get the tile on position (x,y)
    public Tile getTileOnPos(int x, int y) {
        return board[x][y];
    }

    //Check if coordinates (xFrom,yFrom) and (xTo,yTo) are not out of bounds
    public boolean validCoordinates(int xFrom, int yFrom, int xTo, int yTo) {
        return !(((xFrom > 9 || xFrom < 0) || (xTo > 9 || xTo < 0))
                || ((yFrom > 9 || yFrom < 0) || (yTo > 9 || yTo < 0)));
    }

    //Check if move is diagonal, Black: x+1, y+1/y-1. White: x-1, y+1/y-1, and
    //not more than 1 step diagonally
    public boolean validMove(int xFrom, int xTo, int yFrom, int yTo, int turn) {
        if (turn == BLACK) {
            return (((yFrom == (yTo + 1)) || (yFrom == (yTo - 1))) && (xTo == (xFrom + 1)));
        } else {
            return (((yFrom == (yTo + 1)) || (yFrom == (yTo - 1))) && (xTo == (xFrom - 1)));
        }
    }

    //Function that moves a tile with color turnColor from (xFrom,yFrom) to (xTo,yTo)
    //Does this by checking coordinates, move validity, and emptyness of to-tile
    public boolean makeMove(int xFrom, int yFrom, int xTo, int yTo, int turnColor) {
        if (validCoordinates(xFrom, yFrom, xTo, yTo)) //Check if to-coordinates are valid       
        {
            if (validMove(xFrom, xTo, yFrom, yTo, turnColor)) //Check if doesn't want to move more then 1 step
            {
                if (board[xFrom][yFrom].getColor() == turnColor) //Check if tile that is trying to be moved is turn's tile
                {
                    if (board[xTo][yTo].getColor() == EMPTY) { //Check if tile to be moved to is empty
                        Tile tempTile = board[xTo][yTo];
                        tempTile.setPos(xFrom, yFrom);
                        Tile newTile = board[xFrom][yFrom];
                        newTile.setPos(xTo, yTo);

                        board[xTo][yTo] = newTile;
                        board[xFrom][yFrom] = tempTile;

                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    public boolean capture(Tile t, int i){
        LinkedList<Tile> captureSequence = t.getSequences().get(i);
        int x,y,newX,newY;
        int capturedTiles = 0;
        
        if(!captureSequence.isEmpty()){
            for(Tile c : captureSequence){
                x = actualX(c);
                y = actualY(c);
                Tile temp = new Tile(EMPTY,x,y);
                t.addCapturedTile(c);
                board[x][y] = temp;
                capturedTiles++;
            }
            newX = captureSequence.getLast().getX();
            newY = captureSequence.getLast().getY();
            board[newX][newY] = t;
            board[t.getX()][t.getY()] = new Tile(EMPTY,t.getX(),t.getY());
            t.setPos(newX, newY);
            
            if(t.getColor() == BLACK)
                capturedWhites += capturedTiles;
             else 
                capturedBlacks += capturedTiles;
            
            return true;
        }
                
        return false;
    }

    public void iterateCapture(int turnColor) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col].clearList();

                if (board[row][col].getColor() == turnColor) {
                    LinkedList<LinkedList<Tile>> sequence
                            = findLongestCaptureSequence(board[row][col], turnColor);

                    if (!sequence.isEmpty()) {
                        board[row][col].setSequence(sequence);
                    }
                }
            }
        }
    }

    public LinkedList<LinkedList<Tile>> findLongestCaptureSequence(Tile t, int turnColor) {
        boolean allDirs = false;

        LinkedList<Tile> capturable = new LinkedList<>();
        LinkedList<Tile> visited = new LinkedList<>();
        Queue<Tile> queue = new LinkedList<>();

        t.setDepth(0);
        queue.add(t);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            LinkedList<Tile> adj = hasToCapture(current, turnColor, allDirs);

            for (Tile v : adj) {
                if (!visited.contains(v)) {
                    queue.add(v);
                    capturable.add(v);
                    visited.add(v);
                    v.setDepth(current.getDepth() + 1);
                    v.setParent(current);
                }
            }

            allDirs = true;
        }

        LinkedList<LinkedList<Tile>> sequence = new LinkedList<>();
        int i = 0;
        int maxDepth = maxDepth(capturable);
        for (Tile cap : capturable) {
            if (cap.getDepth() == maxDepth) {
                sequence.add(i, backTrack(cap, t));
                i++;
            }
        }

        return sequence;
    }

    public int maxDepth(LinkedList<Tile> tiles) {
        int max = 0;
        for (Tile t : tiles) {
            int currentDepth = t.getDepth();
            if (currentDepth > max) {
                max = currentDepth;
            }
        }

        return max;
    }

    public LinkedList<Tile> backTrack(Tile from, Tile to) {
        LinkedList<Tile> path = new LinkedList<>();
        Tile current = from;
        path.addFirst(current);

        while (current.getParent() != to) {
            current = current.getParent();
            path.addFirst(current);
        }

        return path;
    }

    //Returns all tiles that tile t may capture
    public LinkedList<Tile> hasToCapture(Tile t, int turnColor, boolean allDirs) {
        int x = t.getX();
        int y = t.getY();
        int i;
        int j;

        LinkedList<Tile> tiles = new LinkedList<>();
        if (allDirs) {
            i = 0;
            j = 4;
        } else if (turnColor == BLACK) {
            i = 2;
            j = 4;
        } else { //WHITE
            i = 0;
            j = 2;
        }

        if (board[x][y].isKing()) {
            //to be written....
        } else {
            for (int z = i; z < j; z++) {
                Tile tempTile = diagonalCapture(x, y, z, turnColor);
                if (tempTile.getX() != -1) {
                    tiles.add(tempTile);
                }
            }
        }

        return tiles;
    }

    //(x,y) of tile that has to be checked if can capture diagonally, turn value,
    //direction of capture: 0 left-up 1 right-up, 2 left-down, 3 right-down
    public Tile diagonalCapture(int x, int y, int direction, int turnColor) {
        int dx, dy;

        switch (direction) {
            case 0: //left-up (x-2,y-2) dir 0
                dx = -1;
                dy = -1;
                break;
            case 1: //right-up (x-2,y+2) dir 1
                dx = -1;
                dy = 1;
                break;
            case 2: //left-down (x+2,y-2) dir 2
                dx = 1;
                dy = -1;
                break;
            default: //right-down (x+2,y+2) dir 3
                dx = 1;
                dy = 1;
                break;
        }

        if (validCoordinates(x, y, x + (2 * dx), y + (2 * dy))) { //Check if out of bounds
            if (board[x + dx][y + dy].getColor() == ((turnColor + 1) % 2)) { //Check if to capture piece is from opposite color
                if (board[x + (2 * dx)][y + (2 * dy)].getColor() == EMPTY) { //check if diagonal+1 piece is empty, if yes then turn can capture
                    return board[x + 2 * dx][y + 2 * dy];
                }
            }
        }

        return garbageTile;
    }

    public boolean canTilesCapture() {
        boolean capture = false;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Tile current = board[row][col];
                if (current.canCapture()) {
                    LinkedList<LinkedList<Tile>> currentSequences = current.getSequences();
                    for (LinkedList<Tile> s : currentSequences) {
                        if (!s.isEmpty()) {
                            System.out.println(current + " can capture: " + printableSequence(s));
                            capture = true;
                        }
                    }
                }
            }
        }
        return capture;
    }

    public LinkedList<Tile> printableSequence(LinkedList<Tile> s) {
        LinkedList<Tile> sNew = new LinkedList<>();

        for (Tile t : s) {
            Tile newT = board[(actualX(t))][actualY(t)];
            sNew.add(newT);
        }

        return sNew;
    }
    
    public int actualX(Tile t){
        return (t.getX() + t.getParent().getX()) / 2;
    }
    
    public int actualY(Tile t){
        return (t.getY() + t.getParent().getY()) / 2;
    }
    
    public int getCapturedBlacks(){
        return capturedBlacks;
    }
    
    public int getCapturedWhites(){
        return capturedWhites;
    }

    public String toString() {
        StringBuilder playBoard = new StringBuilder("");
        playBoard.append("    0  1  2  3  4  5  6  7  8  9\n");
        for (int i = 0; i < 10; i++) {
            playBoard.append(i).append(" |");
            for (int j = 0; j < 10; j++) {
                switch (board[i][j].getColor()) {
                    case 0:
                        playBoard.append(" B ");
                        break;
                    case 1:
                        playBoard.append(" W ");
                        break;
                    default:
                        playBoard.append(" _ ");
                }
            }
            playBoard.append("|\n");
        }
        return playBoard.toString();
    }

}
