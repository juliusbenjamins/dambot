package damn;

import java.util.LinkedList;

public class Tile {
    
    private int color;
    private int x,y;
    private boolean kingVal;
    private LinkedList<LinkedList<Tile>> sequence;
    private LinkedList<Tile> capturedTiles;
    private Tile parent;
    private int depth;
    
    public Tile(int color, int x, int y){
        this.color = color;
        this.x = x;
        this.y = y;
        this.kingVal = false;
        this.sequence = new LinkedList<LinkedList<Tile>>();
        this.capturedTiles = new LinkedList<Tile>();
    }
    
    public int getColor(){
        return color;
    }
    
    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public boolean isKing(){
        return kingVal;
    }
    
    public void setKing(boolean newKingVal){
        this.kingVal = newKingVal;
    }
    
    public String colorToString() {
        switch (color) {
            case 0:
                return "Black";
            case 1:
                return "White";
            default:
                return "Empty";
        }
    }
    
    public void addCapturedTile(Tile t){
        capturedTiles.add(t);
    }
    
    public LinkedList<Tile> getCapturedTiles(){
        return capturedTiles;
    }
    
    public boolean canCapture(){
        return (sequence.size() >= 1);
    }
    
    public void clearList(){
        sequence.clear();
    }
    
    public LinkedList<LinkedList<Tile>> getSequences(){
        return sequence;
    }
    
    public void setSequence(LinkedList<LinkedList<Tile>> newSq){
        this.sequence = newSq;
    }
    
    public void setParent(Tile p){
        this.parent = p;
    }
    
    public Tile getParent(){
        return parent;
    }
    
    public int getDepth(){
        return depth;
    }
    
    public void setDepth(int d){
        this.depth = d;
    }
    
    public String toString() {
        return colorToString() + " (" + x + "," + y + ")";
    }
}
