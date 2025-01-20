
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {
    Turtle turtle = new Turtle(500,600);
    AIMedium ai;
    public char[] playerField;  // 1D array for player's game field (10x10)
    public char[] aiField;       // 1D array for AI's game field (10x10)
    public List<Ship> playerShips;  // List for player's ships
    public List<Ship> aiShips;  // List for AI's ships
    public List<Integer> shipLengths = new ArrayList<>(List.of(2,3,4,5));
   // public int[] shipLengths = {2, 3, 4, 5};  // Lengths of the ships
    public int placedPlayerShips = 0;  // Number of placed player ships
    public int playerMoves = 0; // Counts the player's moves in a round
    public boolean gameOver = false;
    //public List<Ship> destroyedShipPlayer;

    // Constructor: Creates and initializes the game fields and ship list
    public GameField() {
        aiField = new char[100];
        playerField = new char[100];
        playerShips = new ArrayList<>();
        aiShips = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
           // for (int j = 0; j < 10; j++) {
                aiField[i] = '~';  // "~" for water
                playerField[i] = '~';   // "~" for water
            //}
        }
       // ai = new AIMedium(this);  // New AI object is created here
        drawStartScreen();

        // Delay for 10 seconds
       // try {
       //     Thread.sleep(5000); // 10 seconds wait
       // } catch (InterruptedException e) {
       //     Thread.currentThread().interrupt();
        //}

        // Clear start screen and display game fields
        turtle.reset(); // Removes the start screen
        drawGameField("Player");
        drawGameField("AI");
    }

    // Method for visualization on the browser
    public void drawGameField(String playerType) {
        int x = 150; // Start position X
        int y = playerType.equals("Player") ? 80 : 305; // Different Y-position for Player and AI
        turtle.textSize = 20;

        // Display header
        turtle.left(90).moveTo(220, y).color(50,205,50);
        turtle.text(playerType.equals("Player") ? "Player" : "Computer");
        turtle.right(90);
        turtle.color(0,0,255);
        // Draw game field
        y += 25; // Space under the header
        turtle.moveTo(x, y);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Draw square
                for (int k = 0; k < 4; k++) {
                    turtle.forward(15).right(90);
                }
                turtle.penUp().forward(15).penDown(); // Next box
            }
            y += 15; // Move Y-position for the next row
            turtle.penUp().moveTo(x, y).penDown();
        }
        turtle.color(0,0,0);
    }

    public void drawStartScreen() {
    // Anzeige der Startnachricht
    turtle.left(90);
    turtle.moveTo(250, 100);
    turtle.textSize = 30;
    turtle.color(0, 0, 255);
    turtle.text("Willkommen zu Schiffe Versenken!");
    turtle.textSize = 20;
    turtle.moveTo(250, 250);
    turtle.text("Bitte wählen Sie einen Schwierigkeitsgrad:");
    //System.out.println("Bitte wählen Sie einen Schwierigkeitsgrad:");
    turtle.textSize = 15;
    turtle.moveTo(250, 300);
    turtle.text("1 - Einfach");
    turtle.moveTo(250, 320);
    turtle.text("2 - Mittel");
    turtle.moveTo(250, 340);
    turtle.text("3 - Schwer");
    turtle.color(0, 0, 0);
    turtle.right(90);

    // Warten auf Benutzereingabe
    Scanner scanner = new Scanner(System.in);
    int difficulty = 0;
    while (difficulty < 1 || difficulty > 3) {
        System.out.print("Bitte waehlen Sie einen Schwierigkeitsgrad (1-3): ");
        if (scanner.hasNextInt()) {
            difficulty = scanner.nextInt();
        } else {
            scanner.next(); // Ungültige Eingabe verwerfen
        }
    }

    // KI-Instanz entsprechend der Auswahl erstellen
    switch (difficulty) {
        case 1:
            ai = new AIEasy(this);  // Beispiel für einfache KI
            System.out.println("Schwierigkeitsgrad 'Einfach' gewaehlt.");
            break;
        case 2:
            ai = new AIMedium(this);
            System.out.println("Schwierigkeitsgrad 'Mittel' gewaehlt.");
            break;
        case 3:
            ai = new AIDifficult(this);
            System.out.println("Schwierigkeitsgrad 'Schwer' gewaehlt.");
            break;
    }
}

    //row und col zum index umwandeln
    public int toIndex(int row, int column) {
        return row * 10 + column;
    }

    // Method to place a ship on the player's field
    public boolean placePlayerShip(int row, int column, boolean horizontal) {
        assert row >= 0 && row <= 9 && column >= 0 && column <= 9 :"Schiffe muessen innerhalb des Spielfelds liegen!";
        if (placedPlayerShips >= shipLengths.size()) throw new IllegalArgumentException("Alle Schiffe wurden bereits platziert.");
        //if (placedPlayerShips >= shipLengths.length) {
         //   System.out.println("Alle Schiffe wurden bereits platziert.");
           // return false;
        //}
        int length = shipLengths.get(placedPlayerShips);
        if (placeShip(playerField, row, column, length, horizontal)) { 
            Ship newShip = new Ship(row, column, length, horizontal);
            playerShips.add(newShip);
            placedPlayerShips++;

            // If the player has placed all ships, the AI automatically places its ships
            if (placedPlayerShips == shipLengths.size() && !ai.aiShipsPlaced) {
                ai.placeAIShips();
            }
            return true;
        }
        return false;
    }

    // Method to place a ship on a field
    public boolean placeShip(char[] field, int startX, int startY, int length, boolean horizontal) {
        if (horizontal) {
            if (startY + length > 10) {
                return false;  
            }
            for (int i = 0; i < length; i++) {
                if (field[toIndex(startX,startY + i)] != '~') {
                    return false;  
                }
            }
            for (int i = 0; i < length; i++) {
                field[toIndex(startX,startY + i)] = 'S';
                placeShipTurtle(startX, startY + i);
            }
        } else {
            if (startX + length > 10) {
                return false;  
            }
            for (int i = 0; i < length; i++) {
                if (field[toIndex(startX + i,startY)] != '~') {
                    return false;  
                }
            }
            for (int i = 0; i < length; i++) {
                field[toIndex(startX + i,startY)] = 'S';
                placeShipTurtle(startX + i, startY);
            }
        }
        return true;
    }


// Method for the player to make a shot
public void shootPlayer(int row, int column) {
    assert row >=0 && row <=9 && column >= 0 && column <= 9 : "Position muss im Spielfeld sein.";
     if (gameOver) throw new IllegalArgumentException("Das Spiel ist bereits beendet. Keine Zuege mehr möglich.");
    //if (gameOver) {
       // System.out.println("Das Spiel ist bereits beendet. Keine Züge mehr möglich.");
       // return;
   // }
    if (aiShips.isEmpty()) {
        throw new IllegalArgumentException("Es gibt kein Schiff auf dem Spielfeld");
       // System.out.println("Es gibt kein Schiff auf dem Spielfeld");
        //return;
    }

    if (playerMoves >= 3) {
        System.out.println("Sie haben bereits 3 Zuege gemacht. Nun ist Computer dran.");
        ai.makeAIMoves(); // KI macht ihre drei Züge
        return;
    }

    // Überprüfung, ob die Position bereits geschossen wurde
    if (aiField[toIndex(row,column)] == 'X' || aiField[toIndex(row,column)] == '0') {
        throw new IllegalArgumentException("Sie haben bereits auf diese Position geschossen! Wähle eine andere.");
        //System.out.println("Du hast bereits auf diese Position geschossen! Wähle eine andere.");
        //return;
    }

    // Überprüfen, ob der Schuss ein Treffer oder Fehlschuss ist
    if (aiField[toIndex(row,column)] == 'S') {
        System.out.println("Treffer!");
        aiField[toIndex(row,column)] = 'X'; // Markiere als Treffer
        playerMoves++;
        drawHit(row, column, "AI");
        checkAndMarkDestroyedShipsAI();
        String winner = checkWinner();
        if (winner != null) {
            System.out.println(winner);
            // System.exit(0); // Spiel beenden
        }
    } else {
        System.out.println("Fehlschuss!");
        aiField[toIndex(row,column)] = '0'; // Markiere als Fehlschuss
        drawMiss(row, column, "AI");
        playerMoves++;
    }

    if (playerMoves == 3 && !gameOver) {
        System.out.println("Sie haben 3 Zuege gemacht. Jetzt ist Computer dran!");
        ai.makeAIMoves(); // KI macht ihre drei Züge
    }
}

// Check if an AI ship is fully destroyed and mark it
public void checkAndMarkDestroyedShipsAI() {
    for (int i = 0; i < aiShips.size(); i++) {
        Ship ship = aiShips.get(i);
        boolean destroyed = true;

        if (ship.horizontal()) {
            for (int j = 0; j < ship.length(); j++) {
                if (aiField[toIndex(ship.row(),ship.column() + j)] != 'X') {
               // if (aiField[ship.row()][ship.column() + j] != 'X') {
                    destroyed = false;
                    break;
                }
            }
        } else {
            for (int j = 0; j < ship.length(); j++) {
                if (aiField[toIndex(ship.row() + j,ship.column())] != 'X') {
                //if (aiField[ship.row() + j][ship.column()] != 'X') {
                    destroyed = false;
                    break;
                }
            }
        }

        if (destroyed) {
            System.out.println("Ein Schiff der KI komplett zerstört");
            drawRedLine(ship, "AI");
            aiShips.remove(i);
            i--;
        }
    }
}

public void removeHitFromList(List<int[]> list, int row, int column) {
    list.removeIf(hit -> hit[0] == row && hit[1] == column);
}

public void checkAndMarkDestroyedShipsPlayer() {
    for (int j = 0; j < playerShips.size(); j++) {
        Ship ship = playerShips.get(j);
        boolean destroyed = true;

        if (ship.horizontal()) {
            for (int i = 0; i < ship.length(); i++) {
                if (playerField[toIndex(ship.row(),ship.column() + i)] != 'X') {
                //if (playerField[ship.row()][ship.column() + i] != 'X') {
                    destroyed = false;
                    break;
                }
            }
        } else {
            for (int i = 0; i < ship.length(); i++) {
                if (playerField[toIndex(ship.row() + i,ship.column())] != 'X') {
                //if (playerField[ship.row() + i][ship.column()] != 'X') {
                    destroyed = false;
                    break;
                }
            }
        }

        if (destroyed) {
            
            if (ship.length() < ai.lastHits.size()) {
               // System.out.println(ai.lastHits.size());
                System.out.println("Ein Schiff des Spielers komplett zerstört");
                drawRedLine(ship, "Player");
                int startRow = ship.row();
                int startCol = ship.column();
                if (ship.horizontal()) {
                    int endCol = startCol + ship.length() - 1;// da indize beginnt mit 0
                    for (int i = startCol; i <= endCol; i++) {
                        removeHitFromList(ai.lastHits, startRow, i);
                    }
                   // System.out.println(ai.lastHits.size());
                } else {
                    int endRow = startRow + ship.length() - 1;
                    for (int i = startRow; i <= endRow; i++) {
                        removeHitFromList(ai.lastHits, i, startCol);
                    }
                    
                }
             //   ai.lastHits.clear();
                ai.shipDirectionFound = false;
                ai.isHorizontal = false;
                shipLengths.remove(Integer.valueOf(ship.length()));
                playerShips.remove(ship);
                j--;
               // System.out.println(ai.lastHits.size());
            } else {
                System.out.println("Ein Schiff des Spielers komplett zerstört");
                ai.lastHits.clear();
                ai.shipDirectionFound = false;
                ai.isHorizontal = false;
                drawRedLine(ship, "Player");
                shipLengths.remove(Integer.valueOf(ship.length()));
                playerShips.remove(ship);
                j--;
            }
        }
    }
}

// Draw a hit marker
public void drawHit(int row, int column, String field) {
    int x = 0;
    int y = 0;
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

// Draw a miss marker
public void drawMiss(int row, int column, String field) {
    int x = 0;
    int y = 0;
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

// Draw a red line over a destroyed ship
public void drawRedLine(Ship ship, String field) {
    double x = 0;
    double y = 0;
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


public void placeShipTurtle(int row, int col) {
    int startX, startY;
    if (placedPlayerShips == shipLengths.size() && !ai.aiShipsPlaced) {
        startX = 150;  // Base X-coordinate of the field
        startY = 330;
        turtle.color(173, 216, 230);
    } else {
        startX = 150;  // Base X-coordinate of the field
        startY = 105;  //
    }
    if(gameOver){
       // System.out.println("HI");
        startX = 150;
        startY = 330;
        turtle.color(0,0,0);
    }
    turtle.textSize = 10;
    int squareLength = 15;  // Size of one square

    // Calculate the target position for the square
    int targetX = startX + (col * squareLength);
    int targetY = startY + (row * squareLength);

    // Move the turtle to the position
    turtle.penUp();
    turtle.moveTo(targetX, targetY);
    turtle.penDown();

    // Now place the "S" in the middle of the square
    // Calculate the center of the square
    int textX = targetX + squareLength / 2;  // X-position for text
    int textY = targetY + squareLength / 2;  // Y-position for text

    // Turtle moves to the exact text position
    turtle.penUp();
    turtle.right(90);
    turtle.moveTo(textX, textY); // Move to the center of the square
    turtle.penDown();
    turtle.text("S");
    turtle.left(90);
    turtle.color(0, 0, 0);  // Draw the "S" in the center
}

// Method to check if a player has lost
public String checkWinner() {
    // Check if the player still has ships
    boolean playerHasShips = false;
    for (int i = 0; i < 100; i++) {
       // for (int j = 0; j < 10; j++) {
            if (playerField[i] == 'S') {
                playerHasShips = true;
                break;
            }
        //}
        if (playerHasShips) break;
    }

    // Check if the AI still has ships
    boolean aiHasShips = false;
    for (int i = 0; i < 100; i++) {
        //for (int j = 0; j < 10; j++) {
            if (aiField[i] == 'S') {
                aiHasShips = true;
                break;
            }
        //}
        if (aiHasShips) break;
    }

    // Determine the winner
    if (!playerHasShips) {
      //  turtle.reset();
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
       // turtle.reset();
        turtle.left(90);
        turtle.textSize = 30;
        turtle.moveTo(250, 520);
        turtle.color(50,205,50);
        turtle.text("Sie haben gewonnen!");
        turtle.right(90);
        System.out.println("Sie haben gewonnen!");
        gameOver = true;
        promptNewGame();
        return "";
    }
    return null; // No winner yet
}

    public void visibleShipsPlayerAfterGameOver(){
       // if (!playerShips.size().isEmpty()){
      // System.out.println(aiShips.size());
            for(int i = 0; i < aiShips.size(); i++){
                Ship restShip = aiShips.get(i);
                for(int j = 0; j < restShip.length(); j++ ){
                    if(restShip.horizontal()){
                        placeShipTurtle(restShip.row(),restShip.column() + j);
                    }else{
                        placeShipTurtle(restShip.row() + j,restShip.column());
                    }
                }
            }
       // }
    }

public void promptNewGame() {
    Scanner scanner = new Scanner(System.in);
    turtle.moveTo(250, 540);
    turtle.textSize = 12;
    turtle.left(90);
    turtle.color(50,205,50);
    turtle.text("Geben Sie 1 ein, um ein neues Spiel zu starten, oder 2, um das Spiel zu beenden.");
    turtle.right(90);
    System.out.println("Geben Sie 1 ein, um ein neues Spiel zu starten, oder 2, um das Spiel zu beenden.");

    String response = scanner.nextLine();

    switch (response) {
        case "1":
            System.out.println("Neues Spiel wird gestartet...");
            startNewGame();
            break;
        case "2":
            System.out.println("Das Spiel wird beendet.");
            return;
        default:
            System.out.println("Ungültige Eingabe. Bitte geben Sie 1 oder 2 ein.");
            promptNewGame();
            break;
    }
}

public void startNewGame() {
    turtle.reset();
    for (int i = 0; i < 100; i++) {
        //for (int j = 0; j < 10; j++) {
            aiField[i] = '~';  // "~" for water
            playerField[i] = '~';   // "~" for water
        //}
    }
    placedPlayerShips = 0;
    ai.aiShipsPlaced = false;
    playerMoves = 0;
    gameOver = false;
    playerShips.clear();
    aiShips.clear();
    shipLengths.clear();
    shipLengths = new ArrayList<>(List.of(2,3,4,5));
    drawStartScreen();  // Display the start screen

    // Delay of 20 seconds
   // try {
     //   Thread.sleep(20000); // Wait for 20 seconds
    //} catch (InterruptedException e) {
      //  Thread.currentThread().interrupt();
    //}

    // Clear start screen and display game fields
    turtle.reset(); // Remove the start screen
    drawGameField("Player");  // Display the player's field
    drawGameField("AI");  // Display the AI's field

     ai.visibleFieldAI = new char[100];
        for (int i = 0; i < 100; i++) {
           // for (int column = 0; column < 10; column++) {
                ai.visibleFieldAI[i] = '~'; // Alles unbekannt
          //  }
        }
        ai.lastHits.clear();
}

}

record Ship(int row, int column, int length, boolean horizontal){}


 /*
 GameField g = new GameField()
 g.placePlayerShip(0,2,true)
 g.placePlayerShip(5,7,true)
 g.placePlayerShip(5,2,false)
 g.placePlayerShip(0,8,false)

g.placePlayerShip(1,2,true)
g.placePlayerShip(1,1,false)
 g.placePlayerShip(4,1,true)
  g.placePlayerShip(0,0,false)

*/
 
