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

       /* if(schiffRichtungGefunden){
            List<int[]> jederTrefferListe = new ArrayList<>();
            for(int[]trefferArray : letzteTreffer){
                jederTrefferListe.add(trefferArray);
                letzteTreffer = jederTrefferListe;
                schiffRichtungGefunden = false;
                findeGezieltenZug();
            }
        }*/
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