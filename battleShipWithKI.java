Clerk.markdown(
    Text.fillOut(
"""
# Schiffe versenken mit KI
## Projekt Präsentation und Erklärung der leichten Abweichung von FunktionsVersprechen

_Roussel Dongmo_, _Programmierung Projekt_

Das Projekt ´Schiffe versenken´ ist eine digitale Umsetzung des klassischen Strategie- und Ratespiels, das üblicherweise von zwei Spielern gespielt wird. Ziel des Spiels ist es, die Flotte des Gegners zu zerstören, indem man die Positionen der Schiffe errät und diese gezielt angreift. Die Implementierung kombiniert Logik, Benutzerinteraktion und eine visuelle Darstellung des Spielfelds. In meiner Anwendung geht es um die minimale Version dieses Spiels , indem ich weder reale Schiffe noch alle Funktionalitäten der bekannten Anwendung eingesetzt habe. Trotzdem war dieses Projekt eine große Herausforderung für mich, da es mein erstes Programmierprojekt ist. Von Anfang bis Ende habe ich mit Begeisterung und Fleiß daran gearbeitet, auch wenn ich manchmal auf erhebliche Schwierigkeiten gestoßen bin. Besonders anspruchsvoll war die Implementierung der Logik für die KI (Computergegner).

Nicht, weil ich keine Ideen hatte, sondern weil ich mich verpflichtet fühlte, den Minimax-Algorithmus zu verwenden. In meinem Funktionsversprechen hatte ich erwähnt, dass ich 'einen geeigneten Algorithmus wie Minimax' implementieren würde. Im Nachhinein betrachtet, war das keine optimale Entscheidung. Zu diesem Zeitpunkt hatte ich noch nicht genügend Wissen über den Minimax-Algorithmus, um zu erkennen, dass es Spiele gibt, bei denen er nicht geeignet oder relevant ist – und "Schiffe versenken" gehört dazu.

Ein Minimax-Algorithmus ist darauf ausgelegt, bei Spielen wie Schach oder Tic-Tac-Toe optimale Entscheidungen zu treffen, da diese Spiele perfekt informationelle Spiele sind. Das bedeutet, dass alle Spieler zu jeder Zeit vollständige Informationen über den Zustand des Spiels haben und der Computer simuliert intern mögliche Züge für ihn und den Spieler durch eine tiefe Suche und macht dann den besten Zug. Bei 'Schiffe versenken' ist das jedoch nicht der Fall, da viele Informationen – wie die genaue Position der gegnerischen Schiffe – verborgen bleiben und jeder Spieler hat sein eigenes Spielfeld und Zug von einem Spieler(KI) hängt nicht vom Zug von dem anderen . Diese Unsicherheit und Unterschied führen dazu, dass Minimax in diesem Kontext weder effizient noch sinnvoll einsetzbar ist. Stattdessen wären andere Ansätze, die auf gezielte Züge nach einem Treffer oder Heuristiken basieren, besser geeignet.

Obwohl ich mit diesem Hindernis zu kämpfen hatte, habe ich viel über die Grenzen und Anwendungsbereiche von Algorithmen gelernt. Diese Erfahrung hat mir gezeigt, wie wichtig es ist, den richtigen Ansatz für die jeweilige Problemstellung zu wählen, und hat mich letztlich als Programmierer weitergebracht. Hier unter dokumentiere ich das Projekt nach genanten Szenarien in Funktionsversprechen weiter, aber zuerst kurz über das gemeinsame Programm.   

### Kurze Allgemeine Beschreibung vom Code des Projekts

Meine Anwendung besteht aus ***zwei Interfaces*** (die die logische Implementierung von dem Spiel von der Vizualisierung trennt und diese beiden Interfaces werden von der Klasse Game implementiert) ,***ein record*** (,das Informationen über jedes Schiff nach Erstellung speichert ), ***vier Klassen*** (eine Klasse für Game logisch und Vizualisierung , 3 andere für die KI nämlich eine für jeden Schwierigkeitgrad. ). Am Anfang des Spiels beim Erstellen eines Objekts von der Hauptklasse Game gibt man einen Wert zwischen 1 und 3 (1 für einfachstes Niveau , 2 für Mittel , 3 für Schwer), der der entsprechende Konstruktor für die KI durch die Methode setDifficult(int difficult) setzt. Dann wird eine Startseite während 5 Sekunden angezeigt und danach das Spielfeld und es geht so los mit Spielen beziehungsweise meine Szenarien.

## Szenarien 

Tatsächlich hat mein Projekt nur 5 Szenarien unter anderen : **Schiffe platzieren**, **Spielzug(Spieler)**, **Spielzug(KI)**, **zerstörte Schiffe aufdecken**, **Siegbedingung und Spielende**. Jedoch habe ich mich in den Spaß der Programmierung gestürzt und habe zusätzliche Funktionen hinzugefügt, die ursprünglich nicht geplant waren. Wie zum Beispiel die Funktionalität ***Startseite***(die nur während 5 Sekunden angezeigt wird) , ***Aufdeckung nach Spielende von nicht zerstörten Schiffen des Computers*** und auch Schwierigkeitgrad (war geplant aber unsicher ). Jetzt möchte ich auf das erste Szenario eingehen    


## I. Szenario 1: Schiffe Platzieren

Zu beachten ist das jeder Spieler in meiner Anwendung 4 Schiffe(die nacheinander platziert werden können) hat und die Schiffe werden nach Reihenfolge von Längen platziert. Das habe ich erreicht, indem ich eine Liste (mit Werten 2,3,4,5) als Objektvariable in klasse Game habe und diese Liste wird durchgelaufen und ein Schiff nimmt an jeder Position als Länge den Wert auf diesen Index  . Das heisst erstes Schiff ist immer automatisch von Länge 2 , zweites Schiff automatisch von Länge 3 , drittes Schiff von Länge 4 , viertes Schiff von Länge 5. Als logisch java habe ich 3 wichtige Methoden , um Schiffe zu Platzieren .Da die KI-Schiffe erst automatisch platziert werden , nachdem der Spieler seine Schiffe platziert hat, beginnen wir mit der boolischen Methode placePlayerShip(int row, int column, boolean horinzontal). 

```battleShipWithKI

 public boolean placePlayerShip(int row, int column, boolean horizontal) {
        if(gameOver){
            throw new IllegalArgumentException("Das Spiel ist bereits beendet");
        }
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

```

Wenn der Spieler seine Schiffe platziert will, ruft er diese Methode auf und gibt für jedes Schiff  einen Anfang Reihe , ein Anfang Column und true, wenn er horizontal platzieren will oder false anderfalls. Diese nutzt die Variable placedPlayerShips(auf 0 initialisiert), um auf die Liste von verschiedenen Längen zuzugreifen und diese Variable wird bei erfolreicher Platzierung eines Schiffs incrementiert .Und es geht so weiter bis die Variable den Wert 4 erreicht. Wenn ein Schiff erfolgreich platziert wurde , gibt die Methode "true" als Rückgabewert und wenn nicht erfolgreich(zum Beispiel , denn die Anfang-Reihe so angegeben wird , dass das Schiff für die entsprechende Länge ausser des Spielfeld wäre) bekommt man "false" zurück. Aber wie konkret wird ein Schiff platziert? Das bringt uns zu dieser zweiten logischen Methode (oder besser Hilfsmethode) boolean placeShip(char[] field, int startX,int startY, int length, boolean horizontal)

```battleShipWithKI

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

```
Durch diese Methode werden Schiffe eigentlich in Spielfeld platziertz , indem das Symbol "S" je nach der angegebenen Richtung von Anfang-Reihe und Anfang-Column bis entsprechende Länge dargestellt wird. Diese Methode nutzt die vizualisierung-Methode placeShipTurtle(int row, int col), die das Symbol "S" auf den angegebenen row und col auf dem Browser darstellt. Die Methode placePlayerShip(int row, int column, boolean horizontal) kann ein Schiff nur erstellen und speichert , wenn die Methode boolean placeShip(char[] field, int startX, int startY, int length, boolean horizontal) alles berücksitigt und "true" zurückgibt. Wenn der Spieler problemlos seine 4 Schiffe platziert hat , können die Schiffe von KI automatisch durch die folgende Methode platziert werden

```battleShipWithKI

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

```

Die Methode placeAISchips() platziert die Schiffe im KI-Spielfeld random unter bestimmten Bedingungen . So habe ich verfaren: solange die Hilfsmethode placeShip(char[] field, int startX, int startY, int length, boolean horizontal) false zurückgibt (Variable success ,die diesen boolischen Rückgabewert nimmt wird auf false initialisiert), werden random Anfang-Reihe , Anfang-Column und Richtung angegeben.Da die Schiffe von Gegner (hier Computer) nicht sichtbar sind, ergibt sich eine Meldung in Konsole , wenn KI erfolreich seine 4 Schiffe platziert hat. Besonders bei Platzierung von KI Schiffen ist bei dem Schwierigkeitgrad schwer, wo ich vermeide , dass Schiffe nebeneinander Platziert werden (da es einfacher für den Spieler zu gewinnen , wenn die Schiffe nebeneinander platziert werden ). Diese zusächliche Methode für Vermeidung von nebeneinander Schiffen wird in KLasse AIDifficult definiert und sieht folgendes so aus:
```battleShipWithKI
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

```
In dieser Methode vergewisse ich mich ,dass die umgeliegende Felder leer sind , bevor ein SChiff platziert wird 

#### Beispiel vom Szenario 1:

Hier zeigen ich wie der Spieler seine 4 Schiffe im Spielfeld platzieren kann, wie er will. Zum Beispiel 2 Schiffe horizontal von Längen 3 und 4 nebeneinander oben links von Spielfeld platzieren will und 2 andere vertikal unter von Länge 2 und 5

Game g = new Game(2)// Schwierigkeitgrad Mittel

g.placePlayerShip(8,8,false);

g.placePlayerShip(0,0,true);

g.placePlayerShip(1,0,true);

g.placePlayerShip(5,9,false);

## II. Szenario 2: Spielzug(Spieler)

Jeder Spieler darf pro Runde 3 Mal spielen , das heisst 3 Züge pro Runde . Der menschlichen Spieler zuerst mal dran und macht einen Zug auf eine Position auf dem Spielfeld vom Computer durch die Methode shootPlayer(int row, int column).

```battleShipWithKI

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

```

Diese Methode nimmt ein Reihe und ein Column(die zwischen 0 und 9 liegen müssen) , und wenn das Spiel nicht fertig ist oder es schon Schiffe auf dem Spielfeld vom Computer gibt, wird überprüft , ob der Spieler noch nicht 3 Züge gemacht hat und ob er auf die angegebene Position nocht nicht geschlossen hat . Nur wenn alle diese Bedingungen erfüllt sind , wird dann entweder die Methode drawHit(int row, int column, String field) oder drawMiss(int row, int column, String field) auf diese Position aufgerufen , um ein "X" bei Treffen(falls auf diese Position ein "S" war) oder ein "0" bei Fehlschuss(falls es auf deise Position ein "0") darzustellen  . Hier unter sind diese 2 Methoden

```battleShipWithKI

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

        turtle.penUp().moveTo(xStart, yStart).right(45).penDown().color(255, 0, 0).forward(20);
        turtle.penUp().right(135).forward(15).right(135).penDown();
        turtle.forward(20).color(0, 0, 0).right(45);
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

        turtle.penUp().moveTo(xStart, yStart).forward(7.5).right(90).forward(7.5).penDown();
        turtle.color(255, 105, 180).text("0").color(0, 0, 0).left(90);
    }

```

Diese beiden Turtle Methoden funktionieren fast gleich: wenn das Spielfeld für KI ist, bewege ich Turtle auf (150,330) und bei Spieler auf (150,105) . Da meine Quadrate ein forward 15 haben , multiplizieren ich dann die angegebene Reihe mal 15 und addiere mit 150 , das gleiche Prinzip für Column (aber addiert mit 330 bei Spielfefl KI und 105 bei Spielfeld Spieler) . Danach stelle ich das entsprechende Symbol dar. So wird insgesamt das Schuss von Spieler gemacht und wenn die Variable playerMoves( int methode von Schuss Spieler definiert) 3 erreicht , darf dann die KI seine Runde machen 

#### Beispiel von Szenario 2

Hier schliesse ich auf Positionen (0,0), (2,5), (9,0)

g.shootPlayer(0,0)

g.shootPlayer(2,5)

g.shootPlayer(9,0)


## III. Szenario 3: Spielzug(KI)

Wenn das Spiel nicht zum Ende ist und es Schiffe auf dem Spielfeld des Spielers gibt, spielt die KI auch wie der Spieler 3 Mal pro Runde , deshalb habe ich eine forSchleife , die ich durchlaufe. Damit das Spiel realitisch ist , habe ich eine Sekunde Wartezeit zwischen Zügen hinzufügt. 

```battleShipWithKI

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
                int row = position[0], col = position[1];
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

```

### 1. Gemeinsame Funktionalitäten von unterschielichen Schwierigkeitgrads

Die 3 unterschiedlichen Niveau (***einfach***, ***mittel***, ***schwer***) haben im Hintergrund das gleiche Prinzip. Hier habe ich viele Hilfsmethode benutzt, die als Rückgabetyp ***Optional<int[]>***, um ungültige Rückgabewerte wie {-1,-1} zu vermeiden. Bei Implementierung habe ich viel überlegt , wie die KI seine Schüsse am Anfang machen kann, da die Schiffe von Gegner verbogen sind. Ich wollte mich auf Random nicht verlassen .Denn eine effiziente KI soll meiner Meinung nach nicht auf Random basierend sein . Bei random könnte die KI zum Beispiel nur auf eine Seite vom Spielfeld Schliessen, was nicht optimal wäre .Deshalb habe ich das Spielfeld vom Spieler in 3 Quadrate unterteilt und habe so gestaltet das sie zuerst auf Mittel(durch die Methode **Optional<int[]> findHeuristicMove()**) von jedem Quadrat schliesst . Nur wenn auf alle Mitte gesclhossen wurde , macht sie das Gleiche bei Ecken(durch Methode **Optional<int[]> findCornerMove()**) .Das heisst , eine gleiche Ecke auf alle Quadrat und so bis Ecke fertig sind . Hier sind wir immer in Fällen vom Anfang oder Suche von heuristischer Position nach ganzer Zerstörung eines Schiffs. Wenn es mit Mittel und Ecken fertig ist, dann schliesst sie vom Ende des Spielfelds bis Anfang(durch die Methode **Optional<int[]> findFallbackMove()**), um immer einen Teil von einem Schiff zu finden. Effizienter wird es ,wenn die KI auf eine Position(auf ein Mittel eines Quadrats , Ecke oder irgendwo) ein Treffer bekommen hat(ich habe eine Liste **lastHits**, die alle Treffer von KI speichert und wenn ein Schiff zerstört wird , werden alle Treffer von diesem Schiff aus der Liste entfernt ). Er priosiert dann nur Nachbarn von dieser Position , um einen zweiten Treffer zu haben(durch die Methode **Optional<int[]> findTargedMove()**). bekommt sie einen zweiten Treffer dann geht sie nur weiter auf diese Richtung . Das heisst , wenn sie zum Beispiel auf (2,3) einen Treffer hat und einen anderen auf (3,3) , soll er weiter vertikal schliessen (die zuständige Methode dafür ist **Optional<int[]> shootVerticalFurther()** ) . Zuerst unter weiter und dann oben weiter (falls das entsprechende Schiff noch nicht zerstört) . wenn sie zweiten Treffer eher auf Position(2,4) bekommt , macht sie das Gleiche aber horizontal(durch die Methode **Optional<int[]> shootHorizontalFurther()**). Da Schiffe in meiner Anwendung nebeneinander platziert werden können , könnte passieren, dass sie auf eine Richtung alle Symbole "S" geschlossen hat aber kein Schiff ganz zerstört wurde(da diese "S" Symbole Teile von verschiedenen Schiffen sind) oder ein Schiff wurde zerstört aber eine getroffene Position nicht von diesem Schiff gehört. Deswegen habe ich eine Methode implementiert, die mit einer Kopie von Liste **lastHits** alle Schiffe zerstört , zu denen diese unterschielichen Treffer gehören . Die Methode **makeAIShoot(int row, int col)** ist dafür zuständig, bei Fehlschuss oder Treffer das entsprechende Symbol darzustellen. Das Gleiche mache ich auch in meinem interne Array  **visibleFieldAI** (die als Attribut in der Klasse auch mit Länge 100 initialisiert wurde).Dank dieses Arrays schliesst die KI niemals auf eine schon geschlossene Position . Da die Methode **isValidMove(int row, int col)** überprüft , ob die angegebene Position im visibleFieldAI leer, bevor den Zug gemacht wird. Jetzt gehe ich auf Besonderheit von jedem Schwierigkeitgrad ein.

### 2. Einfach 

Besonders hier ist zuerst, dass die **checkNeighbors(int row, int col)** nicht berücksichtigt wird. Diese Methode sorgt normalerweise dafür , dass die KI bei Suchen von ersten Treffer nur Schliesst , wenn mindestens ein Nachbar leer ist. Hier in diesen beiden Methoden habe ich diese Überprüfung entfernt, um es weniger effizient zu machen. Die Methoden **Optional<int[]> processSquare(int[][] square, boolean useRowStart,boolean useColStart)** wurde auch überschrieben , um ein Column zu entfernen , sodass es weniger effizient ist , da auf alle Columns zumindest ausser dieses früher geschlossen würde. Aber erst in findFallbackMove() wird später im Spielverlauf geschlossen würden

```battleShipWithKI

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

```

### 3. Mittel 

Dieses Niveau ist besonders , weil es das einzige ist , das diese checkNeighbors(int row, int column). Was genau im Schwierigkeitgrad "einfach" entfent wurde.

```battleShipWithKI

    public boolean checkNeighbors(int row, int column) {
        boolean upValid = row > 0 && visibleFieldAI[game.toIndex(row - 1, column)] == '~';
        boolean downValid = row < 9 && visibleFieldAI[game.toIndex(row + 1, column)] == '~';
        boolean leftValid = column > 0 && visibleFieldAI[game.toIndex(row, column - 1)] == '~';
        boolean rightValid = column < 9 && visibleFieldAI[game.toIndex(row, column + 1)] == '~';

        return upValid || downValid || leftValid || rightValid;
    }

```
### 4. Schwer

Hier gibt es einige Besonderheiten, die gemacht wurden. Zuerst habe ich die Liste vom Ecke und Mittel vom Quadrat mit ***collection.schuffle(list)*** vor jedem Zug zufällig gemacht. Ich habe auch dann noch eine Methode **canShipHere(int row, int col)**, die vor einem Schuss immer überprrüft, ob es sinnvoll wäre auf diese Position zu schliessen. Diese Überprüfung wird gemacht , indem ich die Liste von Länge der Schiffe durchlaufe und zähle horinzontal und vertical ob nebeneinandere Positionen auf eine Richtung mehr oder gleich ein Länge in dieser Liste ist . Und nach Zerstörung eines Schiffs wird seine Länge in dieser Liste entfernt

```battleShipWithKI

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


```

Wenn die KI seine Züge macht, wird nach jedem Zug angezeigt(in jshell) ob es ein Treffer oder Fehlschuss ist und auf welche Position . Und wenn er seine 3 Züge gemacht hat , wird eine Meldung(in jshell) angegeben,  dass der Spieler spielen kann

## IV. Zerstörte Schiffe aufdecken

Mein viertes Szenario besteht darin , dass ein Schiff aufgedeckt werden soll , wenn es komplett zerstört ist . Das stelle ich in meiner Darstellung mit einer rote Linie dar . Um diese Funktionalität in meiner Anwendung zu erreichen, habe ich 2 Methoden implementiert nämlich **checkAndMarkDestroyedShipsAI()** und **checkAndMarkDestroyedShipsPlayer()**. Diese beiden Methoden laufen jeweils die Liste von gespeicherten Schiffen nach der Erstellung und ruft bei jedem Schiff die Methode **isShipDestroyed(Ship ship, char[] field)** , die überprüft ob dieses Schiff nur das Symbol "X" besitzt und wenn der Fall ist "true" zurückgibt. Danach rufe ich die Methode **drawRedLine(Ship ship, String field)**, die dafür zuständig ist, die rote Linie in der Mitte vom Schiff darzustellen und ich entferne zum Schluss dieses Schiff aus der Liste von gespeicherten Schiffen. Bei Schiffen von Spieler ist diese Methode ein bisschen komplexer. Dort muss die Liste ***lastHits*** berücksichtigt werden: wenn sie zum Beispiel Treffer von nur einem Schiff enthält , kann sie einfach danach nur gelerrt werden . Aber wenn sie auch Treffer von anderen Schiffen enthält (da Schiffe nebeneinander platziert werden können), müssen Treffer entfernt werden , die nur zu dem zu zerstörten Schiff gehören. Wenn das gemacht wird dann initialisiere ich die Variablen ***shipDirectionFound***(benutzt ab 2 Treffer , um eine Richtung zu haben) und ***isHorinzontal***(benutzt zum wissen , ob die Richtung horizontal oder vertikal ist) auf false .

```battleShipWithKI

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
                    int startRow = ship.row(), startCol = ship.column();
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

```

## V. Siegbedingung und Spielende

Hier geht es darum, zum prüfen (nach jedem Schuss als Treffer), ob es auf einem Spielfeld überhaupt kein Symbol "S" mehr gibt . In dem Fall würde bedeutet: der Spieler , dessen Spielfeld kein "S" Symbol hat , hat verloren . Da seine Schiffe komplett zerstört wurden. Das erreiche ich durch die Methode **checkWinner()**. Wer gewonnen hat , wird sowohl in jshell als auch in Browser (unter) angezeigt . Ich rufe danach die methode **newGameOrFinish()** auf, die fragt, ob der Benutzer das Spiel neue Starten oder beenden will.

```battleShipWithKI

    public String checkWinner() {
        boolean playerHasShips = hasShips(playerField);
        boolean aiHasShips = hasShips(aiField);

        if (!playerHasShips) {
            turtle.textSize = 30;
            turtle.left(90).moveTo(250, 520).color(255, 0, 0).text("Computer hat gewonnen!").right(90);
            System.out.println("Computer hat gewonnen!");
            gameOver = true;
            visibleShipsPlayerAfterGameOver();
            newGameOrFinish();
            return "";
        } else if (!aiHasShips) {
            turtle.textSize = 30;
            turtle.left(90).moveTo(250, 520).color(50, 205, 50).text("Sie haben gewonnen!").right(90);
            System.out.println("Sie haben gewonnen!");
            gameOver = true;
            newGameOrFinish();
            return "";
        }
        return null;
    }

    private boolean hasShips(char[] field) {
        return IntStream.range(0, field.length)
                .anyMatch(i -> field[i] == 'S');
    }

    public void newGameOrFinish(){
        turtle.moveTo(250, 540);
        turtle.textSize = 12;
        turtle.left(90);
        turtle.color(50, 205, 50);
        turtle.text("Wollen Sie ein neues Spiel starten oder das Spiel beenden?");
        turtle.right(90);
        turtle.color(0,0,0);
    }

```

## Zusächliche Funktionalitäten

- Nicht zerstörte Schiffe nach Spielende sichtbar machen:

Wenn der Computer gewinnt, dann können seine Schiffe wieder sichtbar . So weisst der Spieler zumindest die Position. Das erreiche ich durch die folgende Methode

```battleShipWithKI

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

```

- Methoden **startNewGame(int difficulty)** und **gameIsFinish()**

Dank dieser Methoden kann das Spiel zu Jederzeit neu gestartet werden oder beendet werden: Das heisst im Laufe des Spiels oder am Ende des Spiel . Um neu starten zu können werden fast alle Attribute von Klassen einfach wie im Konstruktoren initialisiert, nachdem ich das Turtle ganz reset() habe.

```battleShipWithKI

    public void startNewGame(int difficulty) {
        if(difficulty < 1 || difficulty > 3){
            throw new IllegalArgumentException("Schwierigkeitgrad muss zwischen 1 und 3 liegen");
        }
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
        setDifficulty(difficulty);
        drawStartScreen(difficulty);
        turtle.reset();
        drawGameField("Player");
        drawGameField("AI");
        ai.visibleFieldAI = new char[100];
        Arrays.fill(ai.visibleFieldAI, '~');
        ai.lastHits.clear();
    }

    public void gameIsFinish(){
        gameOver = true;
        turtle.textSize = 12;
        turtle.left(90);
        turtle.moveTo(250, 560);
        turtle.text("Das Spiel ist beendet! Bis nächstes Mal");
        turtle.right(90);
    }


```
## Schluss

Ich habe in diesem Projekt von Anfang bis Ende viel investiert und immer mit Spass und Geis gearbeitet. Jedoch war das Projekt für mich eine grosse Herausforderung. Denn mein Ziel war, mich zu überzeugen, dass ich nach 2 Semestern gut anwenden kann , was ich gelernt habe. Aber es ist nur den Begin denn meine nächste Herausforderung wäre dieses Projekt danach (in Ferien warscheinlich ) mit meinem eigenen View zu machen. Ich bin trotzdem sehr stolz auf mich, nicht nur denn ich ein anspruchvolle Anwendung gemacht habe , sondern auch denn ich es geschafft habe. Obwohl ich einige Schwierigkeiten oft getroffen habe , war ich immer vom Anfang bis Ende sicher, dass ich etwas von selbst schaffen könnte. Mein Wissen in java haben sich stark verdoppelt denn ich habe normalerweise immer viele Recherche gemacht und gelernt, dass es vieles gaben , die ich nicht wusste oder wusste aber nicht gut anwenden konnte . Das ist letztendlich meiner Dozentin zustehen . Ohne Ihre Pedagogie , Leidenschaft und Anforderungen hätte ich das nicht schaffen können . Ich danke Ihnen ***Frau Krümmel*** , mir die Grundlage von Programmierung beigebracht zu haben.  

""", Text.cutOut("./battleShipWithKI.java", "// myFirstTurtle")));

/*
```battleShipWithKI

```
*/


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

    void startNewGame(int difficulty);

}

public interface GameVisualization {
    void drawStartScreen(int difficulty);

    void drawGameField(String playerType);

    void drawHit(int row, int column, String field);

    void drawMiss(int row, int column, String field);

    void drawRedLine(Ship ship, String field);

    void placeShipTurtle(int row, int col);

    void visibleShipsPlayerAfterGameOver();

    void newGameOrFinish();

    void gameIsFinish();
}

public class Game implements GameLogic, GameVisualization {
    Turtle turtle;
    AIMedium ai;
    public char[] playerField;
    public char[] aiField;
    public List<Ship> playerShips;
    public List<Ship> aiShips;
    public List<Integer> shipLengths = new ArrayList<>(List.of(2, 3, 4, 5));
    public int placedPlayerShips = 0;
    public int playerMoves = 0;
    public boolean gameOver = false;

    public Game(int difficulty) {
        if(difficulty < 1 || difficulty > 3){
            throw new IllegalArgumentException("Schwierigkeitgrad muss zwischen 1 und 3 liegen!");
        }
        turtle = new Turtle(500, 600);
        aiField = new char[100];
        playerField = new char[100];
        playerShips = new ArrayList<>();
        aiShips = new ArrayList<>();

        Arrays.fill(aiField, '~');
        Arrays.fill(playerField, '~');

        setDifficulty(difficulty);
        drawStartScreen(difficulty);
        turtle.reset();
        drawGameField("Player");
        drawGameField("AI");
    }

    private void setDifficulty(int difficulty) {
        switch (difficulty) {
            case 1 -> ai = new AIEasy(this);
            case 2 -> ai = new AIMedium(this);
            case 3 -> ai = new AIDifficult(this);
            default -> throw new IllegalArgumentException("Ungültiger Schwierigkeitsgrad!");
        }
        System.out.println(getDifficultyText(difficulty));
    }


   public void drawStartScreen(int difficulty) {
        turtle.left(90).moveTo(250, 100);
        turtle.textSize = 30;
        turtle.color(0, 0, 255);
        turtle.text("Willkommen zu Schiffe Versenken!");
        turtle.textSize = 20;
        turtle.moveTo(250, 250);
        turtle.text(getDifficultyText(difficulty));

        turtle.textSize = 15;
        turtle.moveTo(250, 300).text("Das Spiel startet in wenigen Sekunden...");
        turtle.color(0, 0, 0).right(90);
        try {
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getDifficultyText(int difficulty) {
        return "Gewählter Schwierigkeitsgrad: " + switch (difficulty) {
            case 1 -> "Einfach";
            case 2 -> "Mittel";
            case 3 -> "Schwer";
            default -> "Unbekannt";
        };
    }



    public void drawGameField(String playerType) {
        int x = 150;
        int y = playerType.equals("Player") ? 80 : 305;
        turtle.textSize = 20;
        turtle.left(90).moveTo(220, y).color(50, 205, 50);
        turtle.text(playerType.equals("Player") ? "Player" : "Computer");
        turtle.right(90).color(0, 0, 255);
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

   

    public int toIndex(int row, int column) {
        return row * 10 + column;
    }

    public boolean placePlayerShip(int row, int column, boolean horizontal) {
        if(gameOver){
            throw new IllegalArgumentException("Das Spiel ist bereits beendet");
        }
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
        int targetX = startX + (col * squareLength), targetY = startY + (row * squareLength);
        turtle.penUp().moveTo(targetX, targetY).penDown();
        int textX = targetX + squareLength / 2, textY = targetY + squareLength / 2;
        turtle.penUp().right(90).moveTo(textX, textY).penDown().text("S").left(90).color(0, 0, 0);
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
                    int startRow = ship.row(), startCol = ship.column();
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

    private boolean isShipDestroyed(Ship ship, char[] field) {
        return IntStream.range(0, ship.length())
                .allMatch(i -> field[toIndex(
                        ship.horizontal() ? ship.row() : ship.row() + i,
                        ship.horizontal() ? ship.column() + i : ship.column())] == 'X');
    }

    private void removeHitFromList(List<int[]> list, int row, int column) {
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

        turtle.penUp().moveTo(xStart, yStart).right(45).penDown().color(255, 0, 0).forward(20);
        turtle.penUp().right(135).forward(15).right(135).penDown();
        turtle.forward(20).color(0, 0, 0).right(45);
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

        turtle.penUp().moveTo(xStart, yStart).forward(7.5).right(90).forward(7.5).penDown();
        turtle.color(255, 105, 180).text("0").color(0, 0, 0).left(90);
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

        turtle.penUp().moveTo(startX, startY).penDown().color(255, 0, 0).lineTo(endX, endY).color(0, 0, 0);
    }

    public String checkWinner() {
        boolean playerHasShips = hasShips(playerField);
        boolean aiHasShips = hasShips(aiField);

        if (!playerHasShips) {
            turtle.textSize = 30;
            turtle.left(90).moveTo(250, 520).color(255, 0, 0).text("Computer hat gewonnen!").right(90);
            System.out.println("Computer hat gewonnen!");
            gameOver = true;
            visibleShipsPlayerAfterGameOver();
            newGameOrFinish();
            return "";
        } else if (!aiHasShips) {
            turtle.textSize = 30;
            turtle.left(90).moveTo(250, 520).color(50, 205, 50).text("Sie haben gewonnen!").right(90);
            System.out.println("Sie haben gewonnen!");
            gameOver = true;
            newGameOrFinish();
            return "";
        }
        return null;
    }

    private boolean hasShips(char[] field) {
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

    public void newGameOrFinish(){
        turtle.moveTo(250, 540);
        turtle.textSize = 12;
        turtle.left(90);
        turtle.color(50, 205, 50);
        turtle.text("Wollen Sie ein neues Spiel starten oder das Spiel beenden?");
        turtle.right(90);
        turtle.color(0,0,0);
    }

    public void startNewGame(int difficulty) {
        if(difficulty < 1 || difficulty > 3){
            throw new IllegalArgumentException("Schwierigkeitgrad muss zwischen 1 und 3 liegen");
        }
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
        setDifficulty(difficulty);
        drawStartScreen(difficulty);
        turtle.reset();
        drawGameField("Player");
        drawGameField("AI");
        ai.visibleFieldAI = new char[100];
        Arrays.fill(ai.visibleFieldAI, '~');
        ai.lastHits.clear();
    }

    public void gameIsFinish(){
        gameOver = true;
        turtle.textSize = 12;
        turtle.left(90);
        turtle.moveTo(250, 560);
        turtle.text("Das Spiel ist beendet! Bis nächstes Mal");
        turtle.right(90);
    }

}

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
                int row = position[0], col = position[1];
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
        if (game.playerField[game.toIndex(row, col)] == 'S') { 
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
                    return Optional.of(new int[] { row, col }); 
                }
            }
        }
        return Optional.empty(); 
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

        return Optional.empty(); 
    }

    public boolean checkNeighbors(int row, int column) {
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
