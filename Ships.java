public class Ship {
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