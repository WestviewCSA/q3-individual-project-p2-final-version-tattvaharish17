import java.io.File;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Runner{


public static void main(String[] args) {
    
    String[][][] maze = getText1("src/easymaze1");
    for(int level = 0; level < maze.length; level++) {
        for(int i = 0; i < maze[level].length; i++) {
            for(int j = 0; j < maze[level][0].length; j++) {
                System.out.print(maze[level][i][j]);
            }
            System.out.println();
        }
    }
}
    

    public static Queue<String> getText(String filename) {

        Queue<String> textBased = new ArrayDeque<>();
        File fileObj = new File(filename);

        try {
            Scanner sc = new Scanner(fileObj);
            int rows     = Integer.parseInt(sc.next());
            int cols     = Integer.parseInt(sc.next());
            int numMazes = Integer.parseInt(sc.next());

            while (sc.hasNext()) {
                String temp = sc.next();

                if (!temp.matches("[.$W@]+")) {
                    System.out.println("There is an invalid character");
                    return new ArrayDeque<>();
                } else {
                    textBased.add(temp);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found -> " + filename);
        }

        return textBased;
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
