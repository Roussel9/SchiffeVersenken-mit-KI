
import java.util.Optional;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

record Ship(int row, int column, int length, boolean horizontal) {
}

public interface GameLogic {
    boolean placePlayerShip(int row, int column, boolean horizontal);

    void shootPlayer(int row, int column);

    void checkAndMarkDestroyedShipsAI();

    void checkAndMarkDestroyedShipsPlayer();

    String checkWinner();

    void startNewGame();
}

public interface GameVisualization {
    void drawGameField(String playerType);

    void drawStartScreen();

    void drawHit(int row, int column, String field);

    void drawMiss(int row, int column, String field);

    void drawRedLine(Ship ship, String field);

    void placeShipTurtle(int row, int col);

    void visibleShipsPlayerAfterGameOver();
}

public class Game implements GameLogic, GameVisualization {
    Turtle turtle = new Turtle(500, 600);
    AIMedium ai;
    public char[] playerField;
    public char[] aiField;
    public List<Ship> playerShips;
    public List<Ship> aiShips;
    public List<Integer> shipLengths = new ArrayList<>(List.of(2, 3, 4, 5));
    public int placedPlayerShips = 0;
    public int playerMoves = 0;
    public boolean gameOver = false;

    public Game() {
        aiField = new char[100];
        playerField = new char[100];
        playerShips = new ArrayList<>();
        aiShips = new ArrayList<>();

        Arrays.fill(aiField, '~');
        Arrays.fill(playerField, '~');

        drawStartScreen();
        turtle.reset();
        drawGameField("Player");
        drawGameField("AI");
    }

    public void drawGameField(String playerType) {
        int x = 150;
        int y = playerType.equals("Player") ? 80 : 305;
        turtle.textSize = 20;
        turtle.left(90).moveTo(220, y).color(50, 205, 50);
        turtle.text(playerType.equals("Player") ? "Player" : "Computer");
        turtle.right(90);
        turtle.color(0, 0, 255);
        y += 25;
        turtle.moveTo(x, y);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 4; k++) {
                    turtle.forward(15).right(90);
                }
                turtle.penUp().forward(15).penDown();
            }
            y += 15;
            turtle.penUp().moveTo(x, y).penDown();
        }
        turtle.color(0, 0, 0);
    }

    public void drawStartScreen() {
        turtle.left(90);
        turtle.moveTo(250, 100);
        turtle.textSize = 30;
        turtle.color(0, 0, 255);
        turtle.text("Willkommen zu Schiffe Versenken!");
        turtle.textSize = 20;
        turtle.moveTo(250, 250);
        turtle.text("Bitte wählen Sie einen Schwierigkeitsgrad:");
        turtle.textSize = 15;
        turtle.moveTo(250, 300);
        turtle.text("1 - Einfach");
        turtle.moveTo(250, 320);
        turtle.text("2 - Mittel");
        turtle.moveTo(250, 340);
        turtle.text("3 - Schwer");
        turtle.color(0, 0, 0);
        turtle.right(90);

        Scanner scanner = new Scanner(System.in);
        int difficulty = 0;
        while (difficulty < 1 || difficulty > 3) {
            System.out.print("Bitte waehlen Sie einen Schwierigkeitsgrad (1-3): ");
            if (scanner.hasNextInt()) {
                difficulty = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        switch (difficulty) {
            case 1 -> {
                ai = new AIEasy(this);
                System.out.println("Schwierigkeitsgrad 'Einfach' gewaehlt.");
            }
            case 2 -> {
                ai = new AIMedium(this);
                System.out.println("Schwierigkeitsgrad 'Mittel' gewaehlt.");
            }
            case 3 -> {
                ai = new AIDifficult(this);
                System.out.println("Schwierigkeitsgrad 'Schwer' gewaehlt.");
            }

        }

    }

    public int toIndex(int row, int column) {
        return row * 10 + column;
    }

    public boolean placePlayerShip(int row, int column, boolean horizontal) {
        assert row >= 0 && row <= 9 && column >= 0 && column <= 9 : "Schiffe muessen innerhalb des Spielfelds liegen!";
        if (placedPlayerShips >= shipLengths.size())
            throw new IllegalArgumentException("Alle Schiffe wurden bereits platziert.");
        int length = shipLengths.get(placedPlayerShips);
        if (placeShip(playerField, row, column, length, horizontal)) {
            playerShips.add(new Ship(row, column, length, horizontal));
            placedPlayerShips++;
            if (placedPlayerShips == shipLengths.size() && !ai.aiShipsPlaced) {
                ai.placeAIShips();
            }
            return true;
        }
        return false;
    }

    public boolean placeShip(char[] field, int startX, int startY, int length, boolean horizontal) {
        if (horizontal) {
            if (startY + length > 10)
                return false;
            for (int i = 0; i < length; i++) {
                if (field[toIndex(startX, startY + i)] != '~')
                    return false;
            }

            IntStream.range(0, length).forEach(i -> {
                field[toIndex(startX, startY + i)] = 'S';
                placeShipTurtle(startX, startY + i);
            });
        } else {
            if (startX + length > 10)
                return false;
            for (int i = 0; i < length; i++) {
                if (field[toIndex(startX + i, startY)] != '~')
                    return false;
            }

            IntStream.range(0, length).forEach(i -> {
                field[toIndex(startX + i, startY)] = 'S';
                placeShipTurtle(startX + i, startY);
            });
        }
        return true;
    }

    public void placeShipTurtle(int row, int col) {
        int startX, startY;
        if (placedPlayerShips == shipLengths.size() && !ai.aiShipsPlaced) {
            startX = 150;
            startY = 330;
            turtle.color(173, 216, 230);
        } else {
            startX = 150;
            startY = 105;
        }
        if (gameOver) {
            startX = 150;
            startY = 330;
            turtle.color(0, 0, 0);
        }
        turtle.textSize = 10;
        int squareLength = 15;
        int targetX = startX + (col * squareLength);
        int targetY = startY + (row * squareLength);
        turtle.penUp();
        turtle.moveTo(targetX, targetY);
        turtle.penDown();
        int textX = targetX + squareLength / 2;
        int textY = targetY + squareLength / 2;
        turtle.penUp();
        turtle.right(90);
        turtle.moveTo(textX, textY);
        turtle.penDown();
        turtle.text("S");
        turtle.left(90);
        turtle.color(0, 0, 0);
    }

    public void shootPlayer(int row, int column) {
        assert row >= 0 && row <= 9 && column >= 0 && column <= 9 : "Position muss im Spielfeld sein.";
        if (gameOver)
            throw new IllegalArgumentException("Das Spiel ist bereits beendet. Keine Zuege mehr moeglich.");
        if (aiShips.isEmpty()) {
            throw new IllegalArgumentException("Es gibt kein Schiff auf dem Spielfeld");
        }

        if (playerMoves >= 3) {
            System.out.println("Sie haben bereits 3 Zuege gemacht. Nun ist Computer dran.");
            ai.makeAIMoves();
            return;
        }

        if (aiField[toIndex(row, column)] == 'X' || aiField[toIndex(row, column)] == '0') {
            throw new IllegalArgumentException("Sie haben bereits auf diese Position geschossen! Wähle eine andere.");
        }

        if (aiField[toIndex(row, column)] == 'S') {
            System.out.println("Treffer!");
            aiField[toIndex(row, column)] = 'X';
            playerMoves++;
            drawHit(row, column, "AI");
            checkAndMarkDestroyedShipsAI();
            String winner = checkWinner();
            if (winner != null) {
                System.out.println(winner);
            }
        } else {
            System.out.println("Fehlschuss!");
            aiField[toIndex(row, column)] = '0';
            drawMiss(row, column, "AI");
            playerMoves++;
        }

        if (playerMoves == 3 && !gameOver) {
            System.out.println("Sie haben 3 Zuege gemacht. Jetzt ist Computer dran!");
            ai.makeAIMoves();
        }
    }

    public void checkAndMarkDestroyedShipsAI() {
        for (int i = 0; i < aiShips.size(); i++) {
            Ship ship = aiShips.get(i);
            if (isShipDestroyed(ship, aiField)) {
                System.out.println("Ein Schiff des Computers komplett zerstoert");
                drawRedLine(ship, "AI");
                aiShips.remove(i);
                i--;
            }
        }
    }

    public void checkAndMarkDestroyedShipsPlayer() {
        for (int j = 0; j < playerShips.size(); j++) {
            Ship ship = playerShips.get(j);
            if (isShipDestroyed(ship, playerField)) {
                if (ship.length() < ai.lastHits.size()) {
                    int startRow = ship.row();
                    int startCol = ship.column();
                    if (ship.horizontal()) {
                        int endCol = startCol + ship.length() - 1;
                        for (int i = startCol; i <= endCol; i++) {
                            removeHitFromList(ai.lastHits, startRow, i);
                        }
                    } else {
                        int endRow = startRow + ship.length() - 1;
                        for (int i = startRow; i <= endRow; i++) {
                            removeHitFromList(ai.lastHits, i, startCol);
                        }

                    }
                } else {
                    ai.lastHits.clear();
                }

                System.out.println("Ein Schiff des Spielers komplett zerstört");
                drawRedLine(ship, "Player");
                ai.shipDirectionFound = false;
                ai.isHorizontal = false;
                shipLengths.remove(Integer.valueOf(ship.length()));
                playerShips.remove(ship);
                j--;
            }
        }
    }

    public boolean isShipDestroyed(Ship ship, char[] field) {
        return IntStream.range(0, ship.length())
                .allMatch(i -> field[toIndex(
                        ship.horizontal() ? ship.row() : ship.row() + i,
                        ship.horizontal() ? ship.column() + i : ship.column())] == 'X');
    }

    public void removeHitFromList(List<int[]> list, int row, int column) {
        list.removeIf(hit -> hit[0] == row && hit[1] == column);
    }

    public void drawHit(int row, int column, String field) {
        int x = 0, y = 0;
        if (field.equals("AI")) {
            x = 150;
            y = 330;
        }
        if (field.equals("Player")) {
            x = 150;
            y = 105;
        }
        int xStart = x + (column * 15);
        int yStart = y + (row * 15);

        turtle.penUp();
        turtle.moveTo(xStart, yStart);
        turtle.right(45);
        turtle.penDown();
        turtle.color(255, 0, 0);
        turtle.forward(20);
        turtle.penUp().right(135).forward(15).right(135).penDown();
        turtle.forward(20);
        turtle.color(0, 0, 0);
        turtle.right(45);
    }

    public void drawMiss(int row, int column, String field) {
        int x = 0, y = 0;
        if (field.equals("AI")) {
            x = 150;
            y = 330;
        }
        if (field.equals("Player")) {
            x = 150;
            y = 105;
        }

        turtle.textSize = 10;
        int xStart = x + (column * 15);
        int yStart = y + (row * 15);

        turtle.penUp();
        turtle.moveTo(xStart, yStart);
        turtle.forward(7.5).right(90).forward(7.5);
        turtle.penDown();
        turtle.color(255, 105, 180);
        turtle.text("0");
        turtle.color(0, 0, 0);
        turtle.left(90);
    }

    public void drawRedLine(Ship ship, String field) {
        double x = 0, y = 0;
        if (field.equals("AI")) {
            x = 150;
            y = 330;
        }
        if (field.equals("Player")) {
            x = 150;
            y = 105;
        }
        double startX, startY, endX, endY;
        double squareLength = 15;

        if (ship.horizontal()) {
            startX = x + (ship.column() * squareLength);
            startY = y + (ship.row() * squareLength) + 7.5;
            endX = startX + (ship.length() * squareLength);
            endY = startY;
        } else {
            startX = x + (ship.column() * squareLength) + 7.5;
            startY = y + (ship.row() * squareLength);
            endX = startX;
            endY = startY + (ship.length() * squareLength);
        }

        turtle.penUp();
        turtle.moveTo(startX, startY);
        turtle.penDown();
        turtle.color(255, 0, 0);
        turtle.lineTo(endX, endY);
        turtle.color(0, 0, 0);
    }

    public String checkWinner() {
        boolean playerHasShips = hasShips(playerField);
        boolean aiHasShips = hasShips(aiField);

        if (!playerHasShips) {
            turtle.left(90);
            turtle.textSize = 30;
            turtle.moveTo(250, 520);
            turtle.color(255, 0, 0);
            turtle.text("Computer hat gewonnen!");
            turtle.right(90);
            System.out.println("Computer hat gewonnen!");
            gameOver = true;
            visibleShipsPlayerAfterGameOver();

            promptNewGame();
            return "";
        } else if (!aiHasShips) {
            turtle.left(90);
            turtle.textSize = 30;
            turtle.moveTo(250, 520);
            turtle.color(50, 205, 50);
            turtle.text("Sie haben gewonnen!");
            turtle.right(90);
            System.out.println("Sie haben gewonnen!");
            gameOver = true;
            promptNewGame();
            return "";
        }
        return null;
    }

    boolean hasShips(char[] field) {
        return IntStream.range(0, field.length)
                .anyMatch(i -> field[i] == 'S');
    }

    public void visibleShipsPlayerAfterGameOver() {
        for (int i = 0; i < aiShips.size(); i++) {
            Ship restShip = aiShips.get(i);
            for (int j = 0; j < restShip.length(); j++) {
                if (restShip.horizontal()) {
                    placeShipTurtle(restShip.row(), restShip.column() + j);
                } else {
                    placeShipTurtle(restShip.row() + j, restShip.column());
                }
            }
        }
    }

    public void promptNewGame() {
        Scanner scanner = new Scanner(System.in);
        turtle.moveTo(250, 540);
        turtle.textSize = 12;
        turtle.left(90);
        turtle.color(50, 205, 50);
        turtle.text("Geben Sie 1 ein, um ein neues Spiel zu starten, oder 2, um das Spiel zu beenden.");
        turtle.right(90);
        System.out.println("Geben Sie 1 ein, um ein neues Spiel zu starten, oder 2, um das Spiel zu beenden.");

        String response = scanner.nextLine();

        switch (response) {
            case "1" -> {
                System.out.println("Neues Spiel wird gestartet...");
                startNewGame();
            }
            case "2" -> {
                System.out.println("Das Spiel wird beendet.");
                return;
            }
            default -> {
                System.out.println("Ungültige Eingabe. Bitte geben Sie 1 oder 2 ein.");
                promptNewGame();
            }
        }

    }

    public void startNewGame() {
        turtle.reset();

        Arrays.fill(aiField, '~');
        Arrays.fill(playerField, '~');
        placedPlayerShips = 0;
        ai.aiShipsPlaced = false;
        playerMoves = 0;
        gameOver = false;
        playerShips.clear();
        aiShips.clear();
        shipLengths.clear();
        shipLengths = new ArrayList<>(List.of(2, 3, 4, 5));
        drawStartScreen();
        turtle.reset();
        drawGameField("Player");
        drawGameField("AI");

        ai.visibleFieldAI = new char[100];
        Arrays.fill(ai.visibleFieldAI, '~');
        ai.lastHits.clear();
    }

}

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

//public final class AIDifficult extends AIMedium {
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

//public final class AIEasy extends AIMedium {
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