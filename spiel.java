import java.util.ArrayList;
import java.util.List;

public class Spiel {
    private Spielfeld spielfeld;
    private List<Schiff> schiffeSpieler;
    private List<Schiff> schiffeKI;
    private int zugSpieler = 0;

    public Spiel(Spielfeld spielfeld) {
        this.spielfeld = spielfeld;
        this.schiffeSpieler = new ArrayList<>();
        this.schiffeKI = new ArrayList<>();
    }

    public void schiffHinzufuegenSpieler(int laenge, List<int[]> positionen) {
        schiffeSpieler.add(new Schiff(laenge, positionen));
    }

    public void schiffHinzufuegenKI(int laenge, List<int[]> positionen) {
        schiffeKI.add(new Schiff(laenge, positionen));
    }

    public void zugSpieler(int zeile, int spalte) {
        if (zugSpieler >= 3) {
            System.out.println("Du hast deine 3 Züge für diese Runde schon gemacht. Es ist jetzt die KI an der Reihe!");
            return;
        }
        zugSpieler++;
        boolean treffer = false;

        for (Schiff schiff : schiffeKI) {
            if (schiff.treffen(zeile, spalte)) {
                treffer = true;
                break;
            }
        }

        if (treffer) {
            System.out.println("Treffer!");
            spielfeld.setzeFeld(zeile, spalte, 'X');
            if (istZerstörtSchiff(schiffeKI)) {
                System.out.println("Ein Schiff der KI wurde zerstört!");
                zeichneZerstörtesSchiff(zeile, spalte);
            }
        } else {
            System.out.println("Verfehlt!");
            spielfeld.setzeFeld(zeile, spalte, '0');
        }
    }

    private boolean istZerstörtSchiff(List<Schiff> schiffe) {
        for (Schiff schiff : schiffe) {
            if (schiff.istZerstört()) {
                return true;
            }
        }
        return false;
    }

    private void zeichneZerstörtesSchiff(int zeile, int spalte) {
        System.out.println("Zerstörtes Schiff an " + zeile + ", " + spalte);
    }
}
