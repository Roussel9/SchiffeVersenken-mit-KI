import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

//public interface AIPlayer {
//  void placeAIShips(); 
//  void makeAIMoves(); 
//}

//sealed interface AIPlayer permits AIMedium, AIDifficult, AIEasy {
//  void placeAIShips();
//   void makeAIMoves();  
//}

//public sealed class AIMedium  permits AIDifficult, AIEasy {
public class AIMedium {
    public char[] visibleFieldAI;
    public Game game;
    public boolean aiShipsPlaced = false;
    public List<int[]> lastHits = new ArrayList<>();
    public boolean shipDirectionFound = false;
    public boolean isHorizontal = false;
    public boolean allMiddleFieldsHit = false;
    public List<int[]> lastHitsCopy = new ArrayList<>(lastHits);

    AIMedium(Game game) {
        this.game = game;

        visibleFieldAI = new char[100];
        Arrays.fill(visibleFieldAI, '~');
        game.playerMoves = 0;
    }

    public void placeAIShips() {
        Random random = new Random();

        for (int i = 0; i < game.shipLengths.size(); i++) {
            int length = game.shipLengths.get(i);
            boolean success = false;

            while (!success) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                success = game.placeShip(game.aiField, xStart, yStart, length, horizontal);

                if (success) {
                    Ship newShip = new Ship(xStart, yStart, length, horizontal);
                    game.aiShips.add(newShip);
                }
            }
        }
        System.out.println("Computer hat seine vier Schiffe auch platziert . Sie koennen jetzt spielen!");
        aiShipsPlaced = true;
    }

    public void makeAIMoves() {
        if (game.gameOver)
            return;

        for (int i = 0; i < 3; i++) {
            if (game.playerShips.isEmpty()) {
                return;
            }
            Optional<int[]> move = findBestMove();

            if (move.isPresent()) {
                int[] position = move.get();
                int row = position[0];
                int col = position[1];
                if (isValidMove(row, col)) {
                    makeAIShot(row, col);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (!game.gameOver && aiShipsPlaced) {
            System.out.println("Der Computer hat schon seine 3 Zuege gemacht . Jetzt sind Sie dran!");
        }
        game.playerMoves = 0;
    }

    public void makeAIShot(int row, int col) {
        if (game.playerField[game.toIndex(row, col)] == 'S') { // Hit
            System.out.println("KI Treffer bei (" + row + ", " + col + ")");
            visibleFieldAI[game.toIndex(row, col)] = 'X';
            game.playerField[game.toIndex(row, col)] = 'X';
            game.drawHit(row, col, "Player");
            lastHits.add(new int[] { row, col });
            game.checkAndMarkDestroyedShipsPlayer();
            if (lastHits.size() > 1) {
                shipDirectionFound = true;
                isHorizontal = lastHits.get(0)[0] == lastHits.get(1)[0];
            }

            String winner = game.checkWinner();
            if (winner != null) {
                System.out.println(winner);
            }

        } else {
            System.out.println("KI Fehlschuss bei (" + row + ", " + col + ")");
            visibleFieldAI[game.toIndex(row, col)] = 'O';
            game.playerField[game.toIndex(row, col)] = 'O';
            game.drawMiss(row, col, "Player");
        }
    }

    public Optional<int[]> findBestMove() {
        if (!shipDirectionFound)
            return shootAtNeighbor();

        if (!lastHits.isEmpty())
            return findTargetedMove();
        if (!allMiddleFieldsHit)
            return findHeuristicMove();
        return findCornerMove();
    }

    public Optional<int[]> findTargetedMove() {
        if (!lastHits.isEmpty()) {
            int[] firstHit = lastHits.get(0);

            if (shipDirectionFound) {
                return isHorizontal ? shootHorizontalFurther() : shootVerticalFurther();
            }

            for (int[] direction : new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } }) {
                int newRow = firstHit[0] + direction[0];
                int newCol = firstHit[1] + direction[1];

                if (newRow >= 0 && newRow < 10 && newCol >= 0 && newCol < 10 &&
                        isValidMove(newRow, newCol)) {
                    return Optional.of(new int[] { newRow, newCol });
                }
            }
        }

        return findHeuristicMove();
    }

    public Optional<int[]> findHeuristicMove() {
        int[][] squares = {
                { 0, 0, 2, 2 }, { 0, 3, 2, 5 }, { 0, 6, 2, 8 },
                { 3, 0, 5, 2 }, { 3, 3, 5, 5 }, { 3, 6, 5, 8 },
                { 6, 0, 8, 2 }, { 6, 3, 8, 5 }, { 6, 6, 8, 8 },
                { 9, 0, 9, 2 }, { 9, 3, 9, 5 }, { 9, 6, 9, 8 }
        };

        for (int[] square : squares) {
            int middleRow = (square[0] + square[2]) / 2;
            int middleCol = (square[1] + square[3]) / 2;
            if (isValidMove(middleRow, middleCol)) {
                return Optional.of(new int[] { middleRow, middleCol });
            }
        }

        allMiddleFieldsHit = true;
        return findCornerMove();
    }

    public Optional<int[]> findCornerMove() {
        int[][] squares = {
                { 0, 0, 2, 2 }, { 0, 3, 2, 5 }, { 0, 6, 2, 9 },
                { 3, 0, 5, 2 }, { 3, 3, 5, 5 }, { 3, 6, 5, 9 },
                { 6, 0, 8, 2 }, { 6, 3, 8, 5 }, { 6, 6, 8, 9 },
                { 9, 0, 9, 2 }, { 9, 3, 9, 5 }, { 9, 6, 9, 9 }
        };

        Optional<int[]> result = processSquares(squares, true, true);
        if (result.isPresent())
            return result;

        result = processSquares(squares, true, false);
        if (result.isPresent())
            return result;

        result = processSquares(squares, false, true);
        if (result.isPresent())
            return result;

        result = processSquares(squares, false, false);
        if (result.isPresent())
            return result;

        return findFallbackMove();
    }

    public Optional<int[]> findFallbackMove() {
        for (int row = 9; row >= 0; row--) {
            for (int col = 9; col >= 0; col--) {
                if (isValidMove(row, col) && checkNeighbors(row, col)) {
                    return Optional.of(new int[] { row, col }); // Rückgabe als Optional
                }
            }
        }
        return Optional.empty(); // Rückgabe von Optional.empty, wenn keine gültige Bewegung gefunden wurde
    }

    public Optional<int[]> shootHorizontalFurther() {
        int[] firstHit = lastHits.get(0);
        int[] lastHitPosition = lastHits.get(lastHits.size() - 1);

        int rightCol = lastHitPosition[1] + 1;
        if (rightCol < 10 && isValidMove(firstHit[0], rightCol)) {
            return Optional.of(new int[] { firstHit[0], rightCol });
        }

        int minCol = lastHits.stream().mapToInt(hit -> hit[1]).min().orElse(-1);
        int leftCol = minCol - 1;
        if (leftCol >= 0 && isValidMove(firstHit[0], leftCol)) {
            return Optional.of(new int[] { firstHit[0], leftCol });
        }

        if (shipDirectionFound) {
            lastHitsCopy = lastHits;
            return shootAtNeighbor();
        }

        return findHeuristicMove();
    }

    public Optional<int[]> shootVerticalFurther() {
        int[] firstHit = lastHits.get(0);
        int[] lastHitPosition = lastHits.get(lastHits.size() - 1);

        int downRow = lastHitPosition[0] + 1;
        if (downRow < 10 && isValidMove(downRow, firstHit[1])) {
            return Optional.of(new int[] { downRow, firstHit[1] });
        }

        int minRow = lastHits.stream().mapToInt(hit -> hit[0]).min().orElse(-1);
        int upRow = minRow - 1;
        if (upRow >= 0 && isValidMove(upRow, firstHit[1])) {
            return Optional.of(new int[] { upRow, firstHit[1] });

        }

        if (shipDirectionFound) {
            lastHitsCopy = lastHits;
            return shootAtNeighbor();
        }

        // Fallback
        return findHeuristicMove();
    }

    public Optional<int[]> shootAtNeighbor() {
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

    public Optional<int[]> processSquares(int[][] squares, boolean useRowStart, boolean useColStart) {
        if (useRowStart && !useColStart) {
            int[][] restField = { { 2, 8 }, { 4, 8 }, { 7, 8 } };
            for (int i = 0; i < restField.length; i++) {
                int row = restField[i][0];
                int col = restField[i][1];
                if (isValidMove(row, col) && checkNeighbors(row, col)) {
                    return Optional.of(new int[] { row, col }); // Rückgabe als Optional
                }
            }
        }

        for (int[] square : squares) {
            int row = useRowStart ? square[0] : square[2];
            int col = useColStart ? square[1] : square[3];
            if (isValidMove(row, col)) {
                return Optional.of(new int[] { row, col }); // Rückgabe als Optional
            }
        }

        return Optional.empty(); // Rückgabe von Optional.empty, wenn keine gültige Bewegung gefunden wurde
    }

    public boolean checkNeighbors(int row, int column) {
        // Grenzprüfungen
        boolean upValid = row > 0 && visibleFieldAI[game.toIndex(row - 1, column)] == '~';
        boolean downValid = row < 9 && visibleFieldAI[game.toIndex(row + 1, column)] == '~';
        boolean leftValid = column > 0 && visibleFieldAI[game.toIndex(row, column - 1)] == '~';
        boolean rightValid = column < 9 && visibleFieldAI[game.toIndex(row, column + 1)] == '~';

        return upValid || downValid || leftValid || rightValid;
    }

    public boolean isValidMove(int row, int col) {
        return visibleFieldAI[game.toIndex(row, col)] == '~';
    }

}

// public final class AIDifficult extends AIMedium {
class AIDifficult extends AIMedium {

    AIDifficult(Game game) {
        super(game);
    }

    @Override
    public void placeAIShips() {
        Random random = new Random();

        for (int i = 0; i < game.shipLengths.size(); i++) {
            int length = game.shipLengths.get(i);
            boolean success = false;

            while (!success) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();
                if (canPlaceWithoutNeighbors(xStart, yStart, length, horizontal)) {
                    success = game.placeShip(game.aiField, xStart, yStart, length, horizontal);

                    if (success) {
                        Ship newShip = new Ship(xStart, yStart, length, horizontal);
                        game.aiShips.add(newShip);
                    }
                }
            }
        }
        System.out.println("Computer hat seine vier Schiffe auch platziert . Sie koennen jetzt spielen!");
        aiShipsPlaced = true;
    }

    @Override
    public Optional<int[]> findHeuristicMove() {
        int[][] squares = {
                { 0, 0, 2, 2 }, { 0, 3, 2, 5 }, { 0, 6, 2, 8 },
                { 3, 0, 5, 2 }, { 3, 3, 5, 5 }, { 3, 6, 5, 8 },
                { 6, 0, 8, 2 }, { 6, 3, 8, 5 }, { 6, 6, 8, 8 },
                { 9, 0, 9, 2 }, { 9, 3, 9, 5 }, { 9, 6, 9, 8 }
        };

        List<int[]> squareList = new ArrayList<>();
        Collections.addAll(squareList, squares);
        Collections.shuffle(squareList);
        for (int[] square : squareList) {
            int middleRow = (square[0] + square[2]) / 2;
            int middleCol = (square[1] + square[3]) / 2;
            if (isValidMove(middleRow, middleCol)) {
                return Optional.of(new int[] { middleRow, middleCol });
            }
        }

        allMiddleFieldsHit = true;
        return findCornerMove();
    }

    @Override
    public Optional<int[]> findCornerMove() {
        List<int[]> squares = defineSquares();

        Optional<int[]> result = processSquares(squares, true, true);
        if (result.isPresent())
            return result;

        result = processSquares(squares, true, false);
        if (result.isPresent())
            return result;

        result = processSquares(squares, false, true);
        if (result.isPresent())
            return result;

        result = processSquares(squares, false, false);
        if (result.isPresent())
            return result;

        return findFallbackMove();
    }

    public List<int[]> defineSquares() {
        return new ArrayList<>(List.of(
                new int[] { 0, 0, 2, 2 }, new int[] { 0, 3, 2, 5 }, new int[] { 0, 6, 2, 9 },
                new int[] { 3, 0, 5, 2 }, new int[] { 3, 3, 5, 5 }, new int[] { 3, 6, 5, 9 },
                new int[] { 6, 0, 8, 2 }, new int[] { 6, 3, 8, 5 }, new int[] { 6, 6, 8, 9 },
                new int[] { 9, 0, 9, 2 }, new int[] { 9, 3, 9, 5 }, new int[] { 9, 6, 9, 9 }));
    }

    public Optional<int[]> processSquares(List<int[]> squares, boolean useRowStart, boolean useColStart) {
        if (useRowStart && !useColStart) {
            List<int[]> restField = new ArrayList<>(
                    List.of(new int[] { 2, 8 }, new int[] { 4, 8 }, new int[] { 7, 8 }));
            Collections.shuffle(restField);
            for (int i = 0; i < restField.size(); i++) {
                int row = restField.get(i)[0];
                int col = restField.get(i)[1];
                if (isValidMove(row, col)) {
                    return Optional.of(new int[] { row, col });
                }
            }
        }

        Collections.shuffle(squares);
        for (int[] square : squares) {
            int row = useRowStart ? square[0] : square[2];
            int col = useColStart ? square[1] : square[3];
            if (isValidMove(row, col) && canShipHere(row, col)) {
                return Optional.of(new int[] { row, col });
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<int[]> findFallbackMove() {
        for (int row = 9; row >= 0; row--) {
            for (int col = 9; col >= 0; col--) {
                if (isValidMove(row, col) && canShipHere(row, col)) {
                    return Optional.of(new int[] { row, col });
                }
            }
        }
        return Optional.empty();
    }

    public boolean canPlaceWithoutNeighbors(int xStart, int yStart, int length, boolean horizontal) {
        if (horizontal) {
            if (yStart + length > 10)
                return false;
        } else {
            if (xStart + length > 10)
                return false;
        }
        int xMin = Math.max(0, xStart - 1);
        int xMax = Math.min(9, horizontal ? xStart + 1 : xStart + length);
        int yMin = Math.max(0, yStart - 1);
        int yMax = Math.min(9, horizontal ? yStart + length : yStart + 1);

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                if (game.aiField[game.toIndex(x, y)] != '~') {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean canShipHere(int row, int col) {
        List<Integer> counter = new ArrayList<>();
        int counterHorizontal = 1, counterVertical = 1;
        for (int forward = col + 1; forward < 10; forward++) {
            if (isValidMove(row, forward)) {
                counterHorizontal++;
            } else {
                break;
            }
        }
        for (int backward = col - 1; backward >= 0; backward--) {
            if (isValidMove(row, backward)) {
                counterHorizontal++;
            } else {
                break;
            }
        }

        for (int upRow = row + 1; upRow < 10; upRow++) {
            if (isValidMove(upRow, col)) {
                counterVertical++;
            } else {
                break;
            }
        }

        for (int downRow = row - 1; downRow >= 0; downRow--) {
            if (isValidMove(downRow, col)) {
                counterVertical++;
            } else {
                break;
            }
        }

        counter.addAll(Arrays.asList(counterHorizontal, counterVertical));
        return canShipFit(counter);
    }

    public boolean canShipFit(List<Integer> counter) {
        for (int shipLength : game.shipLengths) {
            if (shipLength <= counter.get(0) || shipLength <= counter.get(1)) {
                return true;
            }
        }
        return false;
    }

}

// public final class AIEasy extends AIMedium {
class AIEasy extends AIMedium {

    AIEasy(Game game) {
        super(game);
    }

    @Override
    public Optional<int[]> findFallbackMove() {
        for (int row = 9; row >= 0; row--) {
            for (int col = 9; col >= 0; col--) {
                if (isValidMove(row, col)) {
                    return Optional.of(new int[] { row, col });
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<int[]> processSquares(int[][] squares, boolean useRowStart, boolean useColStart) {
        for (int[] square : squares) {
            int row = useRowStart ? square[0] : square[2];
            int col = useColStart ? square[1] : square[3];
            if (isValidMove(row, col)) {
                return Optional.of(new int[] { row, col });
            }
        }
        return Optional.empty();
    }

}