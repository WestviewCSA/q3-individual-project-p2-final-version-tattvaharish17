import java.io.File;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Runner{


	 public static void main(String[] args) {

	        String[][][] maze = getText1("easymaze1");

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
    
    public static String[][][] getText1(String filename) {

        File fileObj = new File(filename);

        try {
            Scanner sc = new Scanner(fileObj);

            int rows = Integer.parseInt(sc.next());
            int cols = Integer.parseInt(sc.next());
            int numMazes = Integer.parseInt(sc.next());
            String[][][] maze1 = new String[numMazes][rows][cols];

            int currentRow = 0;
            int currentLevel = 0;
            while (sc.hasNext()) {
                String line = sc.next();
                
                for (int col = 0; col < line.length(); col++) {
                    maze1[currentLevel][currentRow][col] = String.valueOf(line.charAt(col));
                }

                currentRow++;

                if (currentRow == rows) {
                    currentRow = 0;
                    currentLevel++;
                }
            }

            return maze1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String[][] getCords(String filename) {

        File fileObj = new File(filename);

        try {
            Scanner sc = new Scanner(fileObj);
            int rows     = Integer.parseInt(sc.next());
            int cols     = Integer.parseInt(sc.next());
            int numMazes = Integer.parseInt(sc.next());

            String[][] mazeGrid = new String[rows][cols];

            while (sc.hasNext()) {
                String ch  = sc.next();
                int row    = Integer.parseInt(sc.next());
                int col    = Integer.parseInt(sc.next());
                sc.next();
                mazeGrid[row][col] = ch;
            }

            for (int r = 0; r < mazeGrid.length; r++) {
                for (int c = 0; c < mazeGrid[0].length; c++) {
                    if (mazeGrid[r][c] == null) {
                        mazeGrid[r][c] = ".";
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
