import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {

        Queue<String> mazeText = getText("Easy_Map_2");

        while (!mazeText.isEmpty()) {
            System.out.println(mazeText.poll());
        }

        System.out.println("");

        String[][] mazeGrid = getCords("Easy_Map_Coordinates");

        for (int r = 0; r < mazeGrid.length; r++) {
            for (int c = 0; c < mazeGrid[0].length; c++) {
                System.out.print(mazeGrid[r][c]);
            }
            System.out.println();
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