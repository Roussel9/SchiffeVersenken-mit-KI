import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class AIMedium {
    public char[][] visibleFieldAI;
    public GameField gameField;
    public boolean aiShipsPlaced = false;
    public List<int[]> lastHits = new ArrayList<>();
    public boolean shipDirectionFound = false; // Indicates if a direction has been detected
    public boolean isHorizontal = false; // Stores the direction if found
    public boolean allMiddleFieldsHit = false; // Flag to track if all middle fields are hit
    public List<int[]> lastHitsCopy = new ArrayList<>(lastHits); // In case ships are next to each other and a direction from 2 hits but different ships
    AIMedium(GameField gameField) {
        this.gameField = gameField;

        visibleFieldAI = new char[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                visibleFieldAI[row][col] = '~'; // Everything is unknown
            }
        }
        gameField.playerMoves = 0; // Initial state
    }

    // Method for placing AI ships
    protected void placeAIShips() {
        Random random = new Random();

        for (int i = 0; i < gameField.shipLengths.length; i++) {
            int length = gameField.shipLengths[i];
            boolean success = false;

            // Try to place a ship until it works
            while (!success) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Try to place the ship
                success = gameField.placeShip(gameField.aiField, xStart, yStart, length, horizontal);

                // If successful, store the ship
                if (success) {
                    Ship newShip = new Ship(xStart, yStart, length, horizontal);
                    gameField.aiShips.add(newShip);
                }
            }
        }

        aiShipsPlaced = true;
    }

    // AI makes its three moves
    public void makeAIMoves() {
        if (gameField.gameOver) {
            System.out.println("Das Spiel ist bereits beendet. Keine Zuege mehr möglich.");
            return;
        }

        for (int i = 0; i < 3; i++) { // AI makes three moves
            if (gameField.playerShips.isEmpty()) {
                break;
            }
            int[] move = findBestMove();
            int row = move[0];
            int col = move[1];

            if (isValidMove(row, col)) {
                makeAIShot(row, col);
            }

            // Wait time between AI moves
            try {
                Thread.sleep(1000); // 1 second pause
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        gameField.playerMoves = 0; // Player can play again
    }

    protected boolean isValidMove(int row, int col) {
        return visibleFieldAI[row][col] == '~';
    }

    protected int[] findBestMove() {
        if (!shipDirectionFound) { // To destroy ships next to each other
            return shootAtNeighbor();
        }

        if (!lastHits.isEmpty()) {
            return findTargetedMove();
        }
        if (!allMiddleFieldsHit) {
            return findHeuristicMove(); // Search for middle fields
        }
        return findCornerMove(); // Search for corners if all middle fields are hit
    }

    protected int[] findTargetedMove() {
        if (!lastHits.isEmpty()) {
            int[] firstHit = lastHits.get(0);

            // If a direction has already been detected, continue shooting in that direction
            if (shipDirectionFound) {
                if (isHorizontal) {
                    return shootHorizontalFurther();
                } else {
                    return shootVerticalFurther();
                }
            }

            // If no direction has been detected, try shooting at adjacent fields
            for (int[] direction : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
                int newRow = firstHit[0] + direction[0];
                int newCol = firstHit[1] + direction[1];

                if (newRow >= 0 && newRow < 10 && newCol >= 0 && newCol < 10 &&
                        isValidMove(newRow, newCol)) {
                    return new int[]{newRow, newCol};
                }
            }
        }

        return findHeuristicMove();
    }

    protected int[] shootHorizontalFurther() {
        int[] firstHit = lastHits.get(0);
        int[] lastHitPosition = lastHits.get(lastHits.size() - 1);

        // Try shooting further right
        int rightCol = lastHitPosition[1] + 1;
        if (rightCol < 10 && isValidMove(firstHit[0], rightCol)) {
            return new int[]{firstHit[0], rightCol};
        }

        // Try shooting further left
        int minCol = lastHits.stream().mapToInt(hit -> hit[1]).min().orElse(-1); // Extract column values, find minimum, default to -1
        int leftCol = minCol - 1;
        if (leftCol >= 0 && isValidMove(firstHit[0], leftCol)) {
            return new int[]{firstHit[0], leftCol};
        }

        if (shipDirectionFound) { // If ships are next to each other and a direction from 2 hits but different ships
            lastHitsCopy = lastHits;
            return shootAtNeighbor();
        }

        // Fallback
        return findHeuristicMove();
    }

    protected int[] shootVerticalFurther() {
        int[] firstHit = lastHits.get(0);
        int[] lastHitPosition = lastHits.get(lastHits.size() - 1);

        // Try shooting further down
        int downRow = lastHitPosition[0] + 1;
        if (downRow < 10 && isValidMove(downRow, firstHit[1])) {
            return new int[]{downRow, firstHit[1]};
        }

        // Try shooting further up
        int minRow = lastHits.stream().mapToInt(hit -> hit[0]).min().orElse(-1); // Extract row values, find minimum, default to -1
        int upRow = minRow - 1;
        if (upRow >= 0 && isValidMove(upRow, firstHit[1])) {
            return new int[]{upRow, firstHit[1]};
        }

        if (shipDirectionFound) { // If ships are next to each other and a direction from 2 hits but different ships
            lastHitsCopy = lastHits;
            return shootAtNeighbor();
        }

        // Fallback
        return findHeuristicMove();
    }

    protected int[] shootAtNeighbor() { // If ships are next to each other and a direction from 2 hits but different ships
        if (lastHits.size() == 1) { // Targeted move for each hit on different ships
            return findTargetedMove();
        }

        if (!lastHitsCopy.isEmpty()) {
            for (int[] hitArray : lastHitsCopy) {
                List<int[]> singleHitList = new ArrayList<>();
                singleHitList.add(hitArray);
                lastHits = singleHitList;
                shipDirectionFound = false;
                lastHitsCopy.remove(hitArray);
                return findTargetedMove();
            }
        }
        return findTargetedMove();
    }

    protected int[] findHeuristicMove() {
        // Define the squares of the field in 3x3 regions
        int[][] squares = {
            {0, 0, 2, 2}, {0, 3, 2, 5}, {0, 6, 2, 8},
            {3, 0, 5, 2}, {3, 3, 5, 5}, {3, 6, 5, 8},
            {6, 0, 8, 2}, {6, 3, 8, 5}, {6, 6, 8, 8},
            {9, 0, 9, 2}, {9, 3, 9, 5}, {9, 6, 9, 8}
        };

        // Loop through each square and shoot at the center of the square first
        for (int[] square : squares) {
            int middleRow = (square[0] + square[2]) / 2;
            int middleCol = (square[1] + square[3]) / 2;
            if (isValidMove(middleRow, middleCol)) {
                return new int[]{middleRow, middleCol};
            }
        }

        allMiddleFieldsHit = true; // All middle fields have been hit
        return findCornerMove(); // If all middle fields are hit, go to the corners
    }

    protected int[] findCornerMove() {
        // Loop through each square and shoot at the corners
        int[][] squares = {
            {0, 0, 2, 2}, {0, 3, 2, 5}, {0, 6, 2, 9},
            {3, 0, 5, 2}, {3, 3, 5, 5}, {3, 6, 5, 9},
            {6, 0, 8, 2}, {6, 3, 8, 5}, {6, 6, 8, 9},
            {9, 0, 9, 2}, {9, 3, 9, 5}, {9, 6, 9, 9}
        };

        for (int[] square : squares) {
            int rowStart = square[0];
            int colStart = square[1];
            int rowEnd = square[2];
            int colEnd = square[3];

            if (isValidMove(rowStart, colEnd)) {
                return new int[]{rowStart, colEnd};
            }
        }

        for (int[] square : squares) {
            int rowStart = square[0];
            int colStart = square[1];
            int rowEnd = square[2];
            int colEnd = square[3];

            if (isValidMove(rowStart, colStart)) {
                return new int[]{rowStart, colStart};
            }
        }

        for (int[] square : squares) {
            int rowStart = square[0];
            int colStart = square[1];
            int rowEnd = square[2];
            int colEnd = square[3];

            if (isValidMove(rowEnd, colEnd)) {
                return new int[]{rowEnd, colEnd};
            }
        }

        for (int[] square : squares) {
            int rowStart = square[0];
            int colStart = square[1];
            int rowEnd = square[2];
            int colEnd = square[3];

            if (isValidMove(rowEnd, colStart)) {
                return new int[]{rowEnd, colStart};
            }
        }

        // Last fallback: Check all fields of the game field
        for (int row = 9; row >= 0; row--) {
            for (int col = 9; col >= 0; col--) {
                if (isValidMove(row, col)) {
                    return new int[]{row, col};
                }
            }
        }

        return new int[]{-1, -1}; // No valid moves left
    }

    protected void makeAIShot(int row, int col) {
        if (gameField.playerField[row][col] == 'S') { // Hit
            System.out.println("KI Treffer bei (" + row + ", " + col + ")");
            visibleFieldAI[row][col] = 'X';
            gameField.playerField[row][col] = 'X';
            gameField.drawHit(row, col, "Player");
            lastHits.add(new int[]{row, col});
            gameField.checkAndMarkDestroyedShipsPlayer();
            if (lastHits.size() > 1) {
                shipDirectionFound = true;
                isHorizontal = lastHits.get(0)[0] == lastHits.get(1)[0];
            }

            String winner = gameField.checkWinner();
            if (winner != null) {
                System.out.println(winner);
            }

        } else { // Miss
            System.out.println("KI Fehlschuss bei (" + row + ", " + col + ")");
            visibleFieldAI[row][col] = 'O';
            gameField.playerField[row][col] = 'O';
            gameField.drawMiss(row, col, "Player");
        }
    }
}

/*

class AIDifficult extends AIMedium {
    public AIDifficult(GameField gameField) {
        super(gameField);
    }

    @Override
    private void placeAIShips() {
        Random random = new Random();

        for (int i = 0; i < gameField.shipLengths.length; i++) {
            int length = gameField.shipLengths[i];
            boolean success = false;

            // Try to place a ship until it works
            while (!success) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Try to place the ship
                success = gameField.placeShip(gameFieldAI, xStart, yStart, length, horizontal);

                // If successful, store the ship
                if (success) {
                    Ship newShip = new Ship(xStart, yStart, length, horizontal);
                    aiShips.add(newShip);
                }
            }
        }

        aiShipsPlaced = true;
    }

    public void makeAIMoves() {
        super.makeAIMoves();
    }

    protected boolean isValidMove(int row, int col) {
        return super.isValidMove();
    }

    protected int[] findBestMove() {
        return super.findBestMove();
    }

    protected int[] findTargetedMove() {
        return super.findTargetedMove();
    }

    protected int[] shootHorizontalFurther() {
        return super.shootHorizontalFurther();
    }

    protected int[] shootVerticalFurther() {
        return super.shootVerticalFurther();
    }

    protected int[] shootAtNeighbor() {
        return super.shootAtNeighbor();
    }

    protected int[] findHeuristicMove() {
        return super.findHeuristicMove();
    }

    protected int[] findCornerMove() {
        return super.findCornerMove();
    }
}

*/