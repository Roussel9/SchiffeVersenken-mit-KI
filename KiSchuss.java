import java.util.ArrayList;
import java.util.List;

class KI {
    private char[][] sichtbaresSpielfeldKI;
    private Spielfeld spielfeld;
    private List<int[]> letzteTreffer = new ArrayList<>(); // Speichert die Positionen der letzten Treffer

    KI(Spielfeld spielfeld) {
        this.spielfeld = spielfeld;

        sichtbaresSpielfeldKI = new char[10][10];
        for (int zeile = 0; zeile < 10; zeile++) {
            for (int spalte = 0; spalte < 10; spalte++) {
                sichtbaresSpielfeldKI[zeile][spalte] = '~'; // Alles unbekannt
            }
        }

        spielfeld.zuegeDesSpielers = 0; // Anfangszustand
    }

    // KI führt ihre drei Züge aus
    public void macheKIZuege() {
        for (int i = 0; i < 3; i++) { // KI macht drei Züge
            int[] zug = findeBestenZug(); // Minimax-basierten Zug finden
            int zeile = zug[0];
            int spalte = zug[1];

            // Schuss der KI
            macheSchussKI(zeile, spalte);

            // Wartezeit zwischen Zügen der KI (realistischere Simulation)
            try {
                Thread.sleep(1000); // 1 Sekunde Pause zwischen den Zügen
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        spielfeld.zuegeDesSpielers = 0; // Spieler kann danach wieder spielen
    // Gewinnerprüfung nach dem Schuss
String gewinner = spielfeld.pruefeGewinner();
if (gewinner != null) {
    System.out.println(gewinner);
    //System.exit(0); // Spiel beenden
}

    }

    private int[] findeBestenZug() {
        // 1. Wenn es letzte Treffer gibt, fokussiere dich darauf
        if (!letzteTreffer.isEmpty()) {
            return findeGezieltenZug();
        }

        // 2. Sonst suche das beste Feld basierend auf der heuristischen Bewertung
        return findeHeuristischenZug();
    }

    private int[] findeGezieltenZug() {
        int[] ersteTreffer = letzteTreffer.get(0);
        int[] letzteTrefferPosition = letzteTreffer.get(letzteTreffer.size() - 1);

        int zeile = ersteTreffer[0];
        int spalte = ersteTreffer[1];

        // 1. Prüfe, ob es eine Richtung (horizontal oder vertikal) gibt
        boolean horizontal = letzteTreffer.size() > 1 && ersteTreffer[0] == letzteTrefferPosition[0];
        boolean vertikal = letzteTreffer.size() > 1 && ersteTreffer[1] == letzteTrefferPosition[1];

        // 2. Schieße in der identifizierten Richtung weiter
        if (horizontal) {
            // Versuche rechts
            int rechteSpalte = letzteTrefferPosition[1] + 1;
            if (rechteSpalte < 10 && sichtbaresSpielfeldKI[zeile][rechteSpalte] == '~') {
                return new int[]{zeile, rechteSpalte};
            }

            // Versuche links
            int linkeSpalte = ersteTreffer[1] - 1;
            if (linkeSpalte >= 0 && sichtbaresSpielfeldKI[zeile][linkeSpalte] == '~') {
                return new int[]{zeile, linkeSpalte};
            }
        }

        if (vertikal) {
            // Versuche unten
            int untereZeile = letzteTrefferPosition[0] + 1;
            if (untereZeile < 10 && sichtbaresSpielfeldKI[untereZeile][spalte] == '~') {
                return new int[]{untereZeile, spalte};
            }

            // Versuche oben
            int obereZeile = ersteTreffer[0] - 1;
            if (obereZeile >= 0 && sichtbaresSpielfeldKI[obereZeile][spalte] == '~') {
                return new int[]{obereZeile, spalte};
            }
        }

        // 3. Wenn keine Richtung erkennbar ist, prüfe alle angrenzenden Felder
        for (int[] richtung : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
            int neueZeile = letzteTrefferPosition[0] + richtung[0];
            int neueSpalte = letzteTrefferPosition[1] + richtung[1];

            if (neueZeile >= 0 && neueZeile < 10 && neueSpalte >= 0 && neueSpalte < 10 &&
                    sichtbaresSpielfeldKI[neueZeile][neueSpalte] == '~') {
                return new int[]{neueZeile, neueSpalte};
            }
        }

        // 4. Fallback, falls keine Treffer gefunden werden
        return findeHeuristischenZug();
    }

    private int[] findeHeuristischenZug() {
        int besterWert = Integer.MIN_VALUE;
        int besteZeile = -1;
        int besteSpalte = -1;

        // Durchlaufe alle Felder, um das beste Feld zu finden
        for (int zeile = 0; zeile < 10; zeile++) {
            for (int spalte = 0; spalte < 10; spalte++) {
                if (sichtbaresSpielfeldKI[zeile][spalte] == '~') {
                    int wert = bewerteFeld(zeile, spalte);
                    if (wert > besterWert) {
                        besterWert = wert;
                        besteZeile = zeile;
                        besteSpalte = spalte;
                    }
                }
            }
        }

        return new int[]{besteZeile, besteSpalte};
    }

    private int bewerteFeld(int zeile, int spalte) {
        int wert = 0;

        // 1. Bonuspunkte für Felder in der Mitte des Spielfelds
        wert += 10 - Math.abs(zeile - 4) - Math.abs(spalte - 4);

        // 2. Bonuspunkte für angrenzende Treffer
        int[][] richtungen = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] richtung : richtungen) {
            int neueZeile = zeile + richtung[0];
            int neueSpalte = spalte + richtung[1];
            if (neueZeile >= 0 && neueZeile < 10 && neueSpalte >= 0 && neueSpalte < 10) {
                if (sichtbaresSpielfeldKI[neueZeile][neueSpalte] == 'X') {
                    wert += 15; // Bonus für angrenzende Treffer
                }
            }
        }

        return wert;
    }

    private void macheSchussKI(int zeile, int spalte) {
        if (spielfeld.spielfeldSpieler[zeile][spalte] == 'S') { // Überprüfung des Spieler-Spielfelds
            System.out.println("KI Treffer bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'X'; // Treffer sichtbar machen für KI
            spielfeld.spielfeldSpieler[zeile][spalte] = 'X'; // Original-Spielfeld anpassen
            letzteTreffer.add(new int[]{zeile, spalte}); // Speichern des letzten Treffers
            spielfeld.zeichneTreffer(zeile, spalte,"Spieler");
            // Prüfen und Markieren der zerstörten Schiffe des Spielers
            spielfeld.checkeUndMarkiereVollstaendigZerstörteSchiffeSpieler();
        } else {
            System.out.println("KI Fehlschuss bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'O'; // Fehlschuss sichtbar machen
            spielfeld.spielfeldSpieler[zeile][spalte] = 'O'; // Original-Spielfeld anpassen
            spielfeld.zeichneFehlschuss(zeile, spalte,"Spieler");
        }
    }
}




/*
import java.util.ArrayList;
import java.util.List;

class KI {
    private char[][] sichtbaresSpielfeldKI;
    private Spielfeld spielfeld;
    private List<int[]> letzteTreffer = new ArrayList<>(); // Speichert die Positionen der letzten Treffer
    private boolean verfolgtSchiff = false; // Gibt an, ob die KI ein Schiff verfolgt
    private boolean horizontalVerfolgung = false; // Gibt die Richtung der Verfolgung an
    private boolean vertikalVerfolgung = false;
    
    // Liste der möglichen Züge, die weit voneinander entfernt liegen
    private List<int[]> zugListe = new ArrayList<>();
    private int zugIndex = 0; // Zeigt den aktuellen Zug an
    
    KI(Spielfeld spielfeld) {
        this.spielfeld = spielfeld;

        sichtbaresSpielfeldKI = new char[10][10];
        for (int zeile = 0; zeile < 10; zeile++) {
            for (int spalte = 0; spalte < 10; spalte++) {
                sichtbaresSpielfeldKI[zeile][spalte] = '~'; // Alles unbekannt
            }
        }

        spielfeld.zuegeDesSpielers = 0; // Anfangszustand
        
        // Initialisiere die Liste der Felder, die weit auseinander liegen
        erstelleZugListe();
    }

    // Funktion zur Erstellung der Zugliste mit weit auseinander liegenden Feldern
    private void erstelleZugListe() {
        // Hier definieren wir gezielt Felder, die weit voneinander entfernt liegen
        int[][] felder = {
            {0, 0}, {0, 9}, {9, 0}, {9, 9},  // Ecken des Spielfelds
            {1, 3}, {3, 1}, {7, 7}, {5, 0},  // Weitere zufällige Felder, die auseinander liegen
            {2, 8}, {8, 2}, {4, 6}, {6, 4}
        };

        for (int[] feld : felder) {
            zugListe.add(feld);
        }
    }

    public void macheKIZuege() {
        for (int i = 0; i < 3; i++) { // KI macht drei Züge
            int[] zug = findeBestenZug(); // Minimax-basierten Zug finden
            int zeile = zug[0];
            int spalte = zug[1];

            // Schuss der KI
            macheSchussKI(zeile, spalte);

            // Wartezeit zwischen Zügen der KI (realistischere Simulation)
            try {
                Thread.sleep(1000); // 1 Sekunde Pause zwischen den Zügen
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        spielfeld.zuegeDesSpielers = 0; // Spieler kann danach wieder spielen
    }

    private int[] findeBestenZug() {
        // Wenn ein Schiff verfolgt wird, fokussiere dich auf die Verfolgung des Schiffs
        if (verfolgtSchiff) {
            return findeGezieltenZug();
        }

        // Wenn keine gezielte Verfolgung vorliegt, wähle den nächsten Zug aus der Liste
        return findeZugAusListe();
    }

    private int[] findeGezieltenZug() {
        // Falls ein Treffer gemacht wurde, überprüfen wir, ob wir noch in der gleichen Richtung schießen müssen.
        if (!letzteTreffer.isEmpty()) {
            int[] letzteTrefferPosition = letzteTreffer.get(letzteTreffer.size() - 1);
            int zeile = letzteTrefferPosition[0];
            int spalte = letzteTrefferPosition[1];

            // Wenn horizontal verfolgt wird
            if (horizontalVerfolgung) {
                // Versuche nach rechts
                if (spalte + 1 < 10 && sichtbaresSpielfeldKI[zeile][spalte + 1] == '~') {
                    return new int[]{zeile, spalte + 1};
                }

                // Versuche nach links
                if (spalte - 1 >= 0 && sichtbaresSpielfeldKI[zeile][spalte - 1] == '~') {
                    return new int[]{zeile, spalte - 1};
                }
            }

            // Wenn vertikal verfolgt wird
            if (vertikalVerfolgung) {
                // Versuche nach unten
                if (zeile + 1 < 10 && sichtbaresSpielfeldKI[zeile + 1][spalte] == '~') {
                    return new int[]{zeile + 1, spalte};
                }

                // Versuche nach oben
                if (zeile - 1 >= 0 && sichtbaresSpielfeldKI[zeile - 1][spalte] == '~') {
                    return new int[]{zeile - 1, spalte};
                }
            }
        }

        // Wenn keine Verfolgung erfolgt, gehe zu einem neuen Zug aus der Liste
        return findeZugAusListe();
    }

    private int[] findeZugAusListe() {
        if (zugIndex < zugListe.size()) {
            // Nächster Zug aus der Liste, der weit auseinander liegende Felder berücksichtigt
            int[] zug = zugListe.get(zugIndex);
            zugIndex++; // Nächsten Zug vorbereiten
            return zug;
        }
        // Falls keine Züge mehr verfügbar sind, gebe ein Standardfeld zurück
        return new int[]{0, 0};
    }

    private void macheSchussKI(int zeile, int spalte) {
        if (spielfeld.spielfeldSpieler[zeile][spalte] == 'S') { // Überprüfung des Spieler-Spielfelds
            System.out.println("KI Treffer bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'X'; // Treffer sichtbar machen für KI
            spielfeld.spielfeldSpieler[zeile][spalte] = 'X'; // Original-Spielfeld anpassen
            letzteTreffer.add(new int[]{zeile, spalte}); // Speichern des letzten Treffers

            // Wenn ein Schiff getroffen wurde, verfolge das Schiff in der Richtung
            if (letzteTreffer.size() == 1) {
                // Anfang der Verfolgung
                verfolgtSchiff = true;
                horizontalVerfolgung = true; // Initiale Annahme: Horizontal
            }

            // Prüfen und Markieren der zerstörten Schiffe des Spielers
            spielfeld.checkeUndMarkiereVollstaendigZerstörteSchiffeSpieler();
        } else {
            System.out.println("KI Fehlschuss bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'O'; // Fehlschuss sichtbar machen
            spielfeld.spielfeldSpieler[zeile][spalte] = 'O'; // Original-Spielfeld anpassen
        }
    }
}
*/

/*
import java.util.ArrayList;
import java.util.List;

class KI {
    private char[][] sichtbaresSpielfeldKI;
    private Spielfeld spielfeld;
    private List<int[]> letzteTreffer = new ArrayList<>(); // Speichert die Positionen der letzten Treffer
    private boolean verfolgtSchiff = false; // Gibt an, ob die KI ein Schiff verfolgt
    private boolean horizontalVerfolgung = false; // Gibt die Richtung der Verfolgung an
    private boolean vertikalVerfolgung = false;
    
    // Liste der möglichen Züge, die weit voneinander entfernt liegen
    private List<int[]> zugListe = new ArrayList<>();
    private int zugIndex = 0; // Zeigt den aktuellen Zug an
    
    KI(Spielfeld spielfeld) {
        this.spielfeld = spielfeld;

        sichtbaresSpielfeldKI = new char[10][10];
        for (int zeile = 0; zeile < 10; zeile++) {
            for (int spalte = 0; spalte < 10; spalte++) {
                sichtbaresSpielfeldKI[zeile][spalte] = '~'; // Alles unbekannt
            }
        }

        spielfeld.zuegeDesSpielers = 0; // Anfangszustand
        
        // Initialisiere die Liste der Felder, die weit auseinander liegen
        erstelleZugListe();
    }

    // Funktion zur Erstellung der Zugliste mit weit auseinander liegenden Feldern
    private void erstelleZugListe() {
        // Hier definieren wir gezielt Felder, die weit voneinander entfernt liegen
        int[][] felder = {
            {0, 0}, {0, 9}, {9, 0}, {9, 9},  // Ecken des Spielfelds
            {1, 3}, {3, 1}, {7, 7}, {5, 0},  // Weitere zufällige Felder, die auseinander liegen
            {2, 8}, {8, 2}, {4, 6}, {6, 4}
        };

        for (int[] feld : felder) {
            zugListe.add(feld);
        }
    }

    public void macheKIZuege() {
        for (int i = 0; i < 3; i++) { // KI macht drei Züge
            int[] zug = findeBestenZug(); // Minimax-basierten Zug finden
            int zeile = zug[0];
            int spalte = zug[1];

            // Schuss der KI
            macheSchussKI(zeile, spalte);

            // Wartezeit zwischen Zügen der KI (realistischere Simulation)
            try {
                Thread.sleep(1000); // 1 Sekunde Pause zwischen den Zügen
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        spielfeld.zuegeDesSpielers = 0; // Spieler kann danach wieder spielen
    }

    private int[] findeBestenZug() {
        // Wenn ein Schiff verfolgt wird, fokussiere dich auf die Verfolgung des Schiffs
        if (verfolgtSchiff) {
            return findeGezieltenZug();
        }

        // Wenn keine gezielte Verfolgung vorliegt, wähle den nächsten Zug aus der Liste
        return findeZugAusListe();
    }

    private int[] findeGezieltenZug() {
        // Falls ein Treffer gemacht wurde, überprüfen wir, ob wir noch in der gleichen Richtung schießen müssen.
        if (!letzteTreffer.isEmpty()) {
            int[] letzteTrefferPosition = letzteTreffer.get(letzteTreffer.size() - 1);
            int zeile = letzteTrefferPosition[0];
            int spalte = letzteTrefferPosition[1];

            // Wenn horizontal verfolgt wird
            if (horizontalVerfolgung) {
                // Versuche nach rechts
                if (spalte + 1 < 10 && sichtbaresSpielfeldKI[zeile][spalte + 1] == '~') {
                    return new int[]{zeile, spalte + 1};
                }

                // Versuche nach links
                if (spalte - 1 >= 0 && sichtbaresSpielfeldKI[zeile][spalte - 1] == '~') {
                    return new int[]{zeile, spalte - 1};
                }
            }

            // Wenn vertikal verfolgt wird
            if (vertikalVerfolgung) {
                // Versuche nach unten
                if (zeile + 1 < 10 && sichtbaresSpielfeldKI[zeile + 1][spalte] == '~') {
                    return new int[]{zeile + 1, spalte};
                }

                // Versuche nach oben
                if (zeile - 1 >= 0 && sichtbaresSpielfeldKI[zeile - 1][spalte] == '~') {
                    return new int[]{zeile - 1, spalte};
                }
            }
        }

        // Wenn keine Verfolgung erfolgt, gehe zu einem neuen Zug aus der Liste
        return findeZugAusListe();
    }

    private int[] findeZugAusListe() {
        if (zugIndex < zugListe.size()) {
            // Nächster Zug aus der Liste, der weit auseinander liegende Felder berücksichtigt
            int[] zug = zugListe.get(zugIndex);
            zugIndex++; // Nächsten Zug vorbereiten
            return zug;
        }

        // Falls keine Züge mehr verfügbar sind, gebe ein Standardfeld zurück
        return new int[]{0, 0};
    }

    private void macheSchussKI(int zeile, int spalte) {
        if (spielfeld.spielfeldSpieler[zeile][spalte] == 'S') { // Überprüfung des Spieler-Spielfelds
            System.out.println("KI Treffer bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'X'; // Treffer sichtbar machen für KI
            spielfeld.spielfeldSpieler[zeile][spalte] = 'X'; // Original-Spielfeld anpassen
            letzteTreffer.add(new int[]{zeile, spalte}); // Speichern des letzten Treffers

            // Wenn ein Schiff getroffen wurde, verfolge das Schiff in der Richtung
            if (letzteTreffer.size() == 1) {
                // Anfang der Verfolgung
                verfolgtSchiff = true;
                horizontalVerfolgung = true; // Initiale Annahme: Horizontal
            }

            // Prüfen und Markieren der zerstörten Schiffe des Spielers
            spielfeld.checkeUndMarkiereVollstaendigZerstörteSchiffeSpieler();
        } else {
            System.out.println("KI Fehlschuss bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'O'; // Fehlschuss sichtbar machen
            spielfeld.spielfeldSpieler[zeile][spalte] = 'O'; // Original-Spielfeld anpassen

            // Wenn ein Fehlschuss ist, überprüfe benachbarte Felder
            pruefeBenachbarteFelder(zeile, spalte);
        }
    }

    // Prüft die benachbarten Felder eines Fehlschusses
    private void pruefeBenachbarteFelder(int zeile, int spalte) {
        int[][] benachbarteFelder = {
            {zeile - 1, spalte}, {zeile + 1, spalte}, {zeile, spalte - 1}, {zeile, spalte + 1}
        };

        for (int[] feld : benachbarteFelder) {
            int benachbarteZeile = feld[0];
            int benachbarteSpalte = feld[1];

            if (benachbarteZeile >= 0 && benachbarteZeile < 10 && benachbarteSpalte >= 0 && benachbarteSpalte < 10) {
                if (sichtbaresSpielfeldKI[benachbarteZeile][benachbarteSpalte] == '~') {
                    // Füge benachbartes Feld zur Zugliste hinzu
                    zugListe.add(new int[]{benachbarteZeile, benachbarteSpalte});
                }
            }
        }
    }
}
*/