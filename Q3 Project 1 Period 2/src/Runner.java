import java.io.File;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Runner{


	 public static void main(String[] args) {

	        String[][][] maze = getText("easymaze1");

	        for(int lvl = 0; lvl < maze.length; lvl++) {
	            for(int r = 0; r < maze[lvl].length; r++) {
	                for(int c = 0; c < maze[lvl][0].length; c++) {
	                    System.out.print(maze[lvl][r][c]);
	                }
	                System.out.println();
	            }
	        }

	        Queue<int[]> qv = queueSearch(maze);
	        while(!qv.isEmpty()) {
	        	int[] pos = qv.poll();
	        	System.out.println(pos[0] + " "+ pos[1]+ " "+ pos[2]);
	        }
	        Queue<int[]> sv = stackSearch(maze);
	        while(!sv.isEmpty()) {
	        	int[] pos = sv.poll();
	        	System.out.println(pos[0] + " "+ pos[1]+ " "+ pos[2]);
	        }

	    }
    
    public static String[][][] getText(String filename) {

        File fileObj = new File(filename);

        try {
            Scanner sc = new Scanner(fileObj);

            int r = Integer.parseInt(sc.next());
            int c = Integer.parseInt(sc.next());
            int nm = Integer.parseInt(sc.next());
            String[][][] maze1 = new String[nm][r][c];

            int currrow = 0;
            int currlvl = 0;
            while (sc.hasNext()) {
                String line = sc.next();
                
                for (int col = 0; col < line.length(); col++) {
                    maze1[currlvl][currrow][col] = String.valueOf(line.charAt(col));
                }

                currrow++;

                if (currrow == r) {
                    currrow = 0;
                    currlvl++;
                }
            }

            return maze1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String[][][] getCords(String filename) {

        File fileObj = new File(filename);

        try {
            Scanner sc = new Scanner(fileObj);
            int rows     = Integer.parseInt(sc.next());
            int cols     = Integer.parseInt(sc.next());
            int nummazes = Integer.parseInt(sc.next());

            String[][][] mazeGrid = new String[nummazes][rows][cols];

            while (sc.hasNext()) {
                String ch  = sc.next();
                int row    = Integer.parseInt(sc.next());
                int col    = Integer.parseInt(sc.next());
                int level = Integer.parseInt(sc.next());
                if (row >= rows || col >= cols || level >= nummazes) {
                    System.out.println("Coordinates don't match the given specs");
                    return new String[0][0][0];
                }
                mazeGrid[level][row][col] = ch;

            }
            

            for (int level = 0; level < mazeGrid.length; level++) {
            	for (int row = 0; row < mazeGrid[0].length; row++) {
            		for (int col = 0; col < mazeGrid[0][0].length; col++) {
            			if (mazeGrid[level][row][col] == null) {
                            mazeGrid[level][row][col] = ".";
            		}
            	}
            }
            }

            return mazeGrid;
            
            

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found -> " + filename);
        }

        return null;
    }
}
