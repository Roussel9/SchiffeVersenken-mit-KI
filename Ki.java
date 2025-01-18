import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class AIMedium {
    public char[][] visibleFieldAI;
    public GameField gameField;
    public boolean aiShipsPlaced = false;
    public List<int[]> lastHits = new ArrayList<>();
    public boolean shipDirectionFound = false; 
    public boolean isHorizontal = false; 
    public boolean allMiddleFieldsHit = false; 
    public List<int[]> lastHitsCopy = new ArrayList<>(lastHits); 
    AIMedium(GameField gameField) {
        this.gameField = gameField;

        visibleFieldAI = new char[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                visibleFieldAI[row][col] = '~'; 
            }
        }
        gameField.playerMoves = 0; 
    }

    protected void placeAIShips() {
        Random random = new Random();

        for (int i = 0; i < gameField.shipLengths.length; i++) {
            int length = gameField.shipLengths[i];
            boolean success = false;

            while (!success) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                success = gameField.placeShip(gameField.aiField, xStart, yStart, length, horizontal);

                if (success) {
                    Ship newShip = new Ship(xStart, yStart, length, horizontal);
                    gameField.aiShips.add(newShip);
                }
            }
        }
        System.out.println("Computer hat seine vier Schiffe auch platziert . Sie koennen jetzt spielen!");
        aiShipsPlaced = true;
    }

    public void makeAIMoves() {
        if (gameField.gameOver) {
            throw new IllegalArgumentException("Das Spiel ist bereits beendet. Keine Zuege mehr möglich.");
            //System.out.println("Das Spiel ist bereits beendet. Keine Zuege mehr möglich.");
            //return;
        }

        for (int i = 0; i < 3; i++) { 
            if (gameField.playerShips.isEmpty()) {
                return;
            }
            int[] move = findBestMove();
            int row = move[0];
            int col = move[1];

            if (isValidMove(row, col)) {
                makeAIShot(row, col);
            }

            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if(!gameField.gameOver){
            System.out.println("Der Computer hat schon seine 3 Zuege gemacht . Jetzt sind Sie dran!");
        }
        gameField.playerMoves = 0; 
    }

    protected boolean isValidMove(int row, int col) {
        return visibleFieldAI[row][col] == '~';
    }

    protected int[] findBestMove() {
        if (!shipDirectionFound) { 
            return shootAtNeighbor();
        }

        if (!lastHits.isEmpty()) {
            return findTargetedMove();
        }
        if (!allMiddleFieldsHit) {
            return findHeuristicMove(); 
        }
        return findCornerMove(); 
    }

    protected int[] findTargetedMove() {
        if (!lastHits.isEmpty()) {
            int[] firstHit = lastHits.get(0);

            if (shipDirectionFound) {
                if (isHorizontal) {
                    return shootHorizontalFurther();
                } else {
                    return shootVerticalFurther();
                }
            }

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

        int rightCol = lastHitPosition[1] + 1;
        if (rightCol < 10 && isValidMove(firstHit[0], rightCol)) {
            return new int[]{firstHit[0], rightCol};
        }

        int minCol = lastHits.stream().mapToInt(hit -> hit[1]).min().orElse(-1); 
        int leftCol = minCol - 1;
        if (leftCol >= 0 && isValidMove(firstHit[0], leftCol)) {
            return new int[]{firstHit[0], leftCol};
        }

        if (shipDirectionFound) { 
            lastHitsCopy = lastHits;
            return shootAtNeighbor();
        }

        // Fallback
        return findHeuristicMove();
    }

    protected int[] shootVerticalFurther() {
        int[] firstHit = lastHits.get(0);
        int[] lastHitPosition = lastHits.get(lastHits.size() - 1);

        int downRow = lastHitPosition[0] + 1;
        if (downRow < 10 && isValidMove(downRow, firstHit[1])) {
            return new int[]{downRow, firstHit[1]};
        }

        int minRow = lastHits.stream().mapToInt(hit -> hit[0]).min().orElse(-1); // Extract row values, find minimum, default to -1
        int upRow = minRow - 1;
        if (upRow >= 0 && isValidMove(upRow, firstHit[1])) {
            return new int[]{upRow, firstHit[1]};
        }

        if (shipDirectionFound) { 
            lastHitsCopy = lastHits;
            return shootAtNeighbor();
        }

        // Fallback
        return findHeuristicMove();
    }

    protected int[] shootAtNeighbor() { 
        if (lastHits.size() == 1) { 
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
        int[][] squares = {
            {0, 0, 2, 2}, {0, 3, 2, 5}, {0, 6, 2, 8},
            {3, 0, 5, 2}, {3, 3, 5, 5}, {3, 6, 5, 8},
            {6, 0, 8, 2}, {6, 3, 8, 5}, {6, 6, 8, 8},
            {9, 0, 9, 2}, {9, 3, 9, 5}, {9, 6, 9, 8}
        };

        for (int[] square : squares) {
            int middleRow = (square[0] + square[2]) / 2;
            int middleCol = (square[1] + square[3]) / 2;
            if (isValidMove(middleRow, middleCol)) {
                return new int[]{middleRow, middleCol};
            }
        }

        allMiddleFieldsHit = true; 
        return findCornerMove(); 
    }

    protected int[] findCornerMove() {
        int[][] squares = {
            {0, 0, 2, 2}, {0, 3, 2, 5}, {0, 6, 2, 9},
            {3, 0, 5, 2}, {3, 3, 5, 5}, {3, 6, 5, 9},
            {6, 0, 8, 2}, {6, 3, 8, 5}, {6, 6, 8, 9},
            {9, 0, 9, 2}, {9, 3, 9, 5}, {9, 6, 9, 9}
        };

        // Prüfen in vordefinierter Reihenfolge
        int[] result = processSquares(squares, true, true);
        if (!isInvalidMove(result)) return result;

        result = processSquares(squares, true, false);
        if (!isInvalidMove(result)) return result;

        result = processSquares(squares, false, true);
        if (!isInvalidMove(result)) return result;

        result = processSquares(squares, false, false);
        if (!isInvalidMove(result)) return result;

        // Fallback
        return findFallbackMove();
    }

    protected int[] processSquares(int[][] squares, boolean useRowStart, boolean useColStart) {
            if(useRowStart && !useColStart){
                int[][] restField = {{2,8},{4,8},{7,8}};
                for(int i = 0; i < restField.length; i++){
                    int row = restField[i][0];
                    int col = restField[i][1];
                    if(isValidMove(row,col)){
                        return new int[]{row,col};
                    }
               }
            }
        for (int[] square : squares) {
            int row = useRowStart ? square[0] : square[2];
            int col = useColStart ? square[1] : square[3];
            if (isValidMove(row, col)) {
                return new int[]{row, col};
            }
        }
        return new int[]{-1, -1};
    }

    protected int[] findFallbackMove() {
        for (int row = 9; row >= 0; row--) {
            for (int col = 9; col >= 0; col--) {
                if (isValidMove(row, col)) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{-1, -1};
    }

    protected boolean isInvalidMove(int[] move) {
        return move[0] == -1 && move[1] == -1;
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



class AIDifficult extends AIMedium {

    AIDifficult(GameField gameField) {
        super(gameField);
    }

    @Override
    protected void placeAIShips() {
        Random random = new Random();

        for (int i = 0; i < gameField.shipLengths.length; i++) {
            int length = gameField.shipLengths[i];
            boolean success = false;

            while (!success) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Prüfen, ob das Schiff platziert werden kann und keine Nachbarschaftsverletzung vorliegt
                if (canPlaceWithoutNeighbors(xStart, yStart, length, horizontal)) {
                    success = gameField.placeShip(gameField.aiField, xStart, yStart, length, horizontal);

                    if (success) {
                        Ship newShip = new Ship(xStart, yStart, length, horizontal);
                        gameField.aiShips.add(newShip);
                    }
                }
            }
        }
        System.out.println("Computer hat seine vier Schiffe auch platziert . Sie koennen jetzt spielen!");
        aiShipsPlaced = true;
    }

    @Override
    protected int[] findHeuristicMove() {
        int[][] squares = {
            {0, 0, 2, 2}, {0, 3, 2, 5}, {0, 6, 2, 8},
            {3, 0, 5, 2}, {3, 3, 5, 5}, {3, 6, 5, 8},
            {6, 0, 8, 2}, {6, 3, 8, 5}, {6, 6, 8, 8},
            {9, 0, 9, 2}, {9, 3, 9, 5}, {9, 6, 9, 8}
        };

        List<int[]> squareList = new ArrayList<>();
        Collections.addAll(squareList, squares);// Konvertiere das Array in eine Liste, um die Reihenfolge zu mischen
        Collections.shuffle(squareList);// Zufällige Reihenfolge für die  Schleife
        for (int[] square : squareList) {
            int middleRow = (square[0] + square[2]) / 2;
            int middleCol = (square[1] + square[3]) / 2;
            if (isValidMove(middleRow, middleCol)) {
                return new int[]{middleRow, middleCol};
            }
        }

        allMiddleFieldsHit = true; 
        return findCornerMove(); 
    }


    @Override
    protected int[] findCornerMove() {
        List<int[]> squares = defineSquares();

        int[] result = processSquares(squares, true, true);
        if (!isInvalidMove(result)) return result;

        result = processSquares(squares, true, false);
        if (!isInvalidMove(result)) return result;

        result = processSquares(squares, false, true);
        if (!isInvalidMove(result)) return result;

        result = processSquares(squares, false, false);
        if (!isInvalidMove(result)) return result;

        return findFallbackMove();
    }

    protected List<int[]> defineSquares() {
        // Definiere die Quadrate
       // Erstelle eine veränderliche Liste
        return new ArrayList<>(List.of(
            new int[]{0, 0, 2, 2}, new int[]{0, 3, 2, 5}, new int[]{0, 6, 2, 9},
            new int[]{3, 0, 5, 2}, new int[]{3, 3, 5, 5}, new int[]{3, 6, 5, 9},
            new int[]{6, 0, 8, 2}, new int[]{6, 3, 8, 5}, new int[]{6, 6, 8, 9},
            new int[]{9, 0, 9, 2}, new int[]{9, 3, 9, 5}, new int[]{9, 6, 9, 9}
        ));
        }

    protected int[] processSquares(List<int[]> squares, boolean useRowStart, boolean useColStart) {
            if(useRowStart && !useColStart){
                List<int[]> restField = new ArrayList<>(List.of(new int[]{2,8},new int[]{4,8},new int[]{7,8}));
                Collections.shuffle(restField);
                for(int i = 0; i < restField.size(); i++){
                    int row = restField.get(i)[0];
                    int col = restField.get(i)[1];
                    if(isValidMove(row,col)){
                        return new int[]{row,col};
                    }
               }
            }

        Collections.shuffle(squares);
        for (int[] square : squares) {
            int row = useRowStart ? square[0] : square[2];
            int col = useColStart ? square[1] : square[3];
            if (isValidMove(row, col) && checkNeighbors(row, col)) {
                return new int[]{row, col};
            }
        }
        return new int[]{-1,-1};
    }
    @Override
    protected int[] findFallbackMove() {
        for (int row = 9; row >= 0; row--) {
            for (int col = 9; col >= 0; col--) {
                if (isValidMove(row, col) && checkNeighbors(row, col)) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{-1, -1};
    }



    protected boolean checkNeighbors(int row, int column) {
        // Grenzprüfungen
        boolean upValid = row > 0 && visibleFieldAI[row - 1][column] == '~';
        boolean downValid = row < 9 && visibleFieldAI[row + 1][column] == '~';
        boolean leftValid = column > 0 && visibleFieldAI[row][column - 1] == '~';
        boolean rightValid = column < 9 && visibleFieldAI[row][column + 1] == '~';

    return upValid || downValid || leftValid || rightValid;
    }


    private boolean canPlaceWithoutNeighbors(int xStart, int yStart, int length, boolean horizontal) {
        // Prüfen, ob das Schiff innerhalb der Spielfeldgrenzen bleibt
        if (horizontal) {
            if (yStart + length > 10) return false;
        } else {
            if (xStart + length > 10) return false;
        }

        // Prüfen, ob die Felder um das Schiff herum frei sind
        int xMin = Math.max(0, xStart - 1);
        int xMax = Math.min(9, horizontal ? xStart + 1 : xStart + length);
        int yMin = Math.max(0, yStart - 1);
        int yMax = Math.min(9, horizontal ? yStart + length : yStart + 1);

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                if (gameField.aiField[x][y] != '~') {
                    return false;
                }
            }
        }

        return true;
    }
}




class AIEasy extends AIMedium {

    AIEasy(GameField gameField){
        super(gameField);
    }

    @Override
    protected int[] findHeuristicMove() {
        Random random = new Random();
        int row, column;

        do {
            row = random.nextInt(10);
            column = random.nextInt(10);
        } while (!isValidMove(row, column)); // Wiederholen, bis ein gültiger Zug gefunden wird

        return new int[]{row, column};
    }

    @Override
    protected int[] findCornerMove() {
        return findHeuristicMove();
    }
}