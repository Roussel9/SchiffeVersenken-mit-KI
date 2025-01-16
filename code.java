/*

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class KI {
    private char[][] sichtbaresSpielfeldKI;
    private List<int[]> verbleibendeZuege;
    Spielfeld spielfeld;

    KI(Spielfeld spielfeld) {
        this.spielfeld = spielfeld;

        sichtbaresSpielfeldKI = new char[10][10];
        verbleibendeZuege = new ArrayList<>();
        for (int zeile = 0; zeile < 10; zeile++) {
            for (int spalte = 0; spalte < 10; spalte++) {
                sichtbaresSpielfeldKI[zeile][spalte] = '~'; // Alles unbekannt
                verbleibendeZuege.add(new int[]{zeile, spalte}); // Alle Züge sind zunächst möglich
            }
        }

        spielfeld.zuegeDesSpielers = 0; // Anfangszustand
    }

    public void macheKIZuege() {
        for (int i = 0; i < 3; i++) {
            int[] zug = findeBestenZug(); // Bessere Logik für Prioritäten
            if (zug == null) break; // Keine Züge mehr verfügbar

            int zeile = zug[0];
            int spalte = zug[1];

            macheSchussKI(zeile, spalte);

            // Wartezeit zwischen Zügen
            try {
                Thread.sleep(2000); // 2 Sekunden
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        spielfeld.zuegeDesSpielers = 0; // Spieler kann danach wieder spielen
    }

    private int[] findeBestenZug() {
        PriorityQueue<int[]> priorisierteZuege = new PriorityQueue<>((zug1, zug2) -> 
            Integer.compare(bewerteZug(zug2[0], zug2[1]), bewerteZug(zug1[0], zug1[1]))
        );

        for (int[] zug : verbleibendeZuege) {
            int zeile = zug[0];
            int spalte = zug[1];
            priorisierteZuege.add(new int[]{zeile, spalte});
        }

        if (!priorisierteZuege.isEmpty()) {
            return priorisierteZuege.poll(); // Nimmt den besten Zug
        }
        return null; // Keine Züge mehr übrig
    }

    private int bewerteZug(int zeile, int spalte) {
        int wert = 0;

        // Priorisiere mittlere Felder
        int distanzZurMitte = Math.abs(zeile - 5) + Math.abs(spalte - 5);
        wert += (10 - distanzZurMitte); // Zentralere Felder sind wertvoller

        // Bonus für Treffer in der Nähe
        wert += bewertungNaheFelder(zeile, spalte);

        return wert;
    }

    private int bewertungNaheFelder(int zeile, int spalte) {
        int bonus = 0;
        int[][] richtungen = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] richtung : richtungen) {
            int neueZeile = zeile + richtung[0];
            int neueSpalte = spalte + richtung[1];
            if (neueZeile >= 0 && neueZeile < 10 && neueSpalte >= 0 && neueSpalte < 10) {
                if (sichtbaresSpielfeldKI[neueZeile][neueSpalte] == 'X') {
                    bonus += 20; // Höherer Bonus für angrenzende Treffer
                } else if (sichtbaresSpielfeldKI[neueZeile][neueSpalte] == '~') {
                    bonus += 5; // Bonus für angrenzende unbekannte Felder
                }
            }
        }
        return bonus;
    }

    private void macheSchussKI(int zeile, int spalte) {
        verbleibendeZuege.removeIf(zug -> zug[0] == zeile && zug[1] == spalte); // Zug aus Liste entfernen

        if (spielfeld.spielfeldSpieler[zeile][spalte] == 'S') { // Überprüfung des Spieler-Spielfelds
            System.out.println("KI Treffer bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'X'; // Treffer sichtbar machen
            spielfeld.spielfeldSpieler[zeile][spalte] = 'X'; // Original-Spielfeld anpassen
        } else {
            System.out.println("KI Fehlschuss bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'O'; // Fehlschuss sichtbar machen
            spielfeld.spielfeldSpieler[zeile][spalte] = 'O'; // Original-Spielfeld anpassen
        }
    }
}
*/

private void checkeUndMarkiereVollstaendigZerstörteSchiffeKI() {
        // Erstelle eine Kopie der Liste
    List<Schiff> nochNichtZerstörteSchiffe = new ArrayList<>(schiffeKI);
        for (Schiff schiff : new ArrayList<>(nochNichtZerstörteSchiffe)) { // Weiter  immer neue erstellte Kopie der Liste durchlaufen , um Fehler ConcurrentModificationException zu vermeiden denn Element werden während der Iteration entfernt
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
                nochNichtZerstörteSchiffe.remove(schiff);
                return;
            }
        }



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
        return "KI gewinnt!";
    } else if (!kiHatSchiffe) {
        return "Spieler gewinnt!";
    }
    return null; // Noch kein Gewinner
}
    }


     // Methode zum Platzieren der Schiffe der KI 
    /*private void schiffePlatzierenKI() {
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
    }*/



   //Kopie für letzte treffer methode schussAufNachbar und if anfang findenBestenzug hinzugefugt
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class KIMittel {
    public char[][] sichtbaresSpielfeldKI;
    public Spielfeld spielfeld;
     public boolean kiSchiffePlatziert = false;
    public List<int[]> letzteTreffer = new ArrayList<>();
    public boolean schiffRichtungGefunden = false; // Gibt an, ob eine Richtung erkannt wurde
    public boolean istHorizontal = false; // Speichert die Richtung, falls gefunden
    public boolean alleMittelGetroffen = false; // Flag, um zu verfolgen, ob alle mittleren Felder getroffen wurden
    public List<int[]> letzteTrefferKopie = new ArrayList<>(letzteTreffer);//wenn Schiffe nebeneinander sind und eine richtung von 2 treffer aber unterschiedliche Schiffe
    KIMittel(Spielfeld spielfeld) {
        this.spielfeld = spielfeld;

        sichtbaresSpielfeldKI = new char[10][10];
        for (int zeile = 0; zeile < 10; zeile++) {
            for (int spalte = 0; spalte < 10; spalte++) {
                sichtbaresSpielfeldKI[zeile][spalte] = '~'; // Alles unbekannt
            }
        }
        spielfeld.zuegeDesSpielers = 0; // Anfangszustand
    }


    // Methode zum Platzieren der Schiffe der KI 
    protected void schiffePlatzierenKI() {
        Random random = new Random();


        for (int i = 0; i < spielfeld.schiffLaengen.length; i++) {
            int laenge = spielfeld.schiffLaengen[i];
            boolean erfolgreich = false;

            // Versuche so lange ein Schiff zu platzieren, bis es funktioniert
            while (!erfolgreich) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Versuche, das Schiff zu platzieren
                erfolgreich = spielfeld.schiffPlatzieren(spielfeld.spielfeldKI, xStart, yStart, laenge, horizontal);

                // Wenn erfolgreich, speichere das Schiff
                if (erfolgreich) {
                    Schiff neuesSchiff = new Schiff(xStart, yStart, laenge, horizontal);
                    spielfeld.schiffeKI.add(neuesSchiff);
                }
            }
        }

        kiSchiffePlatziert = true;
    }
    // KI führt ihre drei Züge aus
    public void macheKIZuege() {
        if (spielfeld.spielBeendet) {
            System.out.println("Das Spiel ist bereits beendet. Keine Züge mehr möglich.");
            return;
        }

        for (int i = 0; i < 3; i++) {// KI macht drei Züge
            if(spielfeld.schiffeSpieler.isEmpty()){
                break;
            }
            int[] zug = findeBestenZug();
            int zeile = zug[0];
            int spalte = zug[1];

            if (istGueltigerZug(zeile, spalte)) {
                macheSchussKI(zeile, spalte);
            }

            // Wartezeit zwischen Zügen der KI
            try {
                Thread.sleep(1000); // 1 Sekunde Pause
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        spielfeld.zuegeDesSpielers = 0; // Spieler kann danach wieder spielen
    }

    protected boolean istGueltigerZug(int zeile, int spalte) {
        return sichtbaresSpielfeldKI[zeile][spalte] == '~';
    }

    protected int[] findeBestenZug() {
        if( !schiffRichtungGefunden){ // nebeneinander Schiffe ganz zerstören zu können
           return schussAufNachbar();
        }

        if (!letzteTreffer.isEmpty() ) {
            return findeGezieltenZug();
        }
        if (!alleMittelGetroffen) {
            return findeHeuristischenZug(); // Suche nach mittleren Feldern
        }
        return findeEckenZug(); // Suche nach Ecken, wenn alle mittleren Felder getroffen sind
    }

    protected int[] findeGezieltenZug() {
       // List<int[]> jederTrefferListe = new ArrayList<>();
        if (!letzteTreffer.isEmpty() ) {
            int[] ersteTreffer = letzteTreffer.get(0);

            // Wenn eine Richtung bereits erkannt wurde, ziele weiter in dieser Richtung
            if (schiffRichtungGefunden) {
                if (istHorizontal) {
                    return schießeHorizontalWeiter();
                } else {
                    return schießeVertikalWeiter();
                }
            }

            // Wenn noch keine Richtung erkannt wurde, versuche, angrenzende Felder zu treffen
            for (int[] richtung : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
                int neueZeile = ersteTreffer[0] + richtung[0];
                int neueSpalte = ersteTreffer[1] + richtung[1];

                if (neueZeile >= 0 && neueZeile < 10 && neueSpalte >= 0 && neueSpalte < 10 &&
                        istGueltigerZug(neueZeile, neueSpalte)) {
                    return new int[]{neueZeile, neueSpalte};
                }
            }
        }

        return findeHeuristischenZug();
    }

    protected int[] schießeHorizontalWeiter() {
        int[] ersteTreffer = letzteTreffer.get(0);
        int[] letzteTrefferPosition = letzteTreffer.get(letzteTreffer.size() - 1);

        // Versuche rechts weiter zu schießen
        int rechteSpalte = letzteTrefferPosition[1] + 1;
        if (rechteSpalte < 10 && istGueltigerZug(ersteTreffer[0], rechteSpalte)) {
            return new int[]{ersteTreffer[0], rechteSpalte};
        }

        // Versuche links weiter zu schießen
        int minSpalte = letzteTreffer.stream().mapToInt(treffer -> treffer[1]).min().orElse(-1); // Extrahiere die Spaltenwerte // Finde das Minimum// Standardwert, falls die Liste leer ist
        int linkeSpalte = minSpalte - 1;
        if (linkeSpalte >= 0 && istGueltigerZug(ersteTreffer[0], linkeSpalte)) {
            return new int[]{ersteTreffer[0], linkeSpalte};
        }

        if(schiffRichtungGefunden){//wenn Schiffe nebeneinander sind und eine richtung von 2 treffer aber unterschiedliche Schiffe
           // System.out.println("RUUU");
            letzteTrefferKopie = letzteTreffer;
             return schussAufNachbar();
        }

        // Fallback
        return findeHeuristischenZug();
    }

    protected int[] schießeVertikalWeiter() {
        int[] ersteTreffer = letzteTreffer.get(0);
        int[] letzteTrefferPosition = letzteTreffer.get(letzteTreffer.size() - 1);

        // Versuche unten weiter zu schießen
        int untereZeile = letzteTrefferPosition[0] + 1;
        if (untereZeile < 10 && istGueltigerZug(untereZeile, ersteTreffer[1])) {
            return new int[]{untereZeile, ersteTreffer[1]};
        }

        // Versuche oben weiter zu schießen
        int minZeile = letzteTreffer.stream().mapToInt(treffer -> treffer[0]).min().orElse(-1); // Extrahiere die Zeilenwerte // Finde das Minimum// Standardwert, falls die Liste leer ist
        
        int obereZeile = minZeile - 1;
        if (obereZeile >= 0 && istGueltigerZug(obereZeile, ersteTreffer[1])) {
            return new int[]{obereZeile, ersteTreffer[1]};
        }

        if(schiffRichtungGefunden){//wenn Schiffe nebeneinander sind und eine richtung von 2 treffer aber unterschiedliche Schiffe
           // System.out.println("RUUU");
            letzteTrefferKopie = letzteTreffer;
             return schussAufNachbar();
        }

        // Fallback
        return findeHeuristischenZug();
    }

    protected int[] schussAufNachbar(){//wenn Schiffe nebeneinander sind und eine richtung von 2 treffer aber unterschiedliche Schiffe
       // letzteTrefferKopie = letzteTreffer;
        //List<int[]> letzteTrefferKopie = new ArrayList<>(letzteTreffer);
      if(letzteTreffer.size() == 1){ // Gezielte zug für jeden treffer der verschiedene schiffe machen 
        return findeGezieltenZug();
      }
       
       if(!letzteTrefferKopie.isEmpty()){
            for (int[] trefferArray: letzteTrefferKopie){
             List<int[]> jederTrefferListe = new ArrayList<>();
                jederTrefferListe.add(trefferArray);
                letzteTreffer = jederTrefferListe;
                schiffRichtungGefunden = false;
                letzteTrefferKopie.remove(trefferArray);
               return findeGezieltenZug();
            }
       }
        return findeGezieltenZug();
    }

    protected int[] findeHeuristischenZug() {
        // Definieren der Quadrate des Spielfeldes in 3x3 Regionen
        int[][] quadrate = {
            {0, 0, 2, 2}, {0, 3, 2, 5}, {0, 6, 2, 8},
            {3, 0, 5, 2}, {3, 3, 5, 5}, {3, 6, 5, 8},
            {6, 0, 8, 2}, {6, 3, 8, 5}, {6, 6, 8, 8},
            {9, 0, 9, 2}, {9, 3, 9, 5}, {9, 6, 9, 8}
        };

        // Durchlaufe jedes Quadrat und schieße zuerst in die Mitte des Quadrats
        for (int[] quadrat : quadrate) {
            int mitteZeile = (quadrat[0] + quadrat[2]) / 2;
            int mitteSpalte = (quadrat[1] + quadrat[3]) / 2;
            if (istGueltigerZug(mitteZeile, mitteSpalte)) {
                return new int[]{mitteZeile, mitteSpalte};
            }
        }

        alleMittelGetroffen = true; // Alle mittleren Felder wurden getroffen
        return findeEckenZug(); // Falls alle Mittel getroffen sind, gehe zu den Ecken
    }

    protected int[] findeEckenZug() {
        // Durchlaufe jedes Quadrat und schieße dann auf die Ecken
        int[][] quadrate = {
            {0, 0, 2, 2}, {0, 3, 2, 5}, {0, 6, 2, 9},
            {3, 0, 5, 2}, {3, 3, 5, 5}, {3, 6, 5, 9},
            {6, 0, 8, 2}, {6, 3, 8, 5}, {6, 6, 8, 9},
            {9, 0, 9, 2}, {9, 3, 9, 5}, {9, 6, 9, 9}
        };

        for (int[] quadrat : quadrate) {
            int zeilenStart = quadrat[0];
            int spaltenStart = quadrat[1];
            int zeilenEnde = quadrat[2];
            int spaltenEnde = quadrat[3];

            if (istGueltigerZug(zeilenStart, spaltenEnde)) {
                return new int[]{zeilenStart, spaltenEnde};
            }
            
        }

        for (int[] quadrat : quadrate) {
            int zeilenStart = quadrat[0];
            int spaltenStart = quadrat[1];
            int zeilenEnde = quadrat[2];
            int spaltenEnde = quadrat[3];

            if (istGueltigerZug(zeilenStart, spaltenStart)) {
                return new int[]{zeilenStart, spaltenStart};
            }
            
        }

        for (int[] quadrat : quadrate) {
            int zeilenStart = quadrat[0];
            int spaltenStart = quadrat[1];
            int zeilenEnde = quadrat[2];
            int spaltenEnde = quadrat[3];

            if (istGueltigerZug(zeilenEnde, spaltenEnde)) {
                return new int[]{zeilenEnde, spaltenEnde};
            }
        }


        for (int[] quadrat : quadrate) {
            int zeilenStart = quadrat[0];
            int spaltenStart = quadrat[1];
            int zeilenEnde = quadrat[2];
            int spaltenEnde = quadrat[3];

            if (istGueltigerZug(zeilenEnde, spaltenStart)) {
                return new int[]{zeilenEnde, spaltenStart};
            }
        }

        // Letzter Fallback: Überprüfe alle Felder des Spielfelds
        for (int zeile = 9; zeile >= 0; zeile--) {
            for (int spalte = 9; spalte >= 0; spalte--) {
                if (istGueltigerZug(zeile, spalte)) {
                return new int[]{zeile, spalte};
                }
            }
        }

        return new int[]{-1, -1}; // Keine gültigen Züge mehr
    }

    protected void macheSchussKI(int zeile, int spalte) {
        if (spielfeld.spielfeldSpieler[zeile][spalte] == 'S') { // Treffer
            System.out.println("KI Treffer bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'X';
            spielfeld.spielfeldSpieler[zeile][spalte] = 'X';
            spielfeld.zeichneTreffer(zeile, spalte, "Spieler");
            letzteTreffer.add(new int[]{zeile, spalte});
             spielfeld.checkeUndMarkiereVollstaendigZerstörteSchiffeSpieler();
            if (letzteTreffer.size() > 1) {
                schiffRichtungGefunden = true;
                istHorizontal = letzteTreffer.get(0)[0] == letzteTreffer.get(1)[0];
            }

           
            String gewinner = spielfeld.pruefeGewinner();
            if (gewinner != null) {
                System.out.println(gewinner);
            }

        } else { // Fehlschuss
            System.out.println("KI Fehlschuss bei (" + zeile + ", " + spalte + ")");
            sichtbaresSpielfeldKI[zeile][spalte] = 'O';
            spielfeld.spielfeldSpieler[zeile][spalte] = 'O';
            spielfeld.zeichneFehlschuss(zeile, spalte, "Spieler");
        }
    }
}



class KISchwer extends KIMittel{
    public KISchwer(Spielfeld spielfeld){
        super(spielfeld);
    }


    @Override 
    private void schiffePlatzierenKI() {
        Random random = new Random();


        for (int i = 0; i < spielfeld.schiffLaengen.length; i++) {
            int laenge = spielfeld.schiffLaengen[i];
            boolean erfolgreich = false;

            // Versuche so lange ein Schiff zu platzieren, bis es funktioniert
            while (!erfolgreich) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Versuche, das Schiff zu platzieren
                erfolgreich = spielfeld.schiffPlatzieren(spielfeldKI, xStart, yStart, laenge, horizontal);

                // Wenn erfolgreich, speichere das Schiff
                if (erfolgreich) {
                    Schiff neuesSchiff = new Schiff(xStart, yStart, laenge, horizontal);
                    schiffeKI.add(neuesSchiff);
                }
            }
        }

        kiSchiffePlatziert = true;
    }

    public void macheKIZuege(){
        super.macheKIZuege();
    }

    protected boolean istGueltigerZug(int zeile, int spalte){
        return super.istGueltigerZug();
    }

    protected int[] findeBestenZug(){
        return super.findeBestenZug();
    }
    protected int[] findeGezieltenZug(){
        return super.findeGezieltenZug();
    }
    protected int[] schießeHorizontalWeiter(){
        return super.schießeHorizontalWeiter();
    }
    protected int[] schießeVertikalWeiter(){
        return super.schießeVertikalWeiter();
    }
    protected int[] schussAufNachbar(){
        return super.schussAufNachbar();
    }
    protected int[] findeHeuristischenZug(){
        return super.findeHeuristischenZug();
    }
    protected int[] findeEckenZug(){
        return super.findeEckenZug();
    }
    protected void macheSchussKI(){
        super.macheSchussKI();
    }

}







import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spielfeld {
    Turtle t = new Turtle(500,600);
    KIMittel ki;
    public char[][] spielfeldSpieler;  // 2D-Array für das Spielfeld des Spielers (10x10)
    public char[][] spielfeldKI;       // 2D-Array für das Spielfeld der KI (10x10)
    public List<Schiff> schiffeSpieler;  // Liste für die Schiffe des Spielers
    public List<Schiff> schiffeKI;  // Liste für die Schiffe der KI

    public final int[] schiffLaengen = {2, 3, 4, 5};  // Die Längen der Schiffe
    public int platzierteSchiffeSpieler = 0;  // Anzahl platzierter Schiffe des Spielers
   // public boolean kiSchiffePlatziert = false;  // Status, ob die KI ihre Schiffe bereits platziert hat
    public int zuegeDesSpielers = 0; // Zählt die Züge des Spielers in einer Runde
    public boolean spielBeendet = false;
    //public boolean schiffGanzZerstört = false;
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
        ki = new KIMittel(this);  // Neues KI-Objekt wird hier erstellt
        // 1. Willkommensnachricht anzeigen
    zeichneStartbildschirm();

    // 2. Verzögerung von 10 Sekunden
    try {
        Thread.sleep(5000); // 10 Sekunden warten
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
            if (platzierteSchiffeSpieler == schiffLaengen.length && !ki.kiSchiffePlatziert) {
                ki.schiffePlatzierenKI();
            }
            return true;
        }
        return false;
    }

    // Methode zum Platzieren eines Schiffs auf dem Spielfeld
    protected boolean schiffPlatzieren(char[][] spielfeld, int xStart, int yStart, int laenge, boolean horizontal) {
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
        if (spielBeendet) {
        System.out.println("Das Spiel ist bereits beendet. Keine Züge mehr möglich.");
        return;
    }

    if(schiffeKI.isEmpty()){
        System.out.println("Es gibt kein Schiff auf dem Spielfeld ");
        return;
    }

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
            zuegeDesSpielers++;
            zeichneTreffer(zeile, spalte,"KI");
            checkeUndMarkiereVollstaendigZerstörteSchiffeKI();
            String gewinner = pruefeGewinner();
            if (gewinner != null) {
                System.out.println(gewinner);
            //System.exit(0); // Spiel beenden
            }
        } else {
            System.out.println("Fehlschuss!");
            spielfeldKI[zeile][spalte] = '0';  // Markiere als Fehlschuss
            zeichneFehlschuss(zeile, spalte,"KI");
            zuegeDesSpielers++;
        }

        if (zuegeDesSpielers == 3) {
            System.out.println("Du hast 3 Züge gemacht. Jetzt ist die KI dran.");
            ki.macheKIZuege();// KI macht ihre drei Züge
        }

        // Gewinnerprüfung nach dem Schuss
//String gewinner = pruefeGewinner();

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


private void entferneTrefferAusListe(List<int[]> liste, int zeile, int spalte) {
    liste.removeIf(treffer -> treffer[0] == zeile && treffer[1] == spalte);
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

            if(schiff.getLaenge() < ki.letzteTreffer.size() ){// falls ein schiff zerstort ist aber liste von Treffer auch ein Treffer ein anderes Schiff enthalt
                System.out.println("Ein Schiff des Spielers komplett zerstört");
                 zeichneRoteLinie(schiff,"Spieler");
                // int trefferAnderesSchiff = letzteTreffer.size() - schiffeSpieler.get(i).getLaenge();
                 int beginZeile = schiff.getZeile();
                 int beginSpalte = schiff.getSpalte();
                if(schiff.isHorizontal()){
                    int endeSpalte = beginSpalte + schiff.getLaenge();
                    for(int i = beginSpalte ; i <= endeSpalte; i++){
                        entferneTrefferAusListe(ki.letzteTreffer, beginZeile, i);
                        //ki.letzteTreffer.remove(new int[]{beginZeile,i});
                    }
                } else{
                    int endeZeile = beginZeile + schiff.getLaenge();
                    for(int i = beginZeile ; i <= endeZeile; i++){
                        //ki.letzteTreffer.remove(new int[]{i,beginSpalte});
                       entferneTrefferAusListe(ki.letzteTreffer, i, beginSpalte);
                    }
                }
                // for(int i = letzteTreffer.size())
               // ki.letzteTreffer.clear(); // Treffer KI zurücksetzen
                ki.schiffRichtungGefunden = false; //Richtung zurücksetzen
                ki.istHorizontal = false;

                schiffeSpieler.remove(schiff); // Schiff aus der Liste der nicht zerstörten Schiffe entfernen
                 j--; // Reduziere den Index, um das nächste Schiff korrekt zu überprüfen (wegen Entfernen)
            System.out.println(ki.letzteTreffer.size());
            }else{
                //    schiffGanzZerstört = true;
                 // Rote Linie auf das zerstörte Schiff zeichnen
                System.out.println("Ein Schiff des Spielers komplett zerstört");
                ki.letzteTreffer.clear(); // Treffer KI zurücksetzen
                ki.schiffRichtungGefunden = false; //Richtung zurücksetzen
                ki.istHorizontal = false;
                zeichneRoteLinie(schiff,"Spieler");

                schiffeSpieler.remove(schiff); // Schiff aus der Liste der nicht zerstörten Schiffe entfernen
                 j--; // Reduziere den Index, um das nächste Schiff korrekt zu überprüfen (wegen Entfernen)
            }
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

   


    public void schiffePlatzierenSpielerTurtle(int row, int col, boolean horizontal) {
    int startX, startY;
    if (platzierteSchiffeSpieler == schiffLaengen.length && !ki.kiSchiffePlatziert) {
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
        System.out.println("Computer hat gewonnen!");
        spielBeendet = true;
        frageNachNeuemSpiel();
        return "";
        //return "Computer hat gewonnen!";
    } else if (!kiHatSchiffe) {
        //t.reset();
        t.left(90);
        t.textSize = 30;
        t.moveTo(250,520);
        t.color(0,255,0);
        t.text("Sie haben gewonnen");
        t.right(90);
        System.out.println("Sie haben gewonnen!");
        spielBeendet = true;
       frageNachNeuemSpiel();
       return "";
       // return "Sie haben gewonnen!";
    }
    return null; // Noch kein Gewinner
}

private void frageNachNeuemSpiel() {
        Scanner scanner = new Scanner(System.in);
        t.moveTo(250,540);
        t.textSize = 12;
        t.left(90);
        t.color(0,255,0);
        t.text("Geben Sie 1 ein, um ein neues Spiel zu starten, oder 2, um das Spiel zu beenden." );
        t.right(90);
        System.out.println("Geben Sie 1 ein, um ein neues Spiel zu starten, oder 2, um das Spiel zu beenden.");

        String antwort = scanner.nextLine();

        switch (antwort) {
            case "1":
                System.out.println("Neues Spiel wird gestartet...");
                neuesSpielStarten();
                break;
            case "2":
                System.out.println("Das Spiel wird beendet.");
                return;
            default:
                System.out.println("Ungültige Eingabe. Bitte geben Sie 1 oder 2 ein.");
                frageNachNeuemSpiel();
                break;
        }
    }

    private void neuesSpielStarten() {
        t.reset();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                spielfeldKI[i][j] = '~';  // "~" für Wasser
                spielfeldSpieler[i][j] = '~';   // "~" für Wasser
            }
        }
        platzierteSchiffeSpieler = 0;
        ki.kiSchiffePlatziert = false;
        zuegeDesSpielers = 0;
        spielBeendet = false;
        schiffeSpieler.clear();
        schiffeKI.clear();
        zeichneStartbildschirm();  // Zeigt den Startbildschirm an
        // 2. Verzögerung von 20 Sekunden
    try {
        Thread.sleep(20000); // 20 Sekunden warten
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    // 3. Startbildschirm leeren und Spielfelder anzeigen
    t.reset(); // Entfernt den Startbildschirm

        spielfeldTurtle("Spieler");  // Zeigt das Spielfeld des Spielers
        spielfeldTurtle("KI");  // Zeigt das Spielfeld der KI
        // Neues Spielfeld erstellen und starten
        //System.out.println("Spielfeld wird neu erstellt...");
       // Spielfeld neuesSpielfeld = new Spielfeld();

       ki.sichtbaresSpielfeldKI = new char[10][10];
        for (int zeile = 0; zeile < 10; zeile++) {
            for (int spalte = 0; spalte < 10; spalte++) {
                ki.sichtbaresSpielfeldKI[zeile][spalte] = '~'; // Alles unbekannt
            }
        }
        ki.letzteTreffer.clear();
       // spielfeld.zuegeDesSpielers = 0; // Anfangszustand
    }

}


/*
class AIDifficult extends AIMedium {
    public AIDifficult(GameField gameField) {
        super(gameField);
    }

    @Override
    private void placeAIShips() {
        Random random = new Random();

        for (int i = 0; i < gameField.shipLengths.length; i++) {
            int length = gameField.shipLengths[i];
            boolean success = false;

            // Try to place a ship until it works
            while (!success) {
                int xStart = random.nextInt(10);
                int yStart = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Try to place the ship
                success = gameField.placeShip(gameField.aiField, xStart, yStart, length, horizontal);

                // If successful, store the ship
                if (success) {
                    Ship newShip = new Ship(xStart, yStart, length, horizontal);
                    aiShips.add(newShip);
                }
            }
        }

        aiShipsPlaced = true;
    }

}
*/

public void drawStartScreen() {
        turtle.left(90);
        turtle.moveTo(250, 100);
        turtle.textSize = 30;
        turtle.color(0,0,255);
        turtle.text("Willkommen zu Schiffe Versenken!");
        turtle.textSize = 20;
        turtle.moveTo(250, 250);
        turtle.text("Das Spiel startet gleich...");
        turtle.textSize = 15;
        turtle.moveTo(250, 300);
        turtle.text("Platzieren Sie bitte Ihre Schiffe!");
        turtle.color(0,0,0);
        turtle.right(90);
    }

    protected boolean checkNeighbors(int row, int column){
        int upRow = (row < 9 ? row + 1: );
        int downRow = row - 1;
        int leftColumn = column - 1;
        int rightColumn = column + 1;
        if((visibleFieldAI[upRow][column] == '~' || visibleFieldAI[downRow][column] == '~' || visibleFieldAI[row][leftColumn] == '~' || visibleFieldAI[row][rightColumn] == '~') && row){
            return true;
        }return false;
    }

    
/*
public class Ship { // record hier spater machen 
    private int row;          // Zeile der Schiffposition
    private int column;       // Spalte der Schiffposition
    private int length;       // Länge des Schiffs
    private boolean horizontal; // Gibt an, ob das Schiff horizontal oder vertikal ist

    // Konstruktor der Schiff-Klasse
    public Ship(int row, int column, int length, boolean horizontal) {
        this.row = row;
        this.column = column;
        this.length = length;
        this.horizontal = horizontal;
    }

    // Getter-Methoden
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getLength() {
        return length;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    // Setter-Methoden (falls du die Werte später ändern möchtest)
    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }
}
*/

/*public class Schiff {
    private int zeile;          // Zeile der Schiffposition
    private int spalte;         // Spalte der Schiffposition
    private int laenge;         // Länge des Schiffs
    private boolean horizontal; // Gibt an, ob das Schiff horizontal oder vertikal ist

    // Konstruktor der Schiff-Klasse
    public Schiff(int zeile, int spalte, int laenge, boolean horizontal) {
        this.zeile = zeile;
        this.spalte = spalte;
        this.laenge = laenge;
        this.horizontal = horizontal;
    }

    // Getter-Methoden
    public int getZeile() {
        return zeile;
    }

    public int getSpalte() {
        return spalte;
    }

    public int getLaenge() {
        return laenge;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    // Setter-Methoden (falls du die Werte später ändern möchtest)
    public void setZeile(int zeile) {
        this.zeile = zeile;
    }

    public void setSpalte(int spalte) {
        this.spalte = spalte;
    }

    public void setLaenge(int laenge) {
        this.laenge = laenge;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }
}
*/
