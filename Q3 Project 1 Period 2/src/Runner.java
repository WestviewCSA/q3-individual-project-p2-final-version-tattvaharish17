import java.io.File;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Runner{


	public static void main(String[] args) {

	    Scanner input = new Scanner(System.in);

	    boolean useQueue = false;
	    boolean useStack = false;
	    boolean showTime = false;

	    String fileName;

	    if (args.length > 0) {

	        for (int i = 0; i < args.length - 1; i++) {
	            if (args[i].equals("--Queue")) {
	                useQueue = true;
	            }
	            else if (args[i].equals("--Stack")) {
	                useStack = true;
	            }
	            else if (args[i].equals("--Time")) {
	                showTime = true;
	            }
	        }

	        if ((useQueue && useStack) || (!useQueue && !useStack)) {
	            System.out.println("Use exactly one of --Queue or --Stack.");
	            System.exit(-1);
	        }

	        fileName = args[args.length - 1];

	    } else {

	        System.out.print("Choose mode (queue/stack): ");
	        String mode = input.nextLine();

	        if (mode.equalsIgnoreCase("queue")) {
	            useQueue = true;
	        }
	        else if (mode.equalsIgnoreCase("stack")) {
	            useStack = true;
	        }
	        else {
	            System.out.println("Use queue or stack.");
	            System.exit(-1);
	        }

	        System.out.print("Show runtime? (yes/no): ");
	        String timeChoice = input.nextLine();

	        if (timeChoice.equalsIgnoreCase("yes")) {
	            showTime = true;
	        }

	        System.out.print("Enter maze file name: ");
	        fileName = input.nextLine();
	    }

	    System.out.print("Input format (text/coordinate): ");
	    String inputType = input.nextLine();

	    String[][][] maze;

	    if (inputType.equalsIgnoreCase("text")) {
	        maze = getText(fileName);
	    }
	    else if (inputType.equalsIgnoreCase("coordinate")) {
	        maze = getCords(fileName);
	    }
	    else {
	        System.out.println("Invalid input format.");
	        return;
	    }
	    long startTime = 0;
	    long endTime = 0;

	    Queue<int[]> path;

	    if (showTime) {
	        startTime = System.nanoTime();
	    }

	    if (useQueue) {
	        path = queueSearch(maze);
	    } else {
	        path = stackSearch(maze);
	    }

	    if (showTime) {
	        endTime = System.nanoTime();
	    }

	    markPath(maze, path);

	    if (useQueue) {
	        System.out.println("Queue Path:");
	    } else {
	        System.out.println("Stack Path:");
	    }

	    displayGrid(maze);

	    if (showTime) {
	        double totalSeconds = (endTime - startTime) / 1000000000.0;
	        System.out.println("Total Runtime: " + totalSeconds + " seconds");
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
    public static boolean isOpen(String cell) {
        return cell.equals(".") || cell.equals("$") || cell.equals("|");
    }
    public static Queue<int[]> queueSearch(String[][][] maze){
    	Queue<int[]> toVisit = new ArrayDeque<>();        
        boolean[][][] seen = new boolean[maze.length][maze[0].length][maze[0][0].length];
        
        int[][][][] parent = new int[maze.length][maze[0].length][maze[0][0].length][3];
        
        for (int level = 0; level < maze.length; level++) {
            for (int row = 0; row < maze[0].length; row++) {
                for (int col = 0; col < maze[0][0].length; col++) {
                    parent[level][row][col][0] = -1;
                    parent[level][row][col][1] = -1;
                    parent[level][row][col][2] = -1;
                }
            }
        }
        
        int[] start = new int[3];
        for (int row = 0; row < maze[0].length; row++) {
            for (int col = 0; col < maze[0][0].length; col++) {
                if (maze[0][row][col].equals("W")) {
                    start[0] = row;
                    start[1] = col;
                    start[2] = 0;
                }
            }
        }
        toVisit.add(start);
        seen[start[2]][start[0]][start[1]] = true;

        int[][] moves = {
            {-1, 0},   // north
            {1, 0},    // south
            {0, 1},    // east
            {0, -1}    // west
        };
        
        while (!toVisit.isEmpty()) {
        	int[] current = toVisit.poll();

            int row = current[0];
            int col = current[1];
            int level = current[2];
            
            if (maze[level][row][col].equals("$")) {
                return buildPath(parent, row, col, level);
            }
            

            if (maze[level][row][col].equals("|")) {
                int nextLevel = level + 1;

                for (int r = 0; r < maze[nextLevel].length; r++) {
                    for (int c = 0; c < maze[nextLevel][0].length; c++) {
                        if (maze[nextLevel][r][c].equals("W") && !seen[nextLevel][r][c]) {
                            toVisit.add(new int[]{r, c, nextLevel});
                            seen[nextLevel][r][c] = true;

                            parent[nextLevel][r][c][0] = row;
                            parent[nextLevel][r][c][1] = col;
                            parent[nextLevel][r][c][2] = level;
                        }
                    }
                }
                continue;
            }
            for (int i = 0; i < moves.length; i++) {
                int newRow = row + moves[i][0];
                int newCol = col + moves[i][1];

                if (newRow >= 0 && newRow < maze[level].length &&
                    newCol >= 0 && newCol < maze[level][0].length &&
                    !seen[level][newRow][newCol]) {

                    if (isOpen(maze[level][newRow][newCol])) {
                    	toVisit.add(new int[]{newRow, newCol, level});
                    	seen[level][newRow][newCol] = true;

                    	parent[level][newRow][newCol][0] = row;
                        parent[level][newRow][newCol][1] = col;
                        parent[level][newRow][newCol][2] = level;
                    }
                }
            }
        }

        return new ArrayDeque<>();
    }
    
    public static Queue<int[]> stackSearch(String[][][] maze) {

        Stack<int[]> toVisit = new Stack<>();

        boolean[][][] seen = new boolean[maze.length][maze[0].length][maze[0][0].length];
        int[][][][] parent = new int[maze.length][maze[0].length][maze[0][0].length][3];
        
        for (int lvl = 0; lvl < maze.length; lvl++) {
            for (int r = 0; r < maze[0].length; r++) {
                for (int c = 0; c < maze[0][0].length; c++) {
                    parent[lvl][r][c][0] = -1;
                    parent[lvl][r][c][1] = -1;
                    parent[lvl][r][c][2] = -1;
                }
            }
        }

        int[] start = new int[3];

        for (int row = 0; row < maze[0].length; row++) {
            for (int col = 0; col < maze[0][0].length; col++) {
                if (maze[0][row][col].equals("W")) {
                    start[0] = row;
                    start[1] = col;
                    start[2] = 0;
                }
            }
        }
        toVisit.push(start);
        seen[start[2]][start[0]][start[1]] = true;

        int[][] moves = {
            {-1, 0},   // north
            {1, 0},    // south
            {0, 1},    // east
            {0, -1}    // west
        };
        
        while (!toVisit.isEmpty()) {

            int[] current = toVisit.pop();

            int row = current[0];
            int col = current[1];
            int level = current[2];
            if (maze[level][row][col].equals("$")) {
                return buildPath(parent, row, col, level);
            }
            
            if (maze[level][row][col].equals("|")) {
                int nextLevel = level + 1;
                for (int r = 0; r < maze[nextLevel].length; r++) {
                    for (int c = 0; c < maze[nextLevel][0].length; c++) {
                    	if (maze[nextLevel][r][c].equals("W") && !seen[nextLevel][r][c]) {
                    	    toVisit.push(new int[]{r, c, nextLevel});
                    	    seen[nextLevel][r][c] = true;

                    	    parent[nextLevel][r][c][0] = row;
                    	    parent[nextLevel][r][c][1] = col;
                    	    parent[nextLevel][r][c][2] = level;
                    	}
                    	}
                }
                continue;
               }
            for (int i = 0; i < moves.length; i++) {
                int newRow = row + moves[i][0];
                int newCol = col + moves[i][1];

                if (newRow >= 0 && newRow < maze[level].length &&
                    newCol >= 0 && newCol < maze[level][0].length &&
                    !seen[level][newRow][newCol]) {

                	if (isOpen(maze[level][newRow][newCol])) {
                	    toVisit.push(new int[]{newRow, newCol, level});
                	    seen[level][newRow][newCol] = true;

                	    parent[level][newRow][newCol][0] = row;
                	    parent[level][newRow][newCol][1] = col;
                	    parent[level][newRow][newCol][2] = level;
                	}
                }
            }
        }

        return new ArrayDeque<>();            
        


       
    }
    
    public static Queue<int[]> buildPath(int[][][][] parent, int endRow, int endCol, int endLevel) {

        Stack<int[]> backwards = new Stack<>();
        Queue<int[]> path = new ArrayDeque<>();

        int row = endRow;
        int col = endCol;
        int level = endLevel;

        while (row != -1) {
            backwards.push(new int[]{row, col, level});

            int nextRow = parent[level][row][col][0];
            int nextCol = parent[level][row][col][1];
            int nextLevel = parent[level][row][col][2];

            row = nextRow;
            col = nextCol;
            level = nextLevel;
        }

        while (!backwards.isEmpty()) {
            path.add(backwards.pop());
        }

        return path;
    }
    
    public static void markPath(String[][][] maze, Queue<int[]> path) {

        while (!path.isEmpty()) {
            int[] pos = path.poll();

            int row = pos[0];
            int col = pos[1];
            int level = pos[2];

            if (!maze[level][row][col].equals("W") &&
                !maze[level][row][col].equals("$")
                !=maze[level][row][col].equals("|")){

                maze[level][row][col] = "+";
            }
        }
    }
    
    public static void displayGrid(String[][][] maze) {

        for (int level = 0; level < maze.length; level++) {

            for (int row = 0; row < maze[level].length; row++) {

                for (int col = 0; col < maze[level][0].length; col++) {
                    System.out.print(maze[level][row][col]);
                }

                System.out.println();
            }

        }
    }
    

    
}

  

