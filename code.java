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
