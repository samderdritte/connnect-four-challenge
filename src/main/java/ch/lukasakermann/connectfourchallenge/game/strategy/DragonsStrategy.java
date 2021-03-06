package ch.lukasakermann.connectfourchallenge.game.strategy;

import ch.lukasakermann.connectfourchallenge.connectFourService.Game;
import ch.lukasakermann.connectfourchallenge.connectFourService.Player;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Map;

final class MyEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}

public class DragonsStrategy implements ConnectFourStrategy {
    private static final String EMPTY_CELL = "EMPTY";
    private static final String OWN_TEAM = "Dragons";
    private static String OWN_COLOR;
    private static List<List<String>> board;
    private static Map<Integer, Integer> freeCoordinates;
    //private static String OWN_COLOR = "YELLOW";

    @Override
    public int dropDisc(Game game) {
        this.board = game.getBoard();
        List<Player> players = game.getPlayers();
        for (Player p: players) {
            if (p.getPlayerId().equals(OWN_TEAM)) OWN_COLOR = p.getDisc();
        }

        if(isEmpty()) return 3;
        
        List<String> columns = board.get(0);

        List<Integer> possibilities = getVertical(OWN_COLOR);

        List<Integer> validMoves = IntStream.range(0, columns.size())
                .boxed()
                .filter(column -> columns.get(column).equals(EMPTY_CELL))
                .collect(Collectors.toList());

        Random rand = new Random();
        //freePlaces(board);

        if (possibilities.size() > 0) {
            for (Map.Entry<Integer, Integer> c : freeCoordinates.entrySet()) {
                if (get3Vertical(c, OWN_COLOR)) {
                    return c.getKey();
                } else {
                    String enemyColor = OWN_COLOR.equals("RED") ? "YELLOW" : "RED";
                    if (get3Vertical(c, enemyColor)) {
                        return c.getKey();
                    }
                }
            }
            return possibilities.get(rand.nextInt(possibilities.size()));
        } else {
            return validMoves.get(rand.nextInt(validMoves.size()));
        }
    }

    private static boolean get3Vertical(Map.Entry<Integer, Integer> e, String color) {
        int row = e.getValue();
        int col = e.getKey();
        if (row <= 2){
            boolean three = true;
            for (int i = 1; i < 4 && three; ++i) {
                if (!board.get(row+i).get(col).equals(color)) {
                    three = false;
                }
            }
            return three;
        } else {
            return false;
        }
    }

    /**
     * Returns all the columns where OUR player has the top stone.
     * @return
     */
     private static List<Integer> getVertical(String color) {
    	 List<Integer> possibilities = new ArrayList<>();
    	 Map<Integer, Integer> freePlaces = freePlaces();
    	 for (Entry<Integer, Integer> free : freePlaces.entrySet()) {
    		 int columnNum = free.getKey();
    		 int row = free.getValue();
    		 if(row != 5) {
    			 if (board.get(row+1).get(columnNum).equals(color)) {
    				 possibilities.add(columnNum);
    			 }   			
    		 }
    	 }
         return possibilities;
    }

     /**
      * Checks whether the board is empty
      * @return
      */
     private static boolean isEmpty() {
    	 for(int i=0;i<7;i++) {
    		 if(!board.get(5).get(i).equals(EMPTY_CELL)) {
    			 return false;
    		 }
    	 }
    	 return true;
     }

     /**
      * Check whether the coordinate to the left has OWN_COLOR
      * @param board
      * @param coordinate
      * @return
      */
     private static boolean getLeftOne(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 if(coordinate.getKey() == 0) {
    		 return false;
    	 } else if (board.get(coordinate.getValue()).get(coordinate.getKey()-1).equals(OWN_COLOR)){
    		 return true;
    	 }
    	 else {
    		 return false;
    	 }
     }
     
     /**
      * Check whether the coordinate to the right has OWN_COLOR
      * @param board
      * @param coordinate
      * @return
      */
     private static boolean getRightOne(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 //System.out.println(board.get(coordinate.getValue()).get(coordinate.getKey()+1));
    	 if(coordinate.getKey() == 6) {
    		 return false;
    	 } else if (board.get(coordinate.getValue()).get(coordinate.getKey()+1).equals(OWN_COLOR)){
    		 return true;
    	 }
    	 else {
    		 return false;
    	 }
     }
     
     private static boolean horizontalWin(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 
    	 int countLeft = 0;
    	 int countRight = 0;
    	 
    	 Map.Entry<Integer, Integer> entry = new MyEntry<Integer, Integer>(coordinate.getKey(), coordinate.getValue());
    	 while(getLeftOne(board, entry)){
    		 countLeft++;
    		 entry = new MyEntry<Integer, Integer>(entry.getKey()-1, entry.getValue());
    		 
    	 }
    	 entry = new MyEntry<Integer, Integer>(coordinate.getKey(), coordinate.getValue());
    	 while(getRightOne(board,entry)) {
    		 countRight++;
    		 entry = new MyEntry<Integer, Integer>(entry.getKey()+1, entry.getValue());
    	 }
    	 if ((countLeft == 3 || countRight == 3) || (countLeft == 2 && countRight == 1) || (countLeft == 1 && countRight == 2)) {
    		 return true;
    	 } else {
    		 return false;
    	 }  	 
     }
     
     private static boolean getRightUp(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 //System.out.println(board.get(coordinate.getValue()).get(coordinate.getKey()+1));
    	 if(coordinate.getKey() == 6 || coordinate.getValue() == 0) {
    		 return false;
    	 } else if (board.get(coordinate.getValue()-1).get(coordinate.getKey()+1).equals(OWN_COLOR)){
    		 return true;
    	 }
    	 else {
    		 return false;
    	 }
     }
     private static boolean getLeftUp(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 //System.out.println(board.get(coordinate.getValue()).get(coordinate.getKey()+1));
    	 if(coordinate.getKey() == 0 || coordinate.getValue() == 0) {
    		 return false;
    	 } else if (board.get(coordinate.getValue()-1).get(coordinate.getKey()-1).equals(OWN_COLOR)){
    		 return true;
    	 }
    	 else {
    		 return false;
    	 }
     }
     
     private static boolean getRightDown(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 //System.out.println(board.get(coordinate.getValue()).get(coordinate.getKey()+1));
    	 if(coordinate.getKey() == 6 || coordinate.getValue() == 5) {
    		 return false;
    	 } else if (board.get(coordinate.getValue()+1).get(coordinate.getKey()+1).equals(OWN_COLOR)){
    		 return true;
    	 }
    	 else {
    		 return false;
    	 }
     }
     
     private static boolean getLeftDown(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 //System.out.println(board.get(coordinate.getValue()).get(coordinate.getKey()+1));
    	 if(coordinate.getKey() == 0 || coordinate.getValue() == 5) {
    		 return false;
    	 } else if (board.get(coordinate.getValue()+1).get(coordinate.getKey()-1).equals(OWN_COLOR)){
    		 return true;
    	 }
    	 else {
    		 return false;
    	 }
     }
     
     private static boolean diagonalWin(List<List<String>> board, Map.Entry<Integer, Integer> coordinate) {
    	 
    	 int countLeftUp = 0;
    	 int countLeftDown = 0;
    	 int countRightUp = 0;
    	 int countRightDown = 0;
    	 
    	 Map.Entry<Integer, Integer> entry = new MyEntry<Integer, Integer>(coordinate.getKey(), coordinate.getValue());
    	 while(getLeftUp(board, entry)){
    		 countLeftUp++;
    		 entry = new MyEntry<Integer, Integer>(entry.getKey()-1, entry.getValue()-1);
    		 
    	 }
    	 entry = new MyEntry<Integer, Integer>(coordinate.getKey(), coordinate.getValue());
    	 while(getLeftDown(board,entry)) {
    		 countLeftDown++;
    		 entry = new MyEntry<Integer, Integer>(entry.getKey()-1, entry.getValue()+1);
    	 }
    	 entry = new MyEntry<Integer, Integer>(coordinate.getKey(), coordinate.getValue());
    	 while(getRightUp(board,entry)) {
    		 countRightUp++;
    		 entry = new MyEntry<Integer, Integer>(entry.getKey()+1, entry.getValue()-1);
    	 }
    	 entry = new MyEntry<Integer, Integer>(coordinate.getKey(), coordinate.getValue());
    	 while(getRightDown(board,entry)) {
    		 countRightDown++;
    		 entry = new MyEntry<Integer, Integer>(entry.getKey()+1, entry.getValue()+1);
    	 }
    	 if ((countLeftUp == 3 || countLeftDown == 3 || countRightUp == 3 || countRightDown == 3) || 
    			 (countLeftUp == 2 && countRightDown == 1) || 
    			 (countLeftDown == 2 && countRightUp == 1) || 
    			 (countLeftUp == 1 && countRightDown == 2) || 
    			 (countLeftDown == 1 && countRightUp == 2)) {
    		 return true;
    	 } else {
    		 return false;
    	 }  	 
    	 
    	 
     }
     
    private static Map<Integer, Integer> freePlaces(){

        List<String> columns = board.get(0);

        List<Integer> validMoves = IntStream.range(0, columns.size())
                .boxed()
                .filter(column -> columns.get(column).equals(EMPTY_CELL))
                .collect(Collectors.toList());

        Map<Integer, Integer> free = new HashMap<>();
        for (int r = 0; r < validMoves.size(); ++r) {
            int key = validMoves.get(r);
            for (int i = 1; i < 6 && !free.containsKey(key); ++i){
                if (!board.get(i).get(key).equals(EMPTY_CELL))
                    free.put(key, i-1);
            }
            if (!free.containsKey(key)) {
            	free.put(key, 5);
            }
        }

        freeCoordinates = free;

        return free;
    }

    private List<Integer> getHorizontal() {
        return null;
    }
    
    public static void main(String[] args) {
    	String EMPTY_CELL = "EMPTY";
    	List<List<String>> testBoard = new ArrayList<>();
    	List<String> row_0 = new ArrayList<>();
    	List<String> row_1 = new ArrayList<>();
    	List<String> row_2 = new ArrayList<>();
    	List<String> row_3 = new ArrayList<>();
    	List<String> row_4 = new ArrayList<>();
    	List<String> row_5 = new ArrayList<>();
    	for (int i=0;i<7;i++) {
    		row_0.add(EMPTY_CELL);
    		row_1.add(EMPTY_CELL);
    		row_2.add(EMPTY_CELL);
    		row_3.add(EMPTY_CELL);
    		row_4.add(EMPTY_CELL);
    		row_5.add(EMPTY_CELL);
    		
    	}
    	
    	testBoard.add(row_0);
    	testBoard.add(row_1);
    	testBoard.add(row_2);
    	testBoard.add(row_3);
    	testBoard.add(row_4);
    	testBoard.add(row_5);

    	
    	testBoard.get(5).set(1, "YELLOW");
    	testBoard.get(5).set(3, "RED");   	
    	testBoard.get(4).set(3, "YELLOW");
    	testBoard.get(4).set(2, "YELLOW");
    	testBoard.get(4).set(1, "YELLOW");
    	testBoard.get(3).set(4, "RED");
    	testBoard.get(3).set(3, "YELLOW");
    	
    	System.out.println(testBoard.get(0));
    	System.out.println(testBoard.get(1));
    	System.out.println(testBoard.get(2));
    	System.out.println(testBoard.get(3));
    	System.out.println(testBoard.get(4));
    	System.out.println(testBoard.get(5));

    	//System.out.println(freePlaces(testBoard));
    	//System.out.println(getVertical("RED"));
    	
    	//System.out.println(isEmpty());
    	//System.out.println(freePlaces());
    	
    	Map<Integer,Integer> coordinates = new HashMap<Integer, Integer>();
    	coordinates.put(0,4);
    	Map.Entry<Integer, Integer> entry = coordinates.entrySet().iterator().next();
    	//System.out.println(getRightOne(testBoard, entry));
    	
    	//System.out.println(horizontalWin(testBoard,entry));
    	//System.out.println(getVertical(testBoard));
    	
    	System.out.println(horizontalWin(testBoard, entry));

	}
}
