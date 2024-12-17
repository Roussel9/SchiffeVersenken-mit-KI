import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spielfeld {
    Turtle t = new Turtle();
    private char[][] spielfeldSpieler;  // 2D-Array für das Spielfeld des Spielers (10x10)
    private char[][] spielfeldKI;       // 2D-Array für das Spielfeld der KI (10x10)
    private List<Schiff> schiffeSpieler;  // Liste für die Schiffe des Spielers
    private List<Schiff> schiffeKI;  // Liste für die Schiffe der KI

    private final int[] schiffLaengen = {2, 3, 4, 5};  // Die Längen der Schiffe
    private int platzierteSchiffeSpieler = 0;  // Anzahl platzierter Schiffe des Spielers
    private boolean kiSchiffePlatziert = false;  // Status, ob die KI ihre Schiffe bereits platziert hat
    private int zuegeDesSpielers = 0; // Zählt die Züge des Spielers in einer Runde

    // Konstruktor: Erstellen und Initialisieren der Spielfelder und SchiffsListe
    public Spielfeld() {
        spielfeldKI = new char[10][10];
        spielfeldSpieler = new char[10][10];
        schiffeSpieler = new ArrayList<>();
        schiffeKI = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                spielfeldKI[i][j] = '~';  // "~" für Wasser
                spielfeldSpieler[i][j] = '~';   // "~" für Wasser
            }
        }
    }

    // toString-Methode zum Darstellen des Spielfeldes
    @Override
    public String toString() {
        int x = 150;
        int y = 80;
        t.textSize = 20;
        t.moveTo(x, y);
        StringBuilder sb = new StringBuilder();
        t.left(90).moveTo(220, y).text("Spieler").right(90);
        y = y + 25;
        t.moveTo(x, y);
        sb.append(" Spieler:\n");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 1; k <= 4; k++) {
                    t.forward(15).right(90);
                }
                t.forward(15);
                sb.append(spielfeldSpieler[i][j] + " ");
            }
            y = y + 15;
            t.moveTo(x, y);
            sb.append("\n");
        }
        y = y + 50;

        t.moveTo(x, y).left(90).moveTo(220, y).text("Computer").right(90);
        y = y + 25;
        t.moveTo(x, y);
        sb.append(" Computer:\n");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 1; k <= 4; k++) {
                    t.forward(15).right(90);
                }
                t.forward(15);
                sb.append(spielfeldKI[i][j] + " ");
            }
            y = y + 15;
            t.moveTo(x, y);
            sb.append("\n");
        }
        return sb.toString();
    }

    // Methode zum Platzieren eines Schiffs auf dem Spielfeld des Spielers
    public boolean schiffPlatzierenSpieler(int zeile, int spalte, boolean horizontal) {
        if (platzierteSchiffeSpieler >= schiffLaengen.length) {
            System.out.println("Alle Schiffe wurden bereits platziert.");
            return false;
        }

        int laenge = schiffLaengen[platzierteSchiffeSpieler];
        if (schiffPlatzieren(spielfeldSpieler, zeile, spalte, laenge, horizontal)) { 
            Schiff neuesSchiff = new Schiff(zeile, spalte, laenge, horizontal);
            schiffeSpieler.add(neuesSchiff);
          //  System.out.println(schiffeSpieler);
            platzierteSchiffeSpieler++;

            // Wenn der Spieler alle Schiffe platziert hat, platziert die KI automatisch ihre Schiffe
            if (platzierteSchiffeSpieler == schiffLaengen.length && !kiSchiffePlatziert) {
                schiffePlatzierenKI();
            }
            return true;
        }
        return false;
    }

    // Methode zum Platzieren eines Schiffs auf dem Spielfeld
    private boolean schiffPlatzieren(char[][] spielfeld, int xStart, int yStart, int laenge, boolean horizontal) {
        if (horizontal) {
            if (yStart + laenge > 10) {
                return false;  
            }
            for (int i = 0; i < laenge; i++) {
                if (spielfeld[xStart][yStart + i] != '~') {
                    return false;  
                }
            }
            for (int i = 0; i < laenge; i++) {
                spielfeld[xStart][yStart + i] = 'S';
                schiffePlatzierenSpielerTurtle(xStart,yStart + i,true);
            }
        } else {
            if (xStart + laenge > 10) {
                return false;  
            }
            for (int i = 0; i < laenge; i++) {
                if (spielfeld[xStart + i][yStart] != '~') {
                    return false;  
                }
            }
            for (int i = 0; i < laenge; i++) {
                spielfeld[xStart + i][yStart] = 'S';
                schiffePlatzierenSpielerTurtle(xStart + i, yStart,false);
            }
        }
        return true;
    }

    // Methode für den Spieler, um einen Schuss abzugeben
    public void schussAbgebenSpieler(int zeile, int spalte) {
        if (zuegeDesSpielers >= 3) {
            System.out.println("Du hast bereits 3 Züge gemacht. Nun ist die KI dran.");
            return;
        }
        
        // Überprüfen, ob der Schuss ein Treffer oder Fehlschuss ist
        if (spielfeldKI[zeile][spalte] == 'S') {
            System.out.println("Treffer!");
            spielfeldKI[zeile][spalte] = 'X';  // Markiere als Treffer
            zeichneTreffer(zeile, spalte);
            checkeUndMarkiereVollstaendigZerstörteSchiffe();
        } else {
            System.out.println("Fehlschuss!");
            spielfeldKI[zeile][spalte] = '0';  // Markiere als Fehlschuss
            zeichneFehlschuss(zeile, spalte);
        }

        zuegeDesSpielers++;
        if (zuegeDesSpielers == 3) {
            System.out.println("Du hast 3 Züge gemacht. Jetzt ist die KI dran.");
        }
    }

    // Überprüft, ob ein Schiff vollständig zerstört wurde, und zeichnet eine rote Linie
    private void checkeUndMarkiereVollstaendigZerstörteSchiffe() {
        for (Schiff schiff : schiffeKI) {
            boolean zerstört = true;
            if (schiff.isHorizontal()) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    if (spielfeldKI[schiff.getZeile()][schiff.getSpalte() + i] != 'X') {
                        zerstört = false;
                        break;// Verlasse die Schleife für dieses Schiff
                    }
                }
            } else {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    if (spielfeldKI[schiff.getZeile() + i][schiff.getSpalte()] != 'X') {
                        zerstört = false;
                        break;
                    }
                }
            }

            if (zerstört) {
                // Rote Linie auf das zerstörte Schiff zeichnen
                System.out.println("Ein Schiff komplett zerstört");
                zeichneRoteLinie(schiff);
                return;
            }
        }
    }



private void zeichneTreffer(int zeile, int spalte) {
   // t.right(0);
    int xStart = 150 + (spalte * 15);
    int yStart = 330 + (zeile * 15);

    t.penUp();
    t.moveTo(xStart, yStart);
    t.right(45);
    t.penDown();
    t.color(255, 0, 0); // Rote Farbe für Treffer
    t.forward(20);
    t.penUp().right(135).forward(15).right(135).penDown();
    t.forward(20);
    t.color(0,0,0);
    t.right(45);
}

// Methode zum Zeichnen der "0" für einen Fehlschuss
private void zeichneFehlschuss(int zeile, int spalte) {
  //  t.right(0);
    t.textSize = 10;
    int xStart = 150 + (spalte * 15);
    int yStart = 330 + (zeile * 15);

    t.penUp();
    t.moveTo(xStart, yStart);
    t.forward(7.5).right(90).forward(7.5);
    t.penDown();
    t.color(255,105,180); // pink Farbe für Fehlschuss
    t.text("0");
    t.color(0,0,0);
    t.left(90);
}


    // Zeichnet eine rote Linie über das zerstörte Schiff
    private void zeichneRoteLinie(Schiff schiff) {
        int startX, startY, endX, endY;
        int squareLaenge = 15;

        if (schiff.isHorizontal()) {
            startX = 150 + (schiff.getSpalte() * squareLaenge);
            startY = 105 + (schiff.getZeile() * squareLaenge);
            endX = startX + (schiff.getLaenge() * squareLaenge);
            endY = startY;
        } else {
            startX = 150 + (schiff.getSpalte() * squareLaenge);
            startY = 105 + (schiff.getZeile() * squareLaenge);
            endX = startX;
            endY = startY + (schiff.getLaenge() * squareLaenge);
        }

        t.penUp();
        t.moveTo(startX, startY);
        t.penDown();
        t.color(255, 0, 0);  // Rote Farbe
        t.lineTo(endX, endY);
    }

    // Methode zum Platzieren der Schiffe der KI (derzeit zufällig)
    private void schiffePlatzierenKI() {
        Random random = new Random();


        for (int i = 0; i < schiffLaengen.length; i++) {
            int laenge = schiffLaengen[i];
            boolean erfolgreich = false;

            // Versuche so lange ein Schiff zu platzieren, bis es funktioniert
            while (!erfolgreich) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Versuche, das Schiff zu platzieren
                erfolgreich = schiffPlatzieren(spielfeldKI, xStart, yStart, laenge, horizontal);

                // Wenn erfolgreich, speichere das Schiff
                if (erfolgreich) {
                    Schiff neuesSchiff = new Schiff(xStart, yStart, laenge, horizontal);
                    schiffeKI.add(neuesSchiff);
                }
            }
        }

        kiSchiffePlatziert = true;
    }


    public void schiffePlatzierenSpielerTurtle(int row, int col, boolean horizontal) {
    int startX, startY;
    if (platzierteSchiffeSpieler == schiffLaengen.length && !kiSchiffePlatziert) {
                 startX = 150;  // Basis-X-Koordinate des Spielfelds
                 startY = 330;
                 t.color(0,0,255);
            }else{
                 startX = 150;  // Basis-X-Koordinate des Spielfelds
                 startY = 105;  // 
            }
    t.textSize = 10;
    int squareLaenge = 15;  // Größe eines Quadrats

    // Berechne die Zielposition für das Quadrat
    int targetX = startX + (col * squareLaenge);
    int targetY = startY + (row * squareLaenge);

    // Bewege die Turtle zur Position
    t.penUp();
    t.moveTo(targetX, targetY);
    t.penDown();

    // Jetzt in der Mitte des Quadrats das "S" platzieren
    // Berechnung der Mitte des Quadrats
    int textX = targetX + squareLaenge / 2;  // X-Position für Text
    int textY = targetY + squareLaenge / 2;  // Y-Position für Text

    // Turtle bewegt sich zur exakten Textposition
    t.penUp();
    t.right(90);
    t.moveTo(textX, textY); // Bewege zur Mitte des Quadrats
    t.penDown();
    t.text("S");
    t.left(90);
    t.color(0,0,0);  // Zeichne das "S" in der Mitte
}
}
