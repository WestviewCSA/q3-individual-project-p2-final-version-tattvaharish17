import java.io.File;
import java.util.Stack;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class p1{


	public static void main(String[] args) {
	    try {
	        boolean useQueue = false;
	        boolean useStack = false;
	        boolean useOpt = false;
	        boolean showTime = false;
	        boolean inCoordinate = false;
	        boolean outCoordinate = false;
	        boolean showHelp = false;

	        for (int i = 0; i < args.length - 1; i++) {
	            if (args[i].equals("--Queue")) useQueue = true;
	            else if (args[i].equals("--Stack")) useStack = true;
	            else if (args[i].equals("--Opt")) useOpt = true;
	            else if (args[i].equals("--Time")) showTime = true;
	            else if (args[i].equals("--Incoordinate")) inCoordinate = true;
	            else if (args[i].equals("--Outcoordinate")) outCoordinate = true;
	            else if (args[i].equals("--Help")) showHelp = true;
	        }

	        if (showHelp) {
	            printHelp();
	            System.exit(0);
	        }

	        int count = 0;
	        if (useQueue) count++;
	        if (useStack) count++;
	        if (useOpt) count++;

	        if (count != 1) {
	            System.out.println("Use exactly one of --Queue, --Stack, or --Opt.");
	            System.exit(-1);
	        }

	        if (args.length == 0) {
	            System.out.println("Missing input file.");
	            System.exit(-1);
	        }

	        String fileName = args[args.length - 1];

	        String[][][] maze;
	        if (inCoordinate) {
	            maze = getCords(fileName);
	        } else {
	            maze = getText(fileName);
	        }

	        long start = 0;
	        long end = 0;

	        if (showTime) start = System.nanoTime();

	        Queue<int[]> path;
	        if (useQueue) path = queueSearch(maze);
	        else if (useStack) path = stackSearch(maze);
	        else path = optSearch(maze);

	        if (showTime) end = System.nanoTime();

	        if (path.isEmpty()) {
	            System.out.println("The Wolverine Store is closed.");
	            return;
	        }

	        if (outCoordinate) {
	            printPathCoordinates(path);
	        } else {
	            markPath(maze, path);
	            displayGrid(maze);
	        }

	        if (showTime) {
	            double time = (end - start) / 1_000_000_000.0;
	            System.out.println("Total Runtime: " + time + " seconds");
	        }

	    } catch (Exception e) {
	        System.out.println("Error: " + e.getMessage());
	        System.exit(-1);
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
    
    public static void printPathCoordinates(Queue<int[]> path) {
        while (!path.isEmpty()) {
            int[] pos = path.poll();
            System.out.println(pos[0] + " " + pos[1] + " " + pos[2]);
        }
    }
    
    public static void printHelp() {
        System.out.println("Maze Solver Program");
        System.out.println("--Stack : use stack-based traversal");
        System.out.println("--Queue : use queue-based traversal");
        System.out.println("--Opt : use optimal shortest-path traversal");
        System.out.println("--Time : print total runtime");
        System.out.println("--Incoordinate : input file is coordinate format");
        System.out.println("--Outcoordinate : output path as coordinates");
        System.out.println("--Help : display this message");
    }
    
    public static Queue<int[]> optSearch(String[][][] maze) {

        int levels = maze.length;
        int rows = maze[0].length;
        int cols = maze[0][0].length;

        int[][][] steps = new int[levels][rows][cols];

        for (int lvl = 0; lvl < levels; lvl++) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    steps[lvl][r][c] = -1;
                }
            }
        }

        int startRow = 0;
        int startCol = 0;
        int startLevel = 0;
        int goalRow = -1;
        int goalCol = -1;
        int goalLevel = -1;

        // only the FIRST maze's W is the true start
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[0][r][c].equals("W")) {
                    startRow = r;
                    startCol = c;
                    startLevel = 0;
                }
            }
        }

        // goal can be in any level
        for (int lvl = 0; lvl < levels; lvl++) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (maze[lvl][r][c].equals("$")) {
                        goalRow = r;
                        goalCol = c;
                        goalLevel = lvl;
                    }
                }
            }
        }
        

        steps[startLevel][startRow][startCol] = 0;

        int[][] moves = {
            {-1, 0},
            {1, 0},
            {0, 1},
            {0, -1}
        };

        boolean changed = true;

        while (changed) {
            changed = false;

            for (int lvl = 0; lvl < levels; lvl++) {
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {

                        if (steps[lvl][r][c] == -1) {
                            continue;
                        }

                        int currentStep = steps[lvl][r][c];

                        for (int i = 0; i < moves.length; i++) {
                            int newRow = r + moves[i][0];
                            int newCol = c + moves[i][1];

                            if (newRow >= 0 && newRow < rows &&
                                newCol >= 0 && newCol < cols &&
                                isOpen(maze[lvl][newRow][newCol]) &&
                                steps[lvl][newRow][newCol] == -1) {

                                steps[lvl][newRow][newCol] = currentStep + 1;
                                changed = true;
                            }
                        }

                        if (maze[lvl][r][c].equals("|")) {
                            int nextLevel = lvl + 1;

                            if (nextLevel < levels) {
                                for (int rr = 0; rr < rows; rr++) {
                                    for (int cc = 0; cc < cols; cc++) {
                                        if (maze[nextLevel][rr][cc].equals("W") &&
                                            steps[nextLevel][rr][cc] == -1) {

                                            steps[nextLevel][rr][cc] = currentStep + 1;
                                            changed = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (goalLevel != -1 && steps[goalLevel][goalRow][goalCol] != -1) {
                break;
            }
        }

        if (goalLevel == -1 || steps[goalLevel][goalRow][goalCol] == -1) {
            return new ArrayDeque<>();
        }

        Stack<int[]> backwards = new Stack<>();
        Queue<int[]> path = new ArrayDeque<>();

        int row = goalRow;
        int col = goalCol;
        int lvl = goalLevel;

        backwards.push(new int[]{row, col, lvl});

        while (!(row == startRow && col == startCol && lvl == startLevel)) {
            int currentStep = steps[lvl][row][col];
            boolean foundPrev = false;

            for (int i = 0; i < moves.length; i++) {
                int newRow = row + moves[i][0];
                int newCol = col + moves[i][1];

                if (newRow >= 0 && newRow < rows &&
                    newCol >= 0 && newCol < cols &&
                    steps[lvl][newRow][newCol] == currentStep - 1) {

                    row = newRow;
                    col = newCol;
                    backwards.push(new int[]{row, col, lvl});
                    foundPrev = true;
                    break;
                }
            }

            if (!foundPrev) {
                int prevLevel = lvl - 1;

                if (prevLevel >= 0) {
                    for (int rr = 0; rr < rows && !foundPrev; rr++) {
                        for (int cc = 0; cc < cols && !foundPrev; cc++) {
                            if (maze[prevLevel][rr][cc].equals("|") &&
                                steps[prevLevel][rr][cc] == currentStep - 1) {

                                row = rr;
                                col = cc;
                                lvl = prevLevel;
                                backwards.push(new int[]{row, col, lvl});
                                foundPrev = true;
                            }
                        }
                    }
                }
            }

            if (!foundPrev) {
                return new ArrayDeque<>();
            }
        }

        while (!backwards.isEmpty()) {
            path.add(backwards.pop());
        }

        return path;
    }
    

    
}

  

