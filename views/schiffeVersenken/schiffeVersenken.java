class SchiffeVersenken {
    final String ID;
    final int breite, hoehe;
    final String libPath = "views/schiffeVersenken/schiffeVersenken.js"; // Korrektur: Pfad zum Ordner 'schiffeVersenken'
    LiveView view;

    char[][] spielfeldSpieler = new char[10][10];  // Spielfeld des Spielers (10x10)
    char[][] spielfeldKI = new char[10][10];  // Spielfeld der KI (10x10)
    int[] schiffLaengen = {2, 3, 4, 5}; // Schiffe mit Längen 2, 3, 4 und 5
    int platzierteSchiffeSpieler = 0;  // Zähler für platzierte Schiffe des Spielers
    boolean kiSchiffePlatziert = false;  // Status, ob die KI ihre Schiffe platziert hat

    // Konstruktor
    SchiffeVersenken() {
        this.view = Clerk.view();
        this.breite = 700;  // Standard-Breite des Canvas
        this.hoehe = 800;   // Standard-Höhe des Canvas

        // Lade die JavaScript-Datei, um das SchiffeVersenken-Spiel im Frontend zu initialisieren
        Clerk.load(view, libPath);

        // Erhalte die eindeutige ID für dieses SchiffeVersenken-Objekt
        ID = Clerk.getHashID(this);

        // Canvas für das Spielfeld erstellen und einfügen
        Clerk.write(view, "<canvas id='battleshipCanvas" + ID + "' width='" + this.breite + "' height='" + this.hoehe + "' style='border:1px solid #000;'></canvas>");
        Clerk.script(view, "const schiff" + ID + " = new SchiffeVersenken(document.getElementById('battleshipCanvas" + ID + "'), 'schiff" + ID + "');");

        // Spielfeld initialisieren
        initialisiereSpielfeld();

        // Sobald das Spielfeld initialisiert ist, das Spiel aufrufen
        spieleInitialisieren();
    }

    // Spielfeld initialisieren (mit Wasserzeichen '~')
    void initialisiereSpielfeld() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                spielfeldSpieler[i][j] = '~'; // Alle Felder sind leer
                spielfeldKI[i][j] = '~'; // Alle Felder sind leer
            }
        }
    }

    // Methode, um das Spiel zu initialisieren und das Spielfeld darzustellen
    private void spieleInitialisieren() {
        // Zeige das Spielfeld für den Spieler im Frontend an
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Beispiel: Visualisiere jedes Spielfeld (Optional: Setze ein Wasserzeichen)
                // Hier wird jedes Feld visualisiert, im Beispiel könnte es ein 'Wasser'-Zeichen '~' sein
                schiffePlatzierenSpielerCanvas(i, j, false);  // Beispiel: Vertikale Platzierung als Standard
            }
        }
    }

    // Methode zur Platzierung von Schiffsmarkierungen für den Spieler auf dem Canvas (visualisieren)
    private void schiffePlatzierenSpielerCanvas(int x, int y, boolean horizontal) {
        // Beispielcode für das Zeichnen auf dem Canvas
        Clerk.script(view, "schiff" + ID + ".zeichneSchiff(" + x + ", " + y + ", " + (horizontal ? "true" : "false") + ");");
    }

    // Methode zum Platzieren eines Schiffs auf dem Spielfeld des Spielers
    public boolean schiffPlatzierenSpieler(int zeile, int spalte, boolean horizontal) {
        if (platzierteSchiffeSpieler >= schiffLaengen.length) {
            System.out.println("Alle Schiffe wurden bereits platziert.");
            return false;
        }

        int laenge = schiffLaengen[platzierteSchiffeSpieler];
        if (schiffPlatzieren(spielfeldSpieler, zeile, spalte, laenge, horizontal)) {
            platzierteSchiffeSpieler++;

            // Wenn der Spieler alle Schiffe platziert hat, platziert die KI automatisch ihre Schiffe
            if (platzierteSchiffeSpieler == schiffLaengen.length && !kiSchiffePlatziert) {
                schiffePlatzierenKI();
            }
            return true;
        }
        return false;
    }

    // Hilfsmethode zum Platzieren eines Schiffs auf einem Spielfeld
    private boolean schiffPlatzieren(char[][] spielfeld, int xStart, int yStart, int laenge, boolean horizontal) {
        if (horizontal) {
            if (yStart + laenge > 10) {
                System.out.println("Schiff passt nicht horizontal");
                return false;  // Schiff passt nicht horizontal
            }
            for (int i = 0; i < laenge; i++) {
                if (spielfeld[xStart][yStart + i] != '~') {
                    System.out.println("Überschneidung mit einem anderen Schiff");
                    return false;  // Überschneidung mit einem anderen Schiff
                }
            }
            for (int i = 0; i < laenge; i++) {
                spielfeld[xStart][yStart + i] = 'S';  // 'S' bedeutet ein Teil eines Schiffs
                schiffePlatzierenSpielerCanvas(xStart, yStart + i, true);
            }
        } else {
            if (xStart + laenge > 10) {
                System.out.println("Schiff passt nicht vertikal");
                return false;  // Schiff passt nicht vertikal
            }
            for (int i = 0; i < laenge; i++) {
                if (spielfeld[xStart + i][yStart] != '~') {
                    System.out.println("Überschneidung mit einem anderen Schiff");
                    return false;  // Überschneidung mit einem anderen Schiff
                }
            }
            for (int i = 0; i < laenge; i++) {
                spielfeld[xStart + i][yStart] = 'S';  // 'S' bedeutet ein Teil eines Schiffs
                schiffePlatzierenSpielerCanvas(xStart + i, yStart, false);
            }
        }
        return true;
    }

    // Interne Methode zur automatischen Platzierung der Schiffe für die KI
    private void schiffePlatzierenKI() {
        Random random = new Random();

        for (int i = 0; i < schiffLaengen.length; i++) {
            int laenge = schiffLaengen[i];
            boolean erfolgreich = false;

            // Wiederhole, bis das Schiff erfolgreich platziert wurde
            while (!erfolgreich) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                erfolgreich = schiffPlatzieren(spielfeldKI, xStart, yStart, laenge, horizontal);
            }
        }
        kiSchiffePlatziert = true;  // Status setzen, dass die KI ihre Schiffe platziert hat
    }
}
