import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {

    // 3D array 
    private char[][][] mazes;
    private int rows;
    private int cols;
    private int numMazes;

    
    public void readMapFile(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));

            rows     = sc.nextInt();
            cols     = sc.nextInt();
            numMazes = sc.nextInt();
            sc.nextLine(); 

            // Create  3D array 
            mazes = new char[numMazes][rows][cols];

            // Loop through each maze, row, and character
            for (int m = 0; m < numMazes; m++) {
                for (int r = 0; r < rows; r++) {
                    String line = sc.nextLine();
                    for (int c = 0; c < cols && c < line.length(); c++) {
                        mazes[m][r][c] = line.charAt(c);
                    }
                }
            }

            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found -> " + filename);
        }
    }

    public void readCoordinateFile(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));

            rows     = sc.nextInt();
            cols     = sc.nextInt();
            numMazes = sc.nextInt();

          
            mazes = new char[numMazes][rows][cols];
            for (int m = 0; m < numMazes; m++)
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++)
                        mazes[m][r][c] = '.';

            while (sc.hasNext()) {
                char ch      = sc.next().charAt(0);
                int  row     = sc.nextInt();
                int  col     = sc.nextInt();
                int  mazeIdx = sc.nextInt();
                mazes[mazeIdx][row][col] = ch;
            }

            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found -> " + filename);
        }
    }
}