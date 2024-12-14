import java.util.Random;

public class Spielfeld {
Turtle t = new Turtle();
    private char[][] spielfeldSpieler;  // 2D-Array für das Spielfeld des Spielers (10x10)
    private char[][] spielfeldKI;       // 2D-Array für das Spielfeld der KI (10x10)

    private final int[] schiffLaengen = {2, 3, 4, 5};  // Die Längen der Schiffe
    private int platzierteSchiffeSpieler = 0;  // Anzahl platzierter Schiffe des Spielers
    private boolean kiSchiffePlatziert = false;  // Status, ob die KI ihre Schiffe bereits platziert hat

    // Konstruktor: Erstellen und Initialisieren der Spielfelder
    public Spielfeld() {
        // Initialisierung der Spielfelder (beide mit Wasser füllen)
        spielfeldKI = new char[10][10];
        spielfeldSpieler = new char[10][10];
        
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
        t.moveTo(x,y);
        StringBuilder sb = new StringBuilder();
        t.left(90).moveTo(220,y).text("Spieler").right(90);
        y = y + 25;
        t.moveTo(x,y);
        sb.append(" Spieler:\n");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for(int k= 1; k<=4;k++){
                    t.forward(15).right(90);
                }t.forward(15);
                sb.append(spielfeldSpieler[i][j] + " ");
            }y = y +15;
            t.moveTo(x,y);
            sb.append("\n");
        }y = y + 50;
        

         t.moveTo(x,y).left(90).moveTo(220,y).text("Computer").right(90);
         y = y + 25;
        t.moveTo(x,y);
        sb.append(" Computer:\n");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for(int k = 1; k<=4;k++){
                    t.forward(15).right(90);
                }t.forward(15);
                sb.append(spielfeldKI[i][j] + " ");
            }y = y + 15;
            t.moveTo(x,y);
            sb.append("\n");
        }
       // schiffBau();
        return sb.toString();
    }

    // Methode zum Platzieren eines Schiffs auf dem Spielfeld des Spielers
    public boolean schiffPlatzierenSpieler(int zeile, int spalte, boolean horizontal) {
        if (platzierteSchiffeSpieler >= schiffLaengen.length) {
            System.out.println("Alle Schiffe wurden bereits platziert.");
            return false;
        }

        int laenge = schiffLaengen[platzierteSchiffeSpieler];
        if (schiffPlatzieren(spielfeldSpieler, zeile, spalte, laenge, horizontal)) { //ruft die methode , um Schiffe zu platzieren und dann wird true wenn der erste Schiff plaztiert ist
            
            platzierteSchiffeSpieler++;
            
            // Wenn der Spieler alle Schiffe platziert hat, platziert die KI automatisch ihre Schiffe
            if (platzierteSchiffeSpieler == schiffLaengen.length && !kiSchiffePlatziert) {
                schiffePlatzierenKI();
            }
            return true;
        }
        return false;
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
                spielfeld[xStart][yStart + i] = 'S';
                schiffePlatzierenSpielerTurtle(xStart,yStart + i,true);
            }
        } else {
            if (xStart + laenge > 10) {
                System.out.println(" Schiff passt nicht vertikal");
                return false;  // Schiff passt nicht vertikal
            }
            for (int i = 0; i < laenge; i++) {
                if (spielfeld[xStart + i][yStart] != '~') {
                    System.out.println("Überschneidung mit einem anderen Schiff");
                    return false;  // Überschneidung mit einem anderen Schiff
                }
            }
            for (int i = 0; i < laenge; i++) {
                spielfeld[xStart + i][yStart] = 'S';
                schiffePlatzierenSpielerTurtle(xStart + i, yStart,false);
            }
        }
        return true;
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
