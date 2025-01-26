Clerk.markdown(Text.fillOut("""
    

    # Nim-Spiel mit Monte Carlo

        Roussel Dongmo

    > Die mcMove oder [Monte-Carlo](https://en.wikipedia.org/wiki/Monte_Carlo_tree_search)  methode wird in der Nim Spiel benutzt um den besten Zug herauszufinden,indem eine statische Berechnung von Gewinn und Verlust nach nach jeder N Simulationen durchgeführt wird . Anbei erkläre ich ausfühlicher , wie ich verfahren habe. Hier sind die notwendige Bibliotheke 
    

    ```nimView
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.stream.IntStream;
    ```

    """,Text.cutOut("./nimView.java","// klassseMove")));

//klasseMove
class Move {
    final int row, number;

    static Move of(int row, int number) {
        return new Move(row, number);
    }

    private Move(int row, int number) {
        if (row < 0 || number < 1)
            throw new IllegalArgumentException("Ungültiger Zug");
        this.row = row;
        this.number = number;
    }

    public String toString() {
        return "(" + row + ", " + number + ")";
    }
}
// klasseMove
Clerk.markdown(Text.fillOut(""" 
Zuerst erstelle ich durch verschaltete Schleife  eine Liste von allen möglichen Züge in einem gegeben Nim Spielstelung. Hier wird eine Liste statt Array benutzt , um problemlos Züge einzufügen.
Dann laufe ich diese Schleife durch , um für jeden Zug  eine Anzahl 𝑁 an zufälligen Spielverläufen zu simulieren. Ich erstelle zuerst neue Instanz für jede Simulation , ich  setze den aktuellen Zug um und erstelle eine boolean Variable  für den ersten Spieler , die ich auf true setze . Denn ich nehme tatsächlich an, dass der esrte den Vorteil hat. Danach spiele ich weiter mit zufälligen Zügen, bis das Spiel vorbei ist. Das wird gemacht dank meiner randomMove() Methode von Typ Move, die die Liste von allen möglichen Zügen läuft und zufällig  ein Zug wählt.Hier ist diese Methode
```nimMC
public Move randomMove() {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            for (int j = 1; j <= rows[i]; j++) {
                possibleMoves.add(Move.of(i, j));
            }
        }
        return possibleMoves.get(r.nextInt(possibleMoves.size()));
    }
```

 Ich nutze danach meine Variable g(Gewinn) und v(verlust) , die ich je nach Gewinn oder Verlust nach jeder Simulation implementiere 
Ich berechene dann Gewinnrate für diesen Zug in Prozent, indem ich die Anzahl von Gewinn durch Summe von Gewinn und Verlust(gecastet in double) teile und multiplieziere mit 100 und dann caste ich das Ergebnis gewinnProzent in int und füge zum Schluss in meiner Liste value , die das GewinnProzent von jedem Zug speichert .Besonders in meiner Methode mcMove ist ,dass ich nach jedem Zug den currentMove, gewinnProzent , Anzahl von Gewinn und Anzahl von Verlust durch println auf die Konsole ausgebe .Ich  mache so weiter bis Ende meiner Liste von possibleMoves. Am Ende von diesem laufe ich diese value Liste durch, um den Zug mit der höchsten GewinnProzent max auswählen . Wenn ich schon ein max habe, dann nehme ich direkt die Move in possibleMove auf diese Index von value , da Angabe auf jede Index von Value entspricht jeweils  den Zug beziehungsweise den Move auf diese Index. Danach füge ich diese Move in meiner bestMoves Liste , nachdem ich schon das vorherige Move von der Liste gelöscht habe . Diese Liste is mir eigentlich hilfreich in meim else if , denn ich dort alle Move von gleichen max hinzufüge und ein Move wird danach zufällig auf in dieser Liste gewähtl. danach gebe ich den besten Zug mit seinem GewinnProzent durch println aus . Und ich gebe letztendlich den besten Zug **bestMove** zurück
Anbei die Methode mcMove()

```nimMC
public Move mcMove() {
    ArrayList<Integer> value = new ArrayList<>();
    ArrayList<Move> possibleMoves = new ArrayList<>();
    int N = 10;
    for (int i = 0; i < rows.length; i++) {
        for (int j = 1; j <= rows[i]; j++) {
            possibleMoves.add(Move.of(i, j));
        }
    }
    for (Move currentMove : possibleMoves) {
        int g = 0; 
        int v = 0; 
        for (int sim = 0; sim < N; sim++) {
            Nim playSimulation = Nim.of(this.rows); 
            playSimulation = playSimulation.play(currentMove);
            boolean currentPlayerWon = true; 
            while (!playSimulation.isGameOver()) {
                Move randomMove = playSimulation.randomMove();
                playSimulation = playSimulation.play(randomMove);
                currentPlayerWon = !currentPlayerWon; 
            }

            if (currentPlayerWon) {
                g++; 
            } else {
                v++; 
            }
        }

        
        int gewinnProzent = (int) ((g / (double) (g + v)) * 100); 
        value.add(gewinnProzent);

        
        System.out.println("Zug: " + currentMove + ", Gewinnrate: " + gewinnProzent + "%, Gewinne: " + g + ", Verluste: " + v);
    }

    
    int max = -1;
    ArrayList<Move> bestMoves = new ArrayList<>();
    for (int k = 0; k < value.size(); k++) {
        int currentProzent = value.get(k);
        if (currentProzent > max) {
            max = currentProzent;
            bestMoves.clear(); 
            bestMoves.add(possibleMoves.get(k)); 
        } else if (currentProzent == max) {
            bestMoves.add(possibleMoves.get(k)); 
        }
    }

   
    Move bestMove = bestMoves.get(new Random().nextInt(bestMoves.size()));

    
    System.out.println("Bester Zug: " + bestMove + ", mit Gewinnrate: " + max + "%");

    return bestMove; 
}
```
""",Text.cutOut("./nimMC.java","// code ")));

// code
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Move {
    final int row, number;

    static Move of(int row, int number) {
        return new Move(row, number);
    }

    private Move(int row, int number) {
        if (row < 0 || number < 1)
            throw new IllegalArgumentException("Ungültiger Zug");
        this.row = row;
        this.number = number;
    }

    public String toString() {
        return "(" + row + ", " + number + ")";
    }
}

interface NimGame {

    Move randomMove();

    boolean isGameOver();

    String toString();
}

class Nim implements NimGame {
    private Random r = new Random();
    int[] rows;

    public static Nim of(int... rows) {
        return new Nim(rows);
    }

    private Nim(int... rows) {
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
        if (rows.length > 5 || !(Arrays.stream(rows).allMatch(n -> n <= 7))) {
            throw new IllegalArgumentException("Please enter correct values");
        }
        this.rows = Arrays.copyOf(rows, rows.length);
    }

    private Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }

    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }

    public String toString() {
        String s = "";
        for (int n : rows)
            s += "\n" + "I ".repeat(n);
        return s;
    }

    public Move randomMove() {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            for (int j = 1; j <= rows[i]; j++) {
                possibleMoves.add(Move.of(i, j));
            }
        }
        return possibleMoves.get(r.nextInt(possibleMoves.size()));
    }

    public Move mcMove() {
        ArrayList<Integer> value = new ArrayList<>();
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int N = 10;
        for (int i = 0; i < rows.length; i++) {
            for (int j = 1; j <= rows[i]; j++) {
                possibleMoves.add(Move.of(i, j));
            }
        }
        for (Move currentMove : possibleMoves) {
            int g = 0;
            int v = 0;
            for (int sim = 0; sim < N; sim++) {
                Nim playSimulation = Nim.of(this.rows);
                playSimulation = playSimulation.play(currentMove);
                boolean currentPlayerWon = true;
                while (!playSimulation.isGameOver()) {
                    Move randomMove = playSimulation.randomMove();
                    playSimulation = playSimulation.play(randomMove);
                    currentPlayerWon = !currentPlayerWon;
                }

                if (currentPlayerWon) {
                    g++;
                } else {
                    v++;
                }
            }

            int gewinnProzent = (int) ((g / (double) (g + v)) * 100);
            value.add(gewinnProzent);

            System.out.println(
                    "Zug: " + currentMove + ", Gewinnrate: " + gewinnProzent + "%, Gewinne: " + g + ", Verluste: " + v);
        }

        int max = -1;
        ArrayList<Move> bestMoves = new ArrayList<>();
        for (int k = 0; k < value.size(); k++) {
            int currentProzent = value.get(k);
            if (currentProzent > max) {
                max = currentProzent;
                bestMoves.clear();
                bestMoves.add(possibleMoves.get(k));
            } else if (currentProzent == max) {
                bestMoves.add(possibleMoves.get(k));
            }
        }

        Move bestMove = bestMoves.get(new Random().nextInt(bestMoves.size()));

        System.out.println("Bester Zug: " + bestMove + ", mit Gewinnrate: " + max + "%");

        return bestMove;
    }
}

/*
 * 
 * class NimView {
 * Nim ni;
 * Turtle turtle;
 * int x;
 * int y;
 * final int yInitial = 100;
 * 
 * public NimView(Turtle turtle, Nim ni) {
 * this.ni = ni;
 * this.turtle = turtle;
 * this.y = yInitial;
 * }
 * 
 * public NimView play(Move move) {
 * if (!ni.isGameOver()) {
 * ni = ni.play(move);
 * turtle.reset();
 * turtle.moveTo(x, y);
 * show();
 * }
 * return this;
 * }
 * 
 * public boolean isGameOver() {
 * return ni.isGameOver();
 * }
 * 
 * public String toString() {
 * show();
 * StringBuilder s = new StringBuilder();
 * for (int n : ni.rows) s.append("\n").append("I".repeat(n));
 * return s.toString();
 * }
 * 
 * private void show() {
 * y = yInitial;
 * for (int i = 0; i < ni.rows.length; i++) {
 * x = 100;
 * for (int j = 0; j < ni.rows[i]; j++) {
 * turtle.moveTo(x, y);
 * turtle.left(90);
 * turtle.lineWidth(10);
 * turtle.forward(50);
 * x += 50;
 * turtle.right(90).moveTo(x, y);
 * }
 * y += 60;
 * turtle.moveTo(100, y);
 * }
 * }
 * }
 */
Clerk.markdown(Text.fillOut(""" 
    ## Versuch zum Spielen :
    ```nimMC
    Nim n = Nim.of(2,5,6);
    n.mcMove();
    ```
    """,Text.cutOut("./nimMC.java","// code1 ")));
// code1

// code1

Clerk.markdown(Text.fillOut(""" 
## Beispiel von Ausgabe :
Zug: (0, 1), Gewinnrate: 40%, Gewinne: 4, Verluste: 6

Zug: (0, 2), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Zug: (1, 1), Gewinnrate: 30%, Gewinne: 3, Verluste: 7

Zug: (1, 2), Gewinnrate: 60%, Gewinne: 6, Verluste: 4

Zug: (1, 3), Gewinnrate: 30%, Gewinne: 3, Verluste: 7

Zug: (1, 4), Gewinnrate: 80%, Gewinne: 8, Verluste: 2

Zug: (1, 5), Gewinnrate: 40%, Gewinne: 4, Verluste: 6

Zug: (2, 1), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Zug: (2, 2), Gewinnrate: 40%, Gewinne: 4, Verluste: 6

Zug: (2, 3), Gewinnrate: 20%, Gewinne: 2, Verluste: 8

Zug: (2, 4), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Zug: (2, 5), Gewinnrate: 70%, Gewinne: 7, Verluste: 3

Zug: (2, 6), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Bester Zug: (1, 4), mit Gewinnrate: 80%

   $168 ==> (1,4)
    """,Text.cutOut("./nimView.java","// spielstand1")));
