import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spielfeld {
    Turtle t = new Turtle(500,600);
    KI ki;
    public char[][] spielfeldSpieler;  // 2D-Array für das Spielfeld des Spielers (10x10)
    public char[][] spielfeldKI;       // 2D-Array für das Spielfeld der KI (10x10)
    public List<Schiff> schiffeSpieler;  // Liste für die Schiffe des Spielers
    public List<Schiff> schiffeKI;  // Liste für die Schiffe der KI

    public final int[] schiffLaengen = {2, 3, 4, 5};  // Die Längen der Schiffe
    public int platzierteSchiffeSpieler = 0;  // Anzahl platzierter Schiffe des Spielers
    public boolean kiSchiffePlatziert = false;  // Status, ob die KI ihre Schiffe bereits platziert hat
    public int zuegeDesSpielers = 0; // Zählt die Züge des Spielers in einer Runde

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
        ki = new KI(this);  // Neues KI-Objekt wird hier erstellt
        // 1. Willkommensnachricht anzeigen
    zeichneStartbildschirm();

    // 2. Verzögerung von 20 Sekunden
    try {
        Thread.sleep(20000); // 20 Sekunden warten
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    // 3. Startbildschirm leeren und Spielfelder anzeigen
    t.reset(); // Entfernt den Startbildschirm
        spielfeldTurtle("Spieler");
        spielfeldTurtle("KI");
    }

    // Methode zur Visualisierung auf dem Browser
    public void spielfeldTurtle(String spielerTyp) {
    int x = 150; // Startposition X
    int y = spielerTyp.equals("Spieler") ? 80 : 305; // Unterschiedliche Y-Position für Spieler und KI
    t.textSize = 20;

    // Überschrift anzeigen
    t.left(90).moveTo(220, y).color(0,255,0);
    t.text(spielerTyp.equals("Spieler") ? "Spieler" : "Computer");
    t.right(90);
    t.color(0,0,255);
    // Spielfeld zeichnen
    y += 25; // Platz unter der Überschrift
    t.moveTo(x, y);
    for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
            // Quadrat zeichnen
            for (int k = 0; k < 4; k++) {
                t.forward(15).right(90);
            }
            t.penUp().forward(15).penDown(); // Nächstes Kästchen
        }
        y += 15; // Verschiebe Y-Position für die nächste Zeile
        t.penUp().moveTo(x, y).penDown();
    }t.color(0,0,0);
}

public void zeichneStartbildschirm() {
    t.left(90);
    t.moveTo(250, 100);
    t.textSize = 30;
    t.color(0,0,255);
    t.text("Willkommen zu Schiffe Versenken!");
    t.textSize = 20;
    t.moveTo(250, 250);
    t.text("Das Spiel startet gleich...");
    t.textSize = 15;
    t.moveTo(250, 300);
    t.text("Platzieren Sie bitte Ihre Schiffe!");
    t.color(0,0,0);
    t.right(90);
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
            ki.macheKIZuege();// KI macht ihre drei Züge
            return;
        }

        // Überprüfung, ob die Position bereits geschossen wurde
    if (spielfeldKI[zeile][spalte] == 'X' || spielfeldKI[zeile][spalte] == '0') {
        System.out.println("Du hast bereits auf diese Position geschossen! Wähle eine andere.");
        return;
    }
        
        // Überprüfen, ob der Schuss ein Treffer oder Fehlschuss ist
        if (spielfeldKI[zeile][spalte] == 'S') {
            System.out.println("Treffer!");
            spielfeldKI[zeile][spalte] = 'X';  // Markiere als Treffer
            zeichneTreffer(zeile, spalte,"KI");
            checkeUndMarkiereVollstaendigZerstörteSchiffeKI();
        } else {
            System.out.println("Fehlschuss!");
            spielfeldKI[zeile][spalte] = '0';  // Markiere als Fehlschuss
            zeichneFehlschuss(zeile, spalte,"KI");
        }

        zuegeDesSpielers++;
        if (zuegeDesSpielers == 3) {
            System.out.println("Du hast 3 Züge gemacht. Jetzt ist die KI dran.");
            ki.macheKIZuege();// KI macht ihre drei Züge
        }

        // Gewinnerprüfung nach dem Schuss
String gewinner = pruefeGewinner();
if (gewinner != null) {
    System.out.println(gewinner);
    //System.exit(0); // Spiel beenden
}

    }

    // Überprüft, ob ein Schiff vollständig zerstört wurde, und zeichnet eine rote Linie
    private void checkeUndMarkiereVollstaendigZerstörteSchiffeKI() {
    // Durchlaufe die Liste der Schiffe der KI mit einer normalen Schleife
    for (int i = 0; i < schiffeKI.size(); i++) {
        Schiff schiff = schiffeKI.get(i);
        boolean zerstört = true;

        // Überprüfen, ob das Schiff horizontal oder vertikal ist
        if (schiff.isHorizontal()) {
            // Überprüfen jedes Feld des Schiffes in horizontaler Richtung
            for (int j = 0; j < schiff.getLaenge(); j++) {
                if (spielfeldKI[schiff.getZeile()][schiff.getSpalte() + j] != 'X') {
                    zerstört = false; // Schiff ist noch nicht vollständig zerstört
                    break; // Verlasse die Schleife, wenn eines der Felder nicht getroffen wurde
                }
            }
        } else {
            // Überprüfen jedes Feld des Schiffes in vertikaler Richtung
            for (int j = 0; j < schiff.getLaenge(); j++) {
                if (spielfeldKI[schiff.getZeile() + j][schiff.getSpalte()] != 'X') {
                    zerstört = false; // Schiff ist noch nicht vollständig zerstört
                    break; // Verlasse die Schleife, wenn eines der Felder nicht getroffen wurde
                }
            }
        }

        // Wenn das Schiff vollständig zerstört wurde
        if (zerstört) {
            // Rote Linie auf das zerstörte Schiff zeichnen
            System.out.println("Ein Schiff der KI komplett zerstört");
            zeichneRoteLinie(schiff,"KI");
            schiffeKI.remove(i);  // Entferne das zerstörte Schiff aus der Liste der KI-Schiffe
            i--; // Reduziere den Index, um das nächste Schiff korrekt zu überprüfen (wegen Entfernen)
        }
    }
}


    public void checkeUndMarkiereVollstaendigZerstörteSchiffeSpieler() {
        // Durchlaufe die Liste der Schiffe des Spieler mit einer normalen Schleife
    for (int j = 0; j < schiffeSpieler.size(); j++) {
        Schiff schiff = schiffeSpieler.get(j);

        boolean zerstört = true;

        // Überprüfen, ob das Schiff horizontal oder vertikal ist
        if (schiff.isHorizontal()) {
            // Überprüfen jedes Feld des Schiffes in horizontaler Richtung
            for (int i = 0; i < schiff.getLaenge(); i++) {
                if (spielfeldSpieler[schiff.getZeile()][schiff.getSpalte() + i] != 'X') {
                    zerstört = false; // Schiff ist nicht vollständig zerstört
                    break; // Verlasse die Schleife, da das Schiff nicht zerstört ist
                }
            }
        } else {
            // Überprüfen jedes Feld des Schiffes in vertikaler Richtung
            for (int i = 0; i < schiff.getLaenge(); i++) {
                if (spielfeldSpieler[schiff.getZeile() + i][schiff.getSpalte()] != 'X') {
                    zerstört = false; // Schiff ist nicht vollständig zerstört
                    break; // Verlasse die Schleife, da das Schiff nicht zerstört ist
                }
            }
        }

        // Wenn das Schiff vollständig zerstört wurde
        if (zerstört) {
            // Rote Linie auf das zerstörte Schiff zeichnen
            System.out.println("Ein Schiff des Spielers komplett zerstört");
            zeichneRoteLinie(schiff,"Spieler");
            schiffeSpieler.remove(schiff); // Schiff aus der Liste der nicht zerstörten Schiffe entfernen
             j--; // Reduziere den Index, um das nächste Schiff korrekt zu überprüfen (wegen Entfernen)
        }
    }
}




public void zeichneTreffer(int zeile, int spalte, String spielfeld) {
   // t.right(0);
   int x = 0;
   int y = 0;
        if(spielfeld.equals("KI")){
            x = 150 ;
            y = 330;
        }
        if(spielfeld.equals("Spieler")){
            x = 150;
            y = 105;
        }
    int xStart = x + (spalte * 15);
    int yStart = y + (zeile * 15);

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
public void zeichneFehlschuss(int zeile, int spalte, String spielfeld) {
  //  t.right(0);
  int x = 0;
   int y = 0;
        if(spielfeld.equals("KI")){
            x = 150 ;
            y = 330;
        }
        if(spielfeld.equals("Spieler")){
            x = 150;
            y = 105;
        }

    t.textSize = 10;
    int xStart = x + (spalte * 15);
    int yStart = y + (zeile * 15);

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
    private void zeichneRoteLinie(Schiff schiff, String spielfeld) {
        double x = 0;
        double y = 0;
        if(spielfeld.equals("KI")){
            x = 150 ;
            y = 330;
        }
        if(spielfeld.equals("Spieler")){
            x = 150;
            y = 105;
        }
        double startX, startY, endX, endY;
        double squareLaenge = 15;

        if (schiff.isHorizontal()) {
            startX = x + (schiff.getSpalte() * squareLaenge);
            startY = y + (schiff.getZeile() * squareLaenge) + 7.5;
            endX = startX + (schiff.getLaenge() * squareLaenge);
            endY = startY;
        } else {
            startX = x + (schiff.getSpalte() * squareLaenge) + 7.5;
            startY = y + (schiff.getZeile() * squareLaenge);
            endX = startX;
            endY = startY + (schiff.getLaenge() * squareLaenge);
        }

        t.penUp();
        t.moveTo(startX, startY);
        t.penDown();
        t.color(255, 0, 0);  // Rote Farbe
        t.lineTo(endX, endY);
        t.color(0,0,0);
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


// Methode, um zu prüfen, ob ein Spieler verloren hat
public String pruefeGewinner() {
    // Überprüfe, ob der Spieler noch Schiffe hat
    boolean spielerHatSchiffe = false;
    for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
            if (spielfeldSpieler[i][j] == 'S') {
                spielerHatSchiffe = true;
                break;
            }
        }
        if (spielerHatSchiffe) break;
    }

    // Überprüfe, ob die KI noch Schiffe hat
    boolean kiHatSchiffe = false;
    for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
            if (spielfeldKI[i][j] == 'S') {
                kiHatSchiffe = true;
                break;
            }
        }
        if (kiHatSchiffe) break;
    }

    // Bestimme den Gewinner
    if (!spielerHatSchiffe) {
        //t.reset();
        t.left(90);
        t.textSize = 30;
        t.moveTo(250,520);
        t.color(255,0,0);  
        t.text("Computer hat gewonnen!");
        t.right(90);
        return "Computer hat gewonnen!";
    } else if (!kiHatSchiffe) {
        //t.reset();
        t.left(90);
        t.textSize = 30;
        t.moveTo(250,520);
        t.color(0,255,0);
        t.text("Sie haben gewonnen");
        t.right(90);
        return "Sie haben gewonnen!";
    }
    return null; // Noch kein Gewinner
}


}
